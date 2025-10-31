package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOProveidor;
import model.Proveidor;

/**
 * Implementació DAO per Proveidor (READ-ONLY)
 * Taula mestre de catàleg - només consultes
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOProveidor extends AbstractDAOProveidor {

    // ============================================
    // FIND ALL - Obté tots els proveïdors
    // ============================================
    
    @Override
    public List<Proveidor> findAll() {
        List<Proveidor> proveidors = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pv_codi, pv_cif, pv_rao_social, pv_lin_adre_fac, " +
                        "pv_persona_contacte, pv_telef_contacte, pv_mu_pr_codi, pv_mu_num " +
                        "FROM Proveidor " +
                        "ORDER BY pv_rao_social";
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Proveidor p = mapResultSetToProveidor(rs);
                proveidors.add(p);
            }
            
            System.out.println("findAll() - Trobats " + proveidors.size() + " proveïdors");
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(stmt);
            tancarRecursos(conn);
        }
        
        return proveidors;
    }

    // ============================================
    // FIND BY ID - Cerca per codi
    // ============================================
    
    @Override
    public Proveidor findById(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println("❌ Codi proveïdor no pot ser buit");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pv_codi, pv_cif, pv_rao_social, pv_lin_adre_fac, " +
                        "pv_persona_contacte, pv_telef_contacte, pv_mu_pr_codi, pv_mu_num " +
                        "FROM Proveidor " +
                        "WHERE pv_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, codi);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Proveidor p = mapResultSetToProveidor(rs);
                System.out.println("findById(" + codi + ") - Trobat: " + p.getPvRaoSocial());
                return p;
            } else {
                System.out.println("findById(" + codi + ") - No trobat");
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
    // FILTRAR PER MUNICIPI
    // ============================================
    
    @Override
    public List<Proveidor> filtrarPerMunicipi(String prCodi, String muNum) {
        List<Proveidor> proveidors = new ArrayList<>();
        
        if (prCodi == null || prCodi.trim().isEmpty() || 
            muNum == null || muNum.trim().isEmpty()) {
            System.err.println("❌ Codi província i número municipi obligatoris");
            return proveidors;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pv_codi, pv_cif, pv_rao_social, pv_lin_adre_fac, " +
                        "pv_persona_contacte, pv_telef_contacte, pv_mu_pr_codi, pv_mu_num " +
                        "FROM Proveidor " +
                        "WHERE pv_mu_pr_codi = ? AND pv_mu_num = ? " +
                        "ORDER BY pv_rao_social";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);
            ps.setString(2, muNum);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Proveidor p = mapResultSetToProveidor(rs);
                proveidors.add(p);
            }
            
            System.out.println("✅ filtrarPerMunicipi(" + prCodi + "," + muNum + ") - Trobats " + proveidors.size());
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
        
        return proveidors;
    }

    // ============================================
    // FILTRAR PER NOM (case-insensitive, cerca parcial)
    // ============================================
    
    @Override
    public List<Proveidor> filtrarPerNom(String nomPattern) {
        List<Proveidor> proveidors = new ArrayList<>();
        
        if (nomPattern == null || nomPattern.trim().isEmpty()) {
            System.err.println("❌ Patró de cerca no pot ser buit");
            return proveidors;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT pv_codi, pv_cif, pv_rao_social, pv_lin_adre_fac, " +
                        "pv_persona_contacte, pv_telef_contacte, pv_mu_pr_codi, pv_mu_num " +
                        "FROM Proveidor " +
                        "WHERE UPPER(pv_rao_social) LIKE UPPER(?) " +
                        "ORDER BY pv_rao_social";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nomPattern + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Proveidor p = mapResultSetToProveidor(rs);
                proveidors.add(p);
            }
            
            System.out.println("✅ filtrarPerNom('" + nomPattern + "') - Trobats " + proveidors.size());
            
        } catch (SQLException e) {
            logError(e);
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
        
        return proveidors;
    }

    // ============================================
    // HELPER - Mapeja ResultSet → Proveidor
    // ============================================
    
    private Proveidor mapResultSetToProveidor(ResultSet rs) throws SQLException {
        Proveidor p = new Proveidor();
        p.setPvCodi(rs.getString("pv_codi"));
        p.setPvCif(rs.getString("pv_cif"));
        p.setPvRaoSocial(rs.getString("pv_rao_social"));
        p.setPvLinAdreFac(rs.getString("pv_lin_adre_fac"));
        p.setPvPersonaContacte(rs.getString("pv_persona_contacte"));
        p.setPvTelefContacte(rs.getString("pv_telef_contacte"));
        p.setPvMuPrCodi(rs.getString("pv_mu_pr_codi"));
        p.setPvMuNum(rs.getString("pv_mu_num"));
        return p;
    }
}
