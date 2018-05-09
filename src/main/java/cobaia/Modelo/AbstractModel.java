package cobaia.Modelo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cobaia.Annotations.Colunas;
import cobaia.Validations.ConstructorValidator;

public abstract class AbstractModel {
	private Map<String,String> erros = new HashMap<>();
	
	ConstructorValidator v = new ConstructorValidator();
	
	//protected abstract void validar();
	public void validar() throws Exception {
		v.validate(this);
	}
	
	@Colunas
	protected Integer id;
	
	public void addErro(String erro,String mensagem) {
		erros.put(erro, mensagem);
	}
	
	public boolean isValido() {
		return erros.isEmpty();
	}
	
	public Map<String,String> getErros() {
		return erros;
	}
	
	protected void verificaSenha(String nome, String senha, String senha2,int i, int j) {
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
	
	protected void verificaHorario(String nome,Date horarioinicio,Date horariofinal) {
		if (horarioinicio == null) addErro(nome,"o " + nome + " inicial não pode ser nulo");
		if (horariofinal == null) addErro(nome,"o " + nome + " final não pode ser nulo");
		if (horarioinicio.after(horariofinal)) addErro(nome,"o " + horarioinicio + " não pode ser superior ao " + horariofinal );
	}
	
	@SuppressWarnings("unused")
	protected void verificaData(String nome,Date datainicio,Date datafinal) {
		Date data = new Date(System.currentTimeMillis());
		if (datainicio.before(data)) addErro(nome,"a " + nome + " inicial não pode ser inferior a data atual do sistema");
		if (datafinal.before(data)) addErro(nome,"a " + nome + " final não pode ser inferior a data atual do sistema");
		if (datainicio == null) addErro(nome,"a " + nome + " inicial não pode ser nulo");
		if (datafinal == null) addErro(nome,"a " + nome + " final não pode ser nulo");
		if (datainicio.after(datafinal)) addErro(nome,"a " + datainicio + " não pode ser superior ao " + datafinal );
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isPersistente() {
		return this.id != null;
	}
	
	public boolean save() throws Exception {
		validar();
		System.out.println(getErros());
		if (isValido()) {
			doSave();
			return true;
		}
		return false;
	}
	
	public abstract AbstractModel load(Integer c) throws ClassNotFoundException;
	
	protected abstract void doSave();
	
	protected abstract void delete(int id);
}
