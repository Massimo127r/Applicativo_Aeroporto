package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce la connessione al database PostgreSQL.
 * Implementa il pattern Singleton per garantire una singola istanza di connessione.
 * Fornisce metodi per ottenere, gestire e chiudere la connessione al database.
 */
public class ConnessioneDatabase {
    /**
     * Istanza singleton della classe ConnessioneDatabase.
     */
    private static ConnessioneDatabase instance;

    /**
     * Connessione attiva al database.
     */
    private Connection connection;

    /**
     * URL di connessione al database.
     */
    private final String url;

    /**
     * Nome utente per l'accesso al database.
     */
    private final String user;

    /**
     * Password per l'accesso al database.
     */
    private final String password;

    /**
     * Nome del driver JDBC per PostgreSQL.
     */
    private final String driver = "org.postgresql.Driver";

    /**
     * Costruttore privato che inizializza la connessione al database.
     * 
     * @param url      URL di connessione al database
     * @param user     Nome utente per l'accesso al database
     * @param password Password per l'accesso al database
     * @throws SQLException           Se si verifica un errore durante la connessione al database
     * @throws ClassNotFoundException Se il driver del database non viene trovato
     */
    private ConnessioneDatabase(String url, String user, String password)
            throws SQLException, ClassNotFoundException {
        this.url = url;
        this.user = user;
        this.password = password;

        Class.forName(driver);
        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connessione al database stabilita con successo");
    }

    /**
     * Restituisce l'istanza singleton della connessione al database.
     * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova con parametri predefiniti.
     * 
     * @return L'istanza singleton di ConnessioneDatabase
     * @throws SQLException           Se si verifica un errore durante la connessione al database
     * @throws ClassNotFoundException Se il driver del database non viene trovato
     */
    public static ConnessioneDatabase getInstance()
            throws SQLException, ClassNotFoundException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            String defaultUrl = "jdbc:postgresql://localhost:5432/DB";
            String defaultUser = "postgres";
            String defaultPassword = "Massimorusso127";
            instance = new ConnessioneDatabase(defaultUrl, defaultUser, defaultPassword);
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto Connection attuale.
     * Se la connessione è nulla o chiusa, ne crea una nuova utilizzando i parametri memorizzati.
     * 
     * @return L'oggetto Connection per interagire con il database
     * @throws SQLException Se si verifica un errore durante la connessione al database
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connessione al database ristabilita");
        }
        return connection;
    }

    /**
     * Chiude la connessione al database se è aperta.
     * 
     * @throws SQLException Se si verifica un errore durante la chiusura della connessione
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connessione al database chiusa");
        }
    }
}
