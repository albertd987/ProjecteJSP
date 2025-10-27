package dao;

import java.awt.Component;
import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:50
 */
public interface IDAOComponent {

	public boolean actualitzar();

	public int countTotal();

	public boolean eliminar();

	public List filtrarPerCodi();

	public List filtrarPerPreuMig();

	public List findAll();

	public List findAllPaginat();

	public Object findById();

	public Component getComponentAmbPreuActualitzat();

	public boolean insertar();

}