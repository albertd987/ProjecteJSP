package model;

import java.util.Objects;

/**
 * Classe que representa un Component industrial
 * Taula: Component
 * Hereta de: Item
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Component extends Item {

    private String cmCodi;           // PK, FK a Item
    private String cmUmCodi;         // FK a UnitatMesura
    private String cmCodiFabricant;
    private Double cmPreuMig;        // Calculat automàticament pels triggers Oracle

    public Component() {
        super();
    }
    
    /**
     * Constructor amb paràmetres propis de Component
     * @param cmCodi Codi del component (PK, FK)
     * @param cmUmCodi Codi unitat de mesura (FK)
     * @param cmCodiFabricant Codi del fabricant
     * @param cmPreuMig Preu mitjà (calculat automàticament)
     */
    public Component(String cmCodi, String cmUmCodi, 
                     String cmCodiFabricant, Double cmPreuMig) {
        super();
        this.cmCodi = cmCodi;
        this.cmUmCodi = cmUmCodi;
        this.cmCodiFabricant = cmCodiFabricant;
        this.cmPreuMig = cmPreuMig;
    }
    
    /**
     * Constructor complet (Item + Component)
     * @param itCodi Codi item
     * @param itTipus Tipus ('C')
     * @param itNom Nom
     * @param itDesc Descripció
     * @param itStock Stock
     * @param itFoto Foto
     * @param cmCodi Codi component
     * @param cmUmCodi Unitat mesura
     * @param cmCodiFabricant Codi fabricant
     * @param cmPreuMig Preu mitjà
     */
    public Component(String itCodi, String itTipus, String itNom, 
                     String itDesc, Integer itStock, String itFoto,
                     String cmCodi, String cmUmCodi, 
                     String cmCodiFabricant, Double cmPreuMig) {
        super(itCodi, itTipus, itNom, itDesc, itStock, itFoto);
        this.cmCodi = cmCodi;
        this.cmUmCodi = cmUmCodi;
        this.cmCodiFabricant = cmCodiFabricant;
        this.cmPreuMig = cmPreuMig;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getCmCodi() {
        return cmCodi;
    }
    
    public void setCmCodi(String cmCodi) {
        this.cmCodi = cmCodi;
    }
    
    public String getCmUmCodi() {
        return cmUmCodi;
    }
    
    public void setCmUmCodi(String cmUmCodi) {
        this.cmUmCodi = cmUmCodi;
    }
    
    public String getCmCodiFabricant() {
        return cmCodiFabricant;
    }
    
    public void setCmCodiFabricant(String cmCodiFabricant) {
        this.cmCodiFabricant = cmCodiFabricant;
    }
    
    public Double getCmPreuMig() {
        return cmPreuMig;
    }
    
    public void setCmPreuMig(Double cmPreuMig) {
        this.cmPreuMig = cmPreuMig;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(cmCodi, component.cmCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(cmCodi);
    }
    
    @Override
    public String toString() {
        return "Component{" +
                "cmCodi='" + cmCodi + '\'' +
                ", cmCodiFabricant='" + cmCodiFabricant + '\'' +
                ", cmPreuMig=" + cmPreuMig +
                ", cmUmCodi='" + cmUmCodi + '\'' +
                ", itNom='" + getItNom() + '\'' +
                '}';
    }
}