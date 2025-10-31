package test;

import java.util.List;

import dao.DAOItem;
import model.Item;

/**
 * Test per validar DAOItem
 * Prova operacions CRUD i mètodes específics
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOItem {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("        TEST DAOItem - Operacions CRUD");
        System.out.println("═══════════════════════════════════════════════════\n");
        
        DAOItem dao = new DAOItem();
        
        // 1. TEST COUNT TOTAL
        testCountTotal(dao);
        
        // 2. TEST FINDALL
        testFindAll(dao);
        
        // 3. TEST FINDBYID
        testFindById(dao);
        
        // 4. TEST FILTRAR PER TIPUS
        testFiltrarPerTipus(dao);
        
        // 5. TEST CERCAR PER NOM
        testCercarPerNom(dao);
        
        // 6. TEST STOCK BAIX
        testStockBaix(dao);
        
        // 7. TEST PAGINACIÓ
        testPaginacio(dao);
        
        // 8. TEST INSERTAR (opcional - descomenta si vols provar)
        // testInsertar(dao);
        
        // 9. TEST ACTUALITZAR (opcional - descomenta si vols provar)
        // testActualitzar(dao);
        
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("        FI DELS TESTS");
        System.out.println("═══════════════════════════════════════════════════");
    }
    
    // ================================================
    // TEST 1: COUNT TOTAL
    // ================================================
    
    private static void testCountTotal(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 1: countTotal()");
        System.out.println("=".repeat(50));
        
        int total = dao.countTotal();
        System.out.println("Total items en BD: " + total);
        
        assert total > 0 : "❌ Hauria d'haver almenys 1 item";
        System.out.println("✅ Test countTotal passat!");
    }
    
    // ================================================
    // TEST 2: FINDALL
    // ================================================
    
    private static void testFindAll(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 2: findAll()");
        System.out.println("=".repeat(50));
        
        List<Item> items = dao.findAll();
        System.out.println("S'han trobat " + items.size() + " items\n");
        
        // Mostrar els primers 5 items
        int maxMostrar = Math.min(5, items.size());
        for (int i = 0; i < maxMostrar; i++) {
            Item item = items.get(i);
            System.out.println("  " + (i+1) + ". " + item.getItCodi() + 
                             " [" + item.getItTipus() + "] - " + item.getItNom() + 
                             " (Stock: " + item.getItStock() + ")");
        }
        
        if (items.size() > 5) {
            System.out.println("  ... i " + (items.size() - 5) + " més");
        }
        
        assert !items.isEmpty() : "❌ Hauria d'haver items";
        System.out.println("\n✅ Test findAll passat!");
    }
    
    // ================================================
    // TEST 3: FINDBYID
    // ================================================
    
    private static void testFindById(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 3: findById()");
        System.out.println("=".repeat(50));
        
        // Provar amb un item que existeix (Component C001)
        Item item = dao.findById("C001");
        
        if (item != null) {
            System.out.println("\nItem trobat:");
            System.out.println("  Codi: " + item.getItCodi());
            System.out.println("  Tipus: " + item.getItTipus());
            System.out.println("  Nom: " + item.getItNom());
            System.out.println("  Descripció: " + item.getItDesc());
            System.out.println("  Stock: " + item.getItStock());
            
            assert item.getItCodi().equals("C001") : "❌ Codi incorrecte";
            assert item.getItTipus().equals("C") : "❌ Tipus incorrecte";
            System.out.println("\n✅ Test findById passat!");
        } else {
            System.out.println("⚠️  Item C001 no trobat (potser no existeix a la BD)");
        }
        
        // Provar amb un item que NO existeix
        Item itemInexistent = dao.findById("XXXXX");
        assert itemInexistent == null : "❌ Item inexistent hauria de retornar null";
        System.out.println("✅ Item inexistent retorna null correctament");
    }
    
    // ================================================
    // TEST 4: FILTRAR PER TIPUS
    // ================================================
    
    private static void testFiltrarPerTipus(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 4: filtrarPerTipus()");
        System.out.println("=".repeat(50));
        
        // Components (tipus 'C')
        List<Item> components = dao.filtrarPerTipus("C");
        System.out.println("Components (tipus 'C'): " + components.size());
        
        // Productes (tipus 'P')
        List<Item> productes = dao.filtrarPerTipus("P");
        System.out.println("Productes (tipus 'P'): " + productes.size());
        
        // Validar que tots són del tipus correcte
        for (Item c : components) {
            assert c.getItTipus().equals("C") : "❌ Component amb tipus incorrecte";
        }
        for (Item p : productes) {
            assert p.getItTipus().equals("P") : "❌ Producte amb tipus incorrecte";
        }
        
        System.out.println("✅ Test filtrarPerTipus passat!");
    }
    
    // ================================================
    // TEST 5: CERCAR PER NOM
    // ================================================
    
    private static void testCercarPerNom(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 5: cercarPerNom()");
        System.out.println("=".repeat(50));
        
        String pattern = "Intel";
        List<Item> resultats = dao.cercarPerNom(pattern);
        
        System.out.println("Items amb nom que conté '" + pattern + "': " + resultats.size());
        
        for (Item item : resultats) {
            System.out.println("  - " + item.getItCodi() + ": " + item.getItNom());
            assert item.getItNom().toUpperCase().contains(pattern.toUpperCase()) : 
                   "❌ Nom no conté el patró";
        }
        
        System.out.println("✅ Test cercarPerNom passat!");
    }
    
    // ================================================
    // TEST 6: STOCK BAIX
    // ================================================
    
    private static void testStockBaix(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 6: obtenirItemsAmbStockBaix()");
        System.out.println("=".repeat(50));
        
        int stockMinim = 10;
        List<Item> itemsStockBaix = dao.obtenirItemsAmbStockBaix(stockMinim);
        
        System.out.println("Items amb stock < " + stockMinim + ": " + itemsStockBaix.size());
        
        for (Item item : itemsStockBaix) {
            System.out.println("  - " + item.getItCodi() + ": " + item.getItNom() + 
                             " (Stock: " + item.getItStock() + ")");
            assert item.getItStock() < stockMinim : "❌ Stock no és baix";
        }
        
        System.out.println("✅ Test obtenirItemsAmbStockBaix passat!");
    }
    
    // ================================================
    // TEST 7: PAGINACIÓ
    // ================================================
    
    private static void testPaginacio(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 7: findAllPaginat()");
        System.out.println("=".repeat(50));
        
        int page = 1;
        int size = 5;
        
        List<Item> pagina1 = dao.findAllPaginat(page, size);
        System.out.println("Pàgina " + page + " (mida " + size + "): " + pagina1.size() + " items");
        
        for (Item item : pagina1) {
            System.out.println("  - " + item.getItCodi() + ": " + item.getItNom());
        }
        
        assert pagina1.size() <= size : "❌ Massa items retornats";
        System.out.println("✅ Test findAllPaginat passat!");
    }
    
    // ================================================
    // TEST 8: INSERTAR (opcional)
    // ================================================
    
    @SuppressWarnings("unused")
    private static void testInsertar(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 8: insertar() - OPCIONAL");
        System.out.println("=".repeat(50));
        
        Item nouItem = new Item();
        nouItem.setItCodi("TEST001");
        nouItem.setItTipus("C");
        nouItem.setItNom("Component de Test");
        nouItem.setItDesc("Aquest és un component de prova");
        nouItem.setItStock(100);
        nouItem.setItFoto("test.jpg");
        
        boolean inserit = dao.insertar(nouItem);
        
        if (inserit) {
            System.out.println("✅ Item TEST001 inserit correctament");
            
            // Verificar que existeix
            Item verificat = dao.findById("TEST001");
            assert verificat != null : "❌ Item no trobat després d'inserir";
            
            // Netejar (eliminar el test)
            dao.eliminar("TEST001");
            System.out.println("🧹 Item de test eliminat");
        } else {
            System.out.println("⚠️  No s'ha pogut inserir (potser ja existeix)");
        }
    }
    
    // ================================================
    // TEST 9: ACTUALITZAR (opcional)
    // ================================================
    
    @SuppressWarnings("unused")
    private static void testActualitzar(DAOItem dao) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 9: actualitzar() - OPCIONAL");
        System.out.println("=".repeat(50));
        
        // Primer inserir un item de test
        Item nouItem = new Item();
        nouItem.setItCodi("TEST002");
        nouItem.setItTipus("P");
        nouItem.setItNom("Producte de Test");
        nouItem.setItDesc("Descripció original");
        nouItem.setItStock(50);
        nouItem.setItFoto("test2.jpg");
        
        if (dao.insertar(nouItem)) {
            System.out.println("✅ Item TEST002 inserit");
            
            // Ara actualitzar-lo
            nouItem.setItNom("Producte de Test ACTUALITZAT");
            nouItem.setItDesc("Descripció modificada");
            nouItem.setItStock(75);
            
            boolean actualitzat = dao.actualitzar(nouItem);
            
            if (actualitzat) {
                System.out.println("✅ Item TEST002 actualitzat");
                
                // Verificar els canvis
                Item verificat = dao.findById("TEST002");
                assert verificat.getItNom().contains("ACTUALITZAT") : "❌ Nom no actualitzat";
                assert verificat.getItStock() == 75 : "❌ Stock no actualitzat";
                
                System.out.println("✅ Canvis verificats correctament");
            }
            
            // Netejar
            dao.eliminar("TEST002");
            System.out.println("🧹 Item de test eliminat");
        }
    }
}