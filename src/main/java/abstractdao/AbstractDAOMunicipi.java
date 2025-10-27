package abstractdao;

import dao.IDAOMunicipi;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:35
 */
public abstract class AbstractDAOMunicipi implements IDAOMunicipi {

	public AbstractDAOMunicipi(){

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

	public List findAll(){
		return null;
	}

	public Object findById(){
		return null;
	}

	public List getMunicipisDeProvincia(){
		return null;
	}
}//end AbstractDAOMunicipi