package dao.reports;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.DAOComponent;
import dao.DAOItem;
import dao.DAOProdItem;
import dao.DAOProducte;
import model.Component;
import model.Item;
import model.ProdItem;
import model.Producte;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Generador de reports PDF per Bill of Materials (BOM) amb JasperReports
 * 
 * Responsabilitat única: Generar documents PDF amb la informació del BOM
 * Utilitza JasperReports 6.21.3 per generar PDFs professionals
 * 
 * Característiques:
 * - Generació de BOM complet amb estructura jeràrquica
 * - Càlcul de cost total (components x quantitat)
 * - Plantilla visual editable (.jrxml)
 * - Separació entre disseny (XML) i lògica (Java)
 * - VERSIÓ 2.1: Suport per mostrar foto del producte
 * 
 * @author DomenechObiolAlbert
 * @version 2.1 (afegit suport per fotos)
 */
public class BOMReportJasper {
    
    private static final String TEMPLATE_PATH = "/reports/BOM_Report.jrxml";
    
    private DAOProdItem daoProdItem;
    private DAOProducte daoProducte;
    private DAOItem daoItem;
    private DAOComponent daoComponent;
    
    /**
     * Constructor per defecte
     * Inicialitza els DAOs necessaris
     */
    public BOMReportJasper() {
        this.daoProdItem = new DAOProdItem();
        this.daoProducte = new DAOProducte();
        this.daoItem = new DAOItem();
        this.daoComponent = new DAOComponent();
    }
    
    /**
     * Genera un PDF amb el BOM complet d'un producte i l'escriu a un OutputStream
     * 
     * VERSIÓ 2.1: Afegit paràmetre uploadPath per localitzar fotos
     * 
     * @param prCodi Codi del producte
     * @param outputStream Stream on escriure el PDF
     * @param uploadPath Ruta absoluta a la carpeta uploads (pot ser null)
     * @throws Exception si hi ha error generant el PDF
     */
    public void generarBOMPDF(String prCodi, OutputStream outputStream, String uploadPath) throws Exception {
        System.out.println("🔍 [BOMReportJasper] Inici generació per producte: " + prCodi);
        
        validarParametres(prCodi, outputStream);
        System.out.println("✅ [BOMReportJasper] Paràmetres validats");
        
        // 1. Obtenir dades del producte
        Producte producte = daoProducte.findById(prCodi);
        System.out.println("🔍 [BOMReportJasper] Producte trobat: " + (producte != null ? producte.getPrCodi() : "NULL"));
        if (producte == null) {
            throw new IllegalArgumentException("El producte amb codi " + prCodi + " no existeix");
        }
        
        // 2. Obtenir components
        List<ProdItem> items = daoProdItem.getItemsDelProducte(prCodi);
        System.out.println("🔍 [BOMReportJasper] Components trobats: " + (items != null ? items.size() : "NULL"));
        if (items == null || items.isEmpty()) {
            System.err.println("❌ [BOMReportJasper] ERROR: items és " + (items == null ? "NULL" : "EMPTY"));
            throw new IllegalStateException("El producte " + prCodi + " no té components");
        }
        
        // 3. Preparar dades per JasperReports (amb ruta de foto)
        System.out.println("🔍 [BOMReportJasper] Preparant paràmetres...");
        Map<String, Object> parameters = prepararParametres(producte, items, uploadPath);
        System.out.println("✅ [BOMReportJasper] Paràmetres preparats: " + parameters.size());
        
        System.out.println("🔍 [BOMReportJasper] Preparant DTOs...");
        List<BOMComponentDTO> componentDTOs = prepararComponentDTOs(items);
        System.out.println("✅ [BOMReportJasper] DTOs preparats: " + componentDTOs.size());
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(componentDTOs);
        
        // 4. Compilar plantilla
        System.out.println("🔍 [BOMReportJasper] Carregant plantilla des de: " + TEMPLATE_PATH);
        InputStream templateStream = getClass().getResourceAsStream(TEMPLATE_PATH);
        if (templateStream == null) {
            System.err.println("❌ [BOMReportJasper] ERROR: No s'ha trobat la plantilla!");
            throw new IllegalStateException("No s'ha trobat la plantilla: " + TEMPLATE_PATH);
        }
        System.out.println("✅ [BOMReportJasper] Plantilla carregada");
        
        System.out.println("🔍 [BOMReportJasper] Compilant plantilla...");
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
        System.out.println("✅ [BOMReportJasper] Plantilla compilada");
        
        // 5. Generar el report
        System.out.println("🔍 [BOMReportJasper] Generant report...");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        System.out.println("✅ [BOMReportJasper] Report generat");
        
        // 6. Exportar a PDF
        System.out.println("🔍 [BOMReportJasper] Exportant a PDF...");
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        System.out.println("✅ [BOMReportJasper] PDF exportat correctament");
    }
    
    /**
     * Mètode legacy sense uploadPath (per compatibilitat)
     * @deprecated Usar generarBOMPDF(String, OutputStream, String)
     */
    @Deprecated
    public void generarBOMPDF(String prCodi, OutputStream outputStream) throws Exception {
        generarBOMPDF(prCodi, outputStream, null);
    }
    
    /**
     * Valida els paràmetres d'entrada
     */
    private void validarParametres(String prCodi, OutputStream outputStream) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            throw new IllegalArgumentException("El codi del producte no pot ser null o buit");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("L'OutputStream no pot ser null");
        }
    }
    
    /**
     * Prepara els paràmetres per la plantilla JasperReports
     * 
     * VERSIÓ 2.1: Afegit PRODUCT_IMAGE_PATH per mostrar foto
     */
    private Map<String, Object> prepararParametres(Producte producte, List<ProdItem> items, String uploadPath) {
        Map<String, Object> parameters = new HashMap<>();
        
        parameters.put("PRODUCT_CODE", producte.getPrCodi());
        parameters.put("PRODUCT_NAME", producte.getItNom());
        parameters.put("PRODUCT_DESC", producte.getItDesc());
        parameters.put("PRODUCT_STOCK", producte.getItStock());
        parameters.put("GENERATION_DATE", new Date());
        parameters.put("TOTAL_COST", calcularCostTotal(items));
        
        // VERSIÓ 2.1: Afegir ruta de la foto si existeix
        String fotoPath = obtenirRutaFoto(producte, uploadPath);
        parameters.put("PRODUCT_IMAGE_PATH", fotoPath);
        System.out.println("🖼️ [BOMReportJasper] Ruta foto: " + (fotoPath != null ? fotoPath : "cap foto"));
        
        return parameters;
    }
    
    /**
     * Obté la ruta absoluta de la foto del producte
     * 
     * @param producte Producte amb possible foto
     * @param uploadPath Ruta base de uploads
     * @return Ruta absoluta de la foto o null si no existeix
     */
    private String obtenirRutaFoto(Producte producte, String uploadPath) {
        // Si no tenim ruta d'uploads o el producte no té foto, retornar null
        if (uploadPath == null || uploadPath.isEmpty()) {
            System.out.println("🖼️ [BOMReportJasper] uploadPath és null o buit");
            return null;
        }
        
        String nomFoto = producte.getItFoto();
        if (nomFoto == null || nomFoto.isEmpty()) {
            System.out.println("🖼️ [BOMReportJasper] Producte sense foto assignada");
            return null;
        }
        
        // Construir ruta completa
        String rutaCompleta = uploadPath + File.separator + "productes" + File.separator + nomFoto;
        
        // Verificar que el fitxer existeix
        File fotoFile = new File(rutaCompleta);
        if (!fotoFile.exists()) {
            System.out.println("⚠️ [BOMReportJasper] Fitxer no trobat: " + rutaCompleta);
            return null;
        }
        
        System.out.println("✅ [BOMReportJasper] Foto trobada: " + rutaCompleta);
        return rutaCompleta;
    }
    
    /**
     * Prepara la llista de DTOs per alimentar el DataSource de JasperReports
     */
    private List<BOMComponentDTO> prepararComponentDTOs(List<ProdItem> items) {
        List<BOMComponentDTO> dtos = new ArrayList<>();
        
        for (ProdItem item : items) {
            Item itemInfo = daoItem.findById(item.getPiItCodi());
            if (itemInfo == null) continue;
            
            String tipusItem = itemInfo.getItTipus();
            double preuUnitat = 0.0;
            String unitatMesura = "ud";
            
            // Obtenir preu segons tipus (Component o Producte)
            if ("C".equals(tipusItem)) {
                Component component = daoComponent.findById(item.getPiItCodi());
                if (component != null) {
                    preuUnitat = component.getCmPreuMig() != null ? component.getCmPreuMig() : 0.0;
                    unitatMesura = component.getCmUmCodi() != null ? component.getCmUmCodi() : "ud";
                }
            } else if ("P".equals(tipusItem)) {
                // Per subproductes, calcular cost recursivament
                preuUnitat = daoItem.calcularCostItem(item.getPiItCodi());
                unitatMesura = "conj";
            }
            
            double costTotal = preuUnitat * item.getQuantitat();
            
            BOMComponentDTO dto = new BOMComponentDTO(
                item.getPiItCodi(),
                itemInfo.getItNom(),
                item.getQuantitat(),
                unitatMesura,
                preuUnitat,
                costTotal
            );
            
            dtos.add(dto);
        }
        
        return dtos;
    }
    
    /**
     * Calcula el cost total de tots els components
     */
    private double calcularCostTotal(List<ProdItem> items) {
        double total = 0.0;
        
        for (ProdItem item : items) {
            Item itemInfo = daoItem.findById(item.getPiItCodi());
            if (itemInfo == null) continue;
            
            String tipusItem = itemInfo.getItTipus();
            double preuUnitat = 0.0;
            
            if ("C".equals(tipusItem)) {
                Component component = daoComponent.findById(item.getPiItCodi());
                if (component != null && component.getCmPreuMig() != null) {
                    preuUnitat = component.getCmPreuMig();
                }
            } else if ("P".equals(tipusItem)) {
                preuUnitat = daoItem.calcularCostItem(item.getPiItCodi());
            }
            
            total += preuUnitat * item.getQuantitat();
        }
        
        return total;
    }
}