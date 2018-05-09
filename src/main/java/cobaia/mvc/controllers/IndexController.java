package cobaia.mvc.controllers;

public class IndexController extends AbstractController {

	@Override
	protected String controller() {
		int c = this.getClass().getSimpleName().toLowerCase().indexOf("controller");
		return this.getClass().getSimpleName().toLowerCase().substring(0,c);
	}
	
	public String index() {
		 Session();
		 return view();
	}
}
