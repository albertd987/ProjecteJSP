package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:54
 */
public interface IDAOProducte {

	public boolean actualitzar();

	public double calcularPreuTotal();

	public int countTotal();

	public boolean eliminar();

	public List filtrarPerCodi();

	public List findAll();

	public List findAllPaginat();

	public Object findById();

	public boolean insertar();

}