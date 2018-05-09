package cobaia.mvc.controllers;

import java.util.ArrayList;
import cobaia.Modelo.Curso;
import cobaia.Modelo.Inscritos;

public class CursoController extends AbstractController {

	public String index() {
		Session();
        try {
          dao.abreConexao();
          String sql = "SELECT * FROM cursos LIMIT 10";
          dao.comando(sql);          
          dao.result();
          ArrayList<Curso> cursos = new ArrayList<>();
          while (dao.resultados().next()) {
            Curso curso = new Curso();
            curso.setNome(dao.resultados().getString("nome"));
            curso.setId(dao.resultados().getInt("id"));
            curso.setResumo(dao.resultados().getString("resumo"));
            curso.setPreco(dao.resultados().getDouble("preco"));
            cursos.add(curso);
          }
          dao.fechaConexao();
          viewBag.put("cursos", cursos);
        } catch (Exception e) {
          throw new RuntimeException(e);
        } 
        return view();
	}

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String busca(int i) {
		Session();
	    try {
	    	dao.abreConexao();
	        String sql = "SELECT c.*, a.nome AS area, "
	        		+ "(SELECT COUNT(*) FROM inscricoes "
	        		+ "WHERE id_curso = c.id) AS inscritos "
	        		+ "FROM cursos AS c JOIN areas AS a ON "
	        		+ "c.id_area = a.id WHERE c.id = ?";
	        dao.comando(sql);     
	        dao.comandoinuse().setInt(1, i);
	        dao.result();
	        Curso curso = null;
	        if (dao.resultados().next()) {
	        	curso = new Curso().load(dao.resultados().getInt("id"));
	            curso.setInscritos(dao.resultados().getInt("inscritos"));
	            viewBag.put("curso", curso);
	         }  else {
	        	 	getResponse().status(404);
	        	 	return "Curso " + i + " não encontrado";
	         	}
	         if (u.getId() != null) {
	        	sql = "select * from inscricoes i "
	        			+ "where id_curso = ? and id_usuario = ?";
	       	 	dao.comando(sql);
	       	 	dao.comandoinuse().setInt(1, curso.getId());
	       	 	dao.comandoinuse().setInt(2, u.getId());
	       	 	dao.result();
	       	 	if (dao.resultados().next()) {
	       	 		Inscritos insc = new Inscritos();
	       	 		insc.setCurso(curso);
	       	 		insc.setUsuario(u);
	       	 		viewBag.put("inscritos", insc);
	       	 	}
	         }
	         dao.fechaConexao();
	        } catch (Exception e) {
	        	throw new RuntimeException(e);
	       }
	return view();
	}
}
