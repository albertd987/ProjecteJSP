package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import model.Usuari;
import util.ConnexioOracle;

/**
 * Implementació DAO per gestionar usuaris amb Oracle
 * 
 * Responsabilitat única: Accés a dades d'usuaris amb queries SQL
 * 
 * Funcionalitats:
 * - Login: Buscar usuari per username i obtenir hash Bcrypt
 * - Registre d'accés: Actualitzar us_ultim_acces
 * - Crear usuari: Insert nou usuari (opcional)
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOUsuari implements IDAOUsuari {
    
    private final Connection connexio;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor que obté la connexió Oracle
     */
    public DAOUsuari() throws Exception {
        this.connexio = ConnexioOracle.getConnection();
    }
    
    // ==================== MÈTODES INTERFÍCIE ====================
    
    /**
     * Busca un usuari pel seu username
     * 
     * Query: SELECT * FROM Usuari WHERE us_username = ? AND us_actiu = 1
     * 
     * @param username Nom d'usuari a buscar
     * @return Usuari trobat o null
     */
    @Override
    public Usuari findByUsername(String username) throws Exception {
        // Validació d'entrada
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no pot ser null o buit");
        }
        
        String sql = "SELECT us_id, us_username, us_password, us_nom, us_email, " +
                     "us_actiu, us_data_creacio, us_ultim_acces " +
                     "FROM Usuari " +
                     "WHERE us_username = ? AND us_actiu = 1";
        
        try (PreparedStatement stmt = connexio.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuari(rs);
                }
                return null;  // Usuari no trobat
            }
        }
    }
    
    /**
     * Actualitza el timestamp de l'últim accés
     * 
     * Query: UPDATE Usuari SET us_ultim_acces = CURRENT_TIMESTAMP WHERE us_id = ?
     * 
     * @param usId ID de l'usuari
     */
    @Override
    public void updateUltimAcces(Long usId) throws Exception {
        // Validació d'entrada
        if (usId == null || usId <= 0) {
            throw new IllegalArgumentException("ID d'usuari invàlid");
        }
        
        String sql = "UPDATE Usuari SET us_ultim_acces = CURRENT_TIMESTAMP WHERE us_id = ?";
        
        try (PreparedStatement stmt = connexio.prepareStatement(sql)) {
            stmt.setLong(1, usId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new Exception("No s'ha pogut actualitzar l'últim accés (usuari no trobat)");
            }
        }
    }
    
    /**
     * Crea un nou usuari (OPCIONAL - per futures ampliacions)
     * 
     * Query: INSERT INTO Usuari (us_username, us_password, us_nom, us_email) VALUES (?, ?, ?, ?)
     * 
     * NOTA: us_id es genera automàticament per IDENTITY
     * 
     * @param usuari Objecte usuari a inserir
     * @return Usuari creat amb ID assignat
     */
    @Override
    public Usuari create(Usuari usuari) throws Exception {
        // Validacions d'entrada
        if (usuari == null) {
            throw new IllegalArgumentException("Usuari no pot ser null");
        }
        if (usuari.getUsUsername() == null || usuari.getUsUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username és obligatori");
        }
        if (usuari.getUsPassword() == null || usuari.getUsPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash és obligatori");
        }
        
        String sql = "INSERT INTO Usuari (us_username, us_password, us_nom, us_email, us_actiu) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connexio.prepareStatement(sql, new String[]{"us_id"})) {
            stmt.setString(1, usuari.getUsUsername());
            stmt.setString(2, usuari.getUsPassword());
            stmt.setString(3, usuari.getUsNom());
            stmt.setString(4, usuari.getUsEmail());
            stmt.setInt(5, usuari.isActiu() ? 1 : 0);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new Exception("Error creant usuari, cap fila afectada");
            }
            
            // Obtenir ID generat automàticament
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuari.setUsId(generatedKeys.getLong(1));
                }
            }
            
            return usuari;
        }
    }
    
    // ==================== MÈTODES AUXILIARS ====================
    
    /**
     * Mapeja un ResultSet a un objecte Usuari
     * 
     * @param rs ResultSet amb dades de la consulta
     * @return Objecte Usuari amb dades carregades
     */
    private Usuari mapResultSetToUsuari(ResultSet rs) throws Exception {
        Usuari usuari = new Usuari();
        
        usuari.setUsId(rs.getLong("us_id"));
        usuari.setUsUsername(rs.getString("us_username"));
        usuari.setUsPassword(rs.getString("us_password"));
        usuari.setUsNom(rs.getString("us_nom"));
        usuari.setUsEmail(rs.getString("us_email"));
        usuari.setUsActiu(rs.getInt("us_actiu") == 1);
        
        Timestamp dataCreacio = rs.getTimestamp("us_data_creacio");
        if (dataCreacio != null) {
            usuari.setUsDataCreacio(dataCreacio);
        }
        
        Timestamp ultimAcces = rs.getTimestamp("us_ultim_acces");
        if (ultimAcces != null) {
            usuari.setUsUltimAcces(ultimAcces);
        }
        
        return usuari;
    }
}