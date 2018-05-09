package cobaia.mvc.controllers;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import cobaia.Annotations.Controlador;
import cobaia.Modelo.Area;
import cobaia.Modelo.Curso;
import cobaia.persistencia.GenericDAO;

@Controlador
public class AdminController extends AbstractController {
	private SimpleDateFormat ISODateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private SimpleDateFormat ISOTimeFormat = new SimpleDateFormat("hh:mm");

	@Override
	protected String controller() {
		if (!this.getClass().getSimpleName().toLowerCase().contains("controller")) {
			return this.getClass().getSimpleName().toLowerCase();
		}
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String index() {
		return view();
	}
	
	public String editarcurso(int id) {
		GenericDAO curso = new GenericDAO();
        try {
        	Curso c = new Curso().load(id);
        	if (c.isPersistente()) {
        		viewBag.put("curso", c);
        		Map<Integer,ArrayList<Object>>  area = curso.select(new Area().getClass().getName());
        		ArrayList<Area> areas = new ArrayList<>();
        		for (int j = 0; j < area.size(); j++) {
        			int k = 0;
        			Area a = new Area();
        			a.setId((Integer) area.get(j).get(k));
        			a.setNome((String) area.get(j).get(++k));
        			areas.add(a);
        		}
        		viewBag.put("areas", areas);
        	} else {
        		getResponse().status(404);
        		return "Curso " + id + " não encontrado";
        	}
          } catch (ClassNotFoundException e) {
        	  return "Classe não encontrada";
          }
        return view();
	}
	
	public String excluir(int id) {
		GenericDAO curso = new GenericDAO();
        curso.delete(new Curso().getClass().getName(), id);
        getResponse().redirect("/mvc/admin/cursos");
		return "OK";
	}
	
	public String dadoscurso() {
		dao.abreConexao();
		String sql = "SELECT id,nome from areas";
		dao.comando(sql);
		dao.result();
		ArrayList<Area> areas = new ArrayList<>();
		try {
			while (dao.resultados().next()) {
				Area a = new Area().load(dao.resultados().getInt("id"));
				areas.add(a);
			}
		} catch (SQLException e) {
			return "SQL INVALIDA";
		} catch (ClassNotFoundException e) {
			return "Area não encontrada";
		}
		viewBag.put("areas", areas);
	    return view();
	}
	
	public String editarnovo(int id) {
        try {
        	Curso c = new Curso().load(id);
        	Area a = new Area().load(getRequest().queryMap("area").integerValue());
	        c.setNome(getRequest().queryParams("nome"));
	        c.setResumo(getRequest().queryParams("resumo"));
	        c.setVaga(getRequest().queryMap("vagas").integerValue());
	        c.setPreco(getRequest().queryMap("preco").doubleValue());
	        c.setCarga_horaria(getRequest().queryMap("carga_horaria").integerValue());
	        c.setData_inicio(ISODateFormat.parse(getRequest().queryParams("data_inicio")));
	        c.setData_termino(ISODateFormat.parse(getRequest().queryParams("data_termino")));        
	        if (getRequest().queryParamsValues("dias") != null) c.setDias(String.join(", ", getRequest().queryParamsValues("dias")));
	        c.setHorario_inicio(new java.sql.Time(ISOTimeFormat.parse(getRequest().queryParams("horario_inicio")).getTime()));
	        c.setHorario_termino(new java.sql.Time(ISOTimeFormat.parse(getRequest().queryParams("horario_termino")).getTime()));
	        c.setPrograma(getRequest().queryParams("programa"));
	        c.setImagem(null);
	        c.setArea(a);

	        c.validar();
	        if (!c.isValido()) {
	        	viewBag.put("erro", c.getErros());
        		return view("/admin/editarcurso.pebble");
	        }
        c.save();
        } catch (ClassNotFoundException e) {
        	return "Classe não encontrada";
        } catch (ParseException e) {
			return "Data Invalida";
		} catch (Exception e) {
			return "Dados Invalidos";
		}
        getResponse().redirect("/mvc/admin");
        return "OK";
	}
	
	public String novo() {
        Curso c = new Curso();
        Area a;
		try {
			a = new Area().load(getRequest().queryMap("area").integerValue());
		} catch (ClassNotFoundException e) {
			return "Area não encontrada";
		}
        c.setNome(getRequest().queryParams("nome"));
        c.setResumo(getRequest().queryParams("resumo"));
        c.setVaga(getRequest().queryMap("vagas").integerValue());
        c.setPreco(getRequest().queryMap("preco").doubleValue());
        c.setCarga_horaria(getRequest().queryMap("carga_horaria").integerValue());
        try {
			c.setData_inicio(ISODateFormat.parse(getRequest().queryParams("data_inicio")));
			c.setData_termino(ISODateFormat.parse(getRequest().queryParams("data_termino")));
	 		c.setHorario_inicio(new java.sql.Time(ISOTimeFormat.parse(getRequest().queryParams("horario_inicio")).getTime()));
	        c.setHorario_termino(new java.sql.Time(ISOTimeFormat.parse(getRequest().queryParams("horario_termino")).getTime()));
        } catch (ParseException e) {
			return "Data invalida";
		}        
        if (getRequest().queryParamsValues("dias") != null) c.setDias(String.join(", ", getRequest().queryParamsValues("dias")));
        c.setPrograma(getRequest().queryParams("programa"));
        c.setImagem(null);
        c.setArea(a);

        //faltando area
        try {
			c.validar();
		} catch (Exception e) {
			return "Não validou";
		}
        
        if (!c.isValido()) {
        	viewBag.put("erro", c.getErros());
        	return view("/admin/novocurso.pebble");
        }
        GenericDAO cursos = new GenericDAO();
	    cursos.persiste(c);
	    getResponse().redirect("/mvc/admin");
	    return "OK";
	}
	
	public String cursos() {
		Session();
		GenericDAO dao = new GenericDAO();
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
        	Map<Integer,ArrayList<Object>> temporario = dao.select(new Curso().getClass().getName());
        	Curso curso = null;
        	for (int j = 0; j < temporario.size(); j++) {
        		curso = new Curso().load((Integer) temporario.get(j).get(0));
        		String sql = "select * from inscricoes where  id_curso = ?";
        		dao.abreConexao();
        		dao.comando(sql);
        		dao.comandoinuse().setInt(1, curso.getId());
        		dao.result();
        		int contador = 0;
        		while (dao.resultados().next()) contador++;
        		dao.fechaConexao();
        		curso.setInscritos(contador);
        		cursos.add(curso);
        	}
        } catch (ClassNotFoundException e) {
        	return "Classe não encontrada";
        } catch (SQLException e) {
			return "SQL não encontrada";
		}
        viewBag.put("cursos", cursos);
		return view();
	}
	
	public String curso(int id) {
		try {
            dao.abreConexao();
            System.out.println("entro");
            Curso curso = new Curso().load(id);
            String sql = "SELECT * from inscricoes where id_curso = ?";
            int contador = 0;
            dao.comando(sql);     
            dao.comandoinuse().setInt(1, curso.getId());
            dao.result();
            while (dao.resultados().next()) contador++;
            curso.setInscritos(contador);
            viewBag.put("curso", curso);
            dao.fechaConexao();  
          } catch (Exception e) {
            throw new RuntimeException(e);
          } 
          return view();
	}
	
	public String createBanco() {
		try {     
        	dao.abreConexao();
          
        	dao.comandoStat().execute (
        		  "CREATE TABLE usuarios (id serial NOT NULL PRIMARY KEY, nome VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, senha VARCHAR(32) NOT NULL, saldo numeric(12,2) DEFAULT 0 NULL ,status INTEGER DEFAULT 0 NOT NULL, token CHAR(36) NULL, admin Integer Default 0 NOT NULL)");

        	dao.comandoStat().execute(
        		  "CREATE TABLE areas (id serial NOT NULL PRIMARY KEY, nome VARCHAR(20) NOT NULL)");

        	dao.comandoStat().execute(
        		  "INSERT INTO areas (nome) VALUES ('Artes'),('Beleza'),('Comunicação'),('Informática'),('Gastronomia'),('Idiomas'),('Moda'),('Saúde')");

        	dao.comandoStat().execute(
        		  "CREATE TABLE cursos (id serial NOT NULL PRIMARY KEY, nome VARCHAR(50) NOT NULL, resumo VARCHAR(100), programa VARCHAR(500), preco numeric(12,2) NOT NULL,vagas INTEGER NOT NULL, data_inicio DATE NOT NULL, data_termino DATE NOT NULL, dias VARCHAR(28) NOT NULL, horario_inicio TIME NOT NULL, horario_termino TIME NOT NULL, carga_horaria INTEGER NOT NULL, imagem ByteA, tipo_imagem VARCHAR(3), id_area INTEGER NOT NULL, CONSTRAINT area_fk FOREIGN KEY (id_area) REFERENCES areas (id))");

        	dao.comandoStat().execute(
        		  "CREATE TABLE inscricoes (id serial NOT NULL PRIMARY KEY,id_usuario INTEGER NOT NULL, id_curso INTEGER NOT NULL, concluiu BOOLEAN DEFAULT FALSE NOT NULL, Constraint foreigncurso FOREIGN KEY (id_curso) REFERENCES cursos (id) ON DELETE CASCADE)");          
        	dao.fechaConexao();
        	return "OK";
        } catch (SQLException sqle) {
        	return "Não foi Possivel Criar o banco" + sqle;
        }
	}
	
	public String dropbanco() {
		   try {
	        	dao.abreConexao();
	        	dao.comandoStat().execute("DROP TABLE IF EXISTS inscricoes");
	        	dao.comandoStat().execute("DROP TABLE IF EXISTS usuarios");          
	        	dao.comandoStat().execute("DROP TABLE IF EXISTS cursos");
	        	dao.comandoStat().execute("DROP TABLE IF EXISTS areas");
	        } catch (SQLException sqle) {
	        	return "Não foi possivel criar o banco \n" + sqle;
	        }         
	        return "OK";
	}
}

