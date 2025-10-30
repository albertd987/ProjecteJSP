package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOProvincia;
import model.Provincia;

/**
 * Implementació DAO per Provincia (READ-ONLY)
 * Taula mestre de catàleg - només consultes
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOProvincia extends AbstractDAOProvincia {

    // ============================================
    // FIND ALL - Obté totes les províncies
    // ============================================
    
    @Override
    public List<Provincia> findAll() {
        List<Provincia> provincies = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pr_codi, pr_nom FROM Provincia ORDER BY pr_codi";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Provincia p = mapResultSetToProvincia(rs);
                provincies.add(p);
            }
            
            System.out.println("✅ findAll() - Trobades " + provincies.size() + " províncies");
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(stmt);
            tancarRecursos(conn);
        }
        
        return provincies;
    }

    // ============================================
    // FIND BY ID - Cerca per codi
    // ============================================
    
    @Override
    public Provincia findById(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println("❌ Codi província no pot ser buit");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pr_codi, pr_nom FROM Provincia WHERE pr_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, codi);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Provincia p = mapResultSetToProvincia(rs);
                System.out.println("✅ findById(" + codi + ") - Trobat: " + p.getPrNom());
                return p;
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
    // HELPER - Mapeja ResultSet → Provincia
    // ============================================
    
    private Provincia mapResultSetToProvincia(ResultSet rs) throws SQLException {
        Provincia p = new Provincia();
        p.setPrCodi(rs.getString("pr_codi"));
        p.setPrNom(rs.getString("pr_nom"));
        return p;
    }
}
