package cobaia.Modelo;

import cobaia.persistencia.GenericDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cobaia.Annotations.*;

@Tabela(nome = "areas")
public class Area extends AbstractModel {
	
	//LengthValidator validaLength = new LengthValidator();
	@ValidaLength(min = 5,max = 10)
	@Colunas
	public String nome;
	
	private GenericDAO dao = new GenericDAO();
	
	//@Override
	//public void validar() {
	//	validaLength.validate(this);
	//}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	protected void doSave() {
		dao.persiste(this);
	}

	@Override
	public Area load(Integer cod) throws ClassNotFoundException {
		Map<Integer,ArrayList<Object>> temp = new HashMap<>();
		Area area = new Area();
		temp = dao.select(this.getClass().getName(), cod);
		for (int i = 0; i < temp.size(); i++) {
			int k = 0;
			area.setId((Integer) temp.get(i).get(k));
			area.setNome((String) temp.get(i).get(++k));

		}
		return area;
	}

	@Override
	public String toString() {
		return "Area [id = " + id + " nome=" + nome + "]";
	}

	@Override
	protected void delete(int id) {
		dao.delete(this.getClass().getName(),id);	
	}

}
