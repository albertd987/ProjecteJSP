package dao.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import dao.DAOComponent;
import dao.DAOItem;
import dao.DAOProdItem;
import dao.DAOProducte;
import model.Component;
import model.Item;
import model.ProdItem;
import model.Producte;

/**
 * Generador de reports PDF per Bill of Materials (BOM)
 * 
 * Responsabilitat unica: Generar documents PDF amb la informacio del BOM
 * Utilitza iText 5.5.13.3 per generar PDFs professionals
 * 
 * Caracteristiques:
 * - Generacio de BOM complet amb estructura jerarquica
 * - Calcul de cost total (components x quantitat)
 * - Capçalera i peu de pagina professionals
 * - Taula amb estils corporatius
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class BOMReportPDF {
    
    private static final String COMPANY_NAME = "Tallers Manolo";
    private static final String REPORT_TITLE = "Bill of Materials (BOM)";
    
    private DAOProdItem daoProdItem;
    private DAOProducte daoProducte;
    private DAOItem daoItem;
    private DAOComponent daoComponent;
    
    private Font fontTitle;
    private Font fontSubtitle;
    private Font fontHeader;
    private Font fontNormal;
    private Font fontSmall;
    
    public BOMReportPDF() {
        this.daoProdItem = new DAOProdItem();
        this.daoProducte = new DAOProducte();
        this.daoItem = new DAOItem();
        this.daoComponent = new DAOComponent();
        
        initializeFonts();
    }
    
    /**
     * Inicialitza les fonts del document
     */
    private void initializeFonts() {
        this.fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        this.fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        this.fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        this.fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        this.fontSmall = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
    }
    
    /**
     * Genera un PDF amb el BOM complet d'un producte
     * 
     * @param prCodi Codi del producte
     * @param outputPath Path on guardar el PDF
     * @throws DocumentException si hi ha error generant el PDF
     * @throws IOException si hi ha error d'escriptura
     */
    public void generarBOMPDF(String prCodi, String outputPath) throws DocumentException, IOException {
        validarParametres(prCodi, outputPath);
        
        Producte producte = daoProducte.findById(prCodi);
        if (producte == null) {
            throw new IllegalArgumentException("El producte amb codi " + prCodi + " no existeix");
        }
        
        List<ProdItem> items = daoProdItem.getItemsDelProducte(prCodi);
        if (items.isEmpty()) {
            throw new IllegalStateException("El producte " + prCodi + " no te components");
        }
        
        Document document = new Document(PageSize.A4);
        document.setMargins(50, 50, 50, 50);
        
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);
            
            document.open();
            
            afegirCapçalera(document, producte);
            afegirSeparador(document);
            afegirTaulaBOM(document, items);
            afegirResum(document, items);
            
        } finally {
            document.close();
        }
    }
    
    /**
     * Valida els parametres d'entrada
     */
    private void validarParametres(String prCodi, String outputPath) {
        if (prCodi == null || prCodi.trim().isEmpty()) {
            throw new IllegalArgumentException("Codi de producte no pot ser null o buit");
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Path de sortida no pot ser null o buit");
        }
    }
    
    /**
     * Afegeix la capçalera del document amb informacio del producte
     */
    private void afegirCapçalera(Document document, Producte producte) throws DocumentException {
        Paragraph title = new Paragraph(REPORT_TITLE, fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        
        Paragraph company = new Paragraph(COMPANY_NAME, fontSubtitle);
        company.setAlignment(Element.ALIGN_CENTER);
        company.setSpacingAfter(20);
        document.add(company);
        
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10);
        infoTable.setSpacingAfter(15);
        
        afegirFilaInfo(infoTable, "Codi Producte:", producte.getPrCodi());
        afegirFilaInfo(infoTable, "Nom:", producte.getItNom());
        afegirFilaInfo(infoTable, "Descripcio:", producte.getItDesc() != null ? producte.getItDesc() : "-");
        afegirFilaInfo(infoTable, "Stock Actual:", String.valueOf(producte.getItStock()));
        afegirFilaInfo(infoTable, "Data Generacio:", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        
        document.add(infoTable);
    }
    
    /**
     * Afegeix una fila d'informacio a la taula
     */
    private void afegirFilaInfo(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontNormal));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPaddingBottom(5);
        table.addCell(cellLabel);
        
        PdfPCell cellValue = new PdfPCell(new Phrase(value, fontNormal));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPaddingBottom(5);
        table.addCell(cellValue);
    }
    
    /**
     * Afegeix un separador visual
     */
    private void afegirSeparador(Document document) throws DocumentException {
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);
    }
    
    /**
     * Afegeix la taula principal amb el BOM
     */
    private void afegirTaulaBOM(Document document, List<ProdItem> items) throws DocumentException {
        Paragraph subtitle = new Paragraph("Components del Producte", fontSubtitle);
        subtitle.setSpacingBefore(10);
        subtitle.setSpacingAfter(10);
        document.add(subtitle);
        
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        
        try {
            table.setWidths(new float[]{2.5f, 3f, 1.5f, 1.5f, 1.5f, 2f});
        } catch (DocumentException e) {
            throw new DocumentException("Error configurant amplades de columnes", e);
        }
        
        afegirCapçaleraTaula(table);
        afegirFilesBOM(table, items);
        
        document.add(table);
    }
    
    /**
     * Afegeix la capçalera de la taula BOM
     */
    private void afegirCapçaleraTaula(PdfPTable table) {
        String[] headers = {"Codi Component", "Nom", "Quantitat", "Unitat", "Preu Unit.", "Cost Total"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
            cell.setBackgroundColor(new BaseColor(41, 128, 185));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            cell.setBorderColor(BaseColor.WHITE);
            table.addCell(cell);
        }
    }
    
    /**
     * Afegeix les files amb els components del BOM
     */
    private void afegirFilesBOM(PdfPTable table, List<ProdItem> items) {
        for (ProdItem item : items) {
            Item itemInfo = daoItem.findById(item.getPiItCodi());
            
            if (itemInfo == null) {
                continue;
            }
            
            String tipusItem = itemInfo.getItTipus();
            double preuUnitat = 0.0;
            String unitatMesura = "ud";
            
            if ("C".equals(tipusItem)) {
                Component component = daoComponent.findById(item.getPiItCodi());
                if (component != null) {
                    preuUnitat = component.getCmPreuMig() != null ? component.getCmPreuMig() : 0.0;
                    unitatMesura = component.getCmUmCodi() != null ? component.getCmUmCodi() : "ud";
                }
            } else if ("P".equals(tipusItem)) {
                preuUnitat = daoProducte.calcularPreuTotal(item.getPiItCodi());
                unitatMesura = "ud";
            }
            
            double costTotal = item.getQuantitat() * preuUnitat;
            
            afegirCeldaTaula(table, item.getPiItCodi(), Element.ALIGN_LEFT);
            afegirCeldaTaula(table, itemInfo.getItNom(), Element.ALIGN_LEFT);
            afegirCeldaTaula(table, String.valueOf(item.getQuantitat()), Element.ALIGN_CENTER);
            afegirCeldaTaula(table, unitatMesura, Element.ALIGN_CENTER);
            afegirCeldaTaula(table, String.format("%.2f EUR", preuUnitat), Element.ALIGN_RIGHT);
            afegirCeldaTaula(table, String.format("%.2f EUR", costTotal), Element.ALIGN_RIGHT);
        }
    }
    
    /**
     * Afegeix una cel·la a la taula amb estil consistent
     */
    private void afegirCeldaTaula(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, fontNormal));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
    
    /**
     * Afegeix el resum final amb totals
     */
    private void afegirResum(Document document, List<ProdItem> items) throws DocumentException {
        document.add(Chunk.NEWLINE);
        
        double costTotal = calcularCostTotal(items);
        int totalComponents = items.size();
        int quantitatTotal = items.stream().mapToInt(ProdItem::getQuantitat).sum();
        
        PdfPTable resumTable = new PdfPTable(2);
        resumTable.setWidthPercentage(50);
        resumTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumTable.setSpacingBefore(15);
        
        afegirFilaResum(resumTable, "Total Components:", String.valueOf(totalComponents));
        afegirFilaResum(resumTable, "Quantitat Total:", String.valueOf(quantitatTotal));
        
        PdfPCell labelCell = new PdfPCell(new Phrase("COST TOTAL:", fontSubtitle));
        labelCell.setBorder(Rectangle.TOP);
        labelCell.setBorderWidth(2);
        labelCell.setPadding(8);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumTable.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(String.format("%.2f EUR", costTotal), fontSubtitle));
        valueCell.setBorder(Rectangle.TOP);
        valueCell.setBorderWidth(2);
        valueCell.setPadding(8);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumTable.addCell(valueCell);
        
        document.add(resumTable);
    }
    
    /**
     * Afegeix una fila al resum
     */
    private void afegirFilaResum(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontNormal));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(5);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellLabel);
        
        PdfPCell cellValue = new PdfPCell(new Phrase(value, fontNormal));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPadding(5);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);
    }
    
    /**
     * Calcula el cost total del BOM
     */
    private double calcularCostTotal(List<ProdItem> items) {
        double total = 0.0;
        
        for (ProdItem item : items) {
            Item itemInfo = daoItem.findById(item.getPiItCodi());
            
            if (itemInfo == null) {
                continue;
            }
            
            String tipusItem = itemInfo.getItTipus();
            double preuUnitat = 0.0;
            
            if ("C".equals(tipusItem)) {
                Component component = daoComponent.findById(item.getPiItCodi());
                if (component != null && component.getCmPreuMig() != null) {
                    preuUnitat = component.getCmPreuMig();
                }
            } else if ("P".equals(tipusItem)) {
                preuUnitat = daoProducte.calcularPreuTotal(item.getPiItCodi());
            }
            
            total += item.getQuantitat() * preuUnitat;
        }
        
        return total;
    }
    
    /**
     * Classe interna per gestionar capçalera i peu de pagina
     */
    private class HeaderFooter extends PdfPageEventHelper {
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footer = new PdfPTable(1);
            
            try {
                footer.setWidths(new int[]{1});
                footer.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                footer.setLockedWidth(true);
                footer.getDefaultCell().setBorder(Rectangle.TOP);
                footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
                
                String footerText = String.format("Pagina %d | %s | Generat: %s", 
                    writer.getPageNumber(), 
                    COMPANY_NAME,
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                    
                PdfPCell cell = new PdfPCell(new Phrase(footerText, fontSmall));
                cell.setBorder(Rectangle.TOP);
                cell.setBorderColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingTop(5);
                footer.addCell(cell);
                
                footer.writeSelectedRows(0, -1, 
                    document.leftMargin(), 
                    document.bottomMargin(), 
                    writer.getDirectContent());
                    
            } catch (DocumentException e) {
                throw new RuntimeException("Error afegint peu de pagina", e);
            }
        }
    }
}