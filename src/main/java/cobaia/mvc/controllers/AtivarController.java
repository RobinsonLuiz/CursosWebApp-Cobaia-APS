package cobaia.mvc.controllers;

import java.sql.SQLException;

public class AtivarController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String index() {
		Session();
		viewBag.put("info", "seu codigo de confirmação foi enviado, por favor verifique seu email");
		return view();
	}
	
	public String finalizar() throws SQLException {
		Session();
		String codigo = getRequest().queryParams("codigo");
        String email = getRequest().queryParams("email");
        Integer ativado = 0;
        
        dao.abreConexao();
        String sql = "UPDATE usuarios SET status = 1, token = NULL "
                     + "WHERE token = ? AND email = ? AND status = 0";
        dao.comando(sql);
        dao.comandoinuse().setString(1, codigo);
        dao.comandoinuse().setString(2, email);
        ativado = dao.comandoinuse().executeUpdate();
        dao.fechaConexao();
        if (ativado > 0) {
          viewBag.put("info", "Sua conta foi ativada! Entre com seu e-mail e senha para fazer o login.");
          return view("login/index.pebble");
        } else {
          viewBag.put("erro", "Código não encontrado. Talvez você já tenha ativado sua conta. Tente fazer o login." + ativado);
          return view("ativar/index.pebble");
        }
	}

}
