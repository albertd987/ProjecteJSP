package dao;

import java.util.List;

import model.Provincia;

/**
 * Interfície DAO per Provincia (READ-ONLY)
 * Taula mestre de catàleg - només operacions de consulta
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOProvincia {
    
    /**
     * Obté totes les províncies
     * @return Llista de totes les províncies
     */
    List<Provincia> findAll();
    
    /**
     * Cerca una província pel seu codi
     * @param codi Codi de la província (PK)
     * @return Provincia trobada o null si no existeix
     */
    Provincia findById(String codi);
}
