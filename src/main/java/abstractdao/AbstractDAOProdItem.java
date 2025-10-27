package abstractdao;

import dao.IDAOProdItem;
import java.sql.Connection;
import java.util.List;

/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:37:35
 */
public abstract class AbstractDAOProdItem implements IDAOProdItem {

	public AbstractDAOProdItem(){

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

	public boolean afegirItemAProducte(){
		return false;
	}

	public boolean eliminar(){
		return false;
	}

	public List findAll(){
		return null;
	}

	public Object findById(){
		return null;
	}

	public List getItemsDelProducte(){
		return null;
	}

	public boolean insertar(){
		return false;
	}
}//end AbstractDAOProdItem