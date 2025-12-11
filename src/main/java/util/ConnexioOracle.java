package util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe per obtenir connexió a Oracle ATP via JNDI
 * NOMÉS per producció en GlassFish
 * 
 * @author DomenechObiolAlbert
 */
public class ConnexioOracle {
    
    private static DataSource dataSource;
    
    // Inicialitza DataSource des de JNDI
    static {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleATP");
            System.out.println("✓ DataSource JNDI inicialitzat correctament");
        } catch (NamingException e) {
            System.err.println("✗ Error inicialitzant DataSource JNDI");
            System.err.println("Assegura't que l'aplicació està desplegada en GlassFish");
            throw new RuntimeException("Error lookup JNDI jdbc/OracleATP: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obté connexió a Oracle ATP
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource no inicialitzat. Revisa configuració JNDI.");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Test ràpid de connexió
     */
    public static void main(String[] args) {
        System.out.println("🔍 Provant connexió a Oracle ATP...\n");
        
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connexió exitosa!");
            System.out.println("   BD: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   Versió: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("   URL: " + conn.getMetaData().getURL());
            
        } catch (SQLException e) {
            System.err.println("❌ Error de connexió:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
        }
    }
}