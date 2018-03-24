package cobaia.Modelo;

public class Area extends AbstractModel {
	private Integer id;
	private String nome;
	@Override
	protected void validar() {
		verificaLength("nome",this.getNome(),5,50);
	}
	
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

}
