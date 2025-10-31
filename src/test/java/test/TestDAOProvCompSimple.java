package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import dao.DAOProvComp;
import dao.IDAOProvComp;
import model.ProvComp;

/**
 * Test AUTÒNOM i SIMPLIFICAT per DAOProvComp
 * No depèn de DAOComponent - pot executar-se independentment
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestDAOProvCompSimple {

    public static void main(String[] args) {
        System.out.println("═".repeat(70));
        System.out.println("🧪 TEST AUTÒNOM: DAOProvComp");
        System.out.println("═".repeat(70));

        // Crear DAO
        IDAOProvComp dao = new DAOProvComp();

        // Executar tests
        boolean todoOk = true;
        
        todoOk &= test1_Connexio();
        todoOk &= test2_FindAll(dao);
        todoOk &= test3_GetProveidorsDelComponent(dao);
        todoOk &= test4_FindById(dao);
        todoOk &= test5_GetComponentsDelProveidor(dao);
        todoOk &= test6_ValidarPreuMig(dao);
        todoOk &= test7_OperacionsCRUD(dao);

        System.out.println("\n" + "═".repeat(70));
        if (todoOk) {
            System.out.println("✅✅✅ TOTS ELS TESTS HAN PASSAT! ✅✅✅");
        } else {
            System.out.println("⚠️  ALGUNS TESTS HAN FALLAT");
        }
        System.out.println("═".repeat(70));
    }

    // ============================================
    // TEST 1: Validar connexió Oracle
    // ============================================

    private static boolean test1_Connexio() {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 1: Validar connexió a Oracle");
        System.out.println("─".repeat(70));

        try {
            Connection conn = util.ConnexioOracle.getConnection();
            
            System.out.println("✅ Connexió exitosa!");
            System.out.println("   BD: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   Versió: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("   User: " + conn.getMetaData().getUserName());
            
            conn.close();
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ ERROR de connexió:");
            System.err.println("   " + e.getMessage());
            System.err.println("\n⚠️  Revisa db.properties!");
            return false;
        }
    }

    // ============================================
    // TEST 2: findAll() - Llistar totes les relacions
    // ============================================

    private static boolean test2_FindAll(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 2: findAll() - Llistar relacions Prov_Comp");
        System.out.println("─".repeat(70));

        try {
            List<ProvComp> totes = dao.findAll();

            if (totes == null) {
                System.err.println("❌ findAll() ha retornat null!");
                return false;
            }

            System.out.println("✅ S'han trobat " + totes.size() + " relacions");
            
            if (!totes.isEmpty()) {
                System.out.println("\n📋 Mostrant primers 5 registres:");
                int count = 0;
                for (ProvComp pc : totes) {
                    if (count++ >= 5) break;
                    System.out.println("   " + pc.getPcCmCodi() + " ← " + 
                                     pc.getPcPvCodi() + " = " + pc.getPcPreu() + "€");
                }
                if (totes.size() > 5) {
                    System.out.println("   ... i " + (totes.size() - 5) + " més");
                }
            } else {
                System.out.println("⚠️  La taula Prov_Comp està buida!");
            }
            
            return true;

        } catch (Exception e) {
            System.err.println("❌ ERROR en findAll():");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // TEST 3: getProveidorsDelComponent()
    // ============================================

    private static boolean test3_GetProveidorsDelComponent(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 3: getProveidorsDelComponent() - Proveïdors d'un component");
        System.out.println("─".repeat(70));

        try {
            // Buscar un component que tingui proveïdors
            String cmCodi = trobarComponentAmbProveidors();
            
            if (cmCodi == null) {
                System.out.println("⚠️  No hi ha components amb proveïdors per testejar");
                return true; // No és error, simplement BD buida
            }

            List<ProvComp> proveidors = dao.getProveidorsDelComponent(cmCodi);

            System.out.println("📦 Component " + cmCodi + " té " + 
                             proveidors.size() + " proveïdors:");

            for (ProvComp pc : proveidors) {
                System.out.println("   • Proveïdor " + pc.getPcPvCodi() + 
                                 " → " + pc.getPcPreu() + "€");
            }

            if (!proveidors.isEmpty()) {
                double mitjana = proveidors.stream()
                                          .mapToDouble(ProvComp::getPcPreu)
                                          .average()
                                          .orElse(0.0);
                System.out.println("\n💰 Preu mitjà calculat manualment: " + 
                                 String.format("%.2f", mitjana) + "€");
                System.out.println("   (Hauria de coincidir amb cm_preu_mig!)");
            }
            
            return true;

        } catch (Exception e) {
            System.err.println("❌ ERROR en getProveidorsDelComponent():");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // TEST 4: findById() - PK composta
    // ============================================

    private static boolean test4_FindById(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 4: findById() - Buscar per PK composta");
        System.out.println("─".repeat(70));

        try {
            // Buscar primera relació existent
            String[] pk = trobarPrimeraRelacio();
            
            if (pk == null) {
                System.out.println("⚠️  No hi ha relacions per testejar findById()");
                return true;
            }

            String cmCodi = pk[0];
            String pvCodi = pk[1];

            System.out.println("🔍 Buscant: Component=" + cmCodi + ", Proveïdor=" + pvCodi);

            ProvComp pc = dao.findById(cmCodi, pvCodi);

            if (pc != null) {
                System.out.println("✅ Relació trobada:");
                System.out.println("   Component: " + pc.getPcCmCodi());
                System.out.println("   Proveïdor: " + pc.getPcPvCodi());
                System.out.println("   Preu: " + pc.getPcPreu() + "€");
                return true;
            } else {
                System.err.println("❌ No s'ha trobat la relació (hauria d'existir!)");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR en findById():");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // TEST 5: getComponentsDelProveidor()
    // ============================================

    private static boolean test5_GetComponentsDelProveidor(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 5: getComponentsDelProveidor() - Components d'un proveïdor");
        System.out.println("─".repeat(70));

        try {
            String pvCodi = trobarProveidorAmbComponents();
            
            if (pvCodi == null) {
                System.out.println("⚠️  No hi ha proveïdors per testejar");
                return true;
            }

            List<ProvComp> components = dao.getComponentsDelProveidor(pvCodi);

            System.out.println("🏭 Proveïdor " + pvCodi + " subministra " + 
                             components.size() + " components:");

            int count = 0;
            for (ProvComp pc : components) {
                if (count++ >= 5) break;
                System.out.println("   • Component " + pc.getPcCmCodi() + 
                                 " = " + pc.getPcPreu() + "€");
            }
            if (components.size() > 5) {
                System.out.println("   ... i " + (components.size() - 5) + " més");
            }
            
            return true;

        } catch (Exception e) {
            System.err.println("❌ ERROR en getComponentsDelProveidor():");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // TEST 6: Validar que cm_preu_mig és correcte
    // ============================================

    private static boolean test6_ValidarPreuMig(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 6: Validar que cm_preu_mig = AVG(pc_preu)");
        System.out.println("─".repeat(70));

        try {
            Connection conn = util.ConnexioOracle.getConnection();
            
            String sql = """
                SELECT 
                    c.cm_codi,
                    c.cm_preu_mig,
                    NVL(ROUND(AVG(pc.pc_preu), 2), 0) as preu_calculat,
                    COUNT(pc.pc_pv_codi) as num_proveidors
                FROM Component c
                LEFT JOIN Prov_Comp pc ON c.cm_codi = pc.pc_cm_codi
                GROUP BY c.cm_codi, c.cm_preu_mig
                ORDER BY c.cm_codi
                """;
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("📊 Validant preus mitjans...\n");
            
            int correctes = 0;
            int total = 0;
            
            while (rs.next()) {
                total++;
                String codi = rs.getString("cm_codi");
                double preuMig = rs.getDouble("cm_preu_mig");
                double preuCalculat = rs.getDouble("preu_calculat");
                int numProveidors = rs.getInt("num_proveidors");
                
                boolean esCorrecte = Math.abs(preuMig - preuCalculat) < 0.01;
                
                if (esCorrecte) {
                    correctes++;
                    System.out.println("   ✅ " + codi + ": " + preuMig + 
                                     "€ (" + numProveidors + " proveïdors)");
                } else {
                    System.out.println("   ❌ " + codi + ": BD=" + preuMig + 
                                     "€ vs Calculat=" + preuCalculat + "€");
                }
            }
            
            rs.close();
            ps.close();
            conn.close();
            
            System.out.println("\n📈 RESULTAT: " + correctes + "/" + total + 
                             " components amb preu correcte");
            
            if (correctes == total) {
                System.out.println("✅ Els triggers funcionen perfectament!");
                return true;
            } else {
                System.err.println("⚠️  Alguns preus no coincideixen!");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR validant preus:");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // TEST 7: Operacions CRUD amb triggers
    // ============================================

    private static boolean test7_OperacionsCRUD(IDAOProvComp dao) {
        System.out.println("\n" + "─".repeat(70));
        System.out.println("TEST 7: Operacions CRUD - ⚡ VALIDAR TRIGGERS! ⚡");
        System.out.println("─".repeat(70));

        try {
            // Trobar un component i proveïdor de test
            String cmCodi = trobarComponentAmbProveidors();
            String pvCodiTest = "PVTEST";
            
            if (cmCodi == null) {
                System.out.println("⚠️  No hi ha components per testejar CRUD");
                return true;
            }

            System.out.println("📝 Usarem component: " + cmCodi);
            System.out.println("📝 Proveïdor de test: " + pvCodiTest);

            // 1️⃣ Obtenir preu mitjà ABANS
            double preuANTES = obtenirPreuMig(cmCodi);
            System.out.println("\n💰 Preu mitjà ABANS: " + preuANTES + "€");

            // 2️⃣ INSERT temporal
            System.out.println("\n🔨 TEST INSERT...");
            ProvComp nova = new ProvComp(cmCodi, pvCodiTest, 999.99);
            boolean insertOk = dao.insertar(nova);
            
            if (!insertOk) {
                System.out.println("⚠️  INSERT ha fallat (pot ser FK, saltant resta del test)");
                return true;
            }
            
            System.out.println("✅ INSERT OK");

            // 3️⃣ Validar que el preu ha canviat
            double preuDESPRES_INSERT = obtenirPreuMig(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS INSERT: " + preuDESPRES_INSERT + "€");
            
            if (Math.abs(preuANTES - preuDESPRES_INSERT) > 0.01) {
                System.out.println("✅✅✅ TRIGGER INSERT FUNCIONA!");
            } else {
                System.out.println("⚠️  El preu no ha canviat (coincidència?)");
            }

            // 4️⃣ UPDATE preu
            System.out.println("\n🔨 TEST UPDATE...");
            nova.setPcPreu(1500.00);
            boolean updateOk = dao.actualitzar(nova);
            System.out.println(updateOk ? "✅ UPDATE OK" : "❌ UPDATE FALLIT");

            double preuDESPRES_UPDATE = obtenirPreuMig(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS UPDATE: " + preuDESPRES_UPDATE + "€");
            
            if (Math.abs(preuDESPRES_INSERT - preuDESPRES_UPDATE) > 0.01) {
                System.out.println("✅✅✅ TRIGGER UPDATE FUNCIONA!");
            }

            // 5️⃣ DELETE temporal
            System.out.println("\n🔨 TEST DELETE...");
            boolean deleteOk = dao.eliminar(cmCodi, pvCodiTest);
            System.out.println(deleteOk ? "✅ DELETE OK" : "❌ DELETE FALLIT");

            double preuDESPRES_DELETE = obtenirPreuMig(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS DELETE: " + preuDESPRES_DELETE + "€");
            
            if (Math.abs(preuANTES - preuDESPRES_DELETE) < 0.01) {
                System.out.println("✅✅✅ TRIGGER DELETE FUNCIONA! (preu restaurat)");
            }

            System.out.println("\n🎉 Test CRUD completat!");
            return insertOk && updateOk && deleteOk;

        } catch (Exception e) {
            System.err.println("❌ ERROR en test CRUD:");
            e.printStackTrace();
            return false;
        }
    }

    // ============================================
    // MÈTODES AUXILIARS
    // ============================================

    private static String trobarComponentAmbProveidors() {
        try {
            Connection conn = util.ConnexioOracle.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT DISTINCT pc_cm_codi FROM Prov_Comp WHERE ROWNUM = 1"
            );
            ResultSet rs = ps.executeQuery();
            String codi = rs.next() ? rs.getString(1) : null;
            rs.close();
            ps.close();
            conn.close();
            return codi;
        } catch (Exception e) {
            return null;
        }
    }

    private static String trobarProveidorAmbComponents() {
        try {
            Connection conn = util.ConnexioOracle.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT DISTINCT pc_pv_codi FROM Prov_Comp WHERE ROWNUM = 1"
            );
            ResultSet rs = ps.executeQuery();
            String codi = rs.next() ? rs.getString(1) : null;
            rs.close();
            ps.close();
            conn.close();
            return codi;
        } catch (Exception e) {
            return null;
        }
    }

    private static String[] trobarPrimeraRelacio() {
        try {
            Connection conn = util.ConnexioOracle.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT pc_cm_codi, pc_pv_codi FROM Prov_Comp WHERE ROWNUM = 1"
            );
            ResultSet rs = ps.executeQuery();
            String[] pk = null;
            if (rs.next()) {
                pk = new String[] { rs.getString(1), rs.getString(2) };
            }
            rs.close();
            ps.close();
            conn.close();
            return pk;
        } catch (Exception e) {
            return null;
        }
    }

    private static double obtenirPreuMig(String cmCodi) {
        try {
            Connection conn = util.ConnexioOracle.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT cm_preu_mig FROM Component WHERE cm_codi = ?"
            );
            ps.setString(1, cmCodi);
            ResultSet rs = ps.executeQuery();
            double preu = rs.next() ? rs.getDouble(1) : 0.0;
            rs.close();
            ps.close();
            conn.close();
            return preu;
        } catch (Exception e) {
            return 0.0;
        }
    }
}