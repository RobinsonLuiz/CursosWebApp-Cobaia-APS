package cobaia.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cobaia.Annotations.Colunas;
import cobaia.Annotations.Tabela;
import cobaia.Annotations.ValidaEmail;
import cobaia.Annotations.ValidaLength;
import cobaia.Annotations.ValidaSenha;
import cobaia.Validations.EmailValidator;
import cobaia.Validations.LengthValidator;
import cobaia.Validations.Validator;
import cobaia.persistencia.GenericDAO;

@Tabela(nome = "usuarios")
public class Usuario extends AbstractModel {
	//Status do usuario
	public enum Status {
		REGISTRADO,ATIVADO
	}
	@ValidaSenha(min = 3,max = 30,maiusculas = 1)
	private String verificaSenha;
	@Colunas @ValidaLength(min = 5,max = 25)
	private String nome;
	@Colunas @ValidaSenha(min = 3,max = 30,maiusculas = 1)
	private String senha;
	@Colunas @ValidaEmail
	private String email;
	@Colunas
	private Status status = Status.REGISTRADO;
	@Colunas
	private String token = UUID.randomUUID().toString().split("-")[0];
	private GenericDAO dao = new GenericDAO();
	@Colunas
	private Double saldo;
	
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
	protected void doSave() {
		dao.persiste(this);
	}

	@Override
	public Usuario load(Integer id) throws ClassNotFoundException {
		Map<Integer,ArrayList<Object>> usuarios = new HashMap<>();
		Usuario usuario = new Usuario();
		usuarios = dao.select(this.getClass().getName(), id);
		for (int i = 0; i < usuarios.size(); i++) {
			int k = 0;
			usuario.setId((Integer) usuarios.get(i).get(k));
			usuario.setNome((String) usuarios.get(i).get(++k));
			usuario.setSenha((String) usuarios.get(i).get(++k));
			usuario.setVerificaSenha((String) usuarios.get(i).get(k));
			usuario.setEmail((String) usuarios.get(i).get(++k));
			if ((Integer) usuarios.get(i).get(++k) == 1) usuario.setStatus(Status.ATIVADO);
			else usuario.setStatus(Status.REGISTRADO);
			usuario.setToken((String) usuarios.get(i).get(++k));
			usuario.setSaldo((Double.parseDouble(usuarios.get(i).get(++k).toString())));
		}
		return usuario;
	}

	@Override
	public String toString() {
		return "Usuario [id = " + id + " nome=" + nome + ", email=" + email + ", status=" + status + "]";
	}

	@Override
	public void delete(int id) {
		dao.delete(this.getClass().getName(),id);
	}

	public void setSaldo(Double i) {
		this.saldo = i;
	}
	
	public Double getSaldo() {
		return this.saldo;
	}
}
