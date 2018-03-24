package cobaia.Modelo;

public class Usuario extends AbstractModel {
	
	//Status do usuario
	enum Status {
		REGISTRADO,ATIVADO
	}
	private String verificaSenha;
	private String nome;
	private String senha;
	private String email;
	private Status status;
	private String token;
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getVerificaSenha() {
		return verificaSenha;
	}
	
	public void setVerificaSenha(String verificaSenha) {
		this.verificaSenha = verificaSenha;
	}
	
	@Override
	public void validar() {
		verificaLength("nome", this.nome, 5, 50);
		verificaSenha("senha", this.senha,this.verificaSenha, 5, 20);
		verificaEmail("email", this.email);
	}
}
