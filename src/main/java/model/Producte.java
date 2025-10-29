package model;

import java.util.Objects;

/**
 * Classe que representa un Producte
 * Taula: Producte
 * Hereta de: Item
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Producte extends Item {
    
    // ==========================================
    // ATRIBUTS PROPIS
    // ==========================================
    private String prCodi;  // PK, FK a Item
    
    // ==========================================
    // CONSTRUCTORS
    // ==========================================
    
    /**
     * Constructor buit
     */
    public Producte() {
        super();
    }
    
    /**
     * Constructor amb paràmetre propi de Producte
     * @param prCodi Codi del producte (PK, FK)
     */
    public Producte(String prCodi) {
        super();
        this.prCodi = prCodi;
    }
    
    /**
     * Constructor complet (Item + Producte)
     * @param itCodi Codi item
     * @param itTipus Tipus ('P')
     * @param itNom Nom
     * @param itDesc Descripció
     * @param itStock Stock
     * @param itFoto Foto
     * @param prCodi Codi producte
     */
    public Producte(String itCodi, String itTipus, String itNom, 
                    String itDesc, Integer itStock, String itFoto,
                    String prCodi) {
        super(itCodi, itTipus, itNom, itDesc, itStock, itFoto);
        this.prCodi = prCodi;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getPrCodi() {
        return prCodi;
    }
    
    public void setPrCodi(String prCodi) {
        this.prCodi = prCodi;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producte producte = (Producte) o;
        return Objects.equals(prCodi, producte.prCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(prCodi);
    }
    
    @Override
    public String toString() {
        return "Producte{" +
                "prCodi='" + prCodi + '\'' +
                ", itNom='" + getItNom() + '\'' +
                ", itDesc='" + getItDesc() + '\'' +
                '}';
    }
}