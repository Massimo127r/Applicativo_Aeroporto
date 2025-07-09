package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce la connessione al database PostgreSQL.
 * Fornisce metodi per gestire la connessione al database.
 */
public class ConnessioneDatabase {
    /**
     * Istanza della classe ConnessioneDatabase.
     */
    private static ConnessioneDatabase instance;

    /**
     * Connessione attiva al database.
     */
    public Connection connection = null;

    /**
     * URL di connessione al database.
     */
    private String url = "jdbc:postgresql://localhost:5432/DBFINALE";

    /**
     * Nome utente per l'accesso al database.
     */
    private String user = "postgres";


    /**
     * Password per l'accesso al database.
     */
    private String password = "Massimorusso127";

    /**
     * Nome del driver JDBC per PostgreSQL.
     */
    private String driver = "org.postgresql.Driver";

    /**
     * Costruttore privato che inizializza la connessione al database.
     *
     * @throws SQLException Se si verifica un errore durante la connessione al database
     */
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database Connection Creation Failed : " + e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'istanza della connessione al database.
     * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova.
     *
     * @return L'istanza di ConnessioneDatabase
     * @throws SQLException Se si verifica un errore durante la connessione al database
     */
    public static ConnessioneDatabase getInstance()
            throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }


}
