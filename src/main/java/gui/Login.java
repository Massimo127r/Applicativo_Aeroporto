package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel statusLabel;
    private JComboBox<String> userTypeComboBox;

    // Controller per l'interazione con il database
    private Controller controller;

    public Login(Controller controller) {
        this.controller = controller;

        // Set up the frame
        setTitle("Aeroporto di Napoli - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply theme to frame
        UIManager.styleFrame(this);

        // Style components
        UIManager.stylePanel(mainPanel);
        UIManager.stylePanel(formPanel);
        UIManager.styleTextField(usernameField);
        UIManager.stylePasswordField(passwordField);
        UIManager.styleButton(loginButton);
        UIManager.styleComboBox(userTypeComboBox);

        // Set fonts for labels
        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(UIManager.NORMAL_FONT);
                ((JLabel) comp).setForeground(UIManager.TEXT_COLOR);
            }
        }

        // Style the welcome label
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && !(comp.equals(statusLabel))) {
                JLabel welcomeLabel = (JLabel) comp;
                welcomeLabel.setFont(UIManager.TITLE_FONT);
                welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
            }
        }

        // Initialize user type combo box
        userTypeComboBox.addItem("Amministratore");
        userTypeComboBox.addItem("Utente");
        userTypeComboBox.setSelectedIndex(1); // Default to "Utente"

        // Create and add back button
        backButton = new JButton("Torna Indietro");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        // Replace the existing status label position
        mainPanel.remove(statusLabel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Style the back button and panels
        UIManager.styleButton(backButton);
        UIManager.stylePanel(buttonPanel);
        UIManager.stylePanel(southPanel);

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        // Add action listener to back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToWelcome();
            }
        });

        // Set initial status label color
        statusLabel.setForeground(UIManager.ERROR_COLOR);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedUserType = (String) userTypeComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Inserisci username e password");
            return;
        }

        // Usa il controller per autenticare l'utente verificando anche il ruolo
        Utente user = controller.login(username, password, selectedUserType);

        if (user != null) {
            statusLabel.setText("Login riuscito!");
            statusLabel.setForeground(UIManager.SUCCESS_COLOR);

            // Navigate to appropriate dashboard based on user role
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

    private void openAdminDashboard(Utente admin) {
        // Create and show admin dashboard
        AdminDashboard dashboard = new AdminDashboard(admin);
        dashboard.setVisible(true);
        this.dispose(); // Close login window
    }

    private void openUserDashboard(Utente user) {
        // Create and show user dashboard
        UserDashboard dashboard = new UserDashboard(user);
        dashboard.setVisible(true);
        this.dispose(); // Close login window
    }

    private void goBackToWelcome() {
        // Create and show welcome frame
        Welcome welcomeFrame = new Welcome(controller);
        welcomeFrame.setVisible(true);
        this.dispose(); // Close login window
    }
}
