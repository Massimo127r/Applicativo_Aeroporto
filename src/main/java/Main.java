import controller.Controller;
import gui.Login;
import gui.UIManager;
import javax.swing.SwingUtilities;
import Database.ConnessioneDatabase;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize database enum types
        try {
            ConnessioneDatabase.createStatoVoloEnum();
        } catch (SQLException | IOException e) {
            System.err.println("Errore durante la creazione del tipo enum statovolo: " + e.getMessage());
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Apply application theme
                UIManager.applyTheme();

                // Create controller
                Controller controller = new Controller();

                // Create and show login frame with controller
                Login loginFrame = new Login(controller);
                loginFrame.setVisible(true);
            }
        });
    }
}
