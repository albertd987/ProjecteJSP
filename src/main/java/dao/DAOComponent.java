package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOComponent;
import model.Component;

/**
 * Implementació DAO per a Component
 * Gestiona operacions CRUD sobre les taules Item i Component
 * 
 * IMPORTANT: cm_preu_mig és calculat automàticament pels triggers Oracle
 * quan s'insereixen/actualitzen/eliminen preus a la taula Prov_Comp
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOComponent extends AbstractDAOComponent {

    // ============================================
    // INSERTAR - Crea un component nou
    // ============================================
    
    @Override
    public boolean insertar(Component c) {
        // Validar primer
        if (!validarEntitat(c)) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement psItem = null;
        PreparedStatement psComponent = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);  // Iniciar transacció
            
            // 1️ INSERT a Item (taula pare)
            String sqlItem = "INSERT INTO Item (it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
            psItem = conn.prepareStatement(sqlItem);
            psItem.setString(1, c.getItCodi());
            psItem.setString(2, "C");  // Tipus Component
            psItem.setString(3, c.getItNom());
            psItem.setString(4, c.getItDesc());
            psItem.setInt(5, c.getItStock() != null ? c.getItStock() : 0);
            psItem.setString(6, c.getItFoto());
            psItem.executeUpdate();
            
            // 2️ INSERT a Component (taula filla)
            String sqlComponent = "INSERT INTO Component (cm_codi, cm_um_codi, cm_codi_fabricant, cm_preu_mig) " +
                                 "VALUES (?, ?, ?, ?)";
            psComponent = conn.prepareStatement(sqlComponent);
            psComponent.setString(1, c.getCmCodi());
            psComponent.setString(2, c.getCmUmCodi());
            psComponent.setString(3, c.getCmCodiFabricant());
            psComponent.setDouble(4, 0);  // El trigger el calcularà automàticament
            psComponent.executeUpdate();
            
            conn.commit();
            System.out.println(" Component " + c.getCmCodi() + " inserit correctament");
            return true;
            
        } catch (SQLException e) {
            // Rollback en cas d'error
            if (conn != null) {
                try { 
                    conn.rollback();
                    System.err.println("  Rollback executat");
                } catch (SQLException ex) {
                    System.err.println(" Error fent rollback: " + ex.getMessage());
                }
            }
            logError(e);
            return false;
            
        } finally {
            tancarRecursos(psComponent);
            tancarRecursos(psItem);
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { }
            }
            tancarRecursos(conn);
        }
    }

    // ============================================
    // ACTUALITZAR - Modifica un component existent
    // ============================================
    
    @Override
    public boolean actualitzar(Component c) {
        if (!validarEntitat(c)) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement psItem = null;
        PreparedStatement psComponent = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // 1️ UPDATE Item
            String sqlItem = "UPDATE Item SET it_nom = ?, it_desc = ?, it_stock = ?, it_foto = ? " +
                            "WHERE it_codi = ?";
            psItem = conn.prepareStatement(sqlItem);
            psItem.setString(1, c.getItNom());
            psItem.setString(2, c.getItDesc());
            psItem.setInt(3, c.getItStock() != null ? c.getItStock() : 0);
            psItem.setString(4, c.getItFoto());
            psItem.setString(5, c.getItCodi());
            int rowsItem = psItem.executeUpdate();
            
            // 2️ UPDATE Component (NO actualitzem cm_preu_mig, el trigger ho fa!)
            String sqlComponent = "UPDATE Component SET cm_um_codi = ?, cm_codi_fabricant = ? " +
                                 "WHERE cm_codi = ?";
            psComponent = conn.prepareStatement(sqlComponent);
            psComponent.setString(1, c.getCmUmCodi());
            psComponent.setString(2, c.getCmCodiFabricant());
            psComponent.setString(3, c.getCmCodi());
            int rowsComponent = psComponent.executeUpdate();
            
            if (rowsItem > 0 && rowsComponent > 0) {
                conn.commit();
                System.out.println(" Component " + c.getCmCodi() + " actualitzat");
                return true;
            } else {
                conn.rollback();
                System.err.println(" Component no trobat: " + c.getCmCodi());
                return false;
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }
            logError(e);
            return false;
            
        } finally {
            tancarRecursos(psComponent);
            tancarRecursos(psItem);
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { }
            }
            tancarRecursos(conn);
        }
    }

    // ============================================
    // ELIMINAR - Esborra un component
    // ============================================
    
    @Override
    public boolean eliminar(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println(" Codi no pot ser buit");
            return false;
        }
        
        Connection conn = null;
        PreparedStatement psComponent = null;
        PreparedStatement psItem = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // 1️ DELETE de Component primer (FK constraint)
            String sqlComponent = "DELETE FROM Component WHERE cm_codi = ?";
            psComponent = conn.prepareStatement(sqlComponent);
            psComponent.setString(1, codi);
            int rowsComponent = psComponent.executeUpdate();
            
            // 2️ DELETE de Item
            String sqlItem = "DELETE FROM Item WHERE it_codi = ?";
            psItem = conn.prepareStatement(sqlItem);
            psItem.setString(1, codi);
            int rowsItem = psItem.executeUpdate();
            
            if (rowsComponent > 0 && rowsItem > 0) {
                conn.commit();
                System.out.println(" Component " + codi + " eliminat");
                return true;
            } else {
                conn.rollback();
                System.err.println("  Component no trobat: " + codi);
                return false;
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }
            logError(e);
            return false;
            
        } finally {
            tancarRecursos(psItem);
            tancarRecursos(psComponent);
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { }
            }
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FIND BY ID - Busca component per codi
    // ============================================
    
    @Override
    public Component findById(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto, " +
                     "       c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig " +
                     "FROM Item i " +
                     "JOIN Component c ON i.it_codi = c.cm_codi " +
                     "WHERE c.cm_codi = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, codi);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToComponent(rs);
            }
            return null;
            
        } catch (SQLException e) {
            logError(e);
            return null;
        }
    }

    // ============================================
    // FIND ALL - Llista tots els components
    // ============================================
    
    @Override
    public List<Component> findAll() {
        List<Component> llista = new ArrayList<>();
        
        String sql = "SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto, " +
                     "       c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig " +
                     "FROM Item i " +
                     "JOIN Component c ON i.it_codi = c.cm_codi " +
                     "ORDER BY i.it_codi";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                llista.add(mapResultSetToComponent(rs));
            }
            
        } catch (SQLException e) {
            logError(e);
        }
        
        return llista;
    }

    // ============================================
    // FIND ALL PAGINAT
    // ============================================
    
    @Override
    public List<Component> findAllPaginat(int page, int size) {
        List<Component> llista = new ArrayList<>();
        
        // Calcular offset
        int offset = (page - 1) * size;
        
        String sql = "SELECT * FROM ( " +
                     "  SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto, " +
                     "         c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig, " +
                     "         ROW_NUMBER() OVER (ORDER BY i.it_codi) as rnum " +
                     "  FROM Item i " +
                     "  JOIN Component c ON i.it_codi = c.cm_codi " +
                     ") WHERE rnum > ? AND rnum <= ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, offset);
            ps.setInt(2, offset + size);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                llista.add(mapResultSetToComponent(rs));
            }
            
        } catch (SQLException e) {
            logError(e);
        }
        
        return llista;
    }

    // ============================================
    // FILTRAR PER CODI - Search amb LIKE
    // ============================================
    
    @Override
    public List<Component> filtrarPerCodi(String codiPattern) {
        List<Component> llista = new ArrayList<>();
        
        String sql = "SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto, " +
                     "       c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig " +
                     "FROM Item i " +
                     "JOIN Component c ON i.it_codi = c.cm_codi " +
                     "WHERE UPPER(c.cm_codi) LIKE UPPER(?) " +
                     "ORDER BY i.it_codi";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + codiPattern + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                llista.add(mapResultSetToComponent(rs));
            }
            
        } catch (SQLException e) {
            logError(e);
        }
        
        return llista;
    }

    // ============================================
    // FILTRAR PER PREU MIG - Rang de preus
    // ============================================
    
    @Override
    public List<Component> filtrarPerPreuMig(double min, double max) {
        List<Component> llista = new ArrayList<>();
        
        String sql = "SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto, " +
                     "       c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig " +
                     "FROM Item i " +
                     "JOIN Component c ON i.it_codi = c.cm_codi " +
                     "WHERE c.cm_preu_mig BETWEEN ? AND ? " +
                     "ORDER BY c.cm_preu_mig";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                llista.add(mapResultSetToComponent(rs));
            }
            
        } catch (SQLException e) {
            logError(e);
        }
        
        return llista;
    }

    // ============================================
    // GET COMPONENT AMB PREU ACTUALITZAT
    // Refresca el preu des de BD (per veure canvis dels triggers)
    // ============================================
    
    @Override
    public Component getComponentAmbPreuActualitzat(String cmCodi) {
        // És el mateix que findById, però amb un nom semànticament clar
        return findById(cmCodi);
    }

    // ============================================
    // COUNT TOTAL - Compta components
    // ============================================
    
    @Override
    public int countTotal() {
        String sql = "SELECT COUNT(*) as total FROM Component";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            logError(e);
        }
        
        return 0;
    }

    // ============================================
    // HELPER - Mapeja ResultSet a Component
    // ============================================
    
    /**
     * Mapeja un ResultSet a un objecte Component
     * Assumeix que el ResultSet té totes les columnes necessàries
     */
    private Component mapResultSetToComponent(ResultSet rs) throws SQLException {
        Component comp = new Component();
        
        // Dades d'Item (classe pare)
        comp.setItCodi(rs.getString("it_codi"));
        comp.setItTipus(rs.getString("it_tipus"));
        comp.setItNom(rs.getString("it_nom"));
        comp.setItDesc(rs.getString("it_desc"));
        comp.setItStock(rs.getInt("it_stock"));
        comp.setItFoto(rs.getString("it_foto"));
        
        // Dades de Component (classe filla)
        comp.setCmCodi(rs.getString("it_codi"));
        comp.setCmUmCodi(rs.getString("cm_um_codi"));
        comp.setCmCodiFabricant(rs.getString("cm_codi_fabricant"));
        comp.setCmPreuMig(rs.getDouble("cm_preu_mig"));
        
        return comp;
    }
}