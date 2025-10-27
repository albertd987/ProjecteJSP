package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:56
 */
public interface IDAOProveidor {

	public List filtrarPerMunicipi();

	public List filtrarPerNom();

	public List findAll();

	public Object findById();

}