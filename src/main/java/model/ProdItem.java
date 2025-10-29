package model;

import java.util.Objects;

/**
 * Classe que representa la relació Producte-Item (N:N)
 * Taula: Prod_Item
 * Estructura BOM (Bill of Materials) - components/subproductes d'un producte
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class ProdItem {
    
    // ==========================================
    // ATRIBUTS
    // ==========================================
    private String piPrCodi;  // PK composta, FK a Producte
    private String piItCodi;  // PK composta, FK a Item
    private Integer quantitat; // Quantitat del item en el producte

    public ProdItem() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param piPrCodi Codi del producte (PK, FK)
     * @param piItCodi Codi de l'item (PK, FK)
     * @param quantitat Quantitat de l'item en el producte
     */
    public ProdItem(String piPrCodi, String piItCodi, Integer quantitat) {
        this.piPrCodi = piPrCodi;
        this.piItCodi = piItCodi;
        this.quantitat = quantitat;
    }
	
    public String getPiPrCodi() {
        return piPrCodi;
    }
    
    public void setPiPrCodi(String piPrCodi) {
        this.piPrCodi = piPrCodi;
    }
    
    public String getPiItCodi() {
        return piItCodi;
    }
    
    public void setPiItCodi(String piItCodi) {
        this.piItCodi = piItCodi;
    }
    
    public Integer getQuantitat() {
        return quantitat;
    }
    
    public void setQuantitat(Integer quantitat) {
        this.quantitat = quantitat;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdItem prodItem = (ProdItem) o;
        return Objects.equals(piPrCodi, prodItem.piPrCodi) &&
               Objects.equals(piItCodi, prodItem.piItCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(piPrCodi, piItCodi);
    }
    
    @Override
    public String toString() {
        return "ProdItem{" +
                "piPrCodi='" + piPrCodi + '\'' +
                ", piItCodi='" + piItCodi + '\'' +
                ", quantitat=" + quantitat +
                '}';
    }
}