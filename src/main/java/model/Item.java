package model;

import java.util.Objects;

/**
 * Classe base que representa un Item (Component o Producte)
 * Taula: Item
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Item {
	
    private String itCodi;    // PK
    private String itTipus;   // 'C' = Component, 'P' = Producte
    private String itNom;
    private String itDesc;
    private Integer itStock;
    private String itFoto;

    public Item() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param itCodi Codi de l'item (PK)
     * @param itTipus Tipus d'item ('C' o 'P')
     * @param itNom Nom de l'item
     * @param itDesc Descripció de l'item
     * @param itStock Stock disponible
     * @param itFoto Ruta de la foto
     */
    public Item(String itCodi, String itTipus, String itNom, 
                String itDesc, Integer itStock, String itFoto) {
        this.itCodi = itCodi;
        this.itTipus = itTipus;
        this.itNom = itNom;
        this.itDesc = itDesc;
        this.itStock = itStock;
        this.itFoto = itFoto;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getItCodi() {
        return itCodi;
    }
    
    public void setItCodi(String itCodi) {
        this.itCodi = itCodi;
    }
    
    public String getItTipus() {
        return itTipus;
    }
    
    public void setItTipus(String itTipus) {
        this.itTipus = itTipus;
    }
    
    public String getItNom() {
        return itNom;
    }
    
    public void setItNom(String itNom) {
        this.itNom = itNom;
    }
    
    public String getItDesc() {
        return itDesc;
    }
    
    public void setItDesc(String itDesc) {
        this.itDesc = itDesc;
    }
    
    public Integer getItStock() {
        return itStock;
    }
    
    public void setItStock(Integer itStock) {
        this.itStock = itStock;
    }
    
    public String getItFoto() {
        return itFoto;
    }
    
    public void setItFoto(String itFoto) {
        this.itFoto = itFoto;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(itCodi, item.itCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itCodi);
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "itCodi='" + itCodi + '\'' +
                ", itTipus='" + itTipus + '\'' +
                ", itNom='" + itNom + '\'' +
                ", itStock=" + itStock +
                '}';
    }
}