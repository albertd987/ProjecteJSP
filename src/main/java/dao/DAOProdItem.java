package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOProdItem;
import model.ProdItem;
import util.ConnexioOracle;

/**
 * Implementació Oracle de IDAOProdItem
 * Gestiona la relació N:N entre Producte i Item (BOM - Bill of Materials)
 * 
 * IMPORTANT: PK composta (pi_pr_codi, pi_it_codi)
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public class DAOProdItem extends AbstractDAOProdItem {

    /**
     * Constructor buit
     */
    public DAOProdItem() {
        super();
    }

    // ==========================================
    // MÈTODE ABSTRACTE IMPLEMENTAT
    // ==========================================

    @Override
    protected Connection getConnection() throws SQLException {
        return ConnexioOracle.getConnection();
    }

    // ==========================================
    // MÈTODES CRUD
    // ==========================================

    /**
     * Insereix una nova relació Producte-Item
     */
    @Override
    public boolean insertar(ProdItem prodItem) {
        if (!validarEntitat(prodItem)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();

            String sql = """
                INSERT INTO Prod_Item (pi_pr_codi, pi_it_codi, quantitat)
                VALUES (?, ?, ?)
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prodItem.getPiPrCodi());
            ps.setString(2, prodItem.getPiItCodi());
            ps.setInt(3, prodItem.getQuantitat());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logInfo("ProdItem inserit correctament: " + 
                       prodItem.getPiPrCodi() + " - " + prodItem.getPiItCodi());
                return true;
            } else {
                logError("No s'ha pogut inserir el ProdItem");
                return false;
            }

        } catch (SQLException e) {
            logError("Error insertant ProdItem: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, ps, conn);
        }
    }

    /**
     * Actualitza una relació Producte-Item existent
     * Típicament es canvia la quantitat
     */
    @Override
    public boolean actualitzar(ProdItem prodItem) {
        if (!validarEntitat(prodItem)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();

            String sql = """
                UPDATE Prod_Item
                SET quantitat = ?
                WHERE pi_pr_codi = ? AND pi_it_codi = ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setInt(1, prodItem.getQuantitat());
            ps.setString(2, prodItem.getPiPrCodi());
            ps.setString(3, prodItem.getPiItCodi());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logInfo("ProdItem actualitzat correctament: " + 
                       prodItem.getPiPrCodi() + " - " + prodItem.getPiItCodi());
                return true;
            } else {
                logError("No s'ha trobat el ProdItem a actualitzar: " + 
                        prodItem.getPiPrCodi() + " - " + prodItem.getPiItCodi());
                return false;
            }

        } catch (SQLException e) {
            logError("Error actualitzant ProdItem: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, ps, conn);
        }
    }

    /**
     * Elimina una relació Producte-Item
     * Amb PK composta (2 paràmetres)
     */
    @Override
    public boolean eliminar(String prCodi, String itCodi) {
        if (!validarPKComposta(prCodi, itCodi)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();

            String sql = """
                DELETE FROM Prod_Item
                WHERE pi_pr_codi = ? AND pi_it_codi = ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);
            ps.setString(2, itCodi);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logInfo("ProdItem eliminat correctament: " + prCodi + " - " + itCodi);
                return true;
            } else {
                logError("No s'ha trobat el ProdItem a eliminar: " + prCodi + " - " + itCodi);
                return false;
            }

        } catch (SQLException e) {
            logError("Error eliminant ProdItem: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, ps, conn);
        }
    }

    // ==========================================
    // MÈTODES DE CONSULTA
    // ==========================================

    /**
     * Cerca una relació Producte-Item per la seva PK composta
     * Amb PK composta (2 paràmetres)
     */
    @Override
    public ProdItem findById(String prCodi, String itCodi) {
        if (!validarPKComposta(prCodi, itCodi)) {
            return null;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT pi_pr_codi, pi_it_codi, quantitat
                FROM Prod_Item
                WHERE pi_pr_codi = ? AND pi_it_codi = ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);
            ps.setString(2, itCodi);

            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProdItem(rs);
            } else {
                logInfo("No s'ha trobat el ProdItem: " + prCodi + " - " + itCodi);
                return null;
            }

        } catch (SQLException e) {
            logError("Error cercant ProdItem: " + e.getMessage());
            return null;

        } finally {
            tancarRecursos(rs, ps, conn);
        }
    }

    /**
     * Obté totes les relacions Producte-Item
     */
    @Override
    public List<ProdItem> findAll() {
        List<ProdItem> prodItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT pi_pr_codi, pi_it_codi, quantitat
                FROM Prod_Item
                ORDER BY pi_pr_codi, pi_it_codi
                """;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                prodItems.add(mapResultSetToProdItem(rs));
            }

            logInfo("S'han trobat " + prodItems.size() + " relacions Producte-Item");

        } catch (SQLException e) {
            logError("Error obtenint totes les relacions: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return prodItems;
    }

    /**
     * Obté tots els items (components/subproductes) d'un producte
     * Útil per generar el BOM (Bill of Materials)
     */
    @Override
    public List<ProdItem> getItemsDelProducte(String prCodi) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            logError("Codi de producte null o buit");
            return new ArrayList<>();
        }

        List<ProdItem> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT pi_pr_codi, pi_it_codi, quantitat
                FROM Prod_Item
                WHERE pi_pr_codi = ?
                ORDER BY pi_it_codi
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToProdItem(rs));
            }

            logInfo("Producte " + prCodi + " té " + items.size() + " items");

        } catch (SQLException e) {
            logError("Error obtenint items del producte: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return items;
    }

    /**
     * Afegeix un item a un producte (mètode helper)
     * Equivalent a insertar però amb paràmetres separats
     */
    @Override
    public boolean afegirItemAProducte(String prCodi, String itCodi, int quantitat) {
        if (!validarPKComposta(prCodi, itCodi)) {
            return false;
        }

        if (quantitat <= 0) {
            logError("Quantitat ha de ser > 0, valor actual: " + quantitat);
            return false;
        }

        // Crear ProdItem i cridar insertar
        ProdItem prodItem = new ProdItem(prCodi, itCodi, quantitat);
        return insertar(prodItem);
    }

    /**
     * Compta el total de relacions Producte-Item
     */
    @Override
    public int countTotal() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = "SELECT COUNT(*) AS total FROM Prod_Item";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                logInfo("Total de relacions Producte-Item: " + total);
                return total;
            }

        } catch (SQLException e) {
            logError("Error comptant relacions: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return 0;
    }

}