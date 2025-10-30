package test;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe simple per obtenir connexió a Oracle
 * Per tests bàsics dels models
 * 
 * @author DomenechObiolAlbert
 */
public class ConnexioOracle {
    
    private static Properties props = new Properties();
    
    // Carrega propietats
    static {
        try (InputStream input = ConnexioOracle.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            
            if (input == null) {
                System.err.println("❌ No s'ha trobat db.properties");
                throw new RuntimeException("Fitxer db.properties no trobat");
            }
            
            props.load(input);
            Class.forName(props.getProperty("db.driver"));
            
        } catch (Exception e) {
            throw new RuntimeException("Error inicialitzant connexió: " + e.getMessage());
        }
    }
    
    /**
     * Obté connexió a Oracle
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
    
    /**
     * Test ràpid de connexió
     */
    public static void main(String[] args) {
        System.out.println("🔍 Provant connexió a Oracle...\n");
        
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connexió exitosa!");
            System.out.println("   BD: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   Versió: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("   URL: " + conn.getMetaData().getURL());
            
        } catch (SQLException e) {
            System.err.println("❌ Error de connexió:");
            System.err.println("   " + e.getMessage());
            System.err.println("\n💡 Revisa db.properties amb les teves credencials!");
        }
    }
}