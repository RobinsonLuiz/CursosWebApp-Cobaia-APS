package cobaia.Modelo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.Data;

public abstract class AbstractModel {

	private Map<String,String> erros = new HashMap<>();
	
	protected abstract void validar();
	
	protected void addErro(String erro,String mensagem) {
		erros.put(erro, mensagem);
	}
	
	protected void verificaLength(String nome,String campo,int min,int max) {
		if (campo == null || campo.isEmpty()) {
			addErro(nome,"o " + nome + "não pode ser vazio");
		} else {
			if (campo.length() < min || campo.length() > max) 
				addErro(nome,"o " + nome + " deve conter entre " + 
						min + " e " + max + " caracteres");
		}
	}
	
	public boolean isValido() {
		return erros.isEmpty();
	}
	
	public Map<String,String> getErros() {
		return erros;
	}
	
	protected void verificaSenha(String nome, String senha, String senha2,int i, int j) {
		verificaLength(nome, senha, i, j);
		int cont = 0;
		int cont2 = 0;
		for (int j2 = 0; j2 < senha.length(); j2++) {
			if ((senha.charAt(j2) >= 'A' ) && (senha.charAt(j2) <= 'Z')) cont++;
			if ("123456789".indexOf(senha.charAt(j2)) != -1) cont2++;
		}
		if (!senha.equals(senha2)) addErro(nome,"as senhas não correspondem");
		if (cont == 0) addErro(nome,"a senha deve ter pelo menos uma letra maiscula");
		if (cont2 == 0) addErro(nome,"a senha deve ter pelo menos um número");
		if (cont2 == 0 && cont == 0) addErro(nome,"a senha deve ter pelo menos uma letra maiscula e um número");
	}
	
	protected void verificaEmail(String nome,String email) {
		if (email.isEmpty()) addErro(nome,"email não pode estar vazio");
		if (!email.matches("[\\w._]+@\\w+(\\.\\w+)+")) addErro(nome,"email não está correto");
	}
	
	protected void verificaHorario(String nome,Date horarioinicio,Date horariofinal) {
		if (horarioinicio == null) addErro(nome,"o " + nome + " inicial não pode ser nulo");
		if (horariofinal == null) addErro(nome,"o " + nome + " final não pode ser nulo");
		if (horarioinicio.after(horariofinal)) addErro(nome,"o " + horarioinicio + " não pode ser superior ao " + horariofinal );
	}
	
	protected void verificaData(String nome,Date datainicio,Date datafinal) {
		Date data = new Date(System.currentTimeMillis());
		if (datainicio.before(data)) addErro(nome,"a " + nome + " inicial não pode ser inferior a data atual do sistema");
		if (datafinal.before(data)) addErro(nome,"a " + nome + " final não pode ser inferior a data atual do sistema");
		if (datainicio == null) addErro(nome,"a " + nome + " inicial não pode ser nulo");
		if (datafinal == null) addErro(nome,"a " + nome + " final não pode ser nulo");
		if (datainicio.after(datafinal)) addErro(nome,"a " + datainicio + " não pode ser superior ao " + datafinal );
	}
}
