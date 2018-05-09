package cobaia.persistencia;


import java.util.ArrayList;
import java.util.Map;

import cobaia.Modelo.AbstractModel;

public interface IDAO<TYPE extends AbstractModel> {
	// CRUD
	// Create (criar, insert, salvar)
	// Read   (ler, select, load, carregar)
	// Update (atualizar, update)
	// Delete (excluir, delete, remover)
	/**
	 * cria ou atualiza o objeto
	 * @param objeto
	 */
	
	void persiste(TYPE objeto);

	/**
	 * deleta um objeto
	 * @param deleta um objeto da classe na qual a interface está sendo usada
	 */
	
	void delete(String dados,int id);
	/**
	 * seleciona apenas um objeto escolhido
	 * @param codigo
	 * @return um objeto
	 * @throws ClassNotFoundException 
	 */
	Map<Integer,ArrayList<Object>> select(String dados,int cod) throws ClassNotFoundException;
	
	/**
	 * seleciona todos objetos escolhidos
	 * @param classe
	 * @return objetos
	 * @throws ClassNotFoundException
	 */
	Map<Integer,ArrayList<Object>> select(String dados) throws ClassNotFoundException;
	
}

