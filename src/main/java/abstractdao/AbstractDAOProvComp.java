package abstractdao;

import dao.IDAOProvComp;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:37
 */
public abstract class AbstractDAOProvComp implements IDAOProvComp {

	public AbstractDAOProvComp(){

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

	public boolean actualitzarPreuProveidor(){
		return false;
	}

	public boolean afegirProveidorAComponent(){
		return false;
	}

	public boolean eliminar(){
		return false;
	}

	public boolean eliminarProveidorDeComponent(){
		return false;
	}

	public List findAll(){
		return null;
	}

	public Object findById(){
		return null;
	}

	public List getProveidorsDelComponent(){
		return null;
	}

	public boolean insertar(){
		return false;
	}
}//end AbstractDAOProvComp