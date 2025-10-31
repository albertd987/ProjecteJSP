package abstractdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IDAOProdItem;
import model.ProdItem;

/**
 * Classe abstracta que implementa mètodes comuns per a DAOProdItem
 * Gestiona: connexió BD, tancament de recursos, validacions, logging
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public abstract class AbstractDAOProdItem implements IDAOProdItem {

    /**
     * Constructor buit
     */
    public AbstractDAOProdItem() {
    }

    // ==========================================
    // MÈTODES ABSTRACTES (a implementar per subclasses)
    // ==========================================

    /**
     * Obté una connexió a la base de dades
     * @return Connexió a la BD
     * @throws SQLException Si hi ha error de connexió
     */
    protected abstract Connection getConnection() throws SQLException;

    // ==========================================
    // MÈTODES DE UTILITAT COMUNS
    // ==========================================

    /**
     * Valida que una entitat ProdItem sigui vàlida abans de persistir-la
     * 
     * Validacions:
     * - ProdItem no null
     * - piPrCodi no null ni buit
     * - piItCodi no null ni buit
     * - piPrCodi != piItCodi (CHECK de la taula)
     * - quantitat > 0
     * 
     * @param prodItem ProdItem a validar
     * @return true si és vàlid, false en cas contrari
     */
    protected boolean validarEntitat(ProdItem prodItem) {
        if (prodItem == null) {
            logError("ProdItem null, no es pot persistir");
            return false;
        }
        
        // Validar piPrCodi (part de PK)
        if (prodItem.getPiPrCodi() == null || prodItem.getPiPrCodi().trim().isEmpty()) {
            logError("Codi de producte (piPrCodi) null o buit");
            return false;
        }
        
        // Validar piItCodi (part de PK)
        if (prodItem.getPiItCodi() == null || prodItem.getPiItCodi().trim().isEmpty()) {
            logError("Codi d'item (piItCodi) null o buit");
            return false;
        }
        
        // CRÍTICO: CHECK (pi_pr_codi <> pi_it_codi)
        // Un producte no pot contenir-se a si mateix
        if (prodItem.getPiPrCodi().equals(prodItem.getPiItCodi())) {
            logError("Un producte no pot contenir-se a si mateix! " +
                    "piPrCodi (" + prodItem.getPiPrCodi() + ") = " +
                    "piItCodi (" + prodItem.getPiItCodi() + ")");
            return false;
        }
        
        // Validar quantitat > 0
        if (prodItem.getQuantitat() == null || prodItem.getQuantitat() <= 0) {
            logError("Quantitat ha de ser > 0, valor actual: " + prodItem.getQuantitat());
            return false;
        }
        
        return true;
    }

    /**
     * Valida els paràmetres de la PK composta
     * @param prCodi Codi del producte
     * @param itCodi Codi de l'item
     * @return true si són vàlids, false en cas contrari
     */
    protected boolean validarPKComposta(String prCodi, String itCodi) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            logError("Codi de producte null o buit");
            return false;
        }
        
        if (itCodi == null || itCodi.trim().isEmpty()) {
            logError("Codi d'item null o buit");
            return false;
        }
        
        // CHECK: un producte no pot contenir-se a si mateix
        if (prCodi.equals(itCodi)) {
            logError("Un producte no pot contenir-se a si mateix: " + prCodi);
            return false;
        }
        
        return true;
    }

    /**
     * Tanca els recursos JDBC (ResultSet, PreparedStatement, Connection)
     * Gestiona excepcions sense propagar-les
     * 
     * @param rs ResultSet a tancar (pot ser null)
     * @param ps PreparedStatement a tancar (pot ser null)
     * @param conn Connection a tancar (pot ser null)
     */
    protected void tancarRecursos(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logError("Error tancant ResultSet: " + e.getMessage());
            }
        }
        
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                logError("Error tancant PreparedStatement: " + e.getMessage());
            }
        }
        
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                logError("Error tancant Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Registra un error al sistema de logging
     * @param missatge Missatge d'error a registrar
     */
    protected void logError(String missatge) {
        System.err.println("[ERROR - DAOProdItem] " + missatge);
    }

    /**
     * Registra informació al sistema de logging
     * @param missatge Missatge informatiu a registrar
     */
    protected void logInfo(String missatge) {
        System.out.println("[INFO - DAOProdItem] " + missatge);
    }

    /**
     * Mapeja un ResultSet a un objecte ProdItem
     * 
     * Columnes esperades:
     * - pi_pr_codi, pi_it_codi, quantitat
     * 
     * @param rs ResultSet posicionat en un registre
     * @return ProdItem amb dades del ResultSet
     * @throws SQLException Si hi ha error accedint a les dades
     */
    protected ProdItem mapResultSetToProdItem(ResultSet rs) throws SQLException {
        ProdItem prodItem = new ProdItem();
        
        prodItem.setPiPrCodi(rs.getString("pi_pr_codi"));
        prodItem.setPiItCodi(rs.getString("pi_it_codi"));
        prodItem.setQuantitat(rs.getInt("quantitat"));
        
        return prodItem;
    }

}