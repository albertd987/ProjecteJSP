package model;

import java.util.Objects;

/**
 * Classe que representa un Proveïdor
 * Taula: Proveidor
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Proveidor {
    
    private String pvCodi;              // PK
    private String pvCif;               // UNIQUE
    private String pvRaoSocial;
    private String pvLinAdreFac;
    private String pvPersonaContacte;
    private String pvTelefContacte;
    private String pvMuPrCodi;          // FK a Municipi
    private String pvMuNum;             // FK a Municipi
    
    public Proveidor() {
    }
    
    /**
     * Constructor amb tots els paràmetres
     * @param pvCodi Codi del proveïdor (PK)
     * @param pvCif CIF del proveïdor (UNIQUE)
     * @param pvRaoSocial Raó social del proveïdor
     * @param pvLinAdreFac Línia d'adreça de facturació
     * @param pvPersonaContacte Persona de contacte
     * @param pvTelefContacte Telèfon de contacte
     * @param pvMuPrCodi Codi província del municipi (FK)
     * @param pvMuNum Número del municipi (FK)
     */
    public Proveidor(String pvCodi, String pvCif, String pvRaoSocial, 
                     String pvLinAdreFac, String pvPersonaContacte, 
                     String pvTelefContacte, String pvMuPrCodi, String pvMuNum) {
        this.pvCodi = pvCodi;
        this.pvCif = pvCif;
        this.pvRaoSocial = pvRaoSocial;
        this.pvLinAdreFac = pvLinAdreFac;
        this.pvPersonaContacte = pvPersonaContacte;
        this.pvTelefContacte = pvTelefContacte;
        this.pvMuPrCodi = pvMuPrCodi;
        this.pvMuNum = pvMuNum;
    }

    public String getPvCodi() {
        return pvCodi;
    }
    
    public void setPvCodi(String pvCodi) {
        this.pvCodi = pvCodi;
    }
    
    public String getPvCif() {
        return pvCif;
    }
    
    public void setPvCif(String pvCif) {
        this.pvCif = pvCif;
    }
    
    public String getPvRaoSocial() {
        return pvRaoSocial;
    }
    
    public void setPvRaoSocial(String pvRaoSocial) {
        this.pvRaoSocial = pvRaoSocial;
    }
    
    public String getPvLinAdreFac() {
        return pvLinAdreFac;
    }
    
    public void setPvLinAdreFac(String pvLinAdreFac) {
        this.pvLinAdreFac = pvLinAdreFac;
    }
    
    public String getPvPersonaContacte() {
        return pvPersonaContacte;
    }
    
    public void setPvPersonaContacte(String pvPersonaContacte) {
        this.pvPersonaContacte = pvPersonaContacte;
    }
    
    public String getPvTelefContacte() {
        return pvTelefContacte;
    }
    
    public void setPvTelefContacte(String pvTelefContacte) {
        this.pvTelefContacte = pvTelefContacte;
    }
    
    public String getPvMuPrCodi() {
        return pvMuPrCodi;
    }
    
    public void setPvMuPrCodi(String pvMuPrCodi) {
        this.pvMuPrCodi = pvMuPrCodi;
    }
    
    public String getPvMuNum() {
        return pvMuNum;
    }
    
    public void setPvMuNum(String pvMuNum) {
        this.pvMuNum = pvMuNum;
    }
    
    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveidor proveidor = (Proveidor) o;
        return Objects.equals(pvCodi, proveidor.pvCodi);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pvCodi);
    }
    
    @Override
    public String toString() {
        return "Proveidor{" +
                "pvCodi='" + pvCodi + '\'' +
                ", pvCif='" + pvCif + '\'' +
                ", pvRaoSocial='" + pvRaoSocial + '\'' +
                ", pvMuPrCodi='" + pvMuPrCodi + '\'' +
                ", pvMuNum='" + pvMuNum + '\'' +
                '}';
    }
}