package cobaia.mvc.controllers;

import cobaia.Modelo.Curso;
import cobaia.Modelo.Usuario;

public class InscreverController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String inscricao(int id) throws Exception {
        
		Session();
        dao.abreConexao();
        String sql = "select * from inscricoes where id_usuario = ? and id_curso = ?";
        dao.comando(sql);
        dao.comandoinuse().setInt(1, (Integer) getRequest().session().attribute("id"));
        dao.comandoinuse().setInt(2, id);
        dao.result();
        Usuario u = new Usuario().load(getRequest().session().attribute("id"));
        Curso c = new Curso().load(id);
        if (u.getSaldo() >= c.getPreco() && !dao.resultados().next()) {
        	String sql2 = "Update usuarios set saldo = ? where id = ?";
        	dao.comando(sql2);
        	dao.comandoinuse().setDouble(1, u.getSaldo() - c.getPreco());
        	dao.comandoinuse().setInt(2, u.getId());
        	dao.comandoinuse().execute();
        	Double resposta = null;
        	String atributo = (u.getSaldo() - c.getPreco()) + "";
            if (atributo.length() < 5) resposta = Double.parseDouble(atributo.toString());
            else resposta = Double.parseDouble(atributo.toString().substring(0, 5));
	        getRequest().session().attribute("saldo", resposta);
            sql = "INSERT INTO inscricoes "
		         + "(id_usuario, id_curso) VALUES (?, ?);";
	        dao.comando(sql);
		    dao.comandoinuse().setInt(1, (int) getRequest().session().attribute("id"));
		    dao.comandoinuse().setInt(2, id);
		    dao.comandoinuse().execute();
        	
        } else {
        	viewBag.put("erro", "você não tem saldo suficiente para comprar esse curso");
        	getResponse().redirect("/mvc/curso/busca/" + id);
        }
        dao.fechaConexao();
        getResponse().redirect("/mvc/curso/busca/" + id);
		return "OK";
	}

}
