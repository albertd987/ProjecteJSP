package dao;

import java.util.List;


/**
 * @author domen
 * @version 1.0
 * @created 24-oct.-2025 10:36:52
 */
public interface IDAOMunicipi {

	public List findAll();

	public Object findById();

	public List getMunicipisDeProvincia();

}