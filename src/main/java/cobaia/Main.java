package cobaia;
import com.mitchellbosecke.pebble.PebbleEngine;

import cobaia.mvc.FrontController;
import cobaia.view.helper.MyPebbleExtensions;
import spark.Spark;
import spark.TemplateEngine;
import spark.template.pebble.PebbleTemplateEngine;
public class Main {
	
	public static void main(String[] args) {
		Spark.staticFileLocation("/public"); 
		PebbleEngine engine = new PebbleEngine.Builder().extension(new MyPebbleExtensions()).build();
	    final TemplateEngine pebble = new PebbleTemplateEngine(engine);
		FrontController frontController = new FrontController(pebble);
		Spark.get("/mvc/*", frontController.getHandler);
		Spark.post("/mvc/*", frontController.postHandler);
	}
}
