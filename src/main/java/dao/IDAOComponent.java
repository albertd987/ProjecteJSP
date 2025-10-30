package dao;

import java.util.List;

import model.Component;

/**
 * Interfície DAO per a la gestió de Components
 * Defineix operacions CRUD + mètodes específics
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOComponent {

    /**
     * Insereix un nou component a la BD
     * @param c Component a inserir
     * @return true si s'ha inserit correctament
     */
    boolean insertar(Component c);

    /**
     * Actualitza un component existent
     * @param c Component amb dades actualitzades
     * @return true si s'ha actualitzat correctament
     */
    boolean actualitzar(Component c);

    /**
     * Elimina un component per codi
     * @param codi Codi del component a eliminar
     * @return true si s'ha eliminat correctament
     */
    boolean eliminar(String codi);

    /**
     * Busca un component per codi
     * @param codi Codi del component
     * @return Component trobat o null si no existeix
     */
    Component findById(String codi);

    /**
     * Llista tots els components
     * @return Llista de tots els components
     */
    List<Component> findAll();

    /**
     * Llista components paginats
     * @param page Número de pàgina (començant per 1)
     * @param size Mida de la pàgina
     * @return Llista de components de la pàgina
     */
    List<Component> findAllPaginat(int page, int size);

    /**
     * Filtra components per codi (LIKE)
     * @param codiPattern Patró de cerca (ex: "CMP%")
     * @return Llista de components que compleixen el patró
     */
    List<Component> filtrarPerCodi(String codiPattern);

    /**
     * Filtra components per rang de preu mitjà
     * @param min Preu mínim
     * @param max Preu màxim
     * @return Llista de components dins el rang
     */
    List<Component> filtrarPerPreuMig(double min, double max);

    /**
     * Obté component amb preu actualitzat (recarrega de BD)
     * IMPORTANT: Això refresca cm_preu_mig que calculen els triggers
     * @param cmCodi Codi del component
     * @return Component amb preu actualitzat
     */
    Component getComponentAmbPreuActualitzat(String cmCodi);

    /**
     * Compta el total de components
     * @return Número total de components
     */
    int countTotal();
}