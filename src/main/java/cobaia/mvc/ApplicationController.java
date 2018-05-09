package cobaia.mvc;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.lang.WordUtils;
import cobaia.Annotations.Controlador;
import cobaia.mvc.controllers.*;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

public class ApplicationController {
	
	private final String pacote = "cobaia.mvc.controllers.";
	private Request request;
	private Response response;
	private TemplateEngine engine;
	private ListingPackages lp = new ListingPackages();

	public ApplicationController(Request req, Response resp, TemplateEngine engine) {
		this.request = req;
		this.response = resp;
		this.engine = engine;
		URL resource = this.getClass().getResource("controllers");
		this.lp.printPathFileList(resource.getPath());
	}

	public Object action() {
	
		String[] mapper = this.request.pathInfo().split("/");
		String metodo = "";
		String controller = "";
		if (mapper.length == 2) {
			controller = this.pacote + "Index" + "Controller";
			metodo = "index";
		}
		else {
			controller = this.pacote + WordUtils.capitalize(mapper[2]) + "Controller";
			if (mapper.length == 3) metodo = "index";
			else {
				if (mapper.length == 4 && mapper[3].isEmpty()) metodo = "index";
				else metodo = mapper[3].toLowerCase();
			}
		}
		AbstractController controlador = null;
		Method[] metodos = null;
		ArrayList<Class<?>> classes = new ArrayList<>();
		try {
			for (int i = 0; i < this.lp.getClasses().size(); i++) {
				int t = this.lp.getClasses().get(i).indexOf(".");
				Class<?> temporarias = Class.forName(this.pacote + (lp.getClasses().get(i).substring(0, t)));			
				classes.add(temporarias);
			}
			for (int i = 0; i < classes.size(); i++) {
				if (classes.get(i).isAnnotationPresent(Controlador.class) && !classes.get(i).getName().contains("Controller")) {
					String clz = classes.get(i).getName() + "Controller";
					if (clz.equalsIgnoreCase(controller)) {
						controlador = (AbstractController) classes.get(i).newInstance();
						controlador.setControladorAnonnation(classes.get(i).getSimpleName().toLowerCase());
						metodos = classes.get(i).getMethods();
						break;
					} else {
						if (!classes.get(i).getAnnotation(Controlador.class).nome().isEmpty()) {
							clz = this.pacote + classes.get(i).getAnnotation(Controlador.class).nome();
							if (clz.equalsIgnoreCase(controller)) {
								controlador = (AbstractController) classes.get(i).newInstance();
								int c = classes.get(i).getAnnotation(Controlador.class).nome().indexOf("Controller");
								controlador.setControladorAnonnation(classes.get(i).getAnnotation(Controlador.class).nome().toLowerCase().substring(0,c));
								metodos = classes.get(i).getMethods();
								break;
							} else {
								clz += "Controller";
								if (clz.equalsIgnoreCase(controller)) {
									controlador = (AbstractController) classes.get(i).newInstance();
									controlador.setControladorAnonnation(classes.get(i).getAnnotation(Controlador.class).nome().toLowerCase());
									metodos = classes.get(i).getMethods();
									break;
								}
							}
						}
					}
				}
				if (classes.get(i).getName().equalsIgnoreCase(controller)) {
					controlador = (AbstractController) classes.get(i).newInstance();
					metodos = classes.get(i).getMethods();
					break;
				}
			}
		} catch (Exception e) {
			response.status(404);
			return "Classe não encontrada";
		}
		//caso o metodo não tenha sido encontrado
		if (metodos == null) {
			response.status(404);
			return "Endereço não encontrado";
		}
		//invoca os metodos
		for (Method m : metodos) {
			if (m.getName().equalsIgnoreCase((metodo))) {
				controlador.init(this.request, this.response, metodo, engine);
				int id = 0;
				if (mapper.length > 4) {
					for (int i = 0; i < mapper[4].length(); i++) {
						if ("aeioubcdefghijmnopqrstuv".indexOf(mapper[4].toLowerCase().charAt(i)) != -1)
							try {
								return m.invoke(controlador, mapper[4]);
							} catch (IllegalAccessException | IllegalArgumentException
									| InvocationTargetException e) {
								return "Por favor para esse tipo de busca utilique numéricos";
							}
					}
					id = Integer.parseInt(mapper[4]);
					try {
						return m.invoke(controlador, id);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						return "a busca pelo Objeto " + id + " não foi encontrada";
					}
				}
				try {
					return m.invoke(controlador);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					return "o método " + metodo + " não foi encontrado";
				}
			}
		}
		response.status(404);
		return "o método: " + metodo + " nao foi encontrado";
	}
}
