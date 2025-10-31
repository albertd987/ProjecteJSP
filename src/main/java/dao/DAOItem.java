package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOItem;
import model.Item;

/**
 * Implementació DAO per a Item
 * Gestiona operacions CRUD sobre la taula Item (taula base)
 * 
 * IMPORTANT: Item és la taula base. Component i Producte hereten d'Item.
 * Aquesta classe gestiona només la taula Item, no les taules filles.
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOItem extends AbstractDAOItem {

    // ============================================
    // INSERTAR - Crea un item nou
    // ============================================
    
    @Override
    public boolean insertar(Item item) {
        // Validar primer
        if (!validarEntitat(item)) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            
            String sql = "INSERT INTO Item (it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, item.getItCodi());
            ps.setString(2, item.getItTipus());
            ps.setString(3, item.getItNom());
            ps.setString(4, item.getItDesc());
            ps.setInt(5, item.getItStock() != null ? item.getItStock() : 0);
            ps.setString(6, item.getItFoto());
            
            int files = ps.executeUpdate();
            System.out.println("✅ Item inserit: " + item.getItCodi());
            return files > 0;
            
        } catch (SQLException e) {
            logError(e);
            return false;
        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // ACTUALITZAR - Modifica un item existent
    // ============================================
    
    @Override
    public boolean actualitzar(Item item) {
        if (!validarEntitat(item)) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            
            // Actualitzem tot excepte el codi (PK immutable)
            String sql = "UPDATE Item " +
                        "SET it_tipus = ?, it_nom = ?, it_desc = ?, it_stock = ?, it_foto = ? " +
                        "WHERE it_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, item.getItTipus());
            ps.setString(2, item.getItNom());
            ps.setString(3, item.getItDesc());
            ps.setInt(4, item.getItStock() != null ? item.getItStock() : 0);
            ps.setString(5, item.getItFoto());
            ps.setString(6, item.getItCodi());  // WHERE
            
            int files = ps.executeUpdate();
            
            if (files > 0) {
                System.out.println("✅ Item actualitzat: " + item.getItCodi());
                return true;
            } else {
                System.err.println("⚠️  Item no trobat: " + item.getItCodi());
                return false;
            }
            
        } catch (SQLException e) {
            logError(e);
            return false;
        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // ELIMINAR - Esborra un item
    // ============================================
    
    @Override
    public boolean eliminar(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println("❌ Codi no pot ser buit");
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            
            String sql = "DELETE FROM Item WHERE it_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, codi);
            
            int files = ps.executeUpdate();
            
            if (files > 0) {
                System.out.println("✅ Item eliminat: " + codi);
                return true;
            } else {
                System.err.println("⚠️  Item no trobat: " + codi);
                return false;
            }
            
        } catch (SQLException e) {
            logError(e);
            return false;
        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FINDBYID - Cerca per codi
    // ============================================
    
    @Override
    public Item findById(String codi) {
        if (codi == null || codi.trim().isEmpty()) {
            System.err.println("❌ Codi no pot ser buit");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto " +
                        "FROM Item " +
                        "WHERE it_codi = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, codi);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToItem(rs);
            }
            
            System.out.println("⚠️  Item no trobat: " + codi);
            return null;
            
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
    // FINDALL - Obté tots els items
    // ============================================
    
    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto " +
                        "FROM Item " +
                        "ORDER BY it_codi";
            
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
            System.out.println("📋 Items trobats: " + items.size());
            return items;
            
        } catch (SQLException e) {
            logError(e);
            return items;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FILTRAR PER TIPUS
    // ============================================
    
    @Override
    public List<Item> filtrarPerTipus(String tipus) {
        List<Item> items = new ArrayList<>();
        
        if (tipus == null || tipus.trim().isEmpty()) {
            System.err.println("❌ Tipus no pot ser buit");
            return items;
        }
        
        if (!tipus.equals("C") && !tipus.equals("P")) {
            System.err.println("❌ Tipus ha de ser 'C' o 'P'");
            return items;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto " +
                        "FROM Item " +
                        "WHERE it_tipus = ? " +
                        "ORDER BY it_codi";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, tipus);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
            String tipusNom = tipus.equals("C") ? "Components" : "Productes";
            System.out.println("📋 " + tipusNom + " trobats: " + items.size());
            return items;
            
        } catch (SQLException e) {
            logError(e);
            return items;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // CERCAR PER NOM
    // ============================================
    
    @Override
    public List<Item> cercarPerNom(String nomPattern) {
        List<Item> items = new ArrayList<>();
        
        if (nomPattern == null || nomPattern.trim().isEmpty()) {
            System.err.println("❌ Patró de cerca no pot ser buit");
            return items;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto " +
                        "FROM Item " +
                        "WHERE UPPER(it_nom) LIKE UPPER(?) " +
                        "ORDER BY it_codi";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nomPattern + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
            System.out.println("🔍 Items trobats amb nom '" + nomPattern + "': " + items.size());
            return items;
            
        } catch (SQLException e) {
            logError(e);
            return items;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // OBTENIR ITEMS AMB STOCK BAIX
    // ============================================
    
    @Override
    public List<Item> obtenirItemsAmbStockBaix(int stockMinim) {
        List<Item> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto " +
                        "FROM Item " +
                        "WHERE it_stock < ? " +
                        "ORDER BY it_stock ASC, it_codi";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, stockMinim);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
            System.out.println("⚠️  Items amb stock < " + stockMinim + ": " + items.size());
            return items;
            
        } catch (SQLException e) {
            logError(e);
            return items;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // COUNT TOTAL
    // ============================================
    
    @Override
    public int countTotal() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            String sql = "SELECT COUNT(*) as total FROM Item";
            
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("📊 Total items: " + total);
                return total;
            }
            
            return 0;
            
        } catch (SQLException e) {
            logError(e);
            return 0;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FINDALL PAGINAT
    // ============================================
    
    @Override
    public List<Item> findAllPaginat(int page, int size) {
        List<Item> items = new ArrayList<>();
        
        if (page < 1) {
            System.err.println("❌ La pàgina ha de ser >= 1");
            return items;
        }
        if (size < 1) {
            System.err.println("❌ La mida ha de ser >= 1");
            return items;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Oracle: ROW_NUMBER() per paginació
            String sql = "SELECT * FROM ( " +
                        "  SELECT i.*, ROW_NUMBER() OVER (ORDER BY it_codi) as rnum " +
                        "  FROM Item i " +
                        ") " +
                        "WHERE rnum BETWEEN ? AND ?";
            
            int inici = (page - 1) * size + 1;
            int fi = page * size;
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inici);
            ps.setInt(2, fi);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
            
            System.out.println("📄 Pàgina " + page + " (mida " + size + "): " + items.size() + " items");
            return items;
            
        } catch (SQLException e) {
            logError(e);
            return items;
        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }
}