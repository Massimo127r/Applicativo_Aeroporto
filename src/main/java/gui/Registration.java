package gui;

import controller.Controller;
import model.Utente;
import gui.UserDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Registration extends JFrame {
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    // Controller per l'interazione con il database
    private Controller controller;

    public Registration(Controller controller) {
        this.controller = controller;

        // Set up the frame
        setTitle("Aeroporto di Napoli - Registrazione");
        setSize(550, 500);  // Increased width to accommodate wider fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Registrazione Nuovo Utente");
        titlePanel.add(titleLabel);

        // Form panel
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

        // Add username row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        // Add password row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Add nome row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(nomeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomeField, gbc);

        // Add cognome row
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(cognomeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(cognomeField, gbc);

        // Status label (spans both columns)
        statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        registerButton = new JButton("Registrati");
        backButton = new JButton("Torna Indietro");

        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Ensure buttons are visible
        registerButton.setVisible(true);
        backButton.setVisible(true);

        // Add padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Apply theme to frame
        UIManager.styleFrame(this);

        // Style components
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

        // Style the title label
        titleLabel.setFont(UIManager.TITLE_FONT);
        titleLabel.setForeground(UIManager.PRIMARY_COLOR);

        // Style form labels
        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(UIManager.NORMAL_FONT);
                ((JLabel) comp).setForeground(UIManager.TEXT_COLOR);
            }
        }

        // Style status label
        statusLabel.setForeground(UIManager.ERROR_COLOR);

        // Add action listeners
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

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private void attemptRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || nome.isEmpty() || cognome.isEmpty()) {
            statusLabel.setText("Tutti i campi sono obbligatori");
            return;
        }

        // User role is always "generico" since only "Utente" is available
        String userRole = "generico";
        boolean isAdmin = false;

        // Create user object
        Utente newUser = new Utente(username, password, nome, cognome, userRole);

        // Register user
        boolean success = controller.registraUtente(newUser, isAdmin);

        if (success) {
            statusLabel.setText("Registrazione completata con successo!");
            statusLabel.setForeground(UIManager.SUCCESS_COLOR);

            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Registrazione completata con successo! Accesso automatico in corso...", 
                "Registrazione Completata", 
                JOptionPane.INFORMATION_MESSAGE);

            // Automatically login the user
            Utente user = controller.login(username, password, "Utente");

            if (user != null) {
                // Open user dashboard directly
                UserDashboard dashboard = new UserDashboard(user);
                dashboard.setVisible(true);
                this.dispose(); // Close registration window
            } else {
                // Fallback to login screen if automatic login fails
                Login loginFrame = new Login(controller);
                loginFrame.setVisible(true);
                this.dispose(); // Close registration window
            }
        } else {
            statusLabel.setText("Errore durante la registrazione. Username già in uso?");
            statusLabel.setForeground(UIManager.ERROR_COLOR);
        }
    }

    private void goBackToWelcome() {
        // Create and show welcome frame
        Welcome welcomeFrame = new Welcome(controller);
        welcomeFrame.setVisible(true);
        this.dispose(); // Close registration window
    }
}
