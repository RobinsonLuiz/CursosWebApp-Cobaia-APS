package cobaia.mvc.controllers;

import java.util.HashMap;
import java.util.Map;

import cobaia.Modelo.Usuario;
import cobaia.persistencia.GenericDAO;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

public abstract class AbstractController {

	private Request request;
	private Response response;
	protected GenericDAO dao = new GenericDAO();
	protected Map<String, Object> viewBag = new HashMap<>();
	private String action;
    protected TemplateEngine engine;
    private String TemplatesPasta = "";
    protected Usuario u = new Usuario();

	public void init(Request req,Response resp,String action,TemplateEngine engine) {
		this.request = req;
		this.response = resp;
		this.action = action;
		this.engine = engine;
	}

	protected void Session() {
		if (getRequest().session().attribute("usuario") != null) {
			u.setId(getRequest().session().attribute("id"));
	        u.setEmail(getRequest().session().attribute("email"));
	        u.setNome((getRequest().session().attribute("usuario")));
	        u.setSaldo(getRequest().session().attribute("saldo"));
	        viewBag.put("usuario", u);       
		}
	}
	
	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}
	
	protected String view() {
		if (!TemplatesPasta.isEmpty()) return engine.render(new ModelAndView(viewBag,"mvc/templates/" + TemplatesPasta + "/" + action + ".pebble")); 
		return engine.render(new ModelAndView(viewBag,"mvc/templates/" + controller() + "/" + action + ".pebble"));
	}
	
	protected String view(String model) {
		return engine.render(new ModelAndView(viewBag,"mvc/templates/" + model));
	}
	
	protected abstract String controller();

	public void setControladorAnonnation(String nome) {
		this.TemplatesPasta = nome;
	}
}
