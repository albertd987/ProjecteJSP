package abstractdao;

import dao.IDAOComponent;
import model.Component;
import util.ConnexioOracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Classe abstracta base per a DAOComponent
 * Proporciona mètodes utils comuns per totes les implementacions
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOComponent implements IDAOComponent {

    public AbstractDAOComponent() {
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
        System.err.println("❌ Error SQL en DAOComponent:");
        System.err.println("   Missatge: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQL State: " + e.getSQLState());
        e.printStackTrace();
    }

    /**
     * Valida que l'entitat Component tingui dades vàlides
     * @param c Component a validar
     * @return true si és vàlid, false si no
     */
    protected boolean validarEntitat(Component c) {
        if (c == null) {
            System.err.println("❌ Component no pot ser null");
            return false;
        }
        if (c.getCmCodi() == null || c.getCmCodi().trim().isEmpty()) {
            System.err.println("❌ Codi component obligatori");
            return false;
        }
        if (c.getCmUmCodi() == null || c.getCmUmCodi().trim().isEmpty()) {
            System.err.println("❌ Unitat mesura obligatòria");
            return false;
        }
        if (c.getCmCodiFabricant() == null || c.getCmCodiFabricant().trim().isEmpty()) {
            System.err.println("❌ Codi fabricant obligatori");
            return false;
        }
        return true;
    }

    // ============================================
    // Mètodes abstractes (a implementar per DAOComponent)
    // ============================================

    @Override
    public abstract boolean actualitzar(Component c);

    @Override
    public abstract int countTotal();

    @Override
    public abstract boolean eliminar(String codi);

    @Override
    public abstract List<Component> filtrarPerCodi(String codiPattern);

    @Override
    public abstract List<Component> filtrarPerPreuMig(double min, double max);

    @Override
    public abstract List<Component> findAll();

    @Override
    public abstract List<Component> findAllPaginat(int page, int size);

    @Override
    public abstract Component findById(String codi);

    @Override
    public abstract Component getComponentAmbPreuActualitzat(String cmCodi);

    @Override
    public abstract boolean insertar(Component c);
}