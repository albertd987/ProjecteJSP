package dao;

import model.Usuari;

/**
 * Interfície DAO per gestionar operacions amb usuaris
 * 
 * Responsabilitat única: Definir contracte per accedir a dades d'usuaris
 * 
 * Mètodes principals:
 * - findByUsername: Buscar usuari per nom d'usuari (per login)
 * - updateUltimAcces: Actualitzar timestamp darrer login
 * - create: Crear nou usuari (opcional, per si cal més endavant)
 * 
 * NOTA: No implementem CRUD complet perquè la Tasca 5 diu:
 * "no es demana que feu una gestió d'usuaris"
 * 
 * @author DomenechObiolAlbert
 * @version 1.0
 */
public interface IDAOUsuari {
    
    /**
     * Busca un usuari pel seu nom d'usuari (username)
     * 
     * Usat per autenticació: obtenir el hash Bcrypt per verificar password
     * 
     * @param username Nom d'usuari a buscar
     * @return Usuari si existeix, null si no es troba
     * @throws Exception Si hi ha error de connexió o consulta
     */
    Usuari findByUsername(String username) throws Exception;
    
    /**
     * Actualitza el timestamp de l'últim accés de l'usuari
     * 
     * S'executa després d'un login exitós per registrar activitat
     * 
     * @param usId ID de l'usuari
     * @throws Exception Si hi ha error d'actualització
     */
    void updateUltimAcces(Long usId) throws Exception;
    
    /**
     * Crea un nou usuari (OPCIONAL - per futures ampliacions)
     * 
     * @param usuari Objecte Usuari amb dades a inserir
     * @return Usuari creat amb ID assignat per Oracle
     * @throws Exception Si hi ha error d'inserció o username duplicat
     */
    Usuari create(Usuari usuari) throws Exception;
}