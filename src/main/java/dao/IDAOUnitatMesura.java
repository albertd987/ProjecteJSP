package dao;

import java.util.List;

import model.UnitatMesura;

/**
 * Interfície DAO per UnitatMesura (READ-ONLY)
 * Taula mestre de catàleg - només operacions de consulta
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOUnitatMesura {
    
    /**
     * Obté totes les unitats de mesura
     * @return Llista de totes les unitats de mesura
     */
    List<UnitatMesura> findAll();
    
    /**
     * Cerca una unitat de mesura pel seu codi
     * @param codi Codi de la unitat de mesura (PK)
     * @return UnitatMesura trobada o null si no existeix
     */
    UnitatMesura findById(String codi);
}
