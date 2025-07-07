package Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

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

    /**
     * Esegue lo script SQL per creare il tipo enum statovolo.
     * @throws SQLException se si verifica un errore durante l'esecuzione dello script
     * @throws IOException se si verifica un errore durante la lettura del file

    public static void createStatoVoloEnum() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            // Leggi il contenuto del file SQL
            InputStream inputStream = ConnessioneDatabase.class.getClassLoader().getResourceAsStream("sql/create_statovolo_enum.sql");
            if (inputStream == null) {
                throw new IOException("File SQL non trovato: sql/create_statovolo_enum.sql");
            }

            String sqlScript = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            // Esegui lo script SQL
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlScript);
                System.out.println("Tipo enum statovolo creato con successo");
            }
        }
    }*/
}
