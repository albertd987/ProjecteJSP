package model;

import java.sql.Timestamp;

/**
 * Classe que representa un usuari del sistema AutoFactory
 * 
 * Responsabilitat única: Encapsular les dades d'un usuari amb autenticació Bcrypt
 * 
 * Camps:
 * - us_id: Identificador únic generat automàticament per Oracle
 * - us_username: Nom d'usuari únic per login
 * - us_password: Hash Bcrypt del password (60 caràcters)
 * - us_nom: Nom complet de l'usuari
 * - us_email: Email de contacte
 * - us_actiu: Flag per activar/desactivar usuari (1=actiu, 0=inactiu)
 * - us_data_creacio: Timestamp de creació (auto-generada)
 * - us_ultim_acces: Timestamp del darrer login
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class Usuari {
    
    // ==================== ATRIBUTS ====================
    
    private Long usId;
    private String usUsername;
    private String usPassword;  // Hash Bcrypt
    private String usNom;
    private String usEmail;
    private Boolean usActiu;
    private Timestamp usDataCreacio;
    private Timestamp usUltimAcces;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor per defecte
     */
    public Usuari() {
        this.usActiu = true;  // Per defecte, usuari actiu
    }
    
    /**
     * Constructor per login (només username i password)
     * 
     * @param usUsername Nom d'usuari
     * @param usPassword Hash Bcrypt del password
     */
    public Usuari(String usUsername, String usPassword) {
        this();
        this.usUsername = usUsername;
        this.usPassword = usPassword;
    }
    
    /**
     * Constructor complet per crear usuari nou
     * 
     * @param usUsername Nom d'usuari (únic)
     * @param usPassword Hash Bcrypt del password
     * @param usNom Nom complet
     * @param usEmail Email de contacte
     */
    public Usuari(String usUsername, String usPassword, String usNom, String usEmail) {
        this();
        this.usUsername = usUsername;
        this.usPassword = usPassword;
        this.usNom = usNom;
        this.usEmail = usEmail;
    }
    
    // ==================== GETTERS I SETTERS ====================
    
    public Long getUsId() {
        return usId;
    }
    
    public void setUsId(Long usId) {
        this.usId = usId;
    }
    
    public String getUsUsername() {
        return usUsername;
    }
    
    public void setUsUsername(String usUsername) {
        this.usUsername = usUsername;
    }
    
    public String getUsPassword() {
        return usPassword;
    }
    
    public void setUsPassword(String usPassword) {
        this.usPassword = usPassword;
    }
    
    public String getUsNom() {
        return usNom;
    }
    
    public void setUsNom(String usNom) {
        this.usNom = usNom;
    }
    
    public String getUsEmail() {
        return usEmail;
    }
    
    public void setUsEmail(String usEmail) {
        this.usEmail = usEmail;
    }
    
    public Boolean getUsActiu() {
        return usActiu;
    }
    
    public void setUsActiu(Boolean usActiu) {
        this.usActiu = usActiu;
    }
    
    public Timestamp getUsDataCreacio() {
        return usDataCreacio;
    }
    
    public void setUsDataCreacio(Timestamp usDataCreacio) {
        this.usDataCreacio = usDataCreacio;
    }
    
    public Timestamp getUsUltimAcces() {
        return usUltimAcces;
    }
    
    public void setUsUltimAcces(Timestamp usUltimAcces) {
        this.usUltimAcces = usUltimAcces;
    }
    
    // ==================== MÈTODES ÚTILS ====================
    
    /**
     * Verifica si l'usuari està actiu
     * 
     * @return true si us_actiu == true
     */
    public boolean isActiu() {
        return usActiu != null && usActiu;
    }
    
    /**
     * ToString per debugging (SENSE mostrar el password hash per seguretat)
     */
    @Override
    public String toString() {
        return "Usuari{" +
                "usId=" + usId +
                ", usUsername='" + usUsername + '\'' +
                ", usNom='" + usNom + '\'' +
                ", usEmail='" + usEmail + '\'' +
                ", usActiu=" + usActiu +
                ", usDataCreacio=" + usDataCreacio +
                ", usUltimAcces=" + usUltimAcces +
                '}';
    }
}