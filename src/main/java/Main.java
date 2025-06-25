import gui.Login;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
            }
        });
    }
}
