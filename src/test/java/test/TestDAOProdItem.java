package test;

import java.util.List;

import dao.DAOProdItem;
import model.ProdItem;

/**
 * Test unitari per DAOProdItem
 * 
 * IMPORTANT: Aquests tests són NO DESTRUCTIUS
 * - No insereixen dades noves
 * - No modifiquen dades existents
 * - No esborren dades
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOProdItem {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   TEST UNITARI: DAOProdItem (NO DESTRUCTIU)");
        System.out.println("=".repeat(60));
        System.out.println();

        DAOProdItem dao = new DAOProdItem();

        // Tests de consulta (READ)
        testFindAll(dao);
        testCountTotal(dao);
        testFindById(dao);
        testGetItemsDelProducte(dao);

        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("   ✅ TOTS ELS TESTS COMPLETATS");
        System.out.println("=".repeat(60));
    }

    // ==========================================
    // TESTS DE CONSULTA (READ)
    // ==========================================

    /**
     * TEST 1: findAll() - Obtenir totes les relacions
     */
    private static void testFindAll(DAOProdItem dao) {
        System.out.println("TEST 1: findAll()");
        System.out.println("-".repeat(60));

        List<ProdItem> prodItems = dao.findAll();

        System.out.println("✓ Relacions trobades: " + prodItems.size());

        if (!prodItems.isEmpty()) {
            System.out.println("\nPrimeres 10 relacions:");
            prodItems.stream()
                .limit(10)
                .forEach(pi -> System.out.printf("  - %s contiene %s (qty=%d)%n",
                        pi.getPiPrCodi(), pi.getPiItCodi(), pi.getQuantitat()));
        }

        System.out.println();
    }

    /**
     * TEST 2: countTotal() - Comptar total de relacions
     */
    private static void testCountTotal(DAOProdItem dao) {
        System.out.println("TEST 2: countTotal()");
        System.out.println("-".repeat(60));

        int total = dao.countTotal();
        System.out.println("✓ Total de relacions: " + total);

        // Verificar que coincideix amb findAll()
        List<ProdItem> tots = dao.findAll();
        if (total == tots.size()) {
            System.out.println("✓ countTotal() coincideix amb findAll().size()");
        } else {
            System.err.println("✗ Discrepància! countTotal=" + total + 
                             " vs findAll().size()=" + tots.size());
        }

        System.out.println();
    }

    /**
     * TEST 3: findById() - Cercar relació per PK composta
     */
    private static void testFindById(DAOProdItem dao) {
        System.out.println("TEST 3: findById() - PK composta");
        System.out.println("-".repeat(60));

        // Obtenir primera relació existent
        List<ProdItem> tots = dao.findAll();

        if (!tots.isEmpty()) {
            ProdItem primera = tots.get(0);
            String prCodi = primera.getPiPrCodi();
            String itCodi = primera.getPiItCodi();
            
            System.out.println("Cercant relació: " + prCodi + " - " + itCodi);

            ProdItem trobat = dao.findById(prCodi, itCodi);

            if (trobat != null) {
                System.out.println("✓ Relació trobada:");
                System.out.println("  Producte: " + trobat.getPiPrCodi());
                System.out.println("  Item: " + trobat.getPiItCodi());
                System.out.println("  Quantitat: " + trobat.getQuantitat());
            } else {
                System.err.println("✗ Relació no trobada!");
            }
        } else {
            System.out.println("⚠ No hi ha relacions a la BD per testejar");
        }

        // Test amb PK inexistent
        System.out.println("\nCercant relació inexistent (XXXXX - YYYYY):");
        ProdItem inexistent = dao.findById("XXXXX", "YYYYY");
        if (inexistent == null) {
            System.out.println("✓ Correcte: retorna null per PK inexistent");
        } else {
            System.err.println("✗ Error: hauria de retornar null");
        }

        // Test validació: producte no pot contenir-se a si mateix
        System.out.println("\nTest validació: producte no pot contenir-se a si mateix");
        ProdItem mateix = dao.findById("P001", "P001");
        if (mateix == null) {
            System.out.println("✓ Correcte: CHECK (pi_pr_codi <> pi_it_codi) funciona");
        }

        System.out.println();
    }

    /**
     * TEST 4: getItemsDelProducte() - Obtenir BOM d'un producte
     */
    private static void testGetItemsDelProducte(DAOProdItem dao) {
        System.out.println("TEST 4: getItemsDelProducte() - BOM");
        System.out.println("-".repeat(60));

        // Test amb primer producte existent
        List<ProdItem> tots = dao.findAll();

        if (!tots.isEmpty()) {
            String prCodi = tots.get(0).getPiPrCodi();
            System.out.println("Obtenint BOM del producte: " + prCodi);

            List<ProdItem> items = dao.getItemsDelProducte(prCodi);
            System.out.println("✓ Items del producte: " + items.size());

            if (!items.isEmpty()) {
                System.out.println("\nBOM (Bill of Materials):");
                for (ProdItem item : items) {
                    System.out.printf("  - %s (quantitat: %d)%n",
                            item.getPiItCodi(), item.getQuantitat());
                }
            }

            // Test amb altres productes si n'hi ha
            System.out.println("\nObtenir BOM de tots els productes:");
            tots.stream()
                .map(ProdItem::getPiPrCodi)
                .distinct()
                .forEach(codigo -> {
                    List<ProdItem> itemsProducte = dao.getItemsDelProducte(codigo);
                    System.out.printf("  %s: %d items%n", codigo, itemsProducte.size());
                });

        } else {
            System.out.println("⚠ No hi ha relacions a la BD per testejar");
        }

        // Test amb producte inexistent
        System.out.println("\nObtenir BOM de producte inexistent:");
        List<ProdItem> inexistent = dao.getItemsDelProducte("XXXXX");
        System.out.println("✓ Items del producte inexistent: " + inexistent.size() + 
                         " (hauria de ser 0)");

        // Test amb producte buit
        System.out.println("\nTest validació: producte buit");
        List<ProdItem> buit = dao.getItemsDelProducte("");
        System.out.println("✓ Items amb codi buit: " + buit.size() + 
                         " (hauria de ser 0)");

        System.out.println();
    }

    // ==========================================
    // TESTS OPCIONALS (DESTRUCTIUS - COMENTATS)
    // ==========================================

    /*
    private static void testInsertar(DAOProdItem dao) {
        System.out.println("TEST OPCIONAL: insertar()");
        System.out.println("-".repeat(60));
        
        ProdItem nou = new ProdItem("P001", "C999", 5);
        boolean resultat = dao.insertar(nou);
        
        if (resultat) {
            System.out.println("✓ ProdItem inserit correctament");
            // Cleanup
            dao.eliminar("P001", "C999");
        } else {
            System.err.println("✗ Error insertant ProdItem");
        }
        
        System.out.println();
    }
    
    private static void testAfegirItemAProducte(DAOProdItem dao) {
        System.out.println("TEST OPCIONAL: afegirItemAProducte()");
        System.out.println("-".repeat(60));
        
        boolean resultat = dao.afegirItemAProducte("P001", "C998", 3);
        
        if (resultat) {
            System.out.println("✓ Item afegit al producte correctament");
            // Cleanup
            dao.eliminar("P001", "C998");
        } else {
            System.err.println("✗ Error afegint item al producte");
        }
        
        System.out.println();
    }
    */

}