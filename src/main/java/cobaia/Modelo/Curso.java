package cobaia.Modelo;

import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cobaia.Annotations.Colunas;
import cobaia.Annotations.FK;
import cobaia.Annotations.Tabela;
import cobaia.Annotations.ValidaLength;
import cobaia.persistencia.GenericDAO;

@Tabela(nome = "cursos")
public class Curso extends AbstractModel {
	
	@Colunas @ValidaLength(min = 5, max = 20)
	private String nome; 
	@Colunas @ValidaLength(min = 10,max = 10000)
	private String resumo;
	@Colunas
	private int vagas;
	@Colunas
	private Integer carga_horaria; 
	@Colunas
	private Date data_inicio; 
	@Colunas
	private Date data_termino; 
	@Colunas
	private String dias; 
	@Colunas
	private Time horario_inicio; 
	@Colunas
	private Time horario_termino; 
	@Colunas
	private String programa; 
	@Colunas
	private byte[] imagem;
	private InputStream imagemInputStream = null;
	@Colunas
	private String tipo_Imagem = null; 
	@Colunas @FK(nome = "id_area")
	private Area area;	
	@Colunas
	private Double preco;
	private Integer inscritos;
    private GenericDAO dao = new GenericDAO();
	
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
		return vagas;
	}
	
	public void setVaga(Integer invalid) {
		this.vagas = invalid;
	}
	
	public int getCarga_horaria() {
		return carga_horaria;
	}
	
	public void setCarga_horaria(Integer invalid) {
		this.carga_horaria = invalid;
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
	
	public Time getHorario_inicio() {
		return horario_inicio;
	}
	
	public void setHorario_inicio(Time Date) {
		this.horario_inicio = Date;
	}
	
	public Time getHorario_termino() {
		return horario_termino;
	}
	
	public void setHorario_termino(Time date) {
		this.horario_termino = date;
	}
	
	public String getPrograma() {
		return programa;
	}
	
	public void setPrograma(String string) {
		this.programa = string;
	}
	
	public byte[] getImagem() {
		return imagem;
	}
	
	public void setImagem(byte[] b) {
		this.imagem = b;
	}
	
	public InputStream getImagemInputStream() {
		return imagemInputStream;
	}

	public void setImagemInputStream(InputStream imagemInputStream) {
		this.imagemInputStream = imagemInputStream;
	}

	public String getTipoImagem() {
		return tipo_Imagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipo_Imagem = tipoImagem;
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

	public Double getPreco() {
		return preco;
	}
	
	@Override
	protected void doSave() {
		dao.persiste(this);
	}

	@Override
	public Curso load(Integer cod) throws ClassNotFoundException {
		Map<Integer,ArrayList<Object>> cursos = new HashMap<>();
		Curso curso = new Curso();
		cursos = dao.select(this.getClass().getName(), cod);
		for (int i = 0; i < cursos.size(); i++) {
			int k = 0;
			curso.setId((Integer) cursos.get(i).get(k));
			curso.setNome((String) cursos.get(i).get(++k));
			curso.setResumo((String) cursos.get(i).get(++k));
			curso.setVaga((Integer) cursos.get(i).get(++k));
			curso.setCarga_horaria((Integer) cursos.get(i).get(++k));
			curso.setData_inicio((Date) cursos.get(i).get(++k));
			curso.setData_termino((Date) cursos.get(i).get(++k));
			curso.setDias((String) cursos.get(i).get(++k));
			curso.setHorario_inicio((Time) cursos.get(i).get(++k));
			curso.setHorario_termino((Time) cursos.get(i).get(++k));
			curso.setPrograma((String) cursos.get(i).get(++k));
			curso.setImagem(null);
			k++;
			curso.setTipoImagem((String) cursos.get(i).get(++k));
			curso.setArea(new Area().load((Integer) cursos.get(i).get(++k)));
			curso.setPreco((Double.parseDouble(cursos.get(i).get(++k).toString())));
		}
		return curso;
	}

	@Override
	public String toString() {
		return "Curso [id = " + id + " nome=" + nome + ", resumo=" + resumo + ", inscritos=" + inscritos + ", vaga=" + vagas + ", area="
				+ area + ", carga_horaria=" + carga_horaria + ", data_inicio=" + data_inicio + ", data_termino="
				+ data_termino + ", dias=" + dias + ", horario_inicio=" + horario_inicio + ", horario_termino="
				+ horario_termino + "]";
	}

	@Override
	protected void delete(int id) {
		dao.delete(this.getClass().getName(),id);
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

}
