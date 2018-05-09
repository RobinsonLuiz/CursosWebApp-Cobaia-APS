package cobaia.view.helper;

import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.extension.Filter;

public class MoedaFilters implements Filter {

	@Override
	public List<String> getArgumentNames() {
		return null;
	}

	@Override
	public Object apply(Object o, Map<String, Object> map) {
		if (!(o instanceof Number)) return o;
		String valor = o.toString();
		if (valor.length() == 2) valor += ".00";
		if (valor.length() == 4) valor += "0";
		if (valor.length() > 5) valor = valor.substring(0, 5);
		int reais = Integer.parseInt(valor.substring(0, valor.indexOf(".")));
		int centavos = Integer.parseInt(valor.substring(valor.indexOf(".") + 1,valor.length()));
		StringBuilder resposta = new StringBuilder();
		resposta.append("<span class=\"simbolo\"> R$ </span>  ");
		resposta.append("<span class=\"reais\"> " + reais + ",</span>");
		resposta.append("<span class=\"centavos\">" + centavos + "</span> \n");
		return resposta.toString();
	}

}
