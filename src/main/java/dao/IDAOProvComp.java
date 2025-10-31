package dao;

import java.util.List;

import model.ProvComp;

/**
 * Interfície DAO per a la gestió de Proveïdor-Component (relació N:N)
 * 
 * ⚠️ CRÍTICA: Aquesta taula dispara triggers Oracle!
 * Cada INSERT/UPDATE/DELETE recalcula automàticament cm_preu_mig
 * via trigger trg_prov_comp_after → preu_mig_pkg.recalcula_tots_preus()
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOProvComp {

    /**
     * Insereix una nova relació proveïdor-component
     * ⚠️ DISPARA TRIGGER que recalcula cm_preu_mig
     * 
     * @param pc Relació proveïdor-component a inserir
     * @return true si s'ha inserit correctament
     */
    boolean insertar(ProvComp pc);

    /**
     * Actualitza el preu d'una relació existent
     * ⚠️ DISPARA TRIGGER que recalcula cm_preu_mig
     * 
     * @param pc Relació amb dades actualitzades
     * @return true si s'ha actualitzat correctament
     */
    boolean actualitzar(ProvComp pc);

    /**
     * Elimina una relació proveïdor-component
     * ⚠️ DISPARA TRIGGER que recalcula cm_preu_mig
     * 
     * @param cmCodi Codi del component (PK composta)
     * @param pvCodi Codi del proveïdor (PK composta)
     * @return true si s'ha eliminat correctament
     */
    boolean eliminar(String cmCodi, String pvCodi);

    /**
     * Busca una relació específica per PK composta
     * 
     * @param cmCodi Codi del component
     * @param pvCodi Codi del proveïdor
     * @return Relació trobada o null si no existeix
     */
    ProvComp findById(String cmCodi, String pvCodi);

    /**
     * Llista totes les relacions proveïdor-component
     * 
     * @return Llista de totes les relacions
     */
    List<ProvComp> findAll();

    /**
     * Obté tots els proveïdors d'un component concret
     * Utilitzat pel dropdown "PROVEÏDOR" del formulari editComponent.jsp
     * 
     * @param cmCodi Codi del component
     * @return Llista de relacions (proveïdors) d'aquest component
     */
    List<ProvComp> getProveidorsDelComponent(String cmCodi);

    /**
     * Obté tots els components subministrats per un proveïdor
     * 
     * @param pvCodi Codi del proveïdor
     * @return Llista de relacions (components) d'aquest proveïdor
     */
    List<ProvComp> getComponentsDelProveidor(String pvCodi);
}