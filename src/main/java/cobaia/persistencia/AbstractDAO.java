package cobaia.persistencia;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cobaia.Modelo.AbstractModel;

public abstract class AbstractDAO<TYPE extends AbstractModel> implements IDAO<TYPE> {
	//variaveis imutaveis
	private static final String USUARIO = "postgres";
	private static final String SENHA = "325140";
	private static final String URL = 
			"jdbc:postgresql://localhost/baseaps";
	
	//variaveis mutaveis
	private ResultSet resultados; //reserva os resultados dos comandos
	private PreparedStatement comando; 
	private Statement comando2;
	private Connection conexao;
	
	/**
	 * abre uma conexão com o banco
	 * @return conexão com o banco
	 */
	
	public void abreConexao() { //abre a conexao com o banco solicitado
		try {
		conexao = 
				DriverManager.getConnection(URL, USUARIO, SENHA);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @return comando preparedstatement com um sql sendo utilizado,para ser utilizado nos selects,update
	 */
	public PreparedStatement comando(String sql) {
		try {
			comando = 
					conexao.prepareStatement(sql);
			return comando;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * 
	 * @param sql
	 * @param keys
	 * @return comando preparado para fazer o insert no banco de dados
	 */
	
	public PreparedStatement comando(String sql, int keys) {
		try {
			comando = conexao.prepareStatement(sql, keys);
			return comando;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * 
	 * @return comando preparado para ver qual comando esta em uso no executar
	 */
	
	public PreparedStatement comandoinuse() {
		return this.comando;
	}
	
	/**
	 * 
	 * @return comando para um sql já pronto
	 */
	
	public Statement comandoStat() {
		try {
			comando2 = conexao.createStatement();
			return comando2;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @return retorna os resultados apartir de um comando sql
	 */
	public ResultSet result(String sql) {
		try {
			this.resultados = comando2.executeQuery(sql);
		return this.resultados;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @return resultados apartir de um comando ja executado em um sql
	 */
	
	public ResultSet result() {
		try {
			this.resultados = comando.executeQuery();
			return this.resultados;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 
	 * @return valores disponiveis no resultado da busca no banco no momento pedido
	 */

	public ResultSet resultados() {
		return this.resultados;
	}
	
	/**
	 * @param fecha a conexao com o banco de dados
	 * @throws SQLException
	 */
	public void fechaConexao() throws SQLException {
		if (conexao != null && !conexao.isClosed()) this.conexao.close();
	}
	
}