package cobaia.mvc;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class FrontController {
	
	private TemplateEngine engine = null;
	private ApplicationController appController;
	
	public FrontController(TemplateEngine pebble) {
		this.engine = pebble;
	}
	
	public Route getHandler = new Route() {
		public Object handle(Request req, Response resp) throws Exception {
			appController = new ApplicationController(req,resp,engine);
			return appController.action();
		}
	};
	
	public Route postHandler = new Route() {
		public Object handle(Request req, Response resp) throws Exception {
			appController = new ApplicationController(req,resp,engine);
			return appController.action();
		}
	};

}
