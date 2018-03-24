package cobaia.Modelo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.Part;

public class Curso extends AbstractModel {

	private String nome; 
	private String resumo;
	private Integer inscritos;
	private int vaga;
	private Area area = new Area();
	private int carga_horaria; 
	private Date data_inicio; 
	private Date data_termino; 
	private String dias; 
	private Date horario_inicio; 
	private Date horario_termino; 
	private String programa; 
	private Part imagem;
	private InputStream imagemInputStream = null;
    private String tipoImagem = null; 
	private Integer id;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getResumo() {
		return resumo;
	}
	
	public void setResumo(String resumo) {
		this.resumo = resumo;
	}
	
	public int getVaga() {
		return vaga;
	}
	
	public void setVaga(int vaga) {
		this.vaga = vaga;
	}
	
	public int getCarga_horaria() {
		return carga_horaria;
	}
	
	public void setCarga_horaria(int carga_horaria) {
		this.carga_horaria = carga_horaria;
	}
	
	public java.util.Date getData_inicio() {
		return data_inicio;
	}
	
	public void setData_inicio(Date date) {
		this.data_inicio = date;
	}
	
	public Date getData_termino() {
		return data_termino;
	}
	
	public void setData_termino(Date date) {
		this.data_termino = date;
	}
	
	public String getDias() {
		return dias;
	}
	
	public void setDias(String string) {
		this.dias = string;
	}
	
	public Date getHorario_inicio() {
		return horario_inicio;
	}
	
	public void setHorario_inicio(Date Date) {
		this.horario_inicio = Date;
	}
	
	public Date getHorario_termino() {
		return horario_termino;
	}
	
	public void setHorario_termino(Date date) {
		this.horario_termino = date;
	}
	
	public String getPrograma() {
		return programa;
	}
	
	public void setPrograma(String string) {
		this.programa = string;
	}
	
	public Part getImagem() {
		return imagem;
	}
	
	public void setImagem(Part part) {
		this.imagem = part;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public void validar() {
		if (this.imagem.getSize() > 0) {
	          try {
				imagemInputStream = imagem.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        tipoImagem = imagem.getContentType().split("/")[1];
	          //Image reader = new Image(imagemInputStream);
	          // return reader.getWidth() + "/" + reader.getHeight();
	        }
		
        verificaLength("nome",this.nome, 5, 50);
        
        verificaLength("resumo",this.resumo,10,1000);
        
        if (this.getVaga() < 1) addErro("vagas","a vaga não pode ser inferior a 1");
       
        if (this.getCarga_horaria() < 10) addErro("cargahorario","a carga horario tem que ser superior a 10");
        
        if (this.getDias() == null || this.getDias().length() == 0) addErro("dias","por favor selecione pelo menos um dia da semana");
        
        if (this.area.getId() == null) addErro("area","por favor escolhe uma área para o curso");
        
        verificaData("data",this.getData_inicio(),this.getData_termino()); 
        
        verificaHorario("horario",this.getHorario_inicio(),this.getHorario_termino());
	}

	public InputStream getImagemInputStream() {
		return imagemInputStream;
	}

	public void setImagemInputStream(InputStream imagemInputStream) {
		this.imagemInputStream = imagemInputStream;
	}

	public String getTipoImagem() {
		return tipoImagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipoImagem = tipoImagem;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getInscritos() {
		return inscritos;
	}

	public void setInscritos(Integer inscritos) {
		this.inscritos = inscritos;
	}

}
