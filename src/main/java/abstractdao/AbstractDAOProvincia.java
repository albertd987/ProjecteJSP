package abstractdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.IDAOProvincia;
import model.Provincia;
import util.ConnexioOracle;

/**
 * Classe abstracta base per DAOProvincia
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOProvincia implements IDAOProvincia {

    public AbstractDAOProvincia() {
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
        System.err.println("❌ Error SQL en DAOProvincia:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    protected boolean validarEntitat(Provincia p) {
        if (p == null) {
            System.err.println("❌ Provincia no pot ser null");
            return false;
        }
        if (p.getPrCodi() == null || p.getPrCodi().trim().isEmpty()) {
            System.err.println("❌ Codi província obligatori");
            return false;
        }
        if (p.getPrNom() == null || p.getPrNom().trim().isEmpty()) {
            System.err.println("❌ Nom província obligatori");
            return false;
        }
        return true;
    }
}
