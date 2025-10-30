package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOMunicipi;
import model.Municipi;

/**
 * Implementació DAO per Municipi (READ-ONLY)
 * Taula mestre de catàleg - només consultes
 * Nota: La PK és composta (mu_pr_codi, mu_num)
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOMunicipi extends AbstractDAOMunicipi {

    // ============================================
    // FIND ALL - Obté tots els municipis
    // ============================================
    
    @Override
    public List<Municipi> findAll() {
        List<Municipi> municipis = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT mu_pr_codi, mu_num, mu_nom " +
                        "FROM Municipi " +
                        "ORDER BY mu_pr_codi, mu_num";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Municipi m = mapResultSetToMunicipi(rs);
                municipis.add(m);
            }
            
            System.out.println("✅ findAll() - Trobats " + municipis.size() + " municipis");
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(stmt);
            tancarRecursos(conn);
        }
        
        return municipis;
    }

    // ============================================
    // FIND BY ID - Cerca per PK composta
    // ============================================
    
    @Override
    public Municipi findById(String prCodi, String muNum) {
        if (prCodi == null || prCodi.trim().isEmpty() || 
            muNum == null || muNum.trim().isEmpty()) {
            System.err.println("❌ Codi província i número municipi obligatoris");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT mu_pr_codi, mu_num, mu_nom " +
                        "FROM Municipi " +
                        "WHERE mu_pr_codi = ? AND mu_num = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);
            ps.setString(2, muNum);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Municipi m = mapResultSetToMunicipi(rs);
                System.out.println("✅ findById(" + prCodi + "," + muNum + ") - Trobat: " + m.getMuNom());
                return m;
            } else {
                System.out.println("⚠️  findById(" + prCodi + "," + muNum + ") - No trobat");
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
    // GET MUNICIPIS DE PROVINCIA
    // ============================================
    
    @Override
    public List<Municipi> getMunicipisDeProvincia(String prCodi) {
        List<Municipi> municipis = new ArrayList<>();
        
        if (prCodi == null || prCodi.trim().isEmpty()) {
            System.err.println("❌ Codi província no pot ser buit");
            return municipis;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT mu_pr_codi, mu_num, mu_nom " +
                        "FROM Municipi " +
                        "WHERE mu_pr_codi = ? " +
                        "ORDER BY mu_num";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Municipi m = mapResultSetToMunicipi(rs);
                municipis.add(m);
            }
            
            System.out.println("✅ getMunicipisDeProvincia(" + prCodi + ") - Trobats " + municipis.size() + " municipis");
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
        
        return municipis;
    }

    // ============================================
    // HELPER - Mapeja ResultSet → Municipi
    // ============================================
    
    private Municipi mapResultSetToMunicipi(ResultSet rs) throws SQLException {
        Municipi m = new Municipi();
        m.setMuPrCodi(rs.getString("mu_pr_codi"));
        m.setMuNum(rs.getString("mu_num"));
        m.setMuNom(rs.getString("mu_nom"));
        return m;
    }
}
