package abstractdao;

import dao.IDAOComponent;
import java.awt.Component;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:32
 */
public abstract class AbstractDAOComponent implements IDAOComponent {

	public AbstractDAOComponent(){

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

	public int countTotal(){
		return 0;
	}

	public boolean eliminar(){
		return false;
	}

	public List filtrarPerCodi(){
		return null;
	}

	public List filtrarPerPreuMig(){
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

	public Component getComponentAmbPreuActualitzat(){
		return null;
	}

	public boolean insertar(){
		return false;
	}
}//end AbstractDAOComponent