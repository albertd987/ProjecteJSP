package abstractdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.IDAOItem;
import model.Item;
import util.ConnexioOracle;

/**
 * Classe abstracta base per a DAOItem
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOItem implements IDAOItem {

    public AbstractDAOItem() {
    }

    /**
     * Obté una connexió a la base de dades Oracle
     * @return Connection activa
     * @throws SQLException si hi ha error de connexió
     */
    protected Connection getConnection() throws SQLException {
        return ConnexioOracle.getConnection();
    }

    /**
     * Tanca els recursos JDBC de forma segura
     * @param conn Connexió a tancar
     */
    protected void tancarRecursos(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant connexió: " + e.getMessage());
            }
        }
    }

    /**
     * Tanca Statement de forma segura
     * @param stmt Statement a tancar
     */
    protected void tancarRecursos(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant statement: " + e.getMessage());
            }
        }
    }

    /**
     * Tanca ResultSet de forma segura
     * @param rs ResultSet a tancar
     */
    protected void tancarRecursos(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant resultset: " + e.getMessage());
            }
        }
    }

    /**
     * Registra errors SQL al log
     * @param e Excepció SQL a registrar
     */
    protected void logError(SQLException e) {
        System.err.println("❌ Error SQL en DAOItem:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    /**
     * Valida que l'entitat Item tingui dades vàlides
     * @param item Item a validar
     * @return true si és vàlid, false si no
     */
    protected boolean validarEntitat(Item item) {
        if (item == null) {
            System.err.println("❌ Item no pot ser null");
            return false;
        }
        if (item.getItCodi() == null || item.getItCodi().trim().isEmpty()) {
            System.err.println("❌ Item: Codi no pot ser buit");
            return false;
        }
        if (item.getItTipus() == null || item.getItTipus().trim().isEmpty()) {
            System.err.println("❌ Item: Tipus no pot ser buit");
            return false;
        }
        if (!item.getItTipus().equals("C") && !item.getItTipus().equals("P")) {
            System.err.println("❌ Item: Tipus ha de ser 'C' (Component) o 'P' (Producte)");
            return false;
        }
        if (item.getItNom() == null || item.getItNom().trim().isEmpty()) {
            System.err.println("❌ Item: Nom no pot ser buit");
            return false;
        }
        return true;
    }

    /**
     * Mapeja un ResultSet a un objecte Item
     * @param rs ResultSet amb les dades
     * @return Item amb les dades del ResultSet
     * @throws SQLException si hi ha error llegint el ResultSet
     */
    protected Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItCodi(rs.getString("it_codi"));
        item.setItTipus(rs.getString("it_tipus"));
        item.setItNom(rs.getString("it_nom"));
        item.setItDesc(rs.getString("it_desc"));
        item.setItStock(rs.getInt("it_stock"));
        item.setItFoto(rs.getString("it_foto"));
        return item;
    }
}