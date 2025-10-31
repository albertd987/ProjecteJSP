package abstractdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IDAOProducte;
import model.Producte;

/**
 * Classe abstracta que implementa mètodes comuns per a DAOProducte
 * Gestiona: connexió BD, tancament de recursos, validacions, logging
 * 
 * @author DomenechObiolAlbert
 * @version 2.0
 */
public abstract class AbstractDAOProducte implements IDAOProducte {

    /**
     * Constructor buit
     */
    public AbstractDAOProducte() {
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
     * Valida que una entitat Producte sigui vàlida abans de persistir-la
     * 
     * Validacions:
     * - Producte no null
     * - prCodi no null ni buit
     * - itCodi no null ni buit
     * - prCodi == itCodi (constraint FK)
     * 
     * @param producte Producte a validar
     * @return true si és vàlid, false en cas contrari
     */
    protected boolean validarEntitat(Producte producte) {
        if (producte == null) {
            logError("Producte null, no es pot persistir");
            return false;
        }
        
        // Validar PK de Producte
        if (producte.getPrCodi() == null || producte.getPrCodi().trim().isEmpty()) {
            logError("Codi de producte (prCodi) null o buit");
            return false;
        }
        
        // Validar PK d'Item (heretat)
        if (producte.getItCodi() == null || producte.getItCodi().trim().isEmpty()) {
            logError("Codi d'item (itCodi) null o buit");
            return false;
        }
        
        // CRÍTIC: prCodi ha de ser igual a itCodi (FK constraint)
        if (!producte.getPrCodi().equals(producte.getItCodi())) {
            logError("prCodi (" + producte.getPrCodi() + ") != itCodi (" + 
                    producte.getItCodi() + ") - han de ser iguals!");
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
        System.err.println("[ERROR - DAOProducte] " + missatge);
    }

    /**
     * Registra informació al sistema de logging
     * @param missatge Missatge informatiu a registrar
     */
    protected void logInfo(String missatge) {
        System.out.println("[INFO - DAOProducte] " + missatge);
    }

    /**
     * Mapeja un ResultSet a un objecte Producte
     * Assumeix que el ResultSet conté columnes d'Item JOIN Producte
     * 
     * Columnes esperades:
     * - Item: it_codi, it_tipus, it_nom, it_desc, it_stock, it_foto
     * - Producte: pr_codi
     * 
     * @param rs ResultSet posicionat en un registre
     * @return Producte amb dades del ResultSet
     * @throws SQLException Si hi ha error accedint a les dades
     */
    protected Producte mapResultSetToProducte(ResultSet rs) throws SQLException {
        Producte producte = new Producte();
        
        // Dades d'Item (taula pare)
        producte.setItCodi(rs.getString("it_codi"));
        producte.setItTipus(rs.getString("it_tipus"));
        producte.setItNom(rs.getString("it_nom"));
        producte.setItDesc(rs.getString("it_desc"));
        producte.setItStock(rs.getInt("it_stock"));
        producte.setItFoto(rs.getString("it_foto"));
        
        // Dades de Producte (taula filla)
        producte.setPrCodi(rs.getString("pr_codi"));
        
        return producte;
    }

}
