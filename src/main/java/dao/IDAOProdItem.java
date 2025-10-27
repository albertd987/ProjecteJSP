package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:53
 */
public interface IDAOProdItem {

	public boolean actualitzar();

	public boolean afegirItemAProducte();

	public boolean eliminar();

	public List findAll();

	public Object findById();

	public List getItemsDelProducte();

	public boolean insertar();

}