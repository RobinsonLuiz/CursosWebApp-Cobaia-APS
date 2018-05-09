package cobaia.mvc.controllers;

import java.util.UUID;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import cobaia.Modelo.Usuario;

public class RegistroController extends AbstractController {
	
	String SALT = "cobaiaforever";
	
	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String index() {
		Session();
		return view();
	}
	
	public String finalizar() {
		Session();
		Usuario u = new Usuario();
		String uid = UUID.randomUUID().toString().split("-")[0];
        u.setNome(getRequest().queryParams("nome"));
        u.setEmail(getRequest().queryParams("email"));
        u.setSenha(getRequest().queryParams("senha") + SALT);
        u.setVerificaSenha(getRequest().queryParams("senha2") + SALT);
        u.setToken(uid);
        u.setSaldo((double) 0);
        try {
        	u.validar();
        
        	if (!u.isValido()) {
	        	viewBag.put("erro", u.getErros());
	        	return view("registro/index.pebble");
	        }
	        sendEmail(uid,u);
	        viewBag.put("registrar", u);
	        dao.abreConexao();
	        String sql = "SELECT status FROM usuarios WHERE email = ?";
	        dao.comando(sql);
	        dao.comandoinuse().setString(1, u.getEmail());
	        dao.result();
	        if (dao.resultados().next()) {
	        	viewBag.put("erro", "Este e-mail já está cadastrado.");         
	            return view("registro/index.pebble");
	        } 
	        u.save();
        } catch (Exception e) {
        	return "VSF";
        }
        getResponse().redirect("/mvc/ativar");
        return "OK";
	}
	
	public String sendEmail(String uid,Usuario u) {
		try {
			HtmlEmail mailer = new HtmlEmail();  
	        mailer.setHostName("smtp.googlemail.com");
	        mailer.setSmtpPort(465);
	        mailer.setAuthenticator(new DefaultAuthenticator("nao.responda.ifrs.riogrande@gmail.com", System.getenv("COBAIA_MAIL_PASSWORD")));
	        mailer.setSSLOnConnect(true);
	        mailer.setFrom("nao.responda.ifrs.riogrande@gmail.com");
	        mailer.setSubject("[Cobaia] Confirmar seu registro");
	        mailer.setHtmlMsg("Olá " + u.getNome() + "<br><br>Confirme sua conta com esse código: " + u.getToken() + " ou, se preferir, clique nesse link: <a href=\"http://localhost:4567/mvc/ativar/" + u.getToken() + "\">http://localhost:4567/mvc/ativar/" + u.getToken() + "</a> para direcioná-lo diretamente");
	        mailer.addTo(u.getEmail());
	        mailer.send();
		} catch (EmailException e) {
			return "não foi possivel enviar o email";
		}
		return "OK";
	}
	
	public String reenviar() {
		Session();
		return view();
	}
	
	public String confirmaReenvio() throws Exception {
		Session();
		String email = getRequest().queryParams("email");
        viewBag.put("email", email);
        if (!email.matches("[\\w._]+@\\w+(\\.\\w+)+")) {
          viewBag.put("erro", "E-mail inválido, ele deve ter o formato de usuario@provedor");         
          return view("registro/reenviar.pebble");
        }
        Usuario u = null;
        dao.abreConexao();
        String sql = "SELECT id, status, nome FROM usuarios WHERE email = ?";
        dao.comando(sql);
        dao.comandoinuse().setString(1, email);
        dao.result(); 
        if (dao.resultados().next()) {
        	if (dao.resultados().getInt("status") > 0) {
        		viewBag.put("info", "Esta conta já está ativada, você pode fazer o login.");
        		return view("login/index.pebble");
            } else {
            	u = new Usuario().load(dao.resultados().getInt("id"));
            }
        } else {
        	viewBag.put("erro", "Este e-mail não existe no nosso sistema, você pode fazer o cadastro.");
            return view("registro/index.pebble");
        }
        String uid = UUID.randomUUID().toString().split("-")[0];
        sendEmail(uid,u);
        viewBag.put("info", "O código de ativação foi enviado com sucesso, verifique seu email");
        return view("ativar/index.pebble");
    }
}
