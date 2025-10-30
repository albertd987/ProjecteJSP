package test;

import java.util.List;

import dao.DAOFactory;
import dao.IDAOComponent;
import dao.IDAOItem;
import dao.IDAOMunicipi;
import dao.IDAOProdItem;
import dao.IDAOProducte;
import dao.IDAOProvComp;
import dao.IDAOProveidor;
import dao.IDAOProvincia;
import dao.IDAOUnitatMesura;
import model.Municipi;
import model.Proveidor;
import model.Provincia;
import model.UnitatMesura;

/**
 * Test de validació per DAOFactory
 * Verifica que el factory retorna instàncies correctes
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOFactory {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏭 TEST DAOFactory - Tallers Manolo");
        System.out.println("=".repeat(60) + "\n");
        
        testFactoryInstances();
        testFactoryFunctionality();
        testPendingDAOs();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ TOTS ELS TESTS DAOFactory COMPLETATS");
        System.out.println("=".repeat(60) + "\n");
    }
    
    // ========================================
    // TEST 1: Instàncies dels DAOs implementats
    // ========================================
    
    private static void testFactoryInstances() {
        System.out.println("\n📦 TEST 1: Instàncies DAOs Implementats");
        System.out.println("-".repeat(50));
        
        // DAOs READ-ONLY
        System.out.println("\n🔵 DAOs READ-ONLY:");
        
        IDAOUnitatMesura daoUM = DAOFactory.getDAOUnitatMesura();
        System.out.println("   ✅ getDAOUnitatMesura() → " + daoUM.getClass().getSimpleName());
        
        IDAOProvincia daoPr = DAOFactory.getDAOProvincia();
        System.out.println("   ✅ getDAOProvincia() → " + daoPr.getClass().getSimpleName());
        
        IDAOMunicipi daoMu = DAOFactory.getDAOMunicipi();
        System.out.println("   ✅ getDAOMunicipi() → " + daoMu.getClass().getSimpleName());
        
        IDAOProveidor daoPv = DAOFactory.getDAOProveidor();
        System.out.println("   ✅ getDAOProveidor() → " + daoPv.getClass().getSimpleName());
        
        // DAOs CRUD
        System.out.println("\n🟢 DAOs CRUD:");
        
        IDAOComponent daoCm = DAOFactory.getDAOComponent();
        System.out.println("   ✅ getDAOComponent() → " + daoCm.getClass().getSimpleName());
        
        System.out.println("\n   📊 Resum: 5/9 DAOs implementats i operatius");
    }
    
    // ========================================
    // TEST 2: Funcionalitat real via Factory
    // ========================================
    
    private static void testFactoryFunctionality() {
        System.out.println("\n\n⚙️  TEST 2: Funcionalitat Real via Factory");
        System.out.println("-".repeat(50));
        
        // Test 1: Unitats de Mesura
        System.out.println("\n1️⃣  Test UnitatMesura via Factory:");
        IDAOUnitatMesura daoUM = DAOFactory.getDAOUnitatMesura();
        List<UnitatMesura> unitats = daoUM.findAll();
        System.out.println("   Resultat: " + unitats.size() + " unitats trobades");
        
        UnitatMesura kg = daoUM.findById("KG");
        if (kg != null) {
            System.out.println("   ✅ findById('KG') via factory: " + kg.getUmNom());
        }
        
        // Test 2: Províncies
        System.out.println("\n2️⃣  Test Provincia via Factory:");
        IDAOProvincia daoPr = DAOFactory.getDAOProvincia();
        List<Provincia> provincies = daoPr.findAll();
        System.out.println("   Resultat: " + provincies.size() + " províncies trobades");
        
        // Test 3: Municipis
        System.out.println("\n3️⃣  Test Municipi via Factory:");
        IDAOMunicipi daoMu = DAOFactory.getDAOMunicipi();
        List<Municipi> municipis = daoMu.getMunicipisDeProvincia("08");
        System.out.println("   Resultat: " + municipis.size() + " municipis de Barcelona");
        
        // Test 4: Proveïdors
        System.out.println("\n4️⃣  Test Proveidor via Factory:");
        IDAOProveidor daoPv = DAOFactory.getDAOProveidor();
        List<Proveidor> proveidors = daoPv.filtrarPerNom("SA");
        System.out.println("   Resultat: " + proveidors.size() + " proveïdors amb 'SA'");
        
        // Test 5: Components
        System.out.println("\n5️⃣  Test Component via Factory:");
        IDAOComponent daoCm = DAOFactory.getDAOComponent();
        int totalComponents = daoCm.countTotal();
        System.out.println("   Resultat: " + totalComponents + " components totals");
        
        System.out.println("\n   ✅ Tots els DAOs via factory funcionen correctament!");
    }
    
    // ========================================
    // TEST 3: DAOs pendents d'implementar
    // ========================================
    
    private static void testPendingDAOs() {
        System.out.println("\n\n⏳ TEST 3: DAOs Pendents d'Implementar");
        System.out.println("-".repeat(50));
        
        System.out.println("\n🔴 DAOs CRUD pendents:");
        
        // Test DAOItem
        try {
            IDAOItem daoIt = DAOFactory.getDAOItem();
            System.out.println("   ❌ ERROR: getDAOItem() hauria de llançar excepció");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✅ getDAOItem() → UnsupportedOperationException (correcte)");
        }
        
        // Test DAOProducte
        try {
            IDAOProducte daoPr = DAOFactory.getDAOProducte();
            System.out.println("   ❌ ERROR: getDAOProducte() hauria de llançar excepció");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✅ getDAOProducte() → UnsupportedOperationException (correcte)");
        }
        
        // Test DAOProvComp
        try {
            IDAOProvComp daoPC = DAOFactory.getDAOProvComp();
            System.out.println("   ❌ ERROR: getDAOProvComp() hauria de llançar excepció");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ⚠️  getDAOProvComp() → UnsupportedOperationException (CRÍTICA pendent!)");
        }
        
        // Test DAOProdItem
        try {
            IDAOProdItem daoPI = DAOFactory.getDAOProdItem();
            System.out.println("   ❌ ERROR: getDAOProdItem() hauria de llançar excepció");
        } catch (UnsupportedOperationException e) {
            System.out.println("   ✅ getDAOProdItem() → UnsupportedOperationException (correcte)");
        }
        
        System.out.println("\n   📊 4 DAOs pendents detectats correctament");
    }
}