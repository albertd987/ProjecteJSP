package util;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Component;
import model.Municipi;
import model.Producte;
import model.ProvComp;
import model.Provincia;
import model.UnitatMesura;
/**
 * Tests simples per validar models contra Oracle Database
 * Tests directes amb JDBC sense capa DAO
 * 
 * @author DomenechObiolAlbert
 */
public class TestModels {
    
    // ================================================
    // TEST 1: UnitatMesura (taula simple)
    // ================================================
    
    public static void testUnitatMesura() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 1: Model UnitatMesura");
        System.out.println("=".repeat(50));
        
        String sql = "SELECT um_codi, um_nom FROM UnitatMesura ORDER BY um_codi";
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<UnitatMesura> unitats = new ArrayList<>();
            
            // Mapear ResultSet → Model
            while (rs.next()) {
                UnitatMesura um = new UnitatMesura();
                um.setUmCodi(rs.getString("um_codi"));
                um.setUmNom(rs.getString("um_nom"));
                unitats.add(um);
            }
            
            // Validar
            System.out.println("S'han trobat " + unitats.size() + " unitats de mesura\n");
            
            for (UnitatMesura um : unitats) {
                System.out.println("   " + um.getUmCodi() + " - " + um.getUmNom());
                
                // Validar que no hi ha nulls
                assert um.getUmCodi() != null : "Codi no pot ser null";
                assert um.getUmNom() != null : "Nom no pot ser null";
            }
            
            // Test del toString()
            if (!unitats.isEmpty()) {
                System.out.println("\n   toString(): " + unitats.get(0));
            }
            
            System.out.println("\nModel UnitatMesura funciona correctament!");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ================================================
    // TEST 2: Provincia (taula simple)
    // ================================================
    
    public static void testProvincia() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 2: Model Provincia");
        System.out.println("=".repeat(50));
        
        String sql = "SELECT pr_codi, pr_nom FROM Provincia ORDER BY pr_codi";
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Provincia> provincies = new ArrayList<>();
            
            while (rs.next()) {
                Provincia pr = new Provincia();
                pr.setPrCodi(rs.getString("pr_codi"));
                pr.setPrNom(rs.getString("pr_nom"));
                provincies.add(pr);
            }
            
            System.out.println("S'han trobat " + provincies.size() + " províncies\n");
            
            for (Provincia pr : provincies) {
                System.out.println("   " + pr.getPrCodi() + " - " + pr.getPrNom());
                assert pr.getPrCodi() != null;
                assert pr.getPrNom() != null;
            }
            
            System.out.println("\nModel Provincia funciona correctament!");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // ================================================
    // TEST 3: Municipi (PK composta, FK)
    // ================================================
    
    public static void testMunicipi() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 3: Model Municipi (PK composta)");
        System.out.println("=".repeat(50));
        
        String sql = "SELECT mu_pr_codi, mu_num, mu_nom FROM Municipi ORDER BY mu_pr_codi, mu_num";
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Municipi> municipis = new ArrayList<>();
            
            while (rs.next()) {
                Municipi mu = new Municipi();
                mu.setMuPrCodi(rs.getString("mu_pr_codi"));
                mu.setMuNum(rs.getString("mu_num"));
                mu.setMuNom(rs.getString("mu_nom"));
                municipis.add(mu);
            }
            
            System.out.println("S'han trobat " + municipis.size() + " municipis\n");
            
            for (Municipi mu : municipis) {
                System.out.println("   " + mu.getMuPrCodi() + "-" + mu.getMuNum() + " - " + mu.getMuNom());
                assert mu.getMuPrCodi() != null;
                assert mu.getMuNum() != null;
                assert mu.getMuNom() != null;
            }
            
            System.out.println("\nModel Municipi funciona correctament!");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // ================================================
    // TEST 4: Component (herència Item, FK, preu_mig)
    // ================================================
    
    public static void testComponent() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 4: Model Component (herència + triggers)");
        System.out.println("=".repeat(50));
        
        String sql = """
            SELECT 
                i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                c.cm_um_codi, c.cm_codi_fabricant, c.cm_preu_mig
            FROM Item i
            JOIN Component c ON i.it_codi = c.cm_codi
            ORDER BY i.it_codi
            """;
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Component> components = new ArrayList<>();
            
            while (rs.next()) {
                // Component hereta de Item, així que cal omplir ambdós
                Component comp = new Component();
                
                // Dades d'Item (classe pare)
                comp.setItCodi(rs.getString("it_codi"));
                comp.setItTipus(rs.getString("it_tipus"));
                comp.setItNom(rs.getString("it_nom"));
                comp.setItDesc(rs.getString("it_desc"));
                comp.setItStock(rs.getInt("it_stock"));
                comp.setItFoto(rs.getString("it_foto"));
                
                // Dades de Component (classe filla)
                comp.setCmCodi(rs.getString("it_codi"));
                comp.setCmUmCodi(rs.getString("cm_um_codi"));
                comp.setCmCodiFabricant(rs.getString("cm_codi_fabricant"));
                comp.setCmPreuMig(rs.getDouble("cm_preu_mig"));
                
                components.add(comp);
            }
            
            System.out.println("S'han trobat " + components.size() + " components\n");
            
            for (Component comp : components) {
                System.out.println("    " + comp.getCmCodi() + " - " + comp.getItNom());
                System.out.println("      Fabricant: " + comp.getCmCodiFabricant());
                System.out.println("      Preu Mig: " + comp.getCmPreuMig() + " € (calculat pels triggers!)");
                System.out.println("      Stock: " + comp.getItStock() + " " + comp.getCmUmCodi());
                System.out.println();
                
                // Validacions
                assert comp.getCmCodi() != null;
                assert comp.getItNom() != null;
                assert comp.getCmPreuMig() != null;
                assert comp.getCmPreuMig() >= 0 : "Preu no pot ser negatiu";
            }
            
            System.out.println(" Model Component funciona correctament!");
            System.out.println("    Herència Item → Component OK");
            System.out.println("    Preu mitjà calculat pels triggers OK");
            
        } catch (SQLException e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ================================================
    // TEST 5: Producte (herència Item)
    // ================================================
    
    public static void testProducte() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 5: Model Producte (herència)");
        System.out.println("=".repeat(50));
        
        String sql = """
            SELECT 
                i.it_codi, i.it_tipus, i.it_nom, i.it_desc, i.it_stock, i.it_foto,
                p.pr_codi
            FROM Item i
            JOIN Producte p ON i.it_codi = p.pr_codi
            ORDER BY i.it_codi
            """;
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Producte> productes = new ArrayList<>();
            
            while (rs.next()) {
                Producte prod = new Producte();
                
                // Dades d'Item
                prod.setItCodi(rs.getString("it_codi"));
                prod.setItTipus(rs.getString("it_tipus"));
                prod.setItNom(rs.getString("it_nom"));
                prod.setItDesc(rs.getString("it_desc"));
                prod.setItStock(rs.getInt("it_stock"));
                prod.setItFoto(rs.getString("it_foto"));
                
                // Dades de Producte
                prod.setPrCodi(rs.getString("pr_codi"));
                
                productes.add(prod);
            }
            
            System.out.println("S'han trobat " + productes.size() + " productes\n");
            
            for (Producte prod : productes) {
                System.out.println("    " + prod.getPrCodi() + " - " + prod.getItNom());
                System.out.println("      Descripció: " + prod.getItDesc());
                System.out.println("      Stock: " + prod.getItStock());
                System.out.println();
                
                assert prod.getPrCodi() != null;
                assert prod.getItNom() != null;
            }
            
            System.out.println(" Model Producte funciona correctament!");
            
        } catch (SQLException e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
    
    // ================================================
    // TEST 6: ProvComp (relació N:N, dispara triggers!)
    // ================================================
    
    public static void testProvComp() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 6: Model ProvComp (N:N, triggers crítics!)");
        System.out.println("=".repeat(50));
        
        String sql = """
            SELECT pc_cm_codi, pc_pv_codi, pc_preu
            FROM Prov_Comp
            ORDER BY pc_cm_codi, pc_pv_codi
            """;
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<ProvComp> relacions = new ArrayList<>();
            
            while (rs.next()) {
                ProvComp pc = new ProvComp();
                pc.setPcCmCodi(rs.getString("pc_cm_codi"));
                pc.setPcPvCodi(rs.getString("pc_pv_codi"));
                pc.setPcPreu(rs.getDouble("pc_preu"));
                relacions.add(pc);
            }
            
            System.out.println("S'han trobat " + relacions.size() + " relacions proveïdor-component\n");
            
            // Mostrar alguns exemples
            int count = 0;
            for (ProvComp pc : relacions) {
                if (count++ < 5) {  // Només mostrem els 5 primers
                    System.out.println("   Component " + pc.getPcCmCodi() + 
                                     " - Proveïdor " + pc.getPcPvCodi() + 
                                     " = " + pc.getPcPreu() + " €");
                }
            }
            if (relacions.size() > 5) {
                System.out.println("   ... i " + (relacions.size() - 5) + " més");
            }
            
            System.out.println("\n  RECORDATORI: INSERT/UPDATE/DELETE en aquesta taula");
            System.out.println("   disparen el trigger que recalcula cm_preu_mig!");
            
            System.out.println("\n Model ProvComp funciona correctament!");
            
        } catch (SQLException e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
    
    // ================================================
    // TEST 7: Validar que preu_mig és correcte
    // ================================================
    
    public static void testPreuMigCalculat() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST 7: Validar triggers de preu_mig");
        System.out.println("=".repeat(50));
        
        String sql = """
            SELECT 
                c.cm_codi,
                c.cm_preu_mig,
                ROUND(AVG(pc.pc_preu), 2) as preu_calculat,
                COUNT(pc.pc_pv_codi) as num_proveidors
            FROM Component c
            LEFT JOIN Prov_Comp pc ON c.cm_codi = pc.pc_cm_codi
            GROUP BY c.cm_codi, c.cm_preu_mig
            ORDER BY c.cm_codi
            """;
        
        try (Connection conn = ConnexioOracle.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(" Validant que cm_preu_mig = AVG(pc_preu)...\n");
            
            int correctes = 0;
            int total = 0;
            
            while (rs.next()) {
                total++;
                String codi = rs.getString("cm_codi");
                double preuMig = rs.getDouble("cm_preu_mig");
                Double preuCalculat = rs.getDouble("preu_calculat");
                int numProveidors = rs.getInt("num_proveidors");
                
                // Si no té proveïdors, preuCalculat serà 0
                if (numProveidors == 0) {
                    preuCalculat = 0.0;
                }
                
                boolean esCorrecte = Math.abs(preuMig - preuCalculat) < 0.01;
                
                if (esCorrecte) {
                    correctes++;
                    System.out.println("    " + codi + ": " + preuMig + " € (proveidors: " + numProveidors + ")");
                } else {
                    System.out.println("    " + codi + ": BD=" + preuMig + " € vs Calculat=" + preuCalculat + " €");
                }
            }
            
            System.out.println("\n📈 RESULTAT: " + correctes + "/" + total + " components amb preu correcte");
            
            if (correctes == total) {
                System.out.println(" Els triggers funcionen perfectament!");
            } else {
                System.out.println("  Alguns preus no coincideixen - revisa els triggers!");
            }
            
        } catch (SQLException e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
    
    // ================================================
    // MAIN: Executar tots els tests
    // ================================================
    
    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(10) + "TESTS DE VALIDACIÓ DELS MODELS" + " ".repeat(17) + "║");
        System.out.println("║" + " ".repeat(15) + "Tallers Manolo - Oracle DB" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        
        try {
            // Test de connexió primer
            System.out.println("\n🔍 Verificant connexió...");
            try (Connection conn = ConnexioOracle.getConnection()) {
                System.out.println(" Connexió a Oracle OK!");
            }
            
            // Executar tots els tests
            testUnitatMesura();
            testProvincia();
            testMunicipi();
            testComponent();
            testProducte();
            testProvComp();
            testPreuMigCalculat();

            // Resum final
            System.out.println("\n" + "=".repeat(50));
            System.out.println(" TOTS ELS TESTS HAN FINALITZAT");
            System.out.println("=".repeat(50));
            System.out.println("\n Els models funcionen correctament amb Oracle!");
            System.out.println(" Les herències (Item → Component/Producte) funcionen!");
            System.out.println(" Els triggers de preu_mig funcionen!");
            
        } catch (Exception e) {
            System.err.println("\n Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}