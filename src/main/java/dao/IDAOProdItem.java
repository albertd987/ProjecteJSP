package dao;

import java.util.List;

import model.ProdItem;

/**
 * Interfície DAO per ProdItem (CRUD complet)
 * Gestiona la relació N:N entre Producte i Item (BOM - Bill of Materials)
 * 
 * IMPORTANT: PK composta (pi_pr_codi, pi_it_codi)
 * CHECK: pi_pr_codi <> pi_it_codi (un producte no pot contenir-se a si mateix)
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public interface IDAOProdItem {

    /**
     * Insereix una nova relació Producte-Item
     * @param prodItem Relació a inserir
     * @return true si s'ha inserit correctament, false en cas contrari
     */
    boolean insertar(ProdItem prodItem);

    /**
     * Actualitza una relació Producte-Item existent
     * Típicament es canvia la quantitat
     * @param prodItem Relació amb dades actualitzades
     * @return true si s'ha actualitzat correctament, false en cas contrari
     */
    boolean actualitzar(ProdItem prodItem);

    /**
     * Elimina una relació Producte-Item
     * @param prCodi Codi del producte (part de PK)
     * @param itCodi Codi de l'item (part de PK)
     * @return true si s'ha eliminat correctament, false en cas contrari
     */
    boolean eliminar(String prCodi, String itCodi);

    /**
     * Cerca una relació Producte-Item per la seva PK composta
     * @param prCodi Codi del producte (part de PK)
     * @param itCodi Codi de l'item (part de PK)
     * @return ProdItem trobat o null si no existeix
     */
    ProdItem findById(String prCodi, String itCodi);

    /**
     * Obté totes les relacions Producte-Item
     * @return Llista de totes les relacions
     */
    List<ProdItem> findAll();

    /**
     * Obté tots els items (components/subproductes) d'un producte
     * Útil per generar el BOM (Bill of Materials)
     * @param prCodi Codi del producte
     * @return Llista d'items del producte amb les seves quantitats
     */
    List<ProdItem> getItemsDelProducte(String prCodi);

    /**
     * Afegeix un item a un producte (mètode helper)
     * Equivalent a insertar però amb paràmetres separats
     * @param prCodi Codi del producte
     * @param itCodi Codi de l'item
     * @param quantitat Quantitat de l'item en el producte
     * @return true si s'ha afegit correctament, false en cas contrari
     */
    boolean afegirItemAProducte(String prCodi, String itCodi, int quantitat);

    /**
     * Compta el total de relacions Producte-Item
     * @return Número total de relacions
     */
    int countTotal();

}