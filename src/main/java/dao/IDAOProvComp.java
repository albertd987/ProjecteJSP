package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:55
 */
public interface IDAOProvComp {

	public boolean actualitzar();

	public boolean actualitzarPreuProveidor();

	public boolean afegirProveidorAComponent();

	public boolean eliminar();

	public boolean eliminarProveidorDeComponent();

	public List findAll();

	public Object findById();

	public List getProveidorsDelComponent();

	public boolean insertar();

}