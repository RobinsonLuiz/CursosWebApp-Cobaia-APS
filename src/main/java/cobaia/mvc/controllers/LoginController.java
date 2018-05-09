package cobaia.mvc.controllers;



public class LoginController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String index() {
		Session();
		return view();
	}
	
	public String logout() {
		getRequest().session().removeAttribute("usuario");
		getRequest().session().removeAttribute("admin");
		getRequest().session().removeAttribute("email");
		getRequest().session().removeAttribute("id");
		getRequest().session().removeAttribute("saldo");
		getResponse().redirect("/mvc/index");
		return "OK";
	}
	
	public String confirmar() {
		Session();
		String email = getRequest().queryParams("email");
	    viewBag.put("email", email);
	    String senha = getRequest().queryParams("senha");
	    boolean encontrado = false;
	    try {
	    	String SALT = "cobaiaforever";
	      	dao.abreConexao();
	       	String sql = "SELECT id, nome, email, status, saldo FROM usuarios WHERE email = ? AND senha = ?";
	       	dao.comando(sql);
	       	dao.comandoinuse().setString(1, email);
	       	dao.comandoinuse().setString(2, senha + SALT);
	       	dao.result();
	       	if (dao.resultados().next()) {
	       		if (dao.resultados().getInt("status") == 0) {              
	       			viewBag.put("erro", "Conta não está ativada, digite o código recebido no e-mail para ativá-la");
	       			return view("ativar/index.pebble");
	       		}
		        getRequest().session().attribute("id", dao.resultados().getInt("id"));
		        getRequest().session().attribute("usuario", dao.resultados().getString("nome"));
		        getRequest().session().attribute("email", dao.resultados().getString("email"));
		        getRequest().session().attribute("saldo", dao.resultados().getDouble("saldo"));
		        encontrado = true;
	       	}
	       	dao.fechaConexao();
	        } catch (Exception sqle) {
	        	throw new RuntimeException(sqle);
	        }
	        if (encontrado) {
	          getResponse().redirect("/mvc/perfil");
	        } else {
	          viewBag.put("erro", "E-mail e/ou senha não encontrados");
	          return view("login/index.pebble");
	        }
	        return "OK";
	}
}
