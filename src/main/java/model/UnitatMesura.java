package model;

import java.util.Objects;

/**
 * Classe que representa una Unitat de Mesura
 * @author DomenechObiolAlbert
 * @version 1.0
 * @created 24-oct.-2025 10:38:09
 *
 */
public class UnitatMesura {

    private String umCodi;  // PK
    private String umNom;
    
    public UnitatMesura() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param umCodi Codi de la unitat de mesura (PK)
     * @param umNom Nom de la unitat de mesura
     */
    public UnitatMesura(String umCodi, String umNom) {
        this.umCodi = umCodi;
        this.umNom = umNom;
    }
    
    public String getUmCodi() {
        return umCodi;
    }
    
    public void setUmCodi(String umCodi) {
        this.umCodi = umCodi;
    }
    
    public String getUmNom() {
        return umNom;
    }
    
    public void setUmNom(String umNom) {
        this.umNom = umNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitatMesura that = (UnitatMesura) o;
        return Objects.equals(umCodi, that.umCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(umCodi);
    }
    
    @Override
    public String toString() {
        return "UnitatMesura{" +
                "umCodi='" + umCodi + '\'' +
                ", umNom='" + umNom + '\'' +
                '}';
    }
}



