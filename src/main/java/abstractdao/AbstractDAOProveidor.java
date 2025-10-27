package abstractdao;

import dao.IDAOProveidor;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:38
 */
public abstract class AbstractDAOProveidor implements IDAOProveidor {

	public AbstractDAOProveidor(){

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

	public List filtrarPerMunicipi(){
		return null;
	}

	public List filtrarPerNom(){
		return null;
	}

	public List findAll(){
		return null;
	}

	public Object findById(){
		return null;
	}
}//end AbstractDAOProveidor