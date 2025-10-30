package dao;

import java.util.List;

import model.Proveidor;

/**
 * Interfície DAO per Proveidor (READ-ONLY)
 * Taula mestre de catàleg - només operacions de consulta
 * Nota: Els proveïdors poden veure's a les maquetes però no es modifiquen des de l'app
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOProveidor {
    
    /**
     * Obté tots els proveïdors
     * @return Llista de tots els proveïdors
     */
    List<Proveidor> findAll();
    
    /**
     * Cerca un proveïdor pel seu codi
     * @param codi Codi del proveïdor (PK)
     * @return Proveidor trobat o null si no existeix
     */
    Proveidor findById(String codi);
    
    /**
     * Filtra proveïdors per municipi
     * @param prCodi Codi de província
     * @param muNum Número de municipi
     * @return Llista de proveïdors d'aquest municipi
     */
    List<Proveidor> filtrarPerMunicipi(String prCodi, String muNum);
    
    /**
     * Filtra proveïdors per nom (cerca parcial, case-insensitive)
     * @param nomPattern Patró de cerca per raó social
     * @return Llista de proveïdors que coincideixen amb el patró
     */
    List<Proveidor> filtrarPerNom(String nomPattern);
}
