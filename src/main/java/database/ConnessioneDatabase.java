package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnessioneDatabase {
    private static ConnessioneDatabase instance;
    private Connection connection;

    private final String url;
    private final String user;
    private final String password;
    private final String driver = "org.postgresql.Driver";

    private ConnessioneDatabase(String url, String user, String password)
            throws SQLException, ClassNotFoundException {
        this.url = url;
        this.user = user;
        this.password = password;

        Class.forName(driver);
        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connessione al database stabilita con successo");
    }


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

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connessione al database ristabilita");
        }
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connessione al database chiusa");
        }
    }
}