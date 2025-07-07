import controller.Controller;

import gui.UIManager;
import gui.Welcome;
import javax.swing.SwingUtilities;

/**
 * Classe principale dell'applicazione aeroportuale.
 * Rappresenta il punto di ingresso dell'applicazione e si occupa di inizializzare
 * i componenti principali e avviare l'interfaccia utente.
 */
public class Main {
    /**
     * Metodo principale che avvia l'applicazione.
     * Inizializza il controller, applica il tema all'interfaccia grafica
     * e visualizza la schermata di benvenuto.
     * 
     * L'inizializzazione dell'interfaccia utente viene eseguita nel thread di Swing
     * per garantire la corretta gestione degli eventi dell'interfaccia grafica.
     *
     * @param args Argomenti della linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
                UIManager.applyTheme();
                Controller controller = new Controller();
                Welcome welcomeFrame = new Welcome(controller);
                welcomeFrame.setVisible(true);
            });
        }
}
