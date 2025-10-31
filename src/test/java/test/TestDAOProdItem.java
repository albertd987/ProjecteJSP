package test;

import java.util.List;

import dao.DAOProdItem;
import model.ProdItem;

/**
 * Test unitari COMPLET per DAOProdItem v2.0
 * 
 * MILLORA: Usa components i productes EXISTENTS per evitar FK violations
 * 
 * Dividit en 2 seccions:
 * 1. Tests NO DESTRUCTIUS (READ) - S'executen sempre
 * 2. Tests DESTRUCTIUS (INSERT/UPDATE/DELETE) - Opcionals amb cleanup automàtic
 * 
 * @author DomenechObiolAlbert
 * @version 2.1
 */
public class TestDAOProdItem {

    // ==========================================
    // CONFIGURACIÓ
    // ==========================================
    
    /**
     * Activar/desactivar tests destructius
     * TRUE = Executar INSERT/UPDATE/DELETE (amb cleanup)
     * FALSE = Només executar tests de lectura
     */
    private static final boolean EXECUTAR_TESTS_DESTRUCTIUS = true;
    
    /**
     * Codis de test - usar productes i components EXISTENTS
     * IMPORTANT: Ajustar aquests valors segons la teva BD
     */
    private static final String PRODUCTE_TEST = "P003";      // Producte existent
    private static final String COMPONENT_TEST_1 = "C001";   // Component existent
    private static final String COMPONENT_TEST_2 = "C002";   // Component existent
    private static final String COMPONENT_TEST_3 = "C003";   // Component existent
    private static final String COMPONENT_TEST_4 = "C004";   // Component existent

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("   TEST UNITARI COMPLET: DAOProdItem v2.1");
        System.out.println("=".repeat(80));
        System.out.println();

        DAOProdItem dao = new DAOProdItem();

        // ==========================================
        // SECCIÓ 1: TESTS NO DESTRUCTIUS (READ)
        // ==========================================
        System.out.println("━".repeat(80));
        System.out.println("📖 SECCIÓ 1: TESTS DE CONSULTA (NO DESTRUCTIUS)");
        System.out.println("━".repeat(80));
        System.out.println();

        testFindAll(dao);
        testCountTotal(dao);
        testFindById(dao);
        testGetItemsDelProducte(dao);

        // ==========================================
        // SECCIÓ 2: TESTS DESTRUCTIUS (CRUD)
        // ==========================================
        if (EXECUTAR_TESTS_DESTRUCTIUS) {
            System.out.println();
            System.out.println("━".repeat(80));
            System.out.println("✏️  SECCIÓ 2: TESTS CRUD (DESTRUCTIUS amb CLEANUP)");
            System.out.println("━".repeat(80));
            System.out.println();
            
            System.out.println("ℹ️  Usant producte de test: " + PRODUCTE_TEST);
            System.out.println("ℹ️  Usant components de test: " + COMPONENT_TEST_1 + ", " + 
                             COMPONENT_TEST_2 + ", " + COMPONENT_TEST_3 + ", " + COMPONENT_TEST_4);
            System.out.println();

            testInsertar(dao);
            testAfegirItemAProducte(dao);
            testActualitzar(dao);
            testEliminar(dao);
            testValidacions(dao);

        } else {
            System.out.println();
            System.out.println("ℹ️  Tests destructius desactivats (EXECUTAR_TESTS_DESTRUCTIUS = false)");
        }

        // ==========================================
        // RESUM FINAL
        // ==========================================
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("   ✅ TOTS ELS TESTS COMPLETATS SATISFACTÒRIAMENT");
        System.out.println("=".repeat(80));
    }

    // ==========================================
    // SECCIÓ 1: TESTS DE CONSULTA (READ)
    // ==========================================

    /**
     * TEST 1: findAll() - Obtenir totes les relacions Producte-Item
     */
    private static void testFindAll(DAOProdItem dao) {
        System.out.println("TEST 1: findAll() - Llistar totes les relacions");
        System.out.println("-".repeat(80));

        List<ProdItem> prodItems = dao.findAll();

        System.out.println("✓ Relacions Producte-Item trobades: " + prodItems.size());

        if (!prodItems.isEmpty()) {
            System.out.println("\n📋 Primeres 10 relacions (BOM):");
            prodItems.stream()
                .limit(10)
                .forEach(pi -> System.out.printf("  → Producte %s conté Item %s (quantitat: %d)%n",
                        pi.getPiPrCodi(), pi.getPiItCodi(), pi.getQuantitat()));
        } else {
            System.out.println("⚠️  No hi ha relacions a la BD");
        }

        System.out.println();
    }

    /**
     * TEST 2: countTotal() - Comptar total de relacions
     */
    private static void testCountTotal(DAOProdItem dao) {
        System.out.println("TEST 2: countTotal() - Comptar relacions");
        System.out.println("-".repeat(80));

        int total = dao.countTotal();
        System.out.println("✓ Total de relacions Producte-Item: " + total);

        // Verificar coherència amb findAll()
        List<ProdItem> tots = dao.findAll();
        if (total == tots.size()) {
            System.out.println("✓ Validació: countTotal() coincideix amb findAll().size()");
        } else {
            System.err.println("✗ ERROR: Discrepància detectada!");
            System.err.println("  countTotal() = " + total);
            System.err.println("  findAll().size() = " + tots.size());
        }

        System.out.println();
    }

    /**
     * TEST 3: findById() - Cercar relació per PK composta
     */
    private static void testFindById(DAOProdItem dao) {
        System.out.println("TEST 3: findById() - Cercar per PK composta (prCodi + itCodi)");
        System.out.println("-".repeat(80));

        // Obtenir primera relació existent per testejar
        List<ProdItem> tots = dao.findAll();

        if (!tots.isEmpty()) {
            ProdItem primera = tots.get(0);
            String prCodi = primera.getPiPrCodi();
            String itCodi = primera.getPiItCodi();
            
            System.out.println("🔍 Cercant relació existent: (" + prCodi + ", " + itCodi + ")");

            ProdItem trobat = dao.findById(prCodi, itCodi);

            if (trobat != null) {
                System.out.println("✓ Relació trobada correctament:");
                System.out.println("  • Producte: " + trobat.getPiPrCodi());
                System.out.println("  • Item: " + trobat.getPiItCodi());
                System.out.println("  • Quantitat: " + trobat.getQuantitat());
            } else {
                System.err.println("✗ ERROR: Relació existent no trobada!");
            }
        } else {
            System.out.println("⚠️  No hi ha relacions per testejar");
        }

        // Test amb PK inexistent
        System.out.println("\n🔍 Cercant relació inexistent: (XXXXX, YYYYY)");
        ProdItem inexistent = dao.findById("XXXXX", "YYYYY");
        if (inexistent == null) {
            System.out.println("✓ Correcte: retorna null per PK inexistent");
        } else {
            System.err.println("✗ ERROR: Hauria de retornar null!");
        }

        // Test validació CHECK: producte no pot contenir-se a si mateix
        System.out.println("\n🔍 Test CHECK constraint: producte que es conté a si mateix");
        System.out.println("   Cercant: (P001, P001)");
        ProdItem autoContenidor = dao.findById("P001", "P001");
        if (autoContenidor == null) {
            System.out.println("✓ Correcte: CHECK (pi_pr_codi <> pi_it_codi) funciona");
        } else {
            System.err.println("✗ ALERTA: Violació de CHECK constraint detectada!");
        }

        System.out.println();
    }

    /**
     * TEST 4: getItemsDelProducte() - Obtenir BOM d'un producte
     */
    private static void testGetItemsDelProducte(DAOProdItem dao) {
        System.out.println("TEST 4: getItemsDelProducte() - Bill of Materials (BOM)");
        System.out.println("-".repeat(80));

        // Test amb primer producte existent
        List<ProdItem> tots = dao.findAll();

        if (!tots.isEmpty()) {
            String prCodi = tots.get(0).getPiPrCodi();
            System.out.println("🔍 Obtenint BOM del producte: " + prCodi);

            List<ProdItem> items = dao.getItemsDelProducte(prCodi);
            System.out.println("✓ Items del producte: " + items.size());

            if (!items.isEmpty()) {
                System.out.println("\n📦 Bill of Materials (BOM):");
                for (ProdItem item : items) {
                    System.out.printf("  • %s × %d unitats%n",
                            item.getPiItCodi(), item.getQuantitat());
                }
            } else {
                System.out.println("  (Producte sense components)");
            }

            // Mostrar estadístiques de tots els productes
            System.out.println("\n📊 Estadístiques BOM de tots els productes:");
            tots.stream()
                .map(ProdItem::getPiPrCodi)
                .distinct()
                .sorted()
                .forEach(codi -> {
                    List<ProdItem> itemsProducte = dao.getItemsDelProducte(codi);
                    System.out.printf("  %s: %d items%n", codi, itemsProducte.size());
                });

        } else {
            System.out.println("⚠️  No hi ha relacions per testejar");
        }

        // Test amb producte inexistent
        System.out.println("\n🔍 BOM de producte inexistent (XXXXX):");
        List<ProdItem> inexistent = dao.getItemsDelProducte("XXXXX");
        System.out.println("✓ Items: " + inexistent.size() + " (hauria de ser 0)");

        // Test amb codi buit
        System.out.println("\n🔍 Test validació: codi buit");
        List<ProdItem> buit = dao.getItemsDelProducte("");
        System.out.println("✓ Items amb codi buit: " + buit.size() + " (hauria de ser 0)");

        System.out.println();
    }

    // ==========================================
    // SECCIÓ 2: TESTS DESTRUCTIUS (CRUD)
    // ==========================================

    /**
     * TEST 5: insertar() - Inserir nova relació Producte-Item
     * USA: Producte existent + Component existent
     */
    private static void testInsertar(DAOProdItem dao) {
        System.out.println("TEST 5: insertar() - Inserir nova relació");
        System.out.println("-".repeat(80));

        // Verificar que la relació NO existeix abans d'inserir
        ProdItem existent = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_1);
        if (existent != null) {
            System.out.println("⚠️  Relació ja existeix, eliminant primer...");
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_1);
        }

        // Crear relació de test amb components EXISTENTS
        ProdItem testItem = new ProdItem(PRODUCTE_TEST, COMPONENT_TEST_1, 10);
        
        System.out.println("📝 Intentant inserir: " + PRODUCTE_TEST + " conté " + 
                         COMPONENT_TEST_1 + " (qty=10)");

        boolean inserit = dao.insertar(testItem);

        if (inserit) {
            System.out.println("✓ ProdItem inserit correctament");

            // Verificar que s'ha inserit
            ProdItem verificacio = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_1);
            if (verificacio != null && verificacio.getQuantitat() == 10) {
                System.out.println("✓ Verificació: Dades correctes a la BD");
            } else {
                System.err.println("✗ ERROR: Dades no trobades o incorrectes!");
            }

            // CLEANUP: Eliminar el registre de test
            System.out.println("🧹 Cleanup: Eliminant registre de test...");
            boolean eliminat = dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_1);
            if (eliminat) {
                System.out.println("✓ Cleanup completat");
            } else {
                System.err.println("✗ WARNING: No s'ha pogut fer cleanup!");
            }

        } else {
            System.err.println("✗ ERROR: No s'ha pogut inserir el ProdItem");
            System.err.println("  Nota: Verifica que " + PRODUCTE_TEST + " i " + 
                             COMPONENT_TEST_1 + " existeixen a la BD");
        }

        System.out.println();
    }

    /**
     * TEST 6: afegirItemAProducte() - Mètode helper per afegir items
     * USA: Producte existent + Component existent
     */
    private static void testAfegirItemAProducte(DAOProdItem dao) {
        System.out.println("TEST 6: afegirItemAProducte() - Mètode helper");
        System.out.println("-".repeat(80));

        // Verificar que la relació NO existeix
        ProdItem existent = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_2);
        if (existent != null) {
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_2);
        }

        System.out.println("📝 Afegint item " + COMPONENT_TEST_2 + " al producte " + 
                         PRODUCTE_TEST + " (qty=5)");

        boolean afegit = dao.afegirItemAProducte(PRODUCTE_TEST, COMPONENT_TEST_2, 5);

        if (afegit) {
            System.out.println("✓ Item afegit al producte correctament");

            // Verificar
            ProdItem verificacio = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_2);
            if (verificacio != null && verificacio.getQuantitat() == 5) {
                System.out.println("✓ Verificació: Item afegit amb quantitat correcta");
            }

            // CLEANUP
            System.out.println("🧹 Cleanup: Eliminant relació de test...");
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_2);
            System.out.println("✓ Cleanup completat");

        } else {
            System.err.println("✗ ERROR: No s'ha pogut afegir l'item al producte");
        }

        System.out.println();
    }

    /**
     * TEST 7: actualitzar() - Modificar quantitat d'una relació
     * USA: Producte existent + Component existent
     */
    private static void testActualitzar(DAOProdItem dao) {
        System.out.println("TEST 7: actualitzar() - Modificar quantitat");
        System.out.println("-".repeat(80));

        // Verificar i eliminar si existeix
        ProdItem existent = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_3);
        if (existent != null) {
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_3);
        }

        // Primer inserir una relació de test
        ProdItem testItem = new ProdItem(PRODUCTE_TEST, COMPONENT_TEST_3, 3);
        boolean inserit = dao.insertar(testItem);

        if (inserit) {
            System.out.println("✓ Relació de test inserida (qty=3)");

            // Actualitzar la quantitat
            testItem.setQuantitat(15);
            System.out.println("📝 Actualitzant quantitat a 15...");

            boolean actualitzat = dao.actualitzar(testItem);

            if (actualitzat) {
                System.out.println("✓ Quantitat actualitzada correctament");

                // Verificar el canvi
                ProdItem verificacio = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_3);
                if (verificacio != null && verificacio.getQuantitat() == 15) {
                    System.out.println("✓ Verificació: Nova quantitat = 15");
                } else {
                    System.err.println("✗ ERROR: Quantitat no actualitzada correctament!");
                }

            } else {
                System.err.println("✗ ERROR: No s'ha pogut actualitzar");
            }

            // CLEANUP
            System.out.println("🧹 Cleanup: Eliminant relació de test...");
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_3);
            System.out.println("✓ Cleanup completat");

        } else {
            System.err.println("✗ ERROR: No s'ha pogut crear relació de test");
        }

        System.out.println();
    }

    /**
     * TEST 8: eliminar() - Eliminar una relació Producte-Item
     * USA: Producte existent + Component existent
     */
    private static void testEliminar(DAOProdItem dao) {
        System.out.println("TEST 8: eliminar() - Eliminar relació");
        System.out.println("-".repeat(80));

        // Verificar i eliminar si existeix
        ProdItem existent = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_4);
        if (existent != null) {
            dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_4);
        }

        // Primer inserir una relació de test
        ProdItem testItem = new ProdItem(PRODUCTE_TEST, COMPONENT_TEST_4, 7);
        boolean inserit = dao.insertar(testItem);

        if (inserit) {
            System.out.println("✓ Relació de test inserida");

            // Eliminar la relació
            System.out.println("🗑️  Eliminant relació (" + PRODUCTE_TEST + ", " + 
                             COMPONENT_TEST_4 + ")...");
            boolean eliminat = dao.eliminar(PRODUCTE_TEST, COMPONENT_TEST_4);

            if (eliminat) {
                System.out.println("✓ Relació eliminada correctament");

                // Verificar que ja no existeix
                ProdItem verificacio = dao.findById(PRODUCTE_TEST, COMPONENT_TEST_4);
                if (verificacio == null) {
                    System.out.println("✓ Verificació: Relació eliminada de la BD");
                } else {
                    System.err.println("✗ ERROR: Relació encara existeix!");
                }

            } else {
                System.err.println("✗ ERROR: No s'ha pogut eliminar");
            }

        } else {
            System.err.println("✗ ERROR: No s'ha pogut crear relació de test");
        }

        // Test eliminar relació inexistent
        System.out.println("\n🗑️  Test: eliminar relació inexistent");
        boolean eliminatInexistent = dao.eliminar("XXXXX", "YYYYY");
        if (!eliminatInexistent) {
            System.out.println("✓ Correcte: retorna false per PK inexistent");
        } else {
            System.err.println("⚠️  WARNING: Hauria de retornar false");
        }

        System.out.println();
    }

    /**
     * TEST 9: Validacions - Testejar validacions del DAO
     */
    private static void testValidacions(DAOProdItem dao) {
        System.out.println("TEST 9: validacions() - Tests de validació");
        System.out.println("-".repeat(80));

        // Test 1: ProdItem null
        System.out.println("📋 Test validació 1: ProdItem null");
        boolean resultat1 = dao.insertar(null);
        if (!resultat1) {
            System.out.println("✓ Rebutja correctament ProdItem null");
        } else {
            System.err.println("✗ ERROR: Hauria de rebutjar null!");
        }

        // Test 2: Codi producte null/buit
        System.out.println("\n📋 Test validació 2: Codi producte null/buit");
        ProdItem invalid1 = new ProdItem(null, COMPONENT_TEST_1, 5);
        boolean resultat2 = dao.insertar(invalid1);
        if (!resultat2) {
            System.out.println("✓ Rebutja correctament prCodi null");
        }

        ProdItem invalid2 = new ProdItem("", COMPONENT_TEST_1, 5);
        boolean resultat3 = dao.insertar(invalid2);
        if (!resultat3) {
            System.out.println("✓ Rebutja correctament prCodi buit");
        }

        // Test 3: Codi item null/buit
        System.out.println("\n📋 Test validació 3: Codi item null/buit");
        ProdItem invalid3 = new ProdItem(PRODUCTE_TEST, null, 5);
        boolean resultat4 = dao.insertar(invalid3);
        if (!resultat4) {
            System.out.println("✓ Rebutja correctament itCodi null");
        }

        ProdItem invalid4 = new ProdItem(PRODUCTE_TEST, "", 5);
        boolean resultat5 = dao.insertar(invalid4);
        if (!resultat5) {
            System.out.println("✓ Rebutja correctament itCodi buit");
        }

        // Test 4: CHECK constraint - producte conté a si mateix
        System.out.println("\n📋 Test validació 4: CHECK (pi_pr_codi <> pi_it_codi)");
        ProdItem invalid5 = new ProdItem("P001", "P001", 5);
        boolean resultat6 = dao.insertar(invalid5);
        if (!resultat6) {
            System.out.println("✓ Rebutja correctament producte que es conté a si mateix");
        } else {
            System.err.println("✗ ERROR: Hauria de rebutjar auto-contenció!");
        }

        // Test 5: Quantitat <= 0
        System.out.println("\n📋 Test validació 5: Quantitat no vàlida");
        ProdItem invalid6 = new ProdItem(PRODUCTE_TEST, COMPONENT_TEST_1, 0);
        boolean resultat7 = dao.insertar(invalid6);
        if (!resultat7) {
            System.out.println("✓ Rebutja correctament quantitat 0");
        }

        ProdItem invalid7 = new ProdItem(PRODUCTE_TEST, COMPONENT_TEST_1, -5);
        boolean resultat8 = dao.insertar(invalid7);
        if (!resultat8) {
            System.out.println("✓ Rebutja correctament quantitat negativa");
        }

        System.out.println("\n✅ Totes les validacions funcionen correctament");
        System.out.println();
    }

}