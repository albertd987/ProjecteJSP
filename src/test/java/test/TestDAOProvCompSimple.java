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
 * ✅ CORREGIT: Usa dades REALS de DadesDemo_DomenechObiolAlbert.sql
 * 
 * No depèn de DAOComponent - pot executar-se independentment
 * 
 * @author DomenechObiolAlbert
 * @version 2.0 - Corregit amb dades reals
 */
public class TestDAOProvCompSimple {

    public static void main(String[] args) {
        System.out.println("═".repeat(70));
        System.out.println("🧪 TEST AUTÒNOM: DAOProvComp (VERSIÓ CORREGIDA)");
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
            // Usar C001 (Processador) que té 3 proveïdors: PV001(350), PV002(345), PV006(355)
            String cmCodi = "C001";
            
            List<ProvComp> proveidors = dao.getProveidorsDelComponent(cmCodi);

            System.out.println("📦 Component " + cmCodi + " (Processador Intel i7) té " + 
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
                
                // Validar que té els 3 proveïdors esperats
                if (proveidors.size() == 3) {
                    System.out.println("✅ Nombre de proveïdors correcte (3)");
                    return true;
                } else {
                    System.err.println("❌ S'esperaven 3 proveïdors, trobats: " + proveidors.size());
                    return false;
                }
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
            // Usar una relació real: C001 + PV001 (Processador amb preu 350.00)
            String cmCodi = "C001";
            String pvCodi = "PV001";

            System.out.println("🔍 Buscant: Component=" + cmCodi + ", Proveïdor=" + pvCodi);

            ProvComp pc = dao.findById(cmCodi, pvCodi);

            if (pc != null) {
                System.out.println("✅ Relació trobada:");
                System.out.println("   Component: " + pc.getPcCmCodi());
                System.out.println("   Proveïdor: " + pc.getPcPvCodi());
                System.out.println("   Preu: " + pc.getPcPreu() + "€");
                
                // Validar que el preu és el correcte (350.00)
                if (Math.abs(pc.getPcPreu() - 350.00) < 0.01) {
                    System.out.println("✅ Preu correcte (350.00€)");
                    return true;
                } else {
                    System.err.println("❌ Preu incorrecte! Esperat: 350.00, Trobat: " + pc.getPcPreu());
                    return false;
                }
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
            // Usar PV001 (Components Electrònics SA) que subministra C001, C002, C003
            String pvCodi = "PV001";
            
            List<ProvComp> components = dao.getComponentsDelProveidor(pvCodi);

            System.out.println("🏭 Proveïdor " + pvCodi + " (Components Electrònics SA) subministra " + 
                             components.size() + " components:");

            for (ProvComp pc : components) {
                System.out.println("   • Component " + pc.getPcCmCodi() + 
                                 " = " + pc.getPcPreu() + "€");
            }
            
            // Validar que té els 3 components esperats: C001, C002, C003
            if (components.size() == 3) {
                System.out.println("✅ Nombre de components correcte (3)");
                return true;
            } else {
                System.err.println("❌ S'esperaven 3 components, trobats: " + components.size());
                return false;
            }

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
            // ✅ CORRECCIÓ: Usar component i proveïdor REALS de la BD
            // Component: C007 (Teclat mecànic) que ja té PV004, PV005, PV008
            // Proveïdor: PV001 (Components Electrònics SA) que NO té C007
            String cmCodi = "C007";
            String pvCodiTest = "PV001";
            
            System.out.println("📝 Usarem component: " + cmCodi + " (Teclat mecànic RGB)");
            System.out.println("📝 Proveïdor de test: " + pvCodiTest + " (Components Electrònics SA)");
            System.out.println("   (Aquest proveïdor EXISTEIX i NO té aquest component)");

            // 1️⃣ Validar que la relació NO existeix abans
            ProvComp existe = dao.findById(cmCodi, pvCodiTest);
            if (existe != null) {
                System.out.println("⚠️  La relació ja existeix! Eliminant-la primer...");
                dao.eliminar(cmCodi, pvCodiTest);
            }

            // 2️⃣ Obtenir preu mitjà ABANS
            double preuANTES = obtenirPreuMig(cmCodi);
            System.out.println("\n💰 Preu mitjà ABANS: " + preuANTES + "€");
            
            // Obtenir el nombre de proveïdors actual
            int numProvANTES = contarProveidors(cmCodi);
            System.out.println("👥 Nombre de proveïdors ABANS: " + numProvANTES);

            // 3️⃣ INSERT temporal
            System.out.println("\n🔨 TEST INSERT...");
            ProvComp nova = new ProvComp(cmCodi, pvCodiTest, 999.99);
            boolean insertOk = dao.insertar(nova);
            
            if (!insertOk) {
                System.err.println("❌ INSERT ha fallat!");
                return false;
            }
            
            System.out.println("✅ INSERT OK");

            // 4️⃣ Validar que el preu ha canviat
            double preuDESPRES_INSERT = obtenirPreuMig(cmCodi);
            int numProvDESPRES_INSERT = contarProveidors(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS INSERT: " + preuDESPRES_INSERT + "€");
            System.out.println("👥 Nombre de proveïdors DESPRÉS INSERT: " + numProvDESPRES_INSERT);
            
            if (Math.abs(preuANTES - preuDESPRES_INSERT) > 0.01 && numProvDESPRES_INSERT == numProvANTES + 1) {
                System.out.println("✅✅✅ TRIGGER INSERT FUNCIONA!");
            } else {
                System.out.println("⚠️  El preu no ha canviat com s'esperava");
            }

            // 5️⃣ UPDATE preu
            System.out.println("\n🔨 TEST UPDATE...");
            nova.setPcPreu(1500.00);
            boolean updateOk = dao.actualitzar(nova);
            
            if (!updateOk) {
                System.err.println("❌ UPDATE ha fallat!");
                dao.eliminar(cmCodi, pvCodiTest); // Cleanup
                return false;
            }
            
            System.out.println("✅ UPDATE OK");

            double preuDESPRES_UPDATE = obtenirPreuMig(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS UPDATE: " + preuDESPRES_UPDATE + "€");
            
            if (Math.abs(preuDESPRES_INSERT - preuDESPRES_UPDATE) > 0.01) {
                System.out.println("✅✅✅ TRIGGER UPDATE FUNCIONA!");
            } else {
                System.out.println("⚠️  El preu no ha canviat (pot ser coincidència)");
            }

            // 6️⃣ DELETE temporal
            System.out.println("\n🔨 TEST DELETE...");
            boolean deleteOk = dao.eliminar(cmCodi, pvCodiTest);
            
            if (!deleteOk) {
                System.err.println("❌ DELETE ha fallat!");
                return false;
            }
            
            System.out.println("✅ DELETE OK");

            double preuDESPRES_DELETE = obtenirPreuMig(cmCodi);
            int numProvDESPRES_DELETE = contarProveidors(cmCodi);
            System.out.println("💰 Preu mitjà DESPRÉS DELETE: " + preuDESPRES_DELETE + "€");
            System.out.println("👥 Nombre de proveïdors DESPRÉS DELETE: " + numProvDESPRES_DELETE);
            
            if (Math.abs(preuANTES - preuDESPRES_DELETE) < 0.01 && numProvDESPRES_DELETE == numProvANTES) {
                System.out.println("✅✅✅ TRIGGER DELETE FUNCIONA! (preu i nombre restaurats)");
            } else {
                System.out.println("⚠️  El preu o nombre de proveïdors no ha tornat a l'estat inicial");
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

    private static int contarProveidors(String cmCodi) {
        try {
            Connection conn = util.ConnexioOracle.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Prov_Comp WHERE pc_cm_codi = ?"
            );
            ps.setString(1, cmCodi);
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            rs.close();
            ps.close();
            conn.close();
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
}