package model;

import java.util.Objects;

/**
 * Classe que representa una Província
 * Taula: Provincia
 * @author DomenechObiolAlbert
 * @version 1.0
 * @created 24-oct.-2025 10:39:02
 */
public class Provincia {
    

    private String prCodi;  // PK
    private String prNom;
    
    public Provincia() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param prCodi Codi de la província (PK)
     * @param prNom Nom de la província
     */
    public Provincia(String prCodi, String prNom) {
        this.prCodi = prCodi;
        this.prNom = prNom;
    }

    public String getPrCodi() {
        return prCodi;
    }
    
    public void setPrCodi(String prCodi) {
        this.prCodi = prCodi;
    }
    
    public String getPrNom() {
        return prNom;
    }
    
    public void setPrNom(String prNom) {
        this.prNom = prNom;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provincia provincia = (Provincia) o;
        return Objects.equals(prCodi, provincia.prCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(prCodi);
    }
    
    @Override
    public String toString() {
        return "Provincia{" +
                "prCodi='" + prCodi + '\'' +
                ", prNom='" + prNom + '\'' +
                '}';
    }
}