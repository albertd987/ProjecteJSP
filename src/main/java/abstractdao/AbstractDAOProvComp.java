package abstractdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.IDAOProvComp;
import model.ProvComp;
import util.ConnexioOracle;

/**
 * Classe abstracta per DAOProvComp amb mètodes utils comuns
 * Implementa funcions auxiliars per gestió de recursos i errors
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public abstract class AbstractDAOProvComp implements IDAOProvComp {

    // ============================================
    // MÈTODES UTILS (comuns a totes les implementacions)
    // ============================================

    /**
     * Obté connexió a la base de dades Oracle
     * @return Connexió activa o null si error
     */
    protected Connection getConnection() {
        try {
            return ConnexioOracle.getConnection();
        } catch (SQLException e) {
            logError(e);
            return null;
        }
    }

    /**
     * Tanca un ResultSet de forma segura
     * @param rs ResultSet a tancar
     */
    protected void tancarRecursos(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logError(e);
            }
        }
    }

    /**
     * Tanca un PreparedStatement de forma segura
     * @param ps PreparedStatement a tancar
     */
    protected void tancarRecursos(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                logError(e);
            }
        }
    }

    /**
     * Tanca una Connection de forma segura
     * @param conn Connection a tancar
     */
    protected void tancarRecursos(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logError(e);
            }
        }
    }

    /**
     * Registra errors al sistema
     * @param e Excepció a registrar
     */
    protected void logError(SQLException e) {
        System.err.println(" ERROR SQL: " + e.getMessage());
        System.err.println("   Codi error: " + e.getErrorCode());
        System.err.println("   SQLState: " + e.getSQLState());
        e.printStackTrace();
    }

    /**
     * Valida que una entitat ProvComp té dades vàlides
     * @param pc Entitat a validar
     * @return true si és vàlida
     */
    protected boolean validarEntitat(ProvComp pc) {
        if (pc == null) {
            System.err.println("ProvComp null!");
            return false;
        }
        
        if (pc.getPcCmCodi() == null || pc.getPcCmCodi().trim().isEmpty()) {
            System.err.println("Codi component buit!");
            return false;
        }
        
        if (pc.getPcPvCodi() == null || pc.getPcPvCodi().trim().isEmpty()) {
            System.err.println("Codi proveïdor buit!");
            return false;
        }
        
        if (pc.getPcPreu() == null || pc.getPcPreu() < 0) {
            System.err.println("Preu invàlid!");
            return false;
        }
        
        return true;
    }

    /**
     * Helper per mapejar ResultSet → ProvComp
     * @param rs ResultSet amb dades
     * @return Objecte ProvComp
     * @throws SQLException si error en lectura
     */
    protected ProvComp mapResultSetToProvComp(ResultSet rs) throws SQLException {
        ProvComp pc = new ProvComp();
        pc.setPcCmCodi(rs.getString("pc_cm_codi"));
        pc.setPcPvCodi(rs.getString("pc_pv_codi"));
        pc.setPcPreu(rs.getDouble("pc_preu"));
        return pc;
    }

    // ============================================
    // MÈTODES ABSTRACTES (per implementar a subclasses)
    // ============================================

    @Override
    public abstract boolean insertar(ProvComp pc);

    @Override
    public abstract boolean actualitzar(ProvComp pc);

    @Override
    public abstract boolean eliminar(String cmCodi, String pvCodi);

    @Override
    public abstract ProvComp findById(String cmCodi, String pvCodi);

    @Override
    public abstract List<ProvComp> findAll();

    @Override
    public abstract List<ProvComp> getProveidorsDelComponent(String cmCodi);

    @Override
    public abstract List<ProvComp> getComponentsDelProveidor(String pvCodi);
}