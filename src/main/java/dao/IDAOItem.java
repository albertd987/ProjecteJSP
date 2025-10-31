package dao;

import java.util.List;

import model.Item;

/**
 * Interfície DAO per Item (taula base)
 * Item és la superclasse de Component i Producte
 * Operacions CRUD completes
 * 
 * NOTA: Aquesta interfície segueix el patró corregit de IDAOComponent
 * amb paràmetres correctes i tipus específics
 * 
 * @author DomenechObiolAlbert
 * @version 1.1 - Corregida amb paràmetres
 */
public interface IDAOItem {
    
    // ============================================
    // OPERACIONS CRUD BÀSIQUES
    // ============================================
    
    /**
     * Insereix un nou item a la base de dades
     * @param item Item a inserir
     * @return true si s'ha inserit correctament, false en cas contrari
     */
    boolean insertar(Item item);
    
    /**
     * Actualitza un item existent
     * @param item Item amb les dades actualitzades
     * @return true si s'ha actualitzat correctament, false en cas contrari
     */
    boolean actualitzar(Item item);
    
    /**
     * Elimina un item de la base de dades
     * @param codi Codi de l'item a eliminar
     * @return true si s'ha eliminat correctament, false en cas contrari
     */
    boolean eliminar(String codi);
    
    /**
     * Cerca un item pel seu codi
     * @param codi Codi de l'item a cercar
     * @return Item trobat o null si no existeix
     */
    Item findById(String codi);
    
    /**
     * Obté tots els items
     * @return Llista amb tots els items
     */
    List<Item> findAll();
    
    // ============================================
    // MÈTODES ESPECÍFICS
    // ============================================
    
    /**
     * Filtra items per tipus ('C' = Component, 'P' = Producte)
     * @param tipus Tipus d'item ('C' o 'P')
     * @return Llista d'items del tipus especificat
     */
    List<Item> filtrarPerTipus(String tipus);
    
    /**
     * Cerca items pel nom (LIKE case-insensitive)
     * @param nomPattern Patró de cerca (p.ex. "Intel")
     * @return Llista d'items que coincideixen amb el patró
     */
    List<Item> cercarPerNom(String nomPattern);
    
    /**
     * Obté items amb stock inferior a un valor mínim
     * @param stockMinim Stock mínim (els resultats tindran stock < stockMinim)
     * @return Llista d'items amb stock baix
     */
    List<Item> obtenirItemsAmbStockBaix(int stockMinim);
    
    /**
     * Compta el total d'items
     * @return Nombre total d'items a la base de dades
     */
    int countTotal();
    
    /**
     * Obté items paginats
     * @param page Número de pàgina (comença en 1)
     * @param size Mida de la pàgina
     * @return Llista d'items de la pàgina especificada
     */
    List<Item> findAllPaginat(int page, int size);
}