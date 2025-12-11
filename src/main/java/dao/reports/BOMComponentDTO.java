package dao.reports;

/**
 * DTO (Data Transfer Object) per als components del BOM
 * 
 * Aquesta classe serveix per passar les dades dels components
 * a JasperReports de forma estructurada.
 * 
 * Els noms dels atributs han de coincidir amb els FIELDS del JRXML.
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public class BOMComponentDTO {
    
    private String piItCodi;       // Codi del component/item
    private String componentName;   // Nom del component
    private Integer quantitat;      // Quantitat necessària
    private String unitatMesura;    // Unitat de mesura (ud, kg, etc.)
    private Double preuUnitat;      // Preu per unitat
    private Double costTotal;       // Cost total (preu x quantitat)
    
    /**
     * Constructor buit
     */
    public BOMComponentDTO() {
    }
    
    /**
     * Constructor complet
     * 
     * @param piItCodi Codi del component
     * @param componentName Nom del component
     * @param quantitat Quantitat necessària
     * @param unitatMesura Unitat de mesura
     * @param preuUnitat Preu unitari
     * @param costTotal Cost total calculat
     */
    public BOMComponentDTO(String piItCodi, String componentName, Integer quantitat,
                           String unitatMesura, Double preuUnitat, Double costTotal) {
        this.piItCodi = piItCodi;
        this.componentName = componentName;
        this.quantitat = quantitat;
        this.unitatMesura = unitatMesura;
        this.preuUnitat = preuUnitat;
        this.costTotal = costTotal;
    }
    
    // ==========================================
    // GETTERS I SETTERS
    // ==========================================
    
    public String getPiItCodi() {
        return piItCodi;
    }
    
    public void setPiItCodi(String piItCodi) {
        this.piItCodi = piItCodi;
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
    
    public Integer getQuantitat() {
        return quantitat;
    }
    
    public void setQuantitat(Integer quantitat) {
        this.quantitat = quantitat;
    }
    
    public String getUnitatMesura() {
        return unitatMesura;
    }
    
    public void setUnitatMesura(String unitatMesura) {
        this.unitatMesura = unitatMesura;
    }
    
    public Double getPreuUnitat() {
        return preuUnitat;
    }
    
    public void setPreuUnitat(Double preuUnitat) {
        this.preuUnitat = preuUnitat;
    }

    public Double getCostTotal() {
        return costTotal;
    }
    
    public void setCostTotal(Double costTotal) {
        this.costTotal = costTotal;
    }
    
    // ==========================================
    // TOSTRING
    // ==========================================
    
    @Override
    public String toString() {
        return "BOMComponentDTO{" +
                "piItCodi='" + piItCodi + '\'' +
                ", componentName='" + componentName + '\'' +
                ", quantitat=" + quantitat +
                ", unitatMesura='" + unitatMesura + '\'' +
                ", preuUnitat=" + preuUnitat +
                ", costTotal=" + costTotal +
                '}';
    }
}