package dao;

import java.util.List;

import model.Producte;

/**
 * Interfície DAO per Producte (CRUD complet)
 * Producte hereta d'Item - gestiona la jerarquia d'herència
 * 
 * NOTA: Producte hereta d'Item, per tant:
 * - pr_codi (PK de Producte) == it_codi (FK a Item)
 * - it_tipus = 'P' per a productes
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public interface IDAOProducte {

    /**
     * Insereix un nou producte (INSERT a Item + Producte amb transacció)
     * @param producte Producte a inserir (prCodi ha de ser igual a itCodi)
     * @return true si s'ha inserit correctament, false en cas contrari
     */
    boolean insertar(Producte producte);

    /**
     * Actualitza un producte existent (UPDATE Item)
     * Nota: Producte no té camps propis a actualitzar, només els heretats d'Item
     * @param producte Producte amb dades actualitzades
     * @return true si s'ha actualitzat correctament, false en cas contrari
     */
    boolean actualitzar(Producte producte);

    /**
     * Elimina un producte (DELETE Producte → Item amb transacció)
     * @param prCodi Codi del producte a eliminar
     * @return true si s'ha eliminat correctament, false en cas contrari
     */
    boolean eliminar(String prCodi);

    /**
     * Cerca un producte pel seu codi
     * @param prCodi Codi del producte (PK)
     * @return Producte trobat o null si no existeix
     */
    Producte findById(String prCodi);

    /**
     * Obté tots els productes ordenats per codi
     * @return Llista de tots els productes
     */
    List<Producte> findAll();

    /**
     * Obté productes paginats (usa ROW_NUMBER() de Oracle)
     * @param page Número de pàgina (1-based)
     * @param size Elements per pàgina
     * @return Llista de productes de la pàgina sol·licitada
     */
    List<Producte> findAllPaginat(int page, int size);

    /**
     * Filtra productes per patró de codi (case-insensitive amb LIKE)
     * @param codiPattern Patró de codi a cercar (usa % automàticament)
     * @return Llista de productes que coincideixen amb el patró
     */
    List<Producte> filtrarPerCodi(String codiPattern);

    /**
     * Calcula el preu total d'un producte sumant recursivament
     * el preu de tots els seus components/subproductes (estructura BOM)
     * 
     * Algoritme:
     * - Per cada item de Prod_Item:
     *   - Si és Component (tipus='C'): preu = quantitat × preu_mig
     *   - Si és Producte (tipus='P'): preu = quantitat × calcularPreuTotal(recursiu)
     * 
     * @param prCodi Codi del producte
     * @return Preu total del producte o 0.0 si no existeix o no té components
     */
    double calcularPreuTotal(String prCodi);

    /**
     * Compta el total de productes
     * @return Número total de productes
     */
    int countTotal();

}