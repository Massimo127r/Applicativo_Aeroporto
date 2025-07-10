package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Classe che implementa l'interfaccia grafica per la registrazione di nuovi utenti.
 * Permette agli utenti di creare un nuovo account inserendo username, password e dati personali.
 * Gestisce la validazione dei dati inseriti e l'invio delle informazioni al controller per la registrazione.
 */
public class Registration extends JFrame {
    /**
     * Pannello principale che contiene tutti gli elementi dell'interfaccia.
     */
    private JPanel mainPanel;

    /**
     * Pannello che contiene i campi del form di registrazione.
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
     * Campo di testo per l'inserimento del nome dell'utente.
     */
    private JTextField nomeField;

    /**
     * Campo di testo per l'inserimento del cognome dell'utente.
     */
    private JTextField cognomeField;

    /**
     * Pulsante per effettuare la registrazione.
     */
    private JButton registerButton;

    /**
     * Pulsante per tornare alla schermata precedente.
     */
    private JButton backButton;

    /**
     * Etichetta per visualizzare messaggi di stato o errori.
     */
    private JLabel statusLabel;

    /**
     * Riferimento al controller che gestisce la logica dell'applicazione.
     */
    private Controller controller;

    /**
     * Costruttore della classe Registration.
     * Inizializza l'interfaccia grafica per la registrazione di nuovi utenti.
     * 
     * @param controller Il controller che gestisce la logica dell'applicazione
     */
    public Registration(Controller controller) {
        this.controller = controller;
        setTitle("Aeroporto di Napoli - Registrazione");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Registrazione Nuovo Utente");
        titlePanel.add(titleLabel);

        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 25));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 25));

        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField();
        nomeField.setPreferredSize(new Dimension(200, 25));

        JLabel cognomeLabel = new JLabel("Cognome:");
        cognomeField = new JTextField();
        cognomeField.setPreferredSize(new Dimension(200, 25));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(nomeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(cognomeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(cognomeField, gbc);

        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        registerButton = new JButton("Registrati");
        backButton = new JButton("Torna Indietro");

        buttonsPanel.add(backButton);
        buttonsPanel.add(registerButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        registerButton.setVisible(true);
        backButton.setVisible(true);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        UIManager.styleFrame(this);

        UIManager.stylePanel(mainPanel);
        UIManager.stylePanel(formPanel);
        UIManager.stylePanel(titlePanel);
        UIManager.stylePanel(buttonsPanel);
        UIManager.styleTextField(usernameField);
        UIManager.stylePasswordField(passwordField);
        UIManager.styleTextField(nomeField);
        UIManager.styleTextField(cognomeField);
        UIManager.styleButton(registerButton);
        UIManager.styleButton(backButton);

        titleLabel.setFont(UIManager.TITLE_FONT);
        titleLabel.setForeground(UIManager.PRIMARY_COLOR);

        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel j) {
                j.setFont(UIManager.NORMAL_FONT);
                j.setForeground(UIManager.TEXT_COLOR);
            }
        }
        usernameField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String txt = ((JTextField) input).getText();
                if (txt.length() < 5 || !txt.matches("\\w+") || txt.contains(" ")) {
                    statusLabel.setText("Username: minimo 5 caratteri alfanumerici senza spazi");
                    return false;
                }
                statusLabel.setText("");
                return true;
            }
        });
        passwordField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String pwd = new String(((JPasswordField) input).getPassword());
                if (pwd.length() < 5 || pwd.contains(" ")) {
                    statusLabel.setText("Password: minimo 5 caratteri senza spazi");
                    return false;
                }
                statusLabel.setText("");
                return true;
            }
        });
        nomeField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String n = ((JTextField) input).getText().trim();
                int nonSpaceChars = 0;
                for (char c : n.toCharArray()) {
                    if (c != ' ') nonSpaceChars++;
                }
                if (!n.matches("[A-Za-zàèéìòùĀ-ž ]+") || nonSpaceChars < 3) {
                    statusLabel.setText("Nome deve contenere almeno 3 caratteri diversi dallo spazio");
                    return false;
                }
                statusLabel.setText("");
                return true;
            }
        });
        cognomeField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String n = ((JTextField) input).getText().trim();
                int nonSpaceChars = 0;
                for (char c : n.toCharArray()) {
                    if (c != ' ') nonSpaceChars++;
                }
                if (!n.matches("[A-Za-zàèéìòùĀ-ž ]+") || nonSpaceChars < 3) {
                    statusLabel.setText("Cognome deve contenere almeno 3 caratteri diversi dallo spazio");
                    return false;
                }
                statusLabel.setText("");
                return true;
            }
        });
        statusLabel.setForeground(UIManager.ERROR_COLOR);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptRegistration();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToWelcome();
            }
        });

        setContentPane(mainPanel);
    }

    /**
     * Tenta di registrare un nuovo utente con i dati inseriti nel form.
     * Verifica la validità dei dati inseriti, crea un nuovo utente e lo registra tramite il controller.
     * In caso di successo, mostra un messaggio di conferma e reindirizza l'utente alla dashboard appropriata.
     * In caso di errore, mostra un messaggio di errore.
     */
    private void attemptRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();

        if (username.isEmpty() || password.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
            statusLabel.setText("Tutti i campi sono obbligatori");
            return;
        }
        if (!usernameField.getInputVerifier().verify(usernameField)) {
            usernameField.requestFocus();
            return;
        }
        if (!passwordField.getInputVerifier().verify(passwordField)) {
            passwordField.requestFocus();
            return;
        }
        if (!nomeField.getInputVerifier().verify(nomeField)) {
            nomeField.requestFocus();
            return;
        }
        if (!cognomeField.getInputVerifier().verify(cognomeField)) {
            cognomeField.requestFocus();
            return;
        }


        String userRole = "generico";
        boolean isAdmin = false;

        Utente newUser = new Utente(username, password, nome, cognome, userRole);

        boolean success = controller.registraUtente(newUser, isAdmin);

        if (success) {
            statusLabel.setText("Registrazione completata con successo!");
            statusLabel.setForeground(UIManager.SUCCESS_COLOR);

            JOptionPane.showMessageDialog(this,
                    "Registrazione completata con successo! Accesso automatico in corso...",
                    "Registrazione Completata",
                    JOptionPane.INFORMATION_MESSAGE);

            Utente user = controller.login(username, password, "Utente");

            if (user != null) {
                UserDashboard dashboard = new UserDashboard(user);
                dashboard.setVisible(true);
                this.dispose();
            } else {
                Login loginFrame = new Login(controller);
                loginFrame.setVisible(true);
                this.dispose();
            }
        } else {
            statusLabel.setText("Errore durante la registrazione. Username già in uso?");
            statusLabel.setForeground(UIManager.ERROR_COLOR);
        }
    }

    /**
     * Torna alla schermata di benvenuto.
     * Crea una nuova istanza della schermata di benvenuto, la rende visibile e chiude la finestra di registrazione corrente.
     */
    private void goBackToWelcome() {
        Welcome welcomeFrame = new Welcome(controller);
        welcomeFrame.setVisible(true);
        this.dispose();
    }
}
