package test;

import java.util.List;

import dao.DAOMunicipi;
import dao.DAOProveidor;
import dao.DAOProvincia;
import dao.DAOUnitatMesura;
import model.Municipi;
import model.Proveidor;
import model.Provincia;
import model.UnitatMesura;

/**
 * Test de validació per als 4 DAOs READ-ONLY
 * Executa operacions bàsiques per verificar funcionament
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOsReadOnly {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🧪 TEST DAOs READ-ONLY - Tallers Manolo");
        System.out.println("=".repeat(60) + "\n");

        testDAOUnitatMesura();
        testDAOProvincia();
        testDAOMunicipi();
        testDAOProveidor();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ TOTS ELS TESTS COMPLETATS");
        System.out.println("=".repeat(60) + "\n");
    }

    // ========================================
    // TEST 1: DAOUnitatMesura
    // ========================================

    private static void testDAOUnitatMesura() {
        System.out.println("\n📦 TEST 1: DAOUnitatMesura");
        System.out.println("-".repeat(50));

        DAOUnitatMesura dao = new DAOUnitatMesura();

        // Test findAll()
        System.out.println("\n1️⃣  Test findAll():");
        List<UnitatMesura> unitats = dao.findAll();
        System.out.println("   Resultat: " + unitats.size() + " unitats trobades");
        if (!unitats.isEmpty()) {
            System.out.println("   Primera: " + unitats.get(0));
        }

        // Test findById()
        System.out.println("\n2️⃣  Test findById('KG'):");
        UnitatMesura um = dao.findById("KG");
        if (um != null) {
            System.out.println("   Trobat: " + um);
        }

        // Test findById() amb codi inexistent
        System.out.println("\n3️⃣  Test findById('XXX') - ha de retornar null:");
        UnitatMesura umNull = dao.findById("XXX");
        System.out.println("   Resultat: " + (umNull == null ? "✅ NULL correcte" : "❌ Hauria de ser null"));
    }

    // ========================================
    // TEST 2: DAOProvincia
    // ========================================

    private static void testDAOProvincia() {
        System.out.println("\n\n🗺️  TEST 2: DAOProvincia");
        System.out.println("-".repeat(50));

        DAOProvincia dao = new DAOProvincia();

        // Test findAll()
        System.out.println("\n1️⃣  Test findAll():");
        List<Provincia> provincies = dao.findAll();
        System.out.println("   Resultat: " + provincies.size() + " províncies trobades");
        if (!provincies.isEmpty()) {
            System.out.println("   Primera: " + provincies.get(0));
        }

        // Test findById()
        System.out.println("\n2️⃣  Test findById('08'):");
        Provincia p = dao.findById("08");
        if (p != null) {
            System.out.println("   Trobat: " + p);
        }
    }

    // ========================================
    // TEST 3: DAOMunicipi
    // ========================================

    private static void testDAOMunicipi() {
        System.out.println("\n\n🏘️  TEST 3: DAOMunicipi");
        System.out.println("-".repeat(50));

        DAOMunicipi dao = new DAOMunicipi();

        // Test findAll()
        System.out.println("\n1️⃣  Test findAll():");
        List<Municipi> municipis = dao.findAll();
        System.out.println("   Resultat: " + municipis.size() + " municipis trobats");
        if (!municipis.isEmpty()) {
            System.out.println("   Primer: " + municipis.get(0));
        }

        // Test findById() amb PK composta
        System.out.println("\n2️⃣  Test findById('08', '001'):");
        Municipi m = dao.findById("08", "001");
        if (m != null) {
            System.out.println("   Trobat: " + m);
        }

        // Test getMunicipisDeProvincia()
        System.out.println("\n3️⃣  Test getMunicipisDeProvincia('08'):");
        List<Municipi> munisBCN = dao.getMunicipisDeProvincia("08");
        System.out.println("   Resultat: " + munisBCN.size() + " municipis de Barcelona");
        for (Municipi mun : munisBCN) {
            System.out.println("      - " + mun.getMuNom());
        }
    }

    // ========================================
    // TEST 4: DAOProveidor
    // ========================================

    private static void testDAOProveidor() {
        System.out.println("\n\n🏭 TEST 4: DAOProveidor");
        System.out.println("-".repeat(50));

        DAOProveidor dao = new DAOProveidor();

        // Test findAll()
        System.out.println("\n1️⃣  Test findAll():");
        List<Proveidor> proveidors = dao.findAll();
        System.out.println("   Resultat: " + proveidors.size() + " proveïdors trobats");
        if (!proveidors.isEmpty()) {
            System.out.println("   Primer: " + proveidors.get(0).getPvRaoSocial());
        }

        // Test findById()
        System.out.println("\n2️⃣  Test findById('PV001'):");
        Proveidor p = dao.findById("PV001");
        if (p != null) {
            System.out.println("   Trobat: " + p.getPvRaoSocial() + " (" + p.getPvCif() + ")");
        }

        // Test filtrarPerMunicipi()
        System.out.println("\n3️⃣  Test filtrarPerMunicipi('08', '001'):");
        List<Proveidor> pvsBarcelona = dao.filtrarPerMunicipi("08", "001");
        System.out.println("   Resultat: " + pvsBarcelona.size() + " proveïdors a Barcelona");

        // Test filtrarPerNom()
        System.out.println("\n4️⃣  Test filtrarPerNom('SA'):");
        List<Proveidor> pvsSA = dao.filtrarPerNom("SA");
        System.out.println("   Resultat: " + pvsSA.size() + " proveïdors amb 'SA' al nom");
        for (Proveidor pv : pvsSA) {
            System.out.println("      - " + pv.getPvRaoSocial());
        }
    }
}
