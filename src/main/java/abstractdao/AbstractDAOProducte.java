package abstractdao;

import dao.IDAOProducte;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:36
 */
public abstract class AbstractDAOProducte implements IDAOProducte {

	public AbstractDAOProducte(){

	}

	public void finalize() throws Throwable {

	}
	public Connection getConnection(){
		return null;
	}

	public void logError(){

	}

	public void tancarRecursos(){

	}

	public boolean validarEntitat(){
		return false;
	}

	public boolean actualitzar(){
		return false;
	}

	public double calcularPreuTotal(){
		return 0;
	}

	public int countTotal(){
		return 0;
	}

	public boolean eliminar(){
		return false;
	}

	public List filtrarPerCodi(){
		return null;
	}

	public List findAll(){
		return null;
	}

	public List findAllPaginat(){
		return null;
	}

	public Object findById(){
		return null;
	}

	public boolean insertar(){
		return false;
	}
}//end AbstractDAOProducte