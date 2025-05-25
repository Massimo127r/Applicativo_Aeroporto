package gui;

import controller.Controller;
import model.Amministratore;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField       usernameField;
    private JPasswordField   passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton          loginButton;
    private JButton          registerButton;
    private Controller       controller;

    public LoginFrame() {
        controller = Controller.getInstance();

        setTitle("Login Aeroporto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        initListeners();
    }

    private void initComponents() {
        // 1) pannello principale: 2 righe (form + bottoni)
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 2) form: 3 righe, 2 colonne, gap 5px
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        usernameField   = new JTextField();
        passwordField   = new JPasswordField();
        userTypeCombo   = new JComboBox<>(new String[]{"Utente", "Amministratore"});

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Tipo Utente:"));
        formPanel.add(userTypeCombo);

        // 3) pannello bottoni: 1 riga, 2 colonne, gap 10px
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        loginButton    = new JButton("Login");
        registerButton = new JButton("Registrati");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // 4) assembla
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);
    }

    private void initListeners() {
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeCombo.getSelectedItem();

            if (controller.login(username, password, userType)) {
                JOptionPane.showMessageDialog(this,
                        "Login effettuato con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);

                if (controller.isAmministratore()) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new UserDashboard().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username o password non validi!",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Funzionalità di registrazione non disponibile",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

