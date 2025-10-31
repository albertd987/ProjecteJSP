package test;

import java.util.List;

import dao.DAOProducte;
import model.Producte;

/**
 * Test unitari per DAOProducte
 * 
 * IMPORTANT: Aquests tests són NO DESTRUCTIUS
 * - No insereixen dades noves
 * - No modifiquen dades existents
 * - No esborren dades
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOProducte {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   TEST UNITARI: DAOProducte (NO DESTRUCTIU)");
        System.out.println("=".repeat(60));
        System.out.println();

        DAOProducte dao = new DAOProducte();

        // Tests de consulta (READ)
        testFindAll(dao);
        testCountTotal(dao);
        testFindById(dao);
        testFiltrarPerCodi(dao);
        testFindAllPaginat(dao);
        testCalcularPreuTotal(dao);

        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("   ✅ TOTS ELS TESTS COMPLETATS");
        System.out.println("=".repeat(60));
    }

    // ==========================================
    // TESTS DE CONSULTA (READ)
    // ==========================================

    /**
     * TEST 1: findAll() - Obtenir tots els productes
     */
    private static void testFindAll(DAOProducte dao) {
        System.out.println("TEST 1: findAll()");
        System.out.println("-".repeat(60));

        List<Producte> productes = dao.findAll();

        System.out.println("✓ Productes trobats: " + productes.size());

        if (!productes.isEmpty()) {
            System.out.println("\nPrimers 5 productes:");
            productes.stream()
                .limit(5)
                .forEach(p -> System.out.println("  - " + p.getPrCodi() + " : " + p.getItNom()));
        }

        System.out.println();
    }

    /**
     * TEST 2: countTotal() - Comptar total de productes
     */
    private static void testCountTotal(DAOProducte dao) {
        System.out.println("TEST 2: countTotal()");
        System.out.println("-".repeat(60));

        int total = dao.countTotal();
        System.out.println("✓ Total de productes: " + total);

        // Verificar que coincideix amb findAll()
        List<Producte> tots = dao.findAll();
        if (total == tots.size()) {
            System.out.println("✓ countTotal() coincideix amb findAll().size()");
        } else {
            System.err.println("✗ Discrepància! countTotal=" + total + 
                             " vs findAll().size()=" + tots.size());
        }

        System.out.println();
    }

    /**
     * TEST 3: findById() - Cercar producte per codi
     */
    private static void testFindById(DAOProducte dao) {
        System.out.println("TEST 3: findById()");
        System.out.println("-".repeat(60));

        // Obtenir primer producte existent
        List<Producte> tots = dao.findAll();

        if (!tots.isEmpty()) {
            String codiTest = tots.get(0).getPrCodi();
            System.out.println("Cercant producte: " + codiTest);

            Producte producte = dao.findById(codiTest);

            if (producte != null) {
                System.out.println("✓ Producte trobat:");
                System.out.println("  Codi: " + producte.getPrCodi());
                System.out.println("  Nom: " + producte.getItNom());
                System.out.println("  Desc: " + producte.getItDesc());
                System.out.println("  Stock: " + producte.getItStock());
            } else {
                System.err.println("✗ Producte no trobat!");
            }
        } else {
            System.out.println("⚠ No hi ha productes a la BD per testejar");
        }

        // Test amb codi inexistent
        System.out.println("\nCercant producte inexistent (XXXXX):");
        Producte inexistent = dao.findById("XXXXX");
        if (inexistent == null) {
            System.out.println("✓ Correcte: retorna null per codi inexistent");
        } else {
            System.err.println("✗ Error: hauria de retornar null");
        }

        System.out.println();
    }

    /**
     * TEST 4: filtrarPerCodi() - Filtrar per patró de codi
     */
    private static void testFiltrarPerCodi(DAOProducte dao) {
        System.out.println("TEST 4: filtrarPerCodi()");
        System.out.println("-".repeat(60));

        // Test amb patró "PR"
        String patro = "PR";
        System.out.println("Filtrant per patró: '" + patro + "'");

        List<Producte> filtrats = dao.filtrarPerCodi(patro);
        System.out.println("✓ Productes trobats: " + filtrats.size());

        if (!filtrats.isEmpty()) {
            System.out.println("\nPrimers resultats:");
            filtrats.stream()
                .limit(3)
                .forEach(p -> System.out.println("  - " + p.getPrCodi()));
        }

        // Test amb patró buit
        System.out.println("\nFiltrant amb patró buit:");
        List<Producte> tots = dao.filtrarPerCodi("");
        System.out.println("✓ Hauria de retornar tots: " + tots.size());

        System.out.println();
    }

    /**
     * TEST 5: findAllPaginat() - Paginació
     */
    private static void testFindAllPaginat(DAOProducte dao) {
        System.out.println("TEST 5: findAllPaginat()");
        System.out.println("-".repeat(60));

        int pageSize = 5;

        // Pàgina 1
        System.out.println("Obtenint pàgina 1 (size=" + pageSize + "):");
        List<Producte> pagina1 = dao.findAllPaginat(1, pageSize);
        System.out.println("✓ Productes a la pàgina 1: " + pagina1.size());

        if (!pagina1.isEmpty()) {
            pagina1.forEach(p -> System.out.println("  - " + p.getPrCodi()));
        }

        // Pàgina 2
        System.out.println("\nObtenint pàgina 2:");
        List<Producte> pagina2 = dao.findAllPaginat(2, pageSize);
        System.out.println("✓ Productes a la pàgina 2: " + pagina2.size());

        // Verificar que no hi ha duplicats
        if (!pagina1.isEmpty() && !pagina2.isEmpty()) {
            String primerPag1 = pagina1.get(0).getPrCodi();
            String primerPag2 = pagina2.get(0).getPrCodi();
            if (!primerPag1.equals(primerPag2)) {
                System.out.println("✓ No hi ha duplicats entre pàgines");
            } else {
                System.err.println("✗ Duplicats detectats!");
            }
        }

        System.out.println();
    }

    /**
     * TEST 6: calcularPreuTotal() - Càlcul recursiu del preu
     */
    private static void testCalcularPreuTotal(DAOProducte dao) {
        System.out.println("TEST 6: calcularPreuTotal()");
        System.out.println("-".repeat(60));

        // Obtenir primer producte existent
        List<Producte> tots = dao.findAll();

        if (!tots.isEmpty()) {
            String codiTest = tots.get(0).getPrCodi();
            System.out.println("Calculant preu total de: " + codiTest);

            double preuTotal = dao.calcularPreuTotal(codiTest);
            System.out.println("✓ Preu total: " + preuTotal + " €");

            // Testejar amb més productes si n'hi ha
            if (tots.size() > 1) {
                System.out.println("\nCalculant preus de tots els productes:");
                tots.forEach(p -> {
                    double preu = dao.calcularPreuTotal(p.getPrCodi());
                    System.out.printf("  %s : %.2f €%n", p.getPrCodi(), preu);
                });
            }
        } else {
            System.out.println("⚠ No hi ha productes a la BD per testejar");
        }

        // Test amb codi inexistent
        System.out.println("\nCalculant preu de producte inexistent:");
        double preuInexistent = dao.calcularPreuTotal("XXXXX");
        System.out.println("✓ Preu producte inexistent: " + preuInexistent + " (hauria de ser 0.0)");

        System.out.println();
    }

}
