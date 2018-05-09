package cobaia.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cobaia.Annotations.Colunas;
import cobaia.Annotations.FK;
import cobaia.Annotations.Tabela;
import cobaia.persistencia.GenericDAO;

@Tabela(nome = "inscricoes")
public class Inscritos extends AbstractModel {
	
	@Colunas @FK(nome = "id_usuario")
	private Usuario usuario;
	
	@Colunas @FK(nome = "id_curso")
	private Curso curso;
	private GenericDAO dao = new GenericDAO();
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	@Override
	public Inscritos load(Integer cod) throws ClassNotFoundException {
		Map<Integer,ArrayList<Object>> inscritos = new HashMap<>();
		Inscritos inscrito = new Inscritos();
		inscritos = dao.select(this.getClass().getName(), cod);
		for (int i = 0; i < inscritos.size(); i++) {
			int k = 0;
			inscrito.setId((Integer) inscritos.get(i).get(k));
			inscrito.setUsuario(new Usuario().load((Integer) inscritos.get(i).get(++k)));
			inscrito.setCurso(new Curso().load((Integer) inscritos.get(i).get(++k)));
		}
		if (inscrito.getUsuario() == null) return null;
		else return inscrito;
	}
	
	@Override
	public String toString() {
		return "Inscritos [usuario=" + usuario.getNome() + ", curso=" + curso.getNome() + "]";
	}

	@Override
	protected void doSave() {
		dao.persiste(this);
	}
	
	@Override
	protected void delete(int id) {
		dao.delete(this.getClass().getName(), id);
	}
}
