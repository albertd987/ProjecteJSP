package abstractdao;

import dao.IDAOUnitatMesura;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:40
 */
public abstract class AbstractDAOUnitatMesura implements IDAOUnitatMesura {

	public AbstractDAOUnitatMesura(){

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
}//end AbstractDAOUnitatMesura