package test;

import dao.DAOProdItem;
import dao.DAOProducte;
import model.ProdItem;
import model.Producte;

public class TestValidacioProducte {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("   TEST: Validació de Productes amb Components");
        System.out.println("=".repeat(80));
        System.out.println();

        DAOProducte daoProducte = new DAOProducte();
        DAOProdItem daoProdItem = new DAOProdItem();

        // Test 1: Producte existent amb components (P001)
        System.out.println("📋 Test 1: Producte P001 (hauria de tenir components)");
        boolean te1 = daoProducte.teComponents("P001");
        if (te1) {
            System.out.println("✓ P001 té components");
        } else {
            System.err.println("✗ ERROR: P001 hauria de tenir components!");
        }

        // Test 2: Crear producte NOU sense components
        System.out.println("\n📋 Test 2: Crear producte PTEST sense components");

        // Eliminar si existeix
        daoProducte.eliminar("PTEST");

        // Crear producte buit
        Producte pTest = new Producte(
                "PTEST", // itCodi
                "P", // itTipus
                "Test Producte Buit", // itNom
                "Producte de test", // itDesc
                0, // itStock
                null, // itFoto
                "PTEST" // prCodi
        );
        boolean creat = daoProducte.insertar(pTest);

        if (creat) {
            System.out.println("✓ Producte PTEST creat");

            // Validar que NO té components
            boolean te2 = daoProducte.teComponents("PTEST");
            if (!te2) {
                System.out.println("✓ Correcte: PTEST no té components (BOM buit)");
            } else {
                System.err.println("✗ ERROR: PTEST no hauria de tenir components!");
            }

            // Afegir un component
            System.out.println("\n📝 Afegint component C001 a PTEST...");
            ProdItem item = new ProdItem("PTEST", "C001", 1);
            boolean afegit = daoProdItem.insertar(item);

            if (afegit) {
                System.out.println("✓ Component afegit");

                // Ara SÍ hauria de validar
                boolean te3 = daoProducte.teComponents("PTEST");
                if (te3) {
                    System.out.println("✓ Correcte: PTEST ara té components");
                } else {
                    System.err.println("✗ ERROR: PTEST hauria de tenir components!");
                }
            }

            // Cleanup
            System.out.println("\n🧹 Cleanup...");
            daoProdItem.eliminar("PTEST", "C001");
            daoProducte.eliminar("PTEST");
            System.out.println("✓ Cleanup completat");
        }

        // Test 3: Producte inexistent
        System.out.println("\n📋 Test 3: Producte inexistent (XXXXX)");
        boolean te4 = daoProducte.teComponents("XXXXX");
        if (!te4) {
            System.out.println("✓ Correcte: producte inexistent retorna false");
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("   ✅ TESTS COMPLETATS");
        System.out.println("=".repeat(80));
    }
}