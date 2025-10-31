package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOProducte;
import model.Producte;
import util.ConnexioOracle;

/**
 * Implementació Oracle de IDAOProducte
 * Gestiona la persistència de Producte amb herència d'Item
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public class DAOProducte extends AbstractDAOProducte {

    /**
     * Constructor buit
     */
    public DAOProducte() {
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
     * Insereix un nou producte (INSERT a Item + Producte)
     * Utilitza transacció per garantir consistència
     */
    @Override
    public boolean insertar(Producte producte) {
        if (!validarEntitat(producte)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement psItem = null;
        PreparedStatement psProducte = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Iniciar transacció

            // 1️⃣ INSERT a Item (taula pare)
            String sqlItem = """
                INSERT INTO Item (it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

            psItem = conn.prepareStatement(sqlItem);
            psItem.setString(1, producte.getItCodi());
            psItem.setString(2, "P"); // Tipus: Producte
            psItem.setString(3, producte.getItNom());
            psItem.setString(4, producte.getItDesc());
            psItem.setInt(5, producte.getItStock() != null ? producte.getItStock() : 0);
            psItem.setString(6, producte.getItFoto());

            int rowsItem = psItem.executeUpdate();

            // 2️⃣ INSERT a Producte (taula filla)
            String sqlProducte = """
                INSERT INTO Producte (pr_codi)
                VALUES (?)
                """;

            psProducte = conn.prepareStatement(sqlProducte);
            psProducte.setString(1, producte.getPrCodi());

            int rowsProducte = psProducte.executeUpdate();

            // ✅ Commit si ambdós INSERTs van bé
            if (rowsItem > 0 && rowsProducte > 0) {
                conn.commit();
                logInfo("Producte inserit correctament: " + producte.getPrCodi());
                return true;
            } else {
                conn.rollback();
                logError("No s'ha pogut inserir el producte");
                return false;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logError("Error fent rollback: " + ex.getMessage());
                }
            }
            logError("Error insertant producte: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, psProducte, null);
            tancarRecursos(null, psItem, conn);
        }
    }

    /**
     * Actualitza un producte existent (UPDATE Item + Producte)
     * Nota: Producte no té camps propis a actualitzar, només Item
     */
    @Override
    public boolean actualitzar(Producte producte) {
        if (!validarEntitat(producte)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();

            // UPDATE només a Item (Producte no té camps a actualitzar)
            String sql = """
                UPDATE Item
                SET it_nom = ?, 
                    it_desc = ?, 
                    it_stock = ?, 
                    it_foto = ?
                WHERE it_codi = ? AND it_tipus = 'P'
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, producte.getItNom());
            ps.setString(2, producte.getItDesc());
            ps.setInt(3, producte.getItStock() != null ? producte.getItStock() : 0);
            ps.setString(4, producte.getItFoto());
            ps.setString(5, producte.getItCodi());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logInfo("Producte actualitzat correctament: " + producte.getPrCodi());
                return true;
            } else {
                logError("No s'ha trobat el producte a actualitzar: " + producte.getPrCodi());
                return false;
            }

        } catch (SQLException e) {
            logError("Error actualitzant producte: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, ps, conn);
        }
    }

    /**
     * Elimina un producte (DELETE Producte → Item amb transacció)
     */
    @Override
    public boolean eliminar(String prCodi) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            logError("Codi de producte null o buit");
            return false;
        }

        Connection conn = null;
        PreparedStatement psProducte = null;
        PreparedStatement psItem = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Iniciar transacció

            // 1️⃣ DELETE de Producte (taula filla)
            String sqlProducte = "DELETE FROM Producte WHERE pr_codi = ?";
            psProducte = conn.prepareStatement(sqlProducte);
            psProducte.setString(1, prCodi);
            int rowsProducte = psProducte.executeUpdate();

            // 2️⃣ DELETE d'Item (taula pare)
            String sqlItem = "DELETE FROM Item WHERE it_codi = ? AND it_tipus = 'P'";
            psItem = conn.prepareStatement(sqlItem);
            psItem.setString(1, prCodi);
            int rowsItem = psItem.executeUpdate();

            // ✅ Commit si ambdós DELETEs van bé
            if (rowsProducte > 0 && rowsItem > 0) {
                conn.commit();
                logInfo("Producte eliminat correctament: " + prCodi);
                return true;
            } else {
                conn.rollback();
                logError("No s'ha trobat el producte a eliminar: " + prCodi);
                return false;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logError("Error fent rollback: " + ex.getMessage());
                }
            }
            logError("Error eliminant producte: " + e.getMessage());
            return false;

        } finally {
            tancarRecursos(null, psItem, null);
            tancarRecursos(null, psProducte, conn);
        }
    }

    // ==========================================
    // MÈTODES DE CONSULTA
    // ==========================================

    /**
     * Cerca un producte pel seu codi
     */
    @Override
    public Producte findById(String prCodi) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            logError("Codi de producte null o buit");
            return null;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                       p.pr_codi
                FROM Item i
                JOIN Producte p ON i.it_codi = p.pr_codi
                WHERE p.pr_codi = ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);

            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProducte(rs);
            } else {
                logInfo("No s'ha trobat el producte: " + prCodi);
                return null;
            }

        } catch (SQLException e) {
            logError("Error cercant producte: " + e.getMessage());
            return null;

        } finally {
            tancarRecursos(rs, ps, conn);
        }
    }

    /**
     * Obté tots els productes
     */
    @Override
    public List<Producte> findAll() {
        List<Producte> productes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                       p.pr_codi
                FROM Item i
                JOIN Producte p ON i.it_codi = p.pr_codi
                ORDER BY p.pr_codi
                """;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                productes.add(mapResultSetToProducte(rs));
            }

            logInfo("S'han trobat " + productes.size() + " productes");

        } catch (SQLException e) {
            logError("Error obtenint tots els productes: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return productes;
    }

    /**
     * Obté productes paginats (paginació Oracle amb ROW_NUMBER)
     */
    @Override
    public List<Producte> findAllPaginat(int page, int size) {
        if (page < 1 || size < 1) {
            logError("Paràmetres de paginació invàlids: page=" + page + ", size=" + size);
            return new ArrayList<>();
        }

        List<Producte> productes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            int startRow = (page - 1) * size + 1;
            int endRow = page * size;

            String sql = """
                SELECT * FROM (
                    SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                           p.pr_codi,
                           ROW_NUMBER() OVER (ORDER BY p.pr_codi) AS rnum
                    FROM Item i
                    JOIN Producte p ON i.it_codi = p.pr_codi
                )
                WHERE rnum BETWEEN ? AND ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setInt(1, startRow);
            ps.setInt(2, endRow);

            rs = ps.executeQuery();

            while (rs.next()) {
                productes.add(mapResultSetToProducte(rs));
            }

            logInfo("Pàgina " + page + " amb " + productes.size() + " productes");

        } catch (SQLException e) {
            logError("Error obtenint productes paginats: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return productes;
    }

    /**
     * Filtra productes per patró de codi (case-insensitive)
     */
    @Override
    public List<Producte> filtrarPerCodi(String codiPattern) {
        List<Producte> productes = new ArrayList<>();

        if (codiPattern == null || codiPattern.trim().isEmpty()) {
            logInfo("Patró buit, retornant tots els productes");
            return findAll();
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = """
                SELECT i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                       p.pr_codi
                FROM Item i
                JOIN Producte p ON i.it_codi = p.pr_codi
                WHERE UPPER(p.pr_codi) LIKE UPPER(?)
                ORDER BY p.pr_codi
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + codiPattern + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                productes.add(mapResultSetToProducte(rs));
            }

            logInfo("Filtrant per '" + codiPattern + "': " + productes.size() + " productes trobats");

        } catch (SQLException e) {
            logError("Error filtrant per codi: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return productes;
    }

    /**
     * Calcula el preu total d'un producte sumant recursivament
     * el preu de tots els seus components (BOM)
     * 
     * Algoritme:
     * 1. Si el producte no té components → preu = 0
     * 2. Per cada component del producte:
     *    - Si és Component → sumar (quantitat × preu_mig del component)
     *    - Si és Producte → sumar (quantitat × calcularPreuTotal recursiu)
     */
    @Override
    public double calcularPreuTotal(String prCodi) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            logError("Codi de producte null o buit");
            return 0.0;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double preuTotal = 0.0;

        try {
            conn = getConnection();

            // Obtenir tots els items (components/subproductes) d'aquest producte
            String sql = """
                SELECT 
                    pi.pi_it_codi,
                    pi.quantitat,
                    i.it_tipus,
                    c.cm_preu_mig
                FROM Prod_Item pi
                JOIN Item i ON pi.pi_it_codi = i.it_codi
                LEFT JOIN Component c ON i.it_codi = c.cm_codi
                WHERE pi.pi_pr_codi = ?
                """;

            ps = conn.prepareStatement(sql);
            ps.setString(1, prCodi);

            rs = ps.executeQuery();

            while (rs.next()) {
                String itCodi = rs.getString("pi_it_codi");
                int quantitat = rs.getInt("quantitat");
                String itTipus = rs.getString("it_tipus");
                
                double preuItem = 0.0;

                if ("C".equals(itTipus)) {
                    // És un Component → usar cm_preu_mig directament
                    Double preuMig = rs.getDouble("cm_preu_mig");
                    if (!rs.wasNull()) {
                        preuItem = preuMig;
                    }
                } else if ("P".equals(itTipus)) {
                    // És un Producte → calcular recursivament
                    preuItem = calcularPreuTotal(itCodi);
                }

                preuTotal += (quantitat * preuItem);
            }

            logInfo("Preu total de " + prCodi + ": " + preuTotal);

        } catch (SQLException e) {
            logError("Error calculant preu total: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return preuTotal;
    }

    /**
     * Compta el total de productes
     */
    @Override
    public int countTotal() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = "SELECT COUNT(*) AS total FROM Producte";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                logInfo("Total de productes: " + total);
                return total;
            }

        } catch (SQLException e) {
            logError("Error comptant productes: " + e.getMessage());

        } finally {
            tancarRecursos(rs, ps, conn);
        }

        return 0;
    }

}