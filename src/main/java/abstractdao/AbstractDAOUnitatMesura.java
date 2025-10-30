package abstractdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.IDAOUnitatMesura;
import model.UnitatMesura;
import util.ConnexioOracle;

/**
 * Classe abstracta base per DAOUnitatMesura
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOUnitatMesura implements IDAOUnitatMesura {

    public AbstractDAOUnitatMesura() {
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
        System.err.println("❌ Error SQL en DAOUnitatMesura:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    /**
     * Valida que l'entitat UnitatMesura tingui dades vàlides
     * @param um UnitatMesura a validar
     * @return true si és vàlid, false si no
     */
    protected boolean validarEntitat(UnitatMesura um) {
        if (um == null) {
            System.err.println("❌ UnitatMesura no pot ser null");
            return false;
        }
        if (um.getUmCodi() == null || um.getUmCodi().trim().isEmpty()) {
            System.err.println("❌ Codi unitat mesura obligatori");
            return false;
        }
        if (um.getUmNom() == null || um.getUmNom().trim().isEmpty()) {
            System.err.println("❌ Nom unitat mesura obligatori");
            return false;
        }
        return true;
    }
}
