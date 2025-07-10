package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe che implementa la schermata di benvenuto dell'applicazione aeroportuale.
 * Rappresenta il punto di ingresso dell'applicazione e offre all'utente la possibilità
 * di accedere al sistema o registrarsi come nuovo utente.
 * Fornisce un'interfaccia semplice con due pulsanti principali per navigare verso
 * le schermate di login o registrazione.
 */
public class Welcome extends JFrame {
    /**
     * Pannello principale che contiene tutti gli elementi dell'interfaccia.
     */
    private JPanel mainPanel;

    /**
     * Pulsante per accedere alla schermata di login.
     */
    private JButton loginButton;

    /**
     * Pulsante per accedere alla schermata di registrazione.
     */
    private JButton registerButton;

    /**
     * Etichetta che visualizza il titolo della schermata di benvenuto.
     */
    private JLabel titleLabel;

    /**
     * Riferimento al controller che gestisce la logica dell'applicazione.
     */
    private Controller controller;

    /**
     * Costruttore della schermata di benvenuto.
     * Inizializza l'interfaccia grafica con i pulsanti per accedere al login o alla registrazione.
     * Configura l'aspetto visivo e gli eventi dei componenti dell'interfaccia.
     * 
     * @param controller Il controller che gestisce la logica dell'applicazione
     */
    public Welcome(Controller controller) {
        this.controller = controller;

        setTitle("Aeroporto di Napoli - Benvenuto");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        titleLabel = new JLabel("Benvenuto all'Aeroporto di Napoli");
        titlePanel.add(titleLabel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 10, 10));

        loginButton = new JButton("Accedi");
        registerButton = new JButton("Registrati");

        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        UIManager.styleFrame(this);

        UIManager.stylePanel(mainPanel);
        UIManager.stylePanel(titlePanel);
        UIManager.stylePanel(buttonsPanel);
        UIManager.styleButton(loginButton);
        UIManager.styleButton(registerButton);

        titleLabel.setFont(UIManager.TITLE_FONT);
        titleLabel.setForeground(UIManager.PRIMARY_COLOR);

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

        setContentPane(mainPanel);
    }

    /**
     * Apre la schermata di login.
     * Crea una nuova istanza della schermata di login, la rende visibile e chiude la finestra di benvenuto corrente.
     */
    private void openLoginScreen() {
        Login loginFrame = new Login(controller);
        loginFrame.setVisible(true);
        this.dispose();
    }

    /**
     * Apre la schermata di registrazione.
     * Crea una nuova istanza della schermata di registrazione, la rende visibile e chiude la finestra di benvenuto corrente.
     */
    private void openRegistrationScreen() {
        Registration registrationFrame = new Registration(controller);
        registrationFrame.setVisible(true);
        this.dispose();
    }
}
