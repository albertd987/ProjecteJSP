package model;

import java.util.Objects;

/**
 * Classe que representa la relació Proveïdor-Component (N:N)
 * Taula: Prov_Comp
 * IMPORTANT: INSERT/UPDATE/DELETE en aquesta taula disparen
 * el trigger Oracle que recalcula automàticament cm_preu_mig
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class ProvComp {

    private String pcCmCodi;  // PK composta, FK a Component
    private String pcPvCodi;  // PK composta, FK a Proveidor
    private Double pcPreu;    // Preu del component per aquest proveïdor
    
    public ProvComp() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param pcCmCodi Codi del component (PK, FK)
     * @param pcPvCodi Codi del proveïdor (PK, FK)
     * @param pcPreu Preu del component per aquest proveïdor
     */
    public ProvComp(String pcCmCodi, String pcPvCodi, Double pcPreu) {
        this.pcCmCodi = pcCmCodi;
        this.pcPvCodi = pcPvCodi;
        this.pcPreu = pcPreu;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getPcCmCodi() {
        return pcCmCodi;
    }
    
    public void setPcCmCodi(String pcCmCodi) {
        this.pcCmCodi = pcCmCodi;
    }
    
    public String getPcPvCodi() {
        return pcPvCodi;
    }
    
    public void setPcPvCodi(String pcPvCodi) {
        this.pcPvCodi = pcPvCodi;
    }
    
    public Double getPcPreu() {
        return pcPreu;
    }
    
    public void setPcPreu(Double pcPreu) {
        this.pcPreu = pcPreu;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvComp provComp = (ProvComp) o;
        return Objects.equals(pcCmCodi, provComp.pcCmCodi) &&
               Objects.equals(pcPvCodi, provComp.pcPvCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pcCmCodi, pcPvCodi);
    }
    
    @Override
    public String toString() {
        return "ProvComp{" +
                "pcCmCodi='" + pcCmCodi + '\'' +
                ", pcPvCodi='" + pcPvCodi + '\'' +
                ", pcPreu=" + pcPreu +
                '}';
    }
}