package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:51
 */
public interface IDAOItem {

	public boolean actualitzar();

	public boolean eliminar();

	public List findAll();

	public Object findById();

	public boolean insertar();

}