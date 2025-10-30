package dao;

import java.util.List;

import model.Municipi;

/**
 * Interfície DAO per Municipi (READ-ONLY)
 * Taula mestre de catàleg - només operacions de consulta
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOMunicipi {
    
    /**
     * Obté tots els municipis
     * @return Llista de tots els municipis
     */
    List<Municipi> findAll();
    
    /**
     * Cerca un municipi per la seva clau primària composta
     * @param prCodi Codi de província (PK)
     * @param muNum Número de municipi (PK)
     * @return Municipi trobat o null si no existeix
     */
    Municipi findById(String prCodi, String muNum);
    
    /**
     * Obté tots els municipis d'una província
     * @param prCodi Codi de la província
     * @return Llista de municipis d'aquesta província
     */
    List<Municipi> getMunicipisDeProvincia(String prCodi);
}
