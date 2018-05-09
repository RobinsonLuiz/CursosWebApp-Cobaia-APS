package cobaia.persistencia;


import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import cobaia.Annotations.Colunas;
import cobaia.Annotations.FK;
import cobaia.Annotations.Tabela;
import cobaia.Modelo.AbstractModel;

public class GenericDAO extends AbstractDAO<AbstractModel>{
	
	private String tabela;
	private ArrayList<Field> campos;
	private ArrayList<String> tipos;
	private ArrayList<Object> valores;
	
	@Override
	public void persiste(AbstractModel o) {
		if (!o.getClass().isAnnotationPresent(Tabela.class)) tabela = o.getClass().getSimpleName().toLowerCase();
		else tabela = o.getClass().getAnnotation(Tabela.class).nome();
		campos = new ArrayList<>();
		valores = new ArrayList<>();
		tipos = new ArrayList<>();
		for (Field f : o.getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(Colunas.class)) {
				f.setAccessible(true);
				campos.add(f);
				tipos.add(f.getType().getSimpleName().toLowerCase());
				try {
					valores.add(f.get(o));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				f.setAccessible(false);
			}
		}
		try {
			abreConexao();
			String sql = null;
			if (! o.isPersistente()) sql = sqlInsert();
			else sql = sqlUpdate();
			comando(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			for (int i = 0; i < valores.size(); i++) {
				if (tipos.get(i).toLowerCase().equals("date")) {
					Date data = (Date) valores.get(i);
					comandoinuse().setDate(i + 1, new java.sql.Date (data.getTime()));
				} else {
					if (tipos.get(i).toLowerCase().equals("part")) {
						comandoinuse().setNull(i + 1, Types.BINARY);
					} else {
						if (tipos.get(i).toLowerCase().equals("integer")) {
							comandoinuse().setInt(i + 1, (Integer) valores.get(i));
						} else {
							if (tipos.get(i).toLowerCase().equals("time")) {
								Time time = (Time) valores.get(i);
								comandoinuse().setTime(i + 1, new java.sql.Time (time.getTime()));
							} else {
								if (campos.get(i).getType().isEnum()) {
									int valor = 0;
									Object[] c = campos.get(i).getType().getEnumConstants();
									for (int j = 0; j < c.length; j++) {
										if (c[j].equals(valores.get(i))) {
											valor = ((Enum<?>) c[j]).ordinal();
										}
									}
									comandoinuse().setInt(i + 1, valor);
								} else {
									if (campos.get(i).isAnnotationPresent(FK.class)) {
										AbstractModel a = (AbstractModel) valores.get(i);
										comandoinuse().setInt(i + 1, a.getId());
									} else { 
										if (campos.get(i).toString().contains("byte")) {
											comandoinuse().setBytes(i + 1, (byte[]) valores.get(i));
										} 
										else {
											comandoinuse().setObject(i + 1, valores.get(i));
										}
									}
								}
							}
						}
					}
				}
			}
			
			if (sql.equals(sqlUpdate())) {
				comandoinuse().setObject(valores.size()+1, o.getId());
			}
			// invoca o comando no banco!
			comandoinuse().execute();
			// obtém a chave gerada
			ResultSet chaves = comandoinuse().getGeneratedKeys();
			// têm uma chave?
			if (chaves.next()) {
				// seta a chave no contato
				o.setId(chaves.getInt(1));
			}
			// fecha a conexão
			fechaConexao();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * string usada para fazer insert
	 * @return string usada no sql para insert
	 */
	
	String sqlInsert() {
		String Insert = "Insert INTO " + this.tabela + "(";
		for (int i = 0; i < campos.size(); i++) {
			if (campos.get(i).isAnnotationPresent(FK.class) && !campos.get(i).getType().isEnum()) {
				Insert += campos.get(i).getAnnotation(FK.class).nome();
			}
			else Insert += campos.get(i).getName();
			if (i == campos.size() - 1) break;
			else Insert += ",";
		}
		Insert += ") VALUES (";
		for (int i = 0; i < campos.size(); i++) {
			Insert += "?";
			if (i == campos.size() - 1) break;
			else Insert += ",";
		}
		Insert += ")";
		return Insert;
	}
	
	/**
	 * string usada para fazer update
	 * @return string usada no sql para update
	 */
	String sqlUpdate() {
		ArrayList<String> quantos = new ArrayList<>();
		ArrayList<String> colunas = new ArrayList<>();
		for (int i = 0; i < campos.size();i++) {
	    	quantos.add("?");
	    	if (campos.get(i).isAnnotationPresent(FK.class)) {
				colunas.add(campos.get(i).getAnnotation(FK.class).nome());
			}
			else colunas.add(campos.get(i).getName());
	    }
		String update = "UPDATE " + this.tabela + " SET ";
		for (int i = 0; i < quantos.size(); i++) {
			update += colunas.get(i) + " = " + quantos.get(i);
			if (i == quantos.size() - 1) update += " WHERE id = ?";
			else update += " , ";
		}
		return update;
	}
	
	public void delete(String dados,int id) {
		Class<?> temp = null;
		Object c = null;
		try {
			temp = Class.forName(dados);
			c = temp.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!c.getClass().isAnnotationPresent(Tabela.class)) tabela = c.getClass().getSimpleName().toLowerCase();
		else tabela = c.getClass().getAnnotation(Tabela.class).nome();
		try {
			abreConexao();
			String sql = "DELETE FROM " + this.tabela + 
			           " WHERE id = ?";			
			comando(sql);
						
			comandoinuse().setInt(1, id);
			
			comandoinuse().execute();	
			
			fechaConexao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Map<Integer,ArrayList<Object>> select(String dados) throws ClassNotFoundException {
		Class<?> c = Class.forName(dados);
		Map<Integer,ArrayList<Object>> resposta = new HashMap<>();
		campos = new ArrayList<>();
		String sql = "Select * from ";
		Object o = null;
		try {
			o = c.newInstance();
			for (Field f : o.getClass().getSuperclass().getDeclaredFields()) {
				if(f.isAnnotationPresent(Colunas.class)) {
					f.setAccessible(true);
					campos.add(f);
					f.setAccessible(false);
				}
			}
			if (!o.getClass().isAnnotationPresent(Tabela.class)) sql += o.getClass().getSimpleName();
			else sql += o.getClass().getAnnotation(Tabela.class).nome();
			for (Field f : o.getClass().getDeclaredFields()) {
				if(f.isAnnotationPresent(Colunas.class)) {
					f.setAccessible(true);
					campos.add(f);
					f.setAccessible(false);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			abreConexao();
			comando(sql);
			result();
			int j = 0;
			while (resultados().next()) {
				ArrayList<Object> objetos = new ArrayList<>();
				for (int i = 0; i < campos.size(); i++) {
					 if (campos.get(i).isAnnotationPresent(FK.class)) {
						 objetos.add(resultados().getObject(campos.get(i).getAnnotation(FK.class).nome()));
					 }
					 else {
						 objetos.add(resultados().getObject(campos.get(i).getName()));
					 }
				}
				resposta.put(j, objetos);
				j++;
			}
			fechaConexao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resposta;
	}
	
	@Override
	public Map<Integer,ArrayList<Object>> select(String dados,int cod) throws ClassNotFoundException {
		Class<?> c = Class.forName(dados);
		Map<Integer,ArrayList<Object>> resposta = new HashMap<>();
		campos = new ArrayList<>();
		String sql = "Select * from ";
		Object o = null;
		try {
			o = c.newInstance();
			if (!o.getClass().isAnnotationPresent(Tabela.class)) sql += o.getClass().getSimpleName();
			else sql += o.getClass().getAnnotation(Tabela.class).nome();
			for (Field f : o.getClass().getSuperclass().getDeclaredFields()) {
				if(f.isAnnotationPresent(Colunas.class)) {
					f.setAccessible(true);
					campos.add(f);
					f.setAccessible(false);
				}
			}
			for (Field f : o.getClass().getDeclaredFields()) {
				if(f.isAnnotationPresent(Colunas.class)) {
					f.setAccessible(true);
					campos.add(f);
					f.setAccessible(false);
				}
			}
		 } catch (Exception e) {
			 throw new RuntimeException(e);
		 }
		sql += " Where id = ?";
		try {
			abreConexao();
			comando(sql);
			comandoinuse().setInt(1, cod);
			result();
			int j = 0;
			if (resultados().next()) {
				ArrayList<Object> objetos = new ArrayList<>();
				for (int i = 0; i < campos.size(); i++) {
					 if (campos.get(i).isAnnotationPresent(FK.class)) {
						 objetos.add(resultados().getObject(campos.get(i).getAnnotation(FK.class).nome()));
					 }
					 else {
						 objetos.add(resultados().getObject(campos.get(i).getName()));
					 }
				}
				resposta.put(j, objetos);
				j++;
			}
			fechaConexao();
		} catch (Exception e) {
			
		}
		return resposta;
	}
}
