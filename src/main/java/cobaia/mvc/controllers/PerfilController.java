package cobaia.mvc.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import cobaia.Modelo.Curso;

public class PerfilController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}

	public String index() throws ClassNotFoundException, SQLException {
	  Session();
  	  dao.abreConexao();
  	  String sql = "Select id_curso from inscricoes i "
      		+ "where id_usuario = ?";
  	  dao.comando(sql);
  	  dao.comandoinuse().setInt(1, getRequest().session().attribute("id"));
  	  dao.result();
  	  ArrayList<Curso> cursos = new ArrayList<>();
  	  while (dao.resultados().next()) {
      	  Curso curso = new Curso().load(dao.resultados().getInt("id_curso"));
          cursos.add(curso);
  	  }
  	  viewBag.put("cursos", cursos);
  	  return view();
	}
}
