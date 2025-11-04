package dao.reports;


import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

/**
 * Test unitari per BOMReportPDF
 * 
 * Els PDFs es guarden a /home/pdfs/bom_reports/
 * 
 * Tests implementats:
 * 1. Generacio PDF per producte existent
 * 2. Validacio d'errors per producte inexistent
 * 3. Validacio d'errors per producte sense components
 * 4. Validacio de parametres invalids
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class TestBOMReportPDF {

    private static final String OUTPUT_DIR = "/home/pdfs/bom_reports/";
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("   TEST UNITARI: BOMReportPDF");
        System.out.println("=".repeat(80));
        System.out.println();

        crearDirectoriSortida();

        BOMReportPDF reportGenerator = new BOMReportPDF();

        testGenerarPDFProducteExistent(reportGenerator);
        testGenerarPDFProducteInexistent(reportGenerator);
        testGenerarPDFProducteSenseComponents(reportGenerator);
        testValidacioParametres(reportGenerator);
        testGenerarPDFsMultiples(reportGenerator);

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("   TOTS ELS TESTS COMPLETATS");
        System.out.println("   PDFs generats a: " + OUTPUT_DIR);
        System.out.println("=".repeat(80));
    }

    /**
     * Crea el directori de sortida si no existeix
     */
    private static void crearDirectoriSortida() {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Directori creat: " + OUTPUT_DIR);
            }
        }
        System.out.println();
    }

    /**
     * TEST 1: Generar PDF per un producte existent
     */
    private static void testGenerarPDFProducteExistent(BOMReportPDF generator) {
        System.out.println("TEST 1: Generar PDF per producte existent");
        System.out.println("-".repeat(80));

        String prCodi = "P001";
        String outputPath = OUTPUT_DIR + "BOM_" + prCodi + ".pdf";

        try {
            generator.generarBOMPDF(prCodi, outputPath);
            
            File pdfFile = new File(outputPath);
            if (pdfFile.exists() && pdfFile.length() > 0) {
                System.out.println("OK: PDF generat correctament");
                System.out.println("   Fitxer: " + outputPath);
                System.out.println("   Mida: " + pdfFile.length() + " bytes");
            } else {
                System.err.println("ERROR: PDF no generat o buit");
            }
            
        } catch (DocumentException | IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * TEST 2: Intentar generar PDF per producte inexistent
     */
    private static void testGenerarPDFProducteInexistent(BOMReportPDF generator) {
        System.out.println("TEST 2: Generar PDF per producte inexistent");
        System.out.println("-".repeat(80));

        String prCodi = "PXXXX";
        String outputPath = OUTPUT_DIR + "BOM_" + prCodi + ".pdf";

        try {
            generator.generarBOMPDF(prCodi, outputPath);
            System.err.println("ERROR: Hauria de llançar excepcio per producte inexistent");
            
        } catch (IllegalArgumentException e) {
            System.out.println("OK: Excepcio controlada per producte inexistent");
            System.out.println("   Missatge: " + e.getMessage());
            
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * TEST 3: Intentar generar PDF per producte sense components
     */
    private static void testGenerarPDFProducteSenseComponents(BOMReportPDF generator) {
        System.out.println("TEST 3: Generar PDF per producte sense components");
        System.out.println("-".repeat(80));

        String prCodi = "P003";
        String outputPath = OUTPUT_DIR + "BOM_" + prCodi + ".pdf";

        try {
            generator.generarBOMPDF(prCodi, outputPath);
            
            File pdfFile = new File(outputPath);
            if (pdfFile.exists()) {
                System.out.println("OK: PDF generat (producte pot tenir o no components)");
            } else {
                System.out.println("INFO: Producte sense components - PDF no generat");
            }
            
        } catch (IllegalStateException e) {
            System.out.println("OK: Excepcio controlada per producte sense components");
            System.out.println("   Missatge: " + e.getMessage());
            
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * TEST 4: Validacio de parametres invalids
     */
    private static void testValidacioParametres(BOMReportPDF generator) {
        System.out.println("TEST 4: Validacio de parametres");
        System.out.println("-".repeat(80));

        int errors = 0;

        try {
            generator.generarBOMPDF(null, OUTPUT_DIR + "test.pdf");
            System.err.println("ERROR: Hauria de llançar excepcio per prCodi null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: Validacio prCodi null");
            errors++;
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        try {
            generator.generarBOMPDF("", OUTPUT_DIR + "test.pdf");
            System.err.println("ERROR: Hauria de llançar excepcio per prCodi buit");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: Validacio prCodi buit");
            errors++;
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        try {
            generator.generarBOMPDF("P001", null);
            System.err.println("ERROR: Hauria de llançar excepcio per outputPath null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: Validacio outputPath null");
            errors++;
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        try {
            generator.generarBOMPDF("P001", "");
            System.err.println("ERROR: Hauria de llançar excepcio per outputPath buit");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: Validacio outputPath buit");
            errors++;
        } catch (Exception e) {
            System.err.println("ERROR: Excepcio inesperada: " + e.getMessage());
        }

        System.out.println("\nValidacions correctes: " + errors + "/4");
        System.out.println();
    }

    /**
     * TEST 5: Generar PDFs multiples
     */
    private static void testGenerarPDFsMultiples(BOMReportPDF generator) {
        System.out.println("TEST 5: Generar PDFs multiples");
        System.out.println("-".repeat(80));

        String[] productes = {"P001", "P002", "P001A", "P001B", "P002A"};
        int success = 0;
        int failures = 0;

        for (String prCodi : productes) {
            String outputPath = OUTPUT_DIR + "BOM_" + prCodi + ".pdf";
            
            try {
                generator.generarBOMPDF(prCodi, outputPath);
                
                File pdfFile = new File(outputPath);
                if (pdfFile.exists() && pdfFile.length() > 0) {
                    System.out.println("   OK: " + prCodi + " (" + pdfFile.length() + " bytes)");
                    success++;
                } else {
                    System.err.println("   ERROR: " + prCodi + " - fitxer no generat");
                    failures++;
                }
                
            } catch (Exception e) {
                System.out.println("   INFO: " + prCodi + " - " + e.getMessage());
                failures++;
            }
        }

        System.out.println("\nResultats:");
        System.out.println("   Generats correctament: " + success);
        System.out.println("   Errors o no aplicables: " + failures);
        System.out.println();
    }
}