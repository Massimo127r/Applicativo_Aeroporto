package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {
    private JPanel mainPanel;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel;

    // Controller per l'interazione con il database
    private Controller controller;

    public Welcome(Controller controller) {
        this.controller = controller;

        // Set up the frame
        setTitle("Aeroporto di Napoli - Benvenuto");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titleLabel = new JLabel("Benvenuto all'Aeroporto di Napoli");
        titlePanel.add(titleLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        
        loginButton = new JButton("Accedi");
        registerButton = new JButton("Registrati");
        
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Add padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Apply theme to frame
        UIManager.styleFrame(this);

        // Style components
        UIManager.stylePanel(mainPanel);
        UIManager.stylePanel(titlePanel);
        UIManager.stylePanel(buttonsPanel);
        UIManager.styleButton(loginButton);
        UIManager.styleButton(registerButton);
        
        // Style the title label
        titleLabel.setFont(UIManager.TITLE_FONT);
        titleLabel.setForeground(UIManager.PRIMARY_COLOR);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginScreen();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationScreen();
            }
        });

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private void openLoginScreen() {
        // Create and show login frame
        Login loginFrame = new Login(controller);
        loginFrame.setVisible(true);
        this.dispose(); // Close welcome window
    }

    private void openRegistrationScreen() {
        // Create and show registration frame
        Registration registrationFrame = new Registration(controller);
        registrationFrame.setVisible(true);
        this.dispose(); // Close welcome window
    }
}