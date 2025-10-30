package abstractdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.IDAOProveidor;
import model.Proveidor;
import util.ConnexioOracle;

/**
 * Classe abstracta base per DAOProveidor
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOProveidor implements IDAOProveidor {

    public AbstractDAOProveidor() {
    }

    protected Connection getConnection() throws SQLException {
        return ConnexioOracle.getConnection();
    }

    protected void tancarRecursos(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant connexió: " + e.getMessage());
            }
        }
    }

    protected void tancarRecursos(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant statement: " + e.getMessage());
            }
        }
    }

    protected void tancarRecursos(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("⚠️  Error tancant resultset: " + e.getMessage());
            }
        }
    }

    protected void logError(SQLException e) {
        System.err.println("❌ Error SQL en DAOProveidor:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    protected boolean validarEntitat(Proveidor p) {
        if (p == null) {
            System.err.println("❌ Proveidor no pot ser null");
            return false;
        }
        if (p.getPvCodi() == null || p.getPvCodi().trim().isEmpty()) {
            System.err.println("❌ Codi proveïdor obligatori");
            return false;
        }
        if (p.getPvCif() == null || p.getPvCif().trim().isEmpty()) {
            System.err.println("❌ CIF proveïdor obligatori");
            return false;
        }
        if (p.getPvRaoSocial() == null || p.getPvRaoSocial().trim().isEmpty()) {
            System.err.println("❌ Raó social proveïdor obligatòria");
            return false;
        }
        return true;
    }
}
