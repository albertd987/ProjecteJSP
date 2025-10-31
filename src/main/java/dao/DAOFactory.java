package dao;

/**
 * Factory per obtenir instàncies de DAOs
 * Patró Factory Method - Encapsula la creació d'objectes DAO
 * 
 * Ús:
 *   IDAOComponent dao = DAOFactory.getDAOComponent();
 *   List<Component> components = dao.findAll();
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class DAOFactory {
    
    // Constructor privat per evitar instanciació
    private DAOFactory() {
        throw new AssertionError("DAOFactory no es pot instanciar");
    }
    
    // ============================================
    // DAOs READ-ONLY (Taules Mestres)
    // ============================================
    
    /**
     * Obté una instància de DAOUnitatMesura
     * @return Implementació concreta d'IDAOUnitatMesura
     */
    public static IDAOUnitatMesura getDAOUnitatMesura() {
        return new DAOUnitatMesura();
    }
    
    /**
     * Obté una instància de DAOProvincia
     * @return Implementació concreta d'IDAOProvincia
     */
    public static IDAOProvincia getDAOProvincia() {
        return new DAOProvincia();
    }
    
    /**
     * Obté una instància de DAOMunicipi
     * @return Implementació concreta d'IDAOMunicipi
     */
    public static IDAOMunicipi getDAOMunicipi() {
        return new DAOMunicipi();
    }
    
    /**
     * Obté una instància de DAOProveidor
     * @return Implementació concreta d'IDAOProveidor
     */
    public static IDAOProveidor getDAOProveidor() {
        return new DAOProveidor();
    }
    
    // ============================================
    // DAOs CRUD (Taules Transaccionals)
    // ============================================
    
    /**
     * Obté una instància de DAOComponent
     * @return Implementació concreta d'IDAOComponent
     */
    public static IDAOComponent getDAOComponent() {
        return new DAOComponent();
    }
    
    /**
     * Obté una instància de DAOItem
     * @return Implementació concreta d'IDAOItem
     * @throws UnsupportedOperationException si encara no està implementat
     */
    public static IDAOItem getDAOItem() {
        // TODO: Implementar DAOItem
        throw new UnsupportedOperationException("DAOItem encara no implementat");
    }
    
    /**
     * Obté una instància de DAOProducte
     * @return Implementació concreta d'IDAOProducte
     * @throws UnsupportedOperationException si encara no està implementat
     */
    public static IDAOProducte getDAOProducte() {
        // TODO: Implementar DAOProducte
        throw new UnsupportedOperationException("DAOProducte encara no implementat");
    }
    
    /**
     * Obté una instància de DAOProvComp
     * CRÍTICA: Aquesta classe interactua amb triggers Oracle
     * que recalculen automàticament cm_preu_mig
     * 
     * @return Implementació concreta d'IDAOProvComp
     * @throws UnsupportedOperationException si encara no està implementat
     */
    public static IDAOProvComp getDAOProvComp() {
        // TODO: Implementar DAOProvComp (CRÍTICA - triggers!)
        return new DAOProvComp();
    }
    
    /**
     * Obté una instància de DAOProdItem
     * @return Implementació concreta d'IDAOProdItem
     * @throws UnsupportedOperationException si encara no està implementat
     */
    public static IDAOProdItem getDAOProdItem() {
        // TODO: Implementar DAOProdItem
        throw new UnsupportedOperationException("DAOProdItem encara no implementat");
    }
    /**
 * Obté una instància de DAOProvComp
 * 
 * ⚠️ CRÍTICA: Aquesta classe interactua amb triggers Oracle!
 * Cada INSERT/UPDATE/DELETE en Prov_Comp dispara el trigger
 * trg_prov_comp_after que recalcula automàticament cm_preu_mig
 * 
 * @return Implementació concreta d'IDAOProvComp
 */









    // ============================================
    // Mètodes Utility
    // ============================================
    
    /**
     * Tanca tots els recursos d'un DAO si és necessari
     * (per a futures implementacions amb connection pooling)
     * 
     * @param dao DAO a tancar
     */
    public static void closeDAO(Object dao) {
        // Futura implementació per connection pooling
        // De moment no cal fer res perquè cada mètode tanca les seves connexions
    }
}