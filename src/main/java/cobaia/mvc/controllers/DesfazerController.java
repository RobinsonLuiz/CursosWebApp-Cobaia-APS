package cobaia.mvc.controllers;

import cobaia.Modelo.Curso;
import cobaia.Modelo.Usuario;

public class DesfazerController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String inscricao(int id) throws Exception {
		  Session();
          dao.abreConexao();
          String sql = "delete from inscricoes where id_usuario = ? and id_curso = ?";
          dao.comando(sql);
          Usuario u = new Usuario().load(getRequest().session().attribute("id"));
          Curso c = new Curso().load(id);
          dao.comandoinuse().setInt(1, (Integer) getRequest().session().attribute("id"));
          dao.comandoinuse().setInt(2, id);
          dao.comandoinuse().execute();
          String sql2 = "update usuarios set saldo = ? where id = ?";
          dao.comando(sql2);
          Double resposta = null;
          String atributo = (u.getSaldo() + c.getPreco()) + "";
          if (atributo.length() < 5) resposta = Double.parseDouble(atributo.toString());
          else resposta = Double.parseDouble(atributo.toString().substring(0, 5));
          getRequest().session().attribute("saldo", resposta);
          dao.comandoinuse().setDouble(1, resposta);
          dao.comandoinuse().setInt(2, u.getId());
          dao.comandoinuse().execute();
          dao.fechaConexao();
          getResponse().redirect("/mvc/curso/busca/" + id);
          return "OK";
	}

}
