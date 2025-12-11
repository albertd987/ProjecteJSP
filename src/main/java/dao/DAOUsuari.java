package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import model.Usuari;
import util.ConnexioOracle;

/**
 * Implementació DAO per gestionar usuaris amb Oracle
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOUsuari implements IDAOUsuari {
    
    // ==================== CONSTRUCTOR ====================
    
    public DAOUsuari() {
        // Constructor buit - connexió es crea en cada mètode
    }
    
    // ==================== MÈTODES INTERFÍCIE ====================
    
    @Override
    public Usuari findByUsername(String username) throws Exception {
        // Validació d'entrada
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no pot ser null o buit");
        }
        
        Connection connexio = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connexio = ConnexioOracle.getConnection();
            
            String sql = "SELECT us_id, us_username, us_password, us_nom, us_email, " +
                         "us_actiu, us_data_creacio, us_ultim_acces " +
                         "FROM Usuari " +
                         "WHERE us_username = ? AND us_actiu = 1";
            
            stmt = connexio.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUsuari(rs);
            }
            return null;
            
        } finally {
            tancarRecursos(rs);
            tancarRecursos(stmt);
            tancarRecursos(connexio);
        }
    }
    
    @Override
    public void updateUltimAcces(Long usId) throws Exception {
        // Validació d'entrada
        if (usId == null || usId <= 0) {
            throw new IllegalArgumentException("ID d'usuari invàlid");
        }
        
        Connection connexio = null;
        PreparedStatement stmt = null;
        
        try {
            connexio = ConnexioOracle.getConnection();
            
            String sql = "UPDATE Usuari SET us_ultim_acces = CURRENT_TIMESTAMP WHERE us_id = ?";
            
            stmt = connexio.prepareStatement(sql);
            stmt.setLong(1, usId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new Exception("No s'ha pogut actualitzar l'últim accés (usuari no trobat)");
            }
            
        } finally {
            tancarRecursos(stmt);
            tancarRecursos(connexio);
        }
    }
    
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
        
        Connection connexio = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        
        try {
            connexio = ConnexioOracle.getConnection();
            
            String sql = "INSERT INTO Usuari (us_username, us_password, us_nom, us_email, us_actiu) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            stmt = connexio.prepareStatement(sql, new String[]{"us_id"});
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
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuari.setUsId(generatedKeys.getLong(1));
            }
            
            return usuari;
            
        } finally {
            tancarRecursos(generatedKeys);
            tancarRecursos(stmt);
            tancarRecursos(connexio);
        }
    }
    
    // ==================== MÈTODES AUXILIARS ====================
    
    private Usuari mapResultSetToUsuari(ResultSet rs) throws SQLException {
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
    
    // Mètode helper per tancar recursos
    private void tancarRecursos(AutoCloseable recurs) {
        if (recurs != null) {
            try {
                recurs.close();
            } catch (Exception e) {
                System.err.println("Error tancant recurs: " + e.getMessage());
            }
        }
    }
}