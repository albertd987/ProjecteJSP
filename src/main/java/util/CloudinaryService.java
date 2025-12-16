package util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Servei per gestionar imatges amb Cloudinary
 * 
 * @author DomenechObiolAlbert
 * @version 1.2 - Amb bypass SSL per entorns acadèmics
 */

public class CloudinaryService {
    static {
    try {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        SSLContext.setDefault(sc);

        HttpsURLConnection.setDefaultHostnameVerifier((h, s) -> true);
    } catch (Exception ignored) {}
}
    
    private static CloudinaryService instance;
    private Cloudinary cloudinary;
    
    private static final String FOLDER_PRODUCTES = "tallers-manolo/productes";
    private static final String FOLDER_COMPONENTS = "tallers-manolo/components";
    
    /**
     * Constructor privat (Singleton)
     */
    private CloudinaryService() {
        // Desactivar verificació SSL (per entorns acadèmics/desenvolupament)
        disableSSLVerification();
        
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dqkdtkjur",
            "api_key", "719116439975421",
            "api_secret", "-Ni5xecc5c5hOsrhUCay6-uANhU",
            "secure", true
        ));
    }
    
    /**
     * Desactiva la verificació SSL
     */
    private void disableSSLVerification() {
        try {
            // Crear un TrustManager que accepta tots els certificats
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };
            
            // Instal·lar el TrustManager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            // Crear un HostnameVerifier que accepta tots els hosts
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            
            System.out.println("✅ SSL verification disabled for Cloudinary");
        } catch (Exception e) {
            System.err.println("❌ Error disabling SSL: " + e.getMessage());
        }
    }
    
    /**
     * Obtenir instància única (Singleton)
     */
    public static synchronized CloudinaryService getInstance() {
        if (instance == null) {
            instance = new CloudinaryService();
        }
        return instance;
    }
    
    /**
     * Pujar imatge de producte
     */
    public String pujarImatgeProducte(InputStream inputStream, String codiProducte) throws Exception {
        byte[] bytes = toByteArray(inputStream);
        return pujarImatgeBytes(bytes, codiProducte, FOLDER_PRODUCTES);
    }
    
    /**
     * Pujar imatge de component
     */
    public String pujarImatgeComponent(InputStream inputStream, String codiComponent) throws Exception {
        byte[] bytes = toByteArray(inputStream);
        return pujarImatgeBytes(bytes, codiComponent, FOLDER_COMPONENTS);
    }
    
    /**
     * Convertir InputStream a byte[]
     */
    private byte[] toByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        
        buffer.flush();
        return buffer.toByteArray();
    }
    
    /**
     * Pujar imatge des de bytes
     */
    @SuppressWarnings("unchecked")
    public String pujarImatgeBytes(byte[] bytes, String publicId, String folder) throws Exception {
        String cleanPublicId = publicId.replaceAll("[^a-zA-Z0-9-_]", "_");
        
        Map<String, Object> options = ObjectUtils.asMap(
            "public_id", cleanPublicId,
            "folder", folder,
            "overwrite", true,
            "resource_type", "image"
        );
        
        Map<String, Object> result = cloudinary.uploader().upload(bytes, options);
        
        String url = (String) result.get("secure_url");
        System.out.println("📷 Imatge pujada: " + url);
        return url;
    }
    
    /**
     * Eliminar imatge
     */
    @SuppressWarnings("unchecked")
    public boolean eliminarImatge(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            System.err.println("Error eliminant imatge: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtenir URL optimitzada
     */
    public String getUrlOptimitzada(String publicId, int width, int height) {
        return cloudinary.url()
            .transformation(new com.cloudinary.Transformation()
                .width(width)
                .height(height)
                .crop("fill")
                .quality("auto")
                .fetchFormat("auto"))
            .generate(publicId);
    }
    
    /**
     * Obtenir URL per thumbnail
     */
    public String getUrlThumbnail(String publicId) {
        return getUrlOptimitzada(publicId, 100, 100);
    }
}