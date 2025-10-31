package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import abstractdao.AbstractDAOProvComp;
import model.ProvComp;

/**
 * Implementació DAO per Proveïdor-Component (relació N:N)
 * 
 *  MOLT IMPORTANT 
 * Aquesta classe és CRÍTICA perquè cada INSERT/UPDATE/DELETE
 * en la taula Prov_Comp dispara automàticament el trigger Oracle
 * que recalcula el camp cm_preu_mig de la taula Component!
 * 
 * Flux del trigger:
 * 1. INSERT/UPDATE/DELETE en Prov_Comp
 * 2. Trigger trg_prov_comp_after (AFTER STATEMENT)
 * 3. Crida preu_mig_pkg.recalcula_tots_preus()
 * 4. UPDATE Component SET cm_preu_mig = AVG(pc_preu)
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOProvComp extends AbstractDAOProvComp {

    // ============================================
    // INSERT - Afegeix nou proveïdor a component
    //  DISPARA TRIGGER!
    // ============================================

    @Override
    public boolean insertar(ProvComp pc) {
        if (!validarEntitat(pc)) {
            return false;
        }

        String sql = "INSERT INTO Prov_Comp (pc_cm_codi, pc_pv_codi, pc_preu) " +
                     "VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("No s'ha pogut obtenir connexió");
                return false;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, pc.getPcCmCodi());
            ps.setString(2, pc.getPcPvCodi());
            ps.setDouble(3, pc.getPcPreu());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(" Relació inserida: " + pc.getPcCmCodi() + 
                                 " - " + pc.getPcPvCodi() + " = " + pc.getPcPreu() + "€");
                System.out.println("Trigger activat! cm_preu_mig recalculat automàticament");
                return true;
            }

            return false;

        } catch (SQLException e) {
            // Error comú: FK constraint (component o proveïdor no existeix)
            if (e.getErrorCode() == 2291) {
                System.err.println(" Component o Proveïdor no existeix a la BD");
            }
            // Error comú: PK duplicada
            else if (e.getErrorCode() == 1) {
                System.err.println(" Aquesta relació ja existeix!");
            }
            logError(e);
            return false;

        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // UPDATE - Actualitza preu d'una relació
    // DISPARA TRIGGER!
    // ============================================

    @Override
    public boolean actualitzar(ProvComp pc) {
        if (!validarEntitat(pc)) {
            return false;
        }

        String sql = "UPDATE Prov_Comp " +
                     "SET pc_preu = ? " +
                     "WHERE pc_cm_codi = ? AND pc_pv_codi = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("No s'ha pogut obtenir connexió");
                return false;
            }

            ps = conn.prepareStatement(sql);
            ps.setDouble(1, pc.getPcPreu());
            ps.setString(2, pc.getPcCmCodi());
            ps.setString(3, pc.getPcPvCodi());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Preu actualitzat: " + pc.getPcCmCodi() + 
                                 " - " + pc.getPcPvCodi() + " → " + pc.getPcPreu() + "€");
                System.out.println(" Trigger activat! cm_preu_mig recalculat automàticament");
                return true;
            } else {
                System.err.println(" Relació no trobada: " + 
                                 pc.getPcCmCodi() + " - " + pc.getPcPvCodi());
                return false;
            }

        } catch (SQLException e) {
            logError(e);
            return false;

        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // DELETE - Elimina relació proveïdor-component
    // DISPARA TRIGGER!
    // ============================================

    @Override
    public boolean eliminar(String cmCodi, String pvCodi) {
        if (cmCodi == null || cmCodi.trim().isEmpty() || 
            pvCodi == null || pvCodi.trim().isEmpty()) {
            System.err.println("  Codis buits!");
            return false;
        }

        String sql = "DELETE FROM Prov_Comp " +
                     "WHERE pc_cm_codi = ? AND pc_pv_codi = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("No s'ha pogut obtenir connexió");
                return false;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, cmCodi);
            ps.setString(2, pvCodi);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Relació eliminada: " + cmCodi + " - " + pvCodi);
                System.out.println("  Trigger activat! cm_preu_mig recalculat automàticament");
                return true;
            } else {
                System.err.println(" Relació no trobada: " + cmCodi + " - " + pvCodi);
                return false;
            }

        } catch (SQLException e) {
            logError(e);
            return false;

        } finally {
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FIND BY ID - Busca relació per PK composta
    // ============================================

    @Override
    public ProvComp findById(String cmCodi, String pvCodi) {
        if (cmCodi == null || cmCodi.trim().isEmpty() || 
            pvCodi == null || pvCodi.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT pc_cm_codi, pc_pv_codi, pc_preu " +
                     "FROM Prov_Comp " +
                     "WHERE pc_cm_codi = ? AND pc_pv_codi = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) return null;

            ps = conn.prepareStatement(sql);
            ps.setString(1, cmCodi);
            ps.setString(2, pvCodi);

            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProvComp(rs);
            }

            return null;

        } catch (SQLException e) {
            logError(e);
            return null;

        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }
    }

    // ============================================
    // FIND ALL - Llista totes les relacions
    // ============================================

    @Override
    public List<ProvComp> findAll() {
        List<ProvComp> llista = new ArrayList<>();

        String sql = "SELECT pc_cm_codi, pc_pv_codi, pc_preu " +
                     "FROM Prov_Comp " +
                     "ORDER BY pc_cm_codi, pc_pv_codi";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) return llista;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                llista.add(mapResultSetToProvComp(rs));
            }

            System.out.println(" Trobades " + llista.size() + " relacions proveïdor-component");

        } catch (SQLException e) {
            logError(e);

        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }

        return llista;
    }

    // ============================================
    // GET PROVEÏDORS DEL COMPONENT
    // Utilitzat pel dropdown del formulari editComponent.jsp
    // ============================================

    @Override
    public List<ProvComp> getProveidorsDelComponent(String cmCodi) {
        List<ProvComp> llista = new ArrayList<>();

        if (cmCodi == null || cmCodi.trim().isEmpty()) {
            return llista;
        }

        String sql = "SELECT pc_cm_codi, pc_pv_codi, pc_preu " +
                     "FROM Prov_Comp " +
                     "WHERE pc_cm_codi = ? " +
                     "ORDER BY pc_pv_codi";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) return llista;

            ps = conn.prepareStatement(sql);
            ps.setString(1, cmCodi);

            rs = ps.executeQuery();

            while (rs.next()) {
                llista.add(mapResultSetToProvComp(rs));
            }

            System.out.println("Component " + cmCodi + " té " + 
                             llista.size() + " proveïdors");

        } catch (SQLException e) {
            logError(e);

        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }

        return llista;
    }

    // ============================================
    // GET COMPONENTS DEL PROVEÏDOR
    // ============================================

    @Override
    public List<ProvComp> getComponentsDelProveidor(String pvCodi) {
        List<ProvComp> llista = new ArrayList<>();

        if (pvCodi == null || pvCodi.trim().isEmpty()) {
            return llista;
        }

        String sql = "SELECT pc_cm_codi, pc_pv_codi, pc_preu " +
                     "FROM Prov_Comp " +
                     "WHERE pc_pv_codi = ? " +
                     "ORDER BY pc_cm_codi";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) return llista;

            ps = conn.prepareStatement(sql);
            ps.setString(1, pvCodi);

            rs = ps.executeQuery();

            while (rs.next()) {
                llista.add(mapResultSetToProvComp(rs));
            }

            System.out.println("Proveïdor " + pvCodi + " subministra " + 
                             llista.size() + " components");

        } catch (SQLException e) {
            logError(e);

        } finally {
            tancarRecursos(rs);
            tancarRecursos(ps);
            tancarRecursos(conn);
        }

        return llista;
    }
}