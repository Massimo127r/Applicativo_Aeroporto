package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe che implementa l'interfaccia grafica per la schermata di login.
 * Permette agli utenti di autenticarsi nel sistema specificando username, password e tipo di utente.
 * Gestisce la validazione dei dati inseriti e il reindirizzamento alla dashboard appropriata in base al tipo di utente.
 */
public class Login extends JFrame {
    /**
     * Pannello principale che contiene tutti gli elementi dell'interfaccia.
     */
    private JPanel mainPanel;

    /**
     * Pannello che contiene i campi del form di login.
     */
    private JPanel formPanel;

    /**
     * Campo di testo per l'inserimento del nome utente.
     */
    private JTextField usernameField;

    /**
     * Campo di testo per l'inserimento della password.
     */
    private JPasswordField passwordField;

    /**
     * Pulsante per effettuare il login.
     */
    private JButton loginButton;

    /**
     * Pulsante per tornare alla schermata precedente.
     */
    private JButton backButton;

    /**
     * Etichetta per visualizzare messaggi di stato o errori.
     */
    private JLabel statusLabel;

    /**
     * Menu a tendina per selezionare il tipo di utente.
     */
    private JComboBox<String> userTypeComboBox;

    /**
     * Riferimento al controller che gestisce la logica dell'applicazione.
     */
    private Controller controller;

    /**
     * Costruttore che inizializza la schermata di login.
     * Configura l'interfaccia grafica, imposta gli stili e aggiunge i listener per gli eventi.
     * 
     * @param controller Il controller che gestisce la logica dell'applicazione
     */
    public Login(Controller controller) {
        this.controller = controller;

        setTitle("Aeroporto di Napoli - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.styleFrame(this);

        UIManager.stylePanel(mainPanel);
        UIManager.stylePanel(formPanel);
        UIManager.styleTextField(usernameField);
        UIManager.stylePasswordField(passwordField);
        UIManager.styleButton(loginButton);
        UIManager.styleComboBox(userTypeComboBox);

        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel j ) {
                j.setFont(UIManager.NORMAL_FONT);
                j.setForeground(UIManager.TEXT_COLOR);
            }
        }

        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel welcomeLabel && !(comp.equals(statusLabel))) {
                welcomeLabel.setFont(UIManager.TITLE_FONT);
                welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
            }
        }

        userTypeComboBox.addItem("Amministratore");
        userTypeComboBox.addItem("Utente");
        userTypeComboBox.setSelectedIndex(1);

        backButton = new JButton("Torna Indietro");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        mainPanel.remove(statusLabel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        UIManager.styleButton(backButton);
        UIManager.stylePanel(buttonPanel);
        UIManager.stylePanel(southPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToWelcome();
            }
        });

        statusLabel.setForeground(UIManager.ERROR_COLOR);

        setContentPane(mainPanel);
    }

    /**
     * Tenta di autenticare l'utente con le credenziali inserite.
     * Valida i dati inseriti, verifica le credenziali tramite il controller e,
     * in caso di successo, reindirizza l'utente alla dashboard appropriata.
     */
    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedUserType = (String) userTypeComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Inserisci username e password");
            return;
        }

        if (username.length() < 5 || username.contains(" ")) {
            statusLabel.setText("Username: minimo 5 caratteri senza spazi");
            return;
        }

        if (password.length() < 5 || password.contains(" ")) {
            statusLabel.setText("Password: minimo 5 caratteri senza spazi");
            return;
        }

        Utente user = controller.login(username, password, selectedUserType);

        if (user != null) {
            statusLabel.setText("Login riuscito!");
            statusLabel.setForeground(UIManager.SUCCESS_COLOR);

            if (user.isAmministratore()) {
                openAdminDashboard(user);
            } else if (user.isGenerico()) {
                openUserDashboard(user);
            }

            return;
        }

        statusLabel.setText("Username, password o tipo utente non validi");
        statusLabel.setForeground(UIManager.ERROR_COLOR);
    }

    /**
     * Apre la dashboard dell'amministratore dopo un login riuscito.
     * Chiude la finestra di login corrente.
     * 
     * @param admin L'utente amministratore autenticato
     */
    private void openAdminDashboard(Utente admin) {
        AdminDashboard dashboard = new AdminDashboard(admin);
        dashboard.setVisible(true);
        this.dispose();
    }

    /**
     * Apre la dashboard dell'utente generico dopo un login riuscito.
     * Chiude la finestra di login corrente.
     * 
     * @param user L'utente generico autenticato
     */
    private void openUserDashboard(Utente user) {
        UserDashboard dashboard = new UserDashboard(user);
        dashboard.setVisible(true);
        this.dispose();
    }

    /**
     * Torna alla schermata di benvenuto.
     * Chiude la finestra di login corrente.
     */
    private void goBackToWelcome() {
        Welcome welcomeFrame = new Welcome(controller);
        welcomeFrame.setVisible(true);
        this.dispose();
    }
}
