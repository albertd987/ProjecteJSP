package model;

import java.util.Objects;

/**
 * Classe que representa un Municipi
 * Taula: Municipi
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Municipi {
    
    // ==========================================
    // ATRIBUTS
    // ==========================================
    private String muPrCodi;  // PK composta, FK a Provincia
    private String muNum;     // PK composta
    private String muNom;
    
    // ==========================================
    // CONSTRUCTORS
    // ==========================================
    
    /**
     * Constructor buit
     */
    public Municipi() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param muPrCodi Codi de província (PK, FK)
     * @param muNum Número del municipi (PK)
     * @param muNom Nom del municipi
     */
    public Municipi(String muPrCodi, String muNum, String muNom) {
        this.muPrCodi = muPrCodi;
        this.muNum = muNum;
        this.muNom = muNom;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getMuPrCodi() {
        return muPrCodi;
    }
    
    public void setMuPrCodi(String muPrCodi) {
        this.muPrCodi = muPrCodi;
    }
    
    public String getMuNum() {
        return muNum;
    }
    
    public void setMuNum(String muNum) {
        this.muNum = muNum;
    }
    
    public String getMuNom() {
        return muNom;
    }
    
    public void setMuNom(String muNom) {
        this.muNom = muNom;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Municipi municipi = (Municipi) o;
        return Objects.equals(muPrCodi, municipi.muPrCodi) &&
               Objects.equals(muNum, municipi.muNum);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(muPrCodi, muNum);
    }
    
    @Override
    public String toString() {
        return "Municipi{" +
                "muPrCodi='" + muPrCodi + '\'' +
                ", muNum='" + muNum + '\'' +
                ", muNom='" + muNom + '\'' +
                '}';
    }
}