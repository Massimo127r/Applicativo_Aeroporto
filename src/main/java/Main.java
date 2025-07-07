import controller.Controller;

import gui.UIManager;
import gui.Welcome;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {


         SwingUtilities.invokeLater(()->

            {
                UIManager.applyTheme();
                Controller controller = new Controller();
                Welcome welcomeFrame = new Welcome(controller);
                welcomeFrame.setVisible(true);
            });
        }
}
