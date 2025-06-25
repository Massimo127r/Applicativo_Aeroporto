import gui.Login;
import gui.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Apply application theme
                UIManager.applyTheme();

                // Create and show login frame
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
            }
        });
    }
}
