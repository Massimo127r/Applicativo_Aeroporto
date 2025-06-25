package gui;

import model.Amministratore;
import model.Generico;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JComboBox<String> userTypeComboBox;

    // Temporary user database for testing
    private List<Utente> users;

    public Login() {
        // Initialize user database with some test users
        initializeUsers();

        // Set up the frame
        setTitle("Aeroporto di Napoli - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize user type combo box
        userTypeComboBox.addItem("Amministratore");
        userTypeComboBox.addItem("Utente");
        userTypeComboBox.setSelectedIndex(1); // Default to "Utente"

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        // Set initial status label color
        statusLabel.setForeground(Color.RED);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private void initializeUsers() {
        users = new ArrayList<>();
        users.add(new Amministratore("admin", "admin123", "Admin", "User"));
        users.add(new Generico("user", "user123", "Regular", "User"));
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedUserType = (String) userTypeComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Inserisci username e password");
            return;
        }

        for (Utente user : users) {
            if (user.getNomeUtente().equals(username) && user.getPassword().equals(password)) {
                // Check if user type matches the selected type
                boolean isAdmin = user instanceof Amministratore;
                boolean isUser = user instanceof Generico;

                if ((isAdmin && selectedUserType.equals("Amministratore")) ||
                    (isUser && selectedUserType.equals("Utente"))) {

                    statusLabel.setText("Login riuscito!");
                    statusLabel.setForeground(new Color(50, 168, 82));

                    // Navigate to appropriate dashboard based on user type
                    if (isAdmin) {
                        openAdminDashboard((Amministratore) user);
                    } else if (isUser) {
                        openUserDashboard((Generico) user);
                    }

                    return;
                } else {
                    statusLabel.setText("Tipo utente non corretto");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
            }
        }

        statusLabel.setText("Username o password non validi");
        statusLabel.setForeground(Color.RED);
    }

    private void openAdminDashboard(Amministratore admin) {
        // Create and show admin dashboard
        AdminDashboard dashboard = new AdminDashboard(admin);
        dashboard.setVisible(true);
        this.dispose(); // Close login window
    }

    private void openUserDashboard(Generico user) {
        // Create and show user dashboard
        UserDashboard dashboard = new UserDashboard(user);
        dashboard.setVisible(true);
        this.dispose(); // Close login window
    }
}
