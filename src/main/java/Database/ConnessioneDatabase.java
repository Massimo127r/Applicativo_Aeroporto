package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe per la gestione della connessione al database PostgreSQL.
 */
public class ConnessioneDatabase {
    // Parametri di connessione al database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/DB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Grafica2004#";

    private static Connection connection = null;

    /**
     * Stabilisce una connessione al database PostgreSQL.
     * @return Connection oggetto connessione al database
     * @throws SQLException se si verifica un errore durante la connessione
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carica il driver JDBC di PostgreSQL
                Class.forName("org.postgresql.Driver");

                // Stabilisce la connessione
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connessione al database stabilita con successo");
            } catch (ClassNotFoundException e) {
                System.err.println("Driver PostgreSQL non trovato: " + e.getMessage());
                throw new SQLException("Driver PostgreSQL non trovato", e);
            } catch (SQLException e) {
                System.err.println("Errore durante la connessione al database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Chiude la connessione al database.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connessione al database chiusa");
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
