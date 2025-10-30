package abstractdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.IDAOMunicipi;
import model.Municipi;
import util.ConnexioOracle;

/**
 * Classe abstracta base per DAOMunicipi
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOMunicipi implements IDAOMunicipi {

    public AbstractDAOMunicipi() {
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
        System.err.println("❌ Error SQL en DAOMunicipi:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    protected boolean validarEntitat(Municipi m) {
        if (m == null) {
            System.err.println("❌ Municipi no pot ser null");
            return false;
        }
        if (m.getMuPrCodi() == null || m.getMuPrCodi().trim().isEmpty()) {
            System.err.println("❌ Codi província del municipi obligatori");
            return false;
        }
        if (m.getMuNum() == null || m.getMuNum().trim().isEmpty()) {
            System.err.println("❌ Número municipi obligatori");
            return false;
        }
        if (m.getMuNom() == null || m.getMuNom().trim().isEmpty()) {
            System.err.println("❌ Nom municipi obligatori");
            return false;
        }
        return true;
    }
}
