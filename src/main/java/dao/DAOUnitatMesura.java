package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOUnitatMesura;
import model.UnitatMesura;

/**
 * Implementació DAO per UnitatMesura (READ-ONLY)
 * Taula mestre de catàleg - només consultes
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOUnitatMesura extends AbstractDAOUnitatMesura {

    // ============================================
    // FIND ALL - Obté totes les unitats de mesura
    // ============================================
    
    @Override
    public List<UnitatMesura> findAll() {
        List<UnitatMesura> unitats = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT um_codi, um_nom FROM UnitatMesura ORDER BY um_codi";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                UnitatMesura um = mapResultSetToUnitatMesura(rs);
                unitats.add(um);
            }
            
            System.out.println("✅ findAll() - Trobades " + unitats.size() + " unitats de mesura");
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(stmt);
            tancarRecursos(conn);
        }
        
        return unitats;
    }

    // ============================================
    // FIND BY ID - Cerca per codi
    // ============================================
    
    @Override
    public UnitatMesura findById(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println("❌ Codi unitat mesura no pot ser buit");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT um_codi, um_nom FROM UnitatMesura WHERE um_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, codi);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                UnitatMesura um = mapResultSetToUnitatMesura(rs);
                System.out.println("✅ findById(" + codi + ") - Trobat: " + um.getUmNom());
                return um;
            } else {
                System.out.println("⚠️  findById(" + codi + ") - No trobat");
                return null;
            }
            
        } catch (SQLException e) {
            logError(e);
            return null;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // HELPER - Mapeja ResultSet → UnitatMesura
    // ============================================
    
    private UnitatMesura mapResultSetToUnitatMesura(ResultSet rs) throws SQLException {
        UnitatMesura um = new UnitatMesura();
        um.setUmCodi(rs.getString("um_codi"));
        um.setUmNom(rs.getString("um_nom"));
        return um;
    }
}
