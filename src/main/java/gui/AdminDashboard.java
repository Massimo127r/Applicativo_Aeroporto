package gui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe che implementa l'interfaccia grafica per la dashboard dell'amministratore.
 * Permette agli amministratori di gestire voli, bagagli smarriti e altre operazioni amministrative.
 * Fornisce funzionalità per visualizzare, aggiungere e modificare voli, gestire i bagagli e monitorare le prenotazioni.
 */
public class AdminDashboard extends JFrame {
    /**
     * Pannello principale che contiene tutti gli elementi dell'interfaccia.
     */
    private JPanel mainPanel;

    /**
     * Pannello a schede che organizza le diverse sezioni della dashboard.
     */
    private JTabbedPane tabbedPane;

    /**
     * Tabella che visualizza l'elenco dei voli.
     */
    private JTable flightsTable;

    /**
     * Pannello che contiene la tabella dei voli e i controlli correlati.
     */
    private JPanel flightsPanel;

    /**
     * Pannello che contiene il form per l'aggiunta di nuovi voli.
     */
    private JPanel addFlightPanel;

    /**
     * Pannello che contiene la gestione dei bagagli smarriti.
     */
    private JPanel lostBaggagePanel;

    /**
     * Costante utilizzata per i messaggi di errore nelle finestre di dialogo.
     */
    private static final String ERRORE = "Errore";

    /**
     * Costante utilizzata per i messaggi di successo nelle finestre di dialogo.
     */
    private static final String SUCCESSO = "Successo";

    /**
     * Costante utilizzata per le operazioni di modifica nelle finestre di dialogo.
     */
    private static final String MODIFICA = "Modifica";

    /**
     * Lista di tutti i voli presenti nel sistema.
     */
    private List<Volo> flights;

    /**
     * Lista di tutti i gate dell'aeroporto.
     */
    private List<Gate> gates;

    /**
     * Lista di tutti i bagagli registrati nel sistema.
     */
    private List<Bagaglio> baggages;

    /**
     * Utente amministratore attualmente loggato.
     */
    private Utente admin;

    /**
     * Riferimento al controller che gestisce la logica dell'applicazione.
     */
    private controller.Controller controller;

    /**
     * Costruttore della dashboard amministratore.
     * Inizializza l'interfaccia grafica e carica i dati necessari.
     * 
     * @param admin L'utente amministratore che sta utilizzando la dashboard
     * @throws IllegalArgumentException Se l'utente non ha il ruolo di amministratore
     */
    public AdminDashboard(Utente admin) {
        if (!admin.isAmministratore()) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo di amministratore");
        }
        this.admin = admin;

        this.controller = new controller.Controller();

        initializeTestData();

        setTitle("Dashboard Amministratore - Aeroporto di Napoli");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.styleFrame(this);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIManager.BACKGROUND_COLOR);

        tabbedPane = new JTabbedPane();
        UIManager.styleTabbedPane(tabbedPane);

        createFlightsPanel();
        createAddFlightPanel();
        createLostBaggagePanel();

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIManager.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Benvenuto, " + admin.getNome() + " " + admin.getCognome() + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(UIManager.TITLE_FONT);
        welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(UIManager.BACKGROUND_COLOR);
        JButton logoutButton = new JButton("Log Out");
        UIManager.styleButton(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                Login loginFrame = new Login(controller);
                loginFrame.setVisible(true);
            }
        });
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
    }

    /**
     * Conta il numero di caratteri non spazio in una stringa.
     * 
     * @param text La stringa da analizzare
     * @return Il numero di caratteri non spazio nella stringa
     */
    private int countNonSpaceChars(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c != ' ') count++;
        }
        return count;
    }

    /**
     * Inizializza i dati di test per la dashboard.
     * Carica tutti i voli, i gate e i bagagli dal controller.
     * Se i dati non sono disponibili, inizializza liste vuote.
     */
    private void initializeTestData() {
        flights = controller.getAllVoli();
        if (flights == null) {
            flights = new ArrayList<>();
        }

        gates = controller.getAllGates();
        if (gates == null) {
            gates = new ArrayList<>();
        }

        baggages = controller.getAllBagagli();
        if (baggages == null) {
            baggages = new ArrayList<>();
        }
    }

    /**
     * Crea il pannello per la visualizzazione e gestione dei voli.
     * Include una barra di ricerca, una tabella per visualizzare i voli
     * e funzionalità per filtrare e interagire con i dati dei voli.
     */
    private void createFlightsPanel() {
        flightsPanel = new JPanel(new BorderLayout());
        flightsPanel.setBackground(UIManager.BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(UIManager.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(UIManager.SECONDARY_COLOR, 1),
                        "Ricerca Voli",
                        0,
                        0,
                        UIManager.HEADER_FONT,
                        UIManager.PRIMARY_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{
                "Codice Volo", "Compagnia", "Destinazione", "Origine", "Orario", "Stato", "Data"
        });
        UIManager.styleComboBox(searchTypeComboBox);

        JTextField searchField = new JTextField(20);
        UIManager.styleTextField(searchField);

        JButton searchButton = new JButton("Cerca");
        UIManager.styleButton(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterFlights(searchField.getText(), (String) searchTypeComboBox.getSelectedItem());
            }
        });

        JLabel searchLabel = new JLabel("Cerca per:");
        searchLabel.setFont(UIManager.NORMAL_FONT);
        searchLabel.setForeground(UIManager.TEXT_COLOR);

        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        flightsPanel.add(searchPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Codice Volo");
        model.addColumn("Compagnia");
        model.addColumn("Origine");
        model.addColumn("Destinazione");
        model.addColumn("Gate");

        model.addColumn("Orario");
        model.addColumn("Stato");
        model.addColumn("Data");
        model.addColumn("Ritardo (min)");
        model.addColumn("Posti Totali");
        model.addColumn("Posti Disponibili");

        for (Volo volo : flights) {
            model.addRow(new Object[]{
                    volo.getCodiceVolo(),
                    volo.getCompagnia(),
                    volo.getOrigine(),
                    volo.getDestinazione(),
                    volo.getGate(),

                    volo.getOrarioPrevisto(),
                    volo.getStato(),
                    volo.getData(),
                    volo.getTempoRitardo(),
                    volo.getPostiTotali(),

                    volo.getPostiDisponibili(),

            });
        }

        flightsTable = new JTable(model);

        UIManager.styleTable(flightsTable);

        flightsTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    StatoVolo stato = (StatoVolo) value;
                    switch (stato) {
                        case inRitardo -> {
                            c.setBackground(UIManager.WARNING_COLOR);
                            c.setForeground(Color.WHITE);
                        }
                        case cancellato -> {
                            c.setBackground(UIManager.ERROR_COLOR);
                            c.setForeground(Color.BLACK);
                        }
                        case atterrato, decollato -> {
                            c.setBackground(UIManager.SUCCESS_COLOR);
                            c.setForeground(Color.WHITE);
                        }
                        default -> {
                            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                            c.setForeground(isSelected ? table.getSelectionForeground() : UIManager.TEXT_COLOR);
                        }
                    }
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : UIManager.TEXT_COLOR);
                }

                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        flightsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = flightsTable.rowAtPoint(evt.getPoint());
                    if (row >= 0 && row < flights.size()) {
                        showFlightDetailsDialog(flights.get(row));
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(UIManager.BACKGROUND_COLOR);
        flightsPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIManager.BACKGROUND_COLOR);
        flightsPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Voli", flightsPanel);
    }

    /**
     * Aggiorna la tabella dei voli con i dati più recenti.
     * Recupera tutti i voli dal controller e aggiorna la tabella dell'interfaccia.
     */
    private void refreshFlightsTable() {
        flights = controller.getAllVoli();
        if (flights == null) {
            flights = new ArrayList<>();
        }

        DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();
        model.setRowCount(0);

        for (Volo volo : flights) {
            model.addRow(new Object[]{
                    volo.getCodiceVolo(),
                    volo.getCompagnia(),
                    volo.getOrigine(),
                    volo.getDestinazione(),
                    volo.getGate(),

                    volo.getOrarioPrevisto(),
                    volo.getStato(),
                    volo.getData(),
                    volo.getTempoRitardo(),
                    volo.getPostiTotali(),
                    volo.getPostiDisponibili()
            });
        }
    }

    /**
     * Filtra i voli nella tabella in base ai criteri di ricerca specificati.
     * 
     * @param searchText Il testo da cercare
     * @param searchType Il tipo di ricerca (Codice Volo, Compagnia, Destinazione, ecc.)
     */
    private void filterFlights(String searchText, String searchType) {
        searchText = searchText.toLowerCase();

        DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();
        model.setRowCount(0);

        for (Volo volo : flights) {
            boolean match = false;

            if (searchText.isEmpty()) {
                match = true;
            } else if (searchType.equals("Codice Volo") && volo.getCodiceVolo().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Compagnia") && volo.getCompagnia().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Destinazione") && volo.getDestinazione().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Origine") && volo.getOrigine().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Orario") && volo.getOrarioPrevisto().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Stato") && volo.getStato().toString().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Data") && volo.getData().toString().toLowerCase().contains(searchText)) {
                match = true;
            }

            if (match) {
                model.addRow(new Object[]{
                        volo.getCodiceVolo(),
                        volo.getCompagnia(),
                        volo.getOrigine(),
                        volo.getDestinazione(),
                        volo.getGate(),
                        volo.getOrarioPrevisto(),
                        volo.getStato(),
                        volo.getData(),
                        volo.getTempoRitardo(),
                        volo.getPostiTotali(),
                        volo.getPostiDisponibili()
                });
            }
        }
    }

    /**
     * Crea il pannello per l'aggiunta di nuovi voli.
     * Contiene un form con campi per inserire tutti i dettagli del volo
     * e pulsanti per aggiungere il volo o pulire il form.
     */
    private void createAddFlightPanel() {
        addFlightPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField airlineField = new JTextField();
        JComboBox<String> flightTypeComboBox = new JComboBox<>(new String[]{"Partenza", "Arrivo"});
        JTextField originField = new JTextField("Napoli");
        JTextField destinationField = new JTextField();
        JTextField timeField = new JTextField();
        JComboBox<StatoVolo> statusComboBox = new JComboBox<>(StatoVolo.values());
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField delayField = new JTextField("0");
        delayField.setEnabled(false);
        JTextField totalSeatsField = new JTextField("0");
        JTextField seatsField = new JTextField("0");

        flightTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) flightTypeComboBox.getSelectedItem();
                if ("Partenza".equals(selectedType)) {
                    originField.setText("Napoli");
                    originField.setEditable(false);
                    destinationField.setEditable(true);
                    destinationField.setText("");
                } else {
                    destinationField.setText("Napoli");
                    destinationField.setEditable(false);
                    originField.setEditable(true);
                    originField.setText("");
                }
            }
        });

        flightTypeComboBox.setSelectedIndex(0);

        statusComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoVolo selectedStatus = (StatoVolo) statusComboBox.getSelectedItem();
                delayField.setEnabled(selectedStatus == StatoVolo.inRitardo);
                if (selectedStatus != StatoVolo.inRitardo) {
                    delayField.setText("0");
                }
            }
        });

        formPanel.add(new JLabel("Compagnia:"));
        formPanel.add(airlineField);
        formPanel.add(new JLabel("Tipo Volo:"));
        formPanel.add(flightTypeComboBox);
        formPanel.add(new JLabel("Origine:"));
        formPanel.add(originField);
        formPanel.add(new JLabel("Destinazione:"));
        formPanel.add(destinationField);
        formPanel.add(new JLabel("Orario:"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Data (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Ritardo (min):"));
        formPanel.add(delayField);
        formPanel.add(new JLabel("Posti Totali:"));
        formPanel.add(totalSeatsField);


        addFlightPanel.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi Volo");
        JButton clearButton = new JButton("Pulisci");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String airline = airlineField.getText();
                    String origin = originField.getText();
                    String destination = destinationField.getText();
                    String time = timeField.getText();
                    StatoVolo status = (StatoVolo) statusComboBox.getSelectedItem();
                    LocalDate date = LocalDate.parse(dateField.getText());
                    int delay = Integer.parseInt(delayField.getText());
                    int totalSeats = Integer.parseInt(totalSeatsField.getText());

                    int airlineNonSpaceChars = countNonSpaceChars(airline);
                    int originNonSpaceChars = countNonSpaceChars(origin);
                    int destinationNonSpaceChars = countNonSpaceChars(destination);
                    int timeNonSpaceChars = countNonSpaceChars(time);

                    if (airline.isEmpty() || origin.isEmpty() || destination.isEmpty() || time.isEmpty() ||
                            totalSeats <= 0) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Tutti i campi sono obbligatori. I posti totali devono essere maggiori di zero",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (airlineNonSpaceChars < 3 || originNonSpaceChars < 3 ||
                            destinationNonSpaceChars < 3 || timeNonSpaceChars < 3) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "I campi di testo devono contenere almeno 3 caratteri diversi dallo spazio",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!origin.equals("Napoli") && !destination.equals("Napoli")) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "L'origine o la destinazione deve essere Napoli",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (status == StatoVolo.inRitardo && delay < 1) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Per i voli in ritardo, il ritardo deve essere maggiore o uguale a 1 minuto",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String code = generateFlightCode(airline);
                    boolean success = false;

                    try {
                        Volo volo = new Volo(code, airline, origin, destination, time, status, date, delay, totalSeats, totalSeats, 0);

                        success = controller.inserisciVolo(volo);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                ex.getMessage(),
                                "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (success) {
                        refreshFlightsTable();

                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Volo aggiunto con successo\nCodice Volo: " + code,
                                SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Errore durante l'inserimento del volo nel database",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                    }

                    airlineField.setText("");
                    flightTypeComboBox.setSelectedIndex(0);
                    timeField.setText("");
                    statusComboBox.setSelectedIndex(0);
                    dateField.setText(LocalDate.now().toString());
                    delayField.setText("0");
                    totalSeatsField.setText("0");
                    seatsField.setText("0");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Errore: " + ex.getMessage(),
                            ERRORE, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                airlineField.setText("");
                flightTypeComboBox.setSelectedIndex(0);
                timeField.setText("");
                statusComboBox.setSelectedIndex(0);
                dateField.setText(LocalDate.now().toString());
                delayField.setText("0");
                totalSeatsField.setText("0");
                seatsField.setText("0");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        addFlightPanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Inserisci Volo", addFlightPanel);
    }


    /**
     * Generates a unique flight code based on the airline name
     *
     * @param airline The airline name
     * @return A unique flight code
     */
    private String generateFlightCode(String airline) {
        String prefix = airline.length() > 1 ?
                airline.substring(0, 2).toUpperCase() :
                airline.substring(0, 1).toUpperCase();

        int randomNum = 1000 + new Random().nextInt(9000);

        String code = prefix + randomNum;

        for (Volo volo : flights) {
            if (volo.getCodiceVolo().equals(code)) {
                return generateFlightCode(airline);
            }
        }

        return code;
    }

    /**
     * Crea il pannello per la gestione dei bagagli smarriti.
     * Include una tabella che mostra tutti i bagagli con il loro stato
     * e permette di modificare lo stato dei bagagli.
     */
    private void createLostBaggagePanel() {
        lostBaggagePanel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }


        };
        model.addColumn("Codice Bagaglio");
        model.addColumn("Stato");
        model.addColumn("Azioni");

        JTable lostBaggageTable = new JTable(model);

        lostBaggageTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        lostBaggageTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String status = value.toString();
                    if (status.equals("smarrito")) {
                        c.setBackground(Color.RED);
                        c.setForeground(Color.WHITE);
                    } else if (status.equals("inElaborazione")) {
                        c.setBackground(Color.YELLOW);
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("caricato")) {
                        c.setBackground(Color.GREEN);
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("ritirabile")) {
                        c.setBackground(new Color(173, 216, 230));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                    }
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }

                return c;
            }
        });
        for (Bagaglio bagaglio : baggages) {
            model.addRow(new Object[]{bagaglio.getCodice(), bagaglio.getStato(), MODIFICA});

        }


        lostBaggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = lostBaggageTable.rowAtPoint(evt.getPoint());
                int col = lostBaggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 2) {
                    String baggageCode = (String) lostBaggageTable.getValueAt(row, 0);
                    showBaggageStatusDialog(baggageCode, row, lostBaggageTable);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(lostBaggageTable);
        lostBaggagePanel.add(scrollPane, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Aggiorna");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollPane scrollPane = (JScrollPane) lostBaggagePanel.getComponent(0);
                JTable lostBaggageTable = (JTable) scrollPane.getViewport().getView();
                updateLostBaggageTable(lostBaggageTable);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        lostBaggagePanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Bagagli Smarriti", lostBaggagePanel);
    }

    /**
     * Updates the lost baggage table with the latest data
     *
     * @param lostBaggageTable The table to update
     */
    private void updateLostBaggageTable(JTable lostBaggageTable) {
        DefaultTableModel model = (DefaultTableModel) lostBaggageTable.getModel();
        model.setRowCount(0);

        baggages = controller.getAllBagagli();
        if (baggages == null) {
            baggages = new ArrayList<>();
        }
        for (Bagaglio bagaglio : baggages) {
            if (bagaglio.getStato() == StatoBagaglio.smarrito) {
                model.addRow(new Object[]{
                        bagaglio.getCodice(),
                        bagaglio.getStato(),
                        MODIFICA
                });
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(AdminDashboard.this,
                    "Nessun bagaglio smarrito trovato",
                    "Informazione", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Shows a dialog to modify the status of a lost baggage
     *
     * @param baggageCode      The code of the baggage to modify
     * @param row              The row in the table
     * @param lostBaggageTable The table containing the baggage
     */
    private void showBaggageStatusDialog(String baggageCode, int row, JTable lostBaggageTable) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        statusComboBox.setPreferredSize(new Dimension(150, 25));

        for (Bagaglio bagaglio : baggages) {
            if (bagaglio.getCodice().equals(baggageCode)) {
                statusComboBox.setSelectedItem(bagaglio.getStato());
                break;
            }
        }
        Prenotazione p = controller.getPrenotazioneByBagaglio(baggageCode);
        panel.add(new JLabel("Codice Bagaglio:"));
        panel.add(new JLabel(baggageCode));
        panel.add(new JLabel("Codice Volo:"));
        panel.add(new JLabel(p.getCodiceVolo()));
        panel.add(new JLabel("Passeggero:"));
        panel.add(new JLabel(p.getPasseggero().getNome() + " " + p.getPasseggero().getCognome()));
        panel.add(new JLabel("Numero documento:"));
        panel.add(new JLabel(p.getPasseggero().getnDocumento()));
        panel.add(new JLabel("Nuovo Stato:"));
        panel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifica Stato Bagaglio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            StatoBagaglio newStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

            for (Bagaglio bagaglio : baggages) {
                if (bagaglio.getCodice().equals(baggageCode)) {
                    bagaglio.setStato(newStatus);

                    lostBaggageTable.setValueAt(newStatus, row, 1);

                    boolean success = controller.aggiornaBagaglio(bagaglio);

                    if (success) {
                        JOptionPane.showMessageDialog(this,
                                "Stato del bagaglio aggiornato con successo",
                                SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Errore durante l'aggiornamento dello stato del bagaglio",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                    }

                    if (newStatus != StatoBagaglio.smarrito) {
                        DefaultTableModel model = (DefaultTableModel) lostBaggageTable.getModel();
                        model.removeRow(row);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Shows a dialog with details about a flight, including bookings and seat occupancy
     *
     * @param flight The flight to show details for
     */
    private void showFlightDetailsDialog(Volo flight) {
        JDialog dialog = new JDialog(this, "Dettagli Volo: " + flight.getCodiceVolo(), true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane dialogTabs = new JTabbedPane();

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Dettagli del volo"));

        detailsPanel.add(new JLabel("Codice Volo:"));
        detailsPanel.add(new JLabel(flight.getCodiceVolo()));
        detailsPanel.add(new JLabel("Compagnia:"));
        detailsPanel.add(new JLabel(flight.getCompagnia()));
        detailsPanel.add(new JLabel("Origine:"));
        detailsPanel.add(new JLabel(flight.getOrigine()));
        detailsPanel.add(new JLabel("Destinazione:"));
        detailsPanel.add(new JLabel(flight.getDestinazione()));
        detailsPanel.add(new JLabel("Orario:"));
        detailsPanel.add(new JLabel(flight.getOrarioPrevisto()));
        detailsPanel.add(new JLabel("Stato:"));
        detailsPanel.add(new JLabel(flight.getStato().toString()));
        detailsPanel.add(new JLabel("Data:"));
        detailsPanel.add(new JLabel(flight.getData().toString()));
        detailsPanel.add(new JLabel("Ritardo:"));
        detailsPanel.add(new JLabel(flight.getTempoRitardo() + " min"));

        int totalSeats = flight.getPostiTotali();
        int availableSeats = flight.getPostiDisponibili();
        int occupiedSeats = totalSeats - availableSeats;

        detailsPanel.add(new JLabel("Posti Totali:"));
        detailsPanel.add(new JLabel(String.valueOf(totalSeats)));
        detailsPanel.add(new JLabel("Posti Disponibili:"));
        detailsPanel.add(new JLabel(String.valueOf(availableSeats)));
        detailsPanel.add(new JLabel("Posti Occupati:"));
        detailsPanel.add(new JLabel(occupiedSeats + " / " + totalSeats));

        detailsPanel.add(new JLabel("Gate:"));
        detailsPanel.add(new JLabel(flight.getGate() > 0 ? String.valueOf(flight.getGate()) : "Non assegnato"));

        infoPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton(MODIFICA);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateFlightDialog(flight, dialog);
            }
        });
        editButtonPanel.add(editButton);

        if (flight.getOrigine().equalsIgnoreCase("NAPOLI")) {
            JButton assignGateButton = new JButton("Assegna Gate");
            assignGateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog gateDialog = new JDialog(dialog, "Assegna Gate", true);
                    gateDialog.setSize(400, 200);
                    gateDialog.setLocationRelativeTo(dialog);

                    JPanel gatePanel = new JPanel(new BorderLayout());
                    gatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                    JLabel gateLabel = new JLabel("Seleziona Gate:");
                    JComboBox<Integer> gateComboBox = new JComboBox<>();

                    for (Gate gate : gates) {
                        gateComboBox.addItem(gate.getCodice());
                    }

                    if (flight.getGate() > 0) {
                        gateComboBox.setSelectedItem(flight.getGate());
                    }

                    formPanel.add(gateLabel);
                    formPanel.add(gateComboBox);

                    gatePanel.add(formPanel, BorderLayout.CENTER);

                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton confirmButton = new JButton("Conferma");
                    JButton cancelButton = new JButton("Annulla");

                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int selectedGate = (Integer) gateComboBox.getSelectedItem();

                            flight.setGate(selectedGate);
                            controller.assegnaGate(selectedGate, flight.getCodiceVolo());

                            for (int i = 0; i < detailsPanel.getComponentCount(); i++) {
                                Component comp = detailsPanel.getComponent(i);
                                if (comp instanceof JLabel && ((JLabel) comp).getText().equals("Gate:")) {
                                    JLabel gateValueLabel = (JLabel) detailsPanel.getComponent(i + 1);
                                    gateValueLabel.setText(String.valueOf(selectedGate));
                                    break;
                                }
                            }

                            detailsPanel.revalidate();
                            detailsPanel.repaint();

                            JOptionPane.showMessageDialog(gateDialog,
                                    "Gate " + selectedGate + " assegnato al volo " + flight.getCodiceVolo(),
                                    "Gate Assegnato", JOptionPane.INFORMATION_MESSAGE);

                            refreshFlightsTable();

                            gateDialog.dispose();
                        }
                    });

                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gateDialog.dispose();
                        }
                    });

                    buttonPanel.add(confirmButton);
                    buttonPanel.add(cancelButton);
                    gatePanel.add(buttonPanel, BorderLayout.SOUTH);

                    gateDialog.setContentPane(gatePanel);
                    gateDialog.setVisible(true);
                }
            });
            editButtonPanel.add(assignGateButton);
        }

        infoPanel.add(editButtonPanel, BorderLayout.SOUTH);


        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Numero Biglietto");

        model.addColumn("Passeggero");
        model.addColumn("Numero documento");

        model.addColumn("Posto");
        model.addColumn("Stato");
        model.addColumn("Bagagli");
        model.addColumn("Azioni");

        List<Prenotazione> prenotazioni = controller.getPrenotazioniByVolo(flight);
        for (Prenotazione p : prenotazioni) {
            model.addRow(new Object[]{p.getNumeroBiglietto(), p.getPasseggero().getNome() + " " + p.getPasseggero().getCognome(), p.getPasseggero().getnDocumento(), p.getPosto(), p.getStato(), p.getBagagli().size(), MODIFICA});

        }


        JTable bookingsTable = new JTable(model);

        bookingsTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    StatoPrenotazione stato = (StatoPrenotazione) value;
                    switch (stato) {
                        case confermata -> {
                            c.setBackground(UIManager.SUCCESS_COLOR);
                            c.setForeground(Color.WHITE);
                        }
                        case inAttesa -> {
                            c.setBackground(Color.YELLOW);
                            c.setForeground(Color.BLACK);
                        }
                        case cancellato -> {
                            c.setBackground(Color.RED);
                            c.setForeground(Color.WHITE);
                        }
                        default -> {
                            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                            c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                        }
                    }
                }

                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        bookingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookingsTable.rowAtPoint(evt.getPoint());
                int col = bookingsTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 6) {
                    String ticketNumber = (String) bookingsTable.getValueAt(row, 0);
                    showBookingDetailsDialog(ticketNumber, row, bookingsTable);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prenotazioni"));
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel baggageButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton manageBaggageButton = new JButton("Gestione Collettiva Bagagli");
        manageBaggageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCollectiveBaggageManagementDialog(flight, bookingsTable);
            }
        });
        baggageButtonPanel.add(manageBaggageButton);
        bookingsPanel.add(baggageButtonPanel, BorderLayout.SOUTH);

        dialogTabs.addTab("Dettagli Volo", infoPanel);
        dialogTabs.addTab("Prenotazioni", bookingsPanel);

        mainPanel.add(dialogTabs, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog to update flight details
     *
     * @param flight       The flight to update
     * @param parentDialog The parent dialog
     */
    private void showUpdateFlightDialog(Volo flight, JDialog parentDialog) {
        JDialog dialog = new JDialog(parentDialog, "Aggiorna Volo: " + flight.getCodiceVolo(), true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(parentDialog);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Aggiorna Volo"));

        JTextField airlineField = new JTextField(flight.getCompagnia());
        JTextField originField = new JTextField(flight.getOrigine());
        JTextField destinationField = new JTextField(flight.getDestinazione());
        JTextField timeField = new JTextField(flight.getOrarioPrevisto());
        JComboBox<StatoVolo> statusComboBox = new JComboBox<>(StatoVolo.values());
        statusComboBox.setSelectedItem(flight.getStato());
        JTextField dateField = new JTextField(flight.getData().toString());
        JTextField delayField = new JTextField(String.valueOf(flight.getTempoRitardo()));
        delayField.setEnabled(flight.getStato() == StatoVolo.inRitardo);
        JTextField totalSeatsField = new JTextField(String.valueOf(flight.getPostiTotali()));
        JTextField availableSeatsField = new JTextField(String.valueOf(flight.getPostiDisponibili()));

        formPanel.add(new JLabel("Compagnia:"));
        formPanel.add(airlineField);
        formPanel.add(new JLabel("Origine:"));
        formPanel.add(originField);
        formPanel.add(new JLabel("Destinazione:"));
        formPanel.add(destinationField);
        formPanel.add(new JLabel("Orario:"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Data (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Ritardo (min):"));
        formPanel.add(delayField);

        statusComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoVolo selectedStatus = (StatoVolo) statusComboBox.getSelectedItem();

                delayField.setEnabled(selectedStatus == StatoVolo.inRitardo);

                if (selectedStatus == StatoVolo.inRitardo && flight.getStato() != StatoVolo.inRitardo) {
                    String delayStr = JOptionPane.showInputDialog(dialog,
                            "Inserisci il ritardo in minuti (deve essere maggiore o uguale a 1):",
                            "Ritardo Volo", JOptionPane.QUESTION_MESSAGE);

                    try {
                        if (delayStr == null) {
                            statusComboBox.setSelectedItem(flight.getStato());
                            return;
                        }

                        int delayValue = Integer.parseInt(delayStr);
                        if (delayValue < 1) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Il ritardo deve essere maggiore o uguale a 1 minuto",
                                    ERRORE, JOptionPane.ERROR_MESSAGE);
                            statusComboBox.setSelectedItem(flight.getStato());
                        } else {
                            delayField.setText(String.valueOf(delayValue));
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                "Inserisci un valore numerico valido per il ritardo",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        statusComboBox.setSelectedItem(flight.getStato());
                    }
                } else if (selectedStatus != StatoVolo.inRitardo) {
                    delayField.setText("0");
                }
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Aggiorna");
        JButton cancelButton = new JButton("Annulla");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String airline = airlineField.getText();
                    String origin = originField.getText();
                    String destination = destinationField.getText();
                    String time = timeField.getText();
                    StatoVolo status = (StatoVolo) statusComboBox.getSelectedItem();
                    LocalDate date = LocalDate.parse(dateField.getText());
                    int delay = Integer.parseInt(delayField.getText());
                    int totalSeats = Integer.parseInt(totalSeatsField.getText());
                    int availableSeats = Integer.parseInt(availableSeatsField.getText());

                    if (airline.isEmpty() || origin.isEmpty() || destination.isEmpty() || time.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                                "Tutti i campi sono obbligatori",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (status == StatoVolo.inRitardo && delay < 1) {
                        JOptionPane.showMessageDialog(dialog,
                                "Per i voli in ritardo, il ritardo deve essere maggiore o uguale a 1 minuto",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (delay < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Errore, inserire un valore di ritardo maggiore o uguale a zero",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (availableSeats > totalSeats) {
                        JOptionPane.showMessageDialog(dialog,
                                "I posti disponibili non possono essere maggiori dei posti totali",
                                ERRORE, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        flight.setCompagnia(airline);
                        flight.setOrigine(origin);
                        flight.setDestinazione(destination);
                        flight.setOrarioPrevisto(time);
                        flight.setStato(status);
                        flight.setData(date);
                        flight.setTempoRitardo(delay);
                        flight.setPostiTotali(totalSeats);
                        flight.setPostiDisponibili(availableSeats);

                        controller.modificaVolo(flight);

                        JOptionPane.showMessageDialog(dialog,
                                "Volo aggiornato con successo",
                                SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                ex.getMessage(),
                                "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    refreshFlightsTable();

                    dialog.dispose();

                    parentDialog.dispose();
                    showFlightDetailsDialog(flight);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Inserisci valori numerici validi per ritardo, posti totali e posti disponibili",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Errore durante l'aggiornamento del volo: " + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog to manage all baggage for a flight collectively
     *
     * @param flight        The flight
     * @param bookingsTable The bookings table
     */
    private void showCollectiveBaggageManagementDialog(Volo flight, JTable bookingsTable) {
        JDialog dialog = new JDialog(this, "Gestione Collettiva Bagagli: " + flight.getCodiceVolo(), true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni"));

        infoPanel.add(new JLabel("Volo: " + flight.getCodiceVolo() + " - " + flight.getCompagnia()));
        infoPanel.add(new JLabel("Tratta: " + flight.getOrigine() + " -> " + flight.getDestinazione()));
        infoPanel.add(new JLabel("Data: " + flight.getData().toString()));

        int totalBaggage = 0;
        for (int i = 0; i < bookingsTable.getRowCount(); i++) {
            totalBaggage += Integer.parseInt(bookingsTable.getValueAt(i, 5).toString());
        }

        infoPanel.add(new JLabel("Totale Bagagli: " + totalBaggage));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));

        JLabel statusLabel = new JLabel("Imposta stato per tutti i bagagli:");
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());

        actionPanel.add(statusLabel);
        actionPanel.add(statusComboBox);

        mainPanel.add(actionPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Applica");
        JButton cancelButton = new JButton("Annulla");

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoBagaglio selectedStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

                boolean success = controller.aggiornaAllBagagli(flight.getCodiceVolo(), selectedStatus);


                if (success) {
                    JOptionPane.showMessageDialog(dialog,
                            "Stato '" + selectedStatus + "' applicato a tutti i bagagli del volo " + flight.getCodiceVolo(),
                            SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Errore durante l'aggiornamento dello stato dei bagagli",
                            ERRORE, JOptionPane.ERROR_MESSAGE);
                }


                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }


    /**
     * Shows a dialog to modify a booking
     *
     * @param ticketNumber  The ticket number of the booking to modify
     * @param row           The row in the table
     * @param bookingsTable The table containing the booking
     */
    private void showBookingDetailsDialog(String ticketNumber, int row, JTable bookingsTable) {
        JDialog dialog = new JDialog(this, "Modifica Prenotazione: " + ticketNumber, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane dialogTabs = new JTabbedPane();

        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dettagli Prenotazione"));

        String passengerName = (String) bookingsTable.getValueAt(row, 1);
        String ndoc = (String) bookingsTable.getValueAt(row, 2);

        String seat = (String) bookingsTable.getValueAt(row, 3);
        StatoPrenotazione status = (StatoPrenotazione) bookingsTable.getValueAt(row, 4);
        int baggageCount = (int) bookingsTable.getValueAt(row, 5);

        JTextField nameField = new JTextField(passengerName);
        JTextField seatField = new JTextField(seat);
        JComboBox<StatoPrenotazione> statusComboBox = new JComboBox<>(StatoPrenotazione.values());

        statusComboBox.setSelectedItem(status);

        formPanel.add(new JLabel("Numero Biglietto:"));
        formPanel.add(new JLabel(ticketNumber));
        formPanel.add(new JLabel("Passeggero:"));
        formPanel.add(new JLabel(passengerName));
        formPanel.add(new JLabel("N°Documento:"));
        formPanel.add(new JLabel(ndoc));
        formPanel.add(new JLabel("Posto:"));
        formPanel.add(new JLabel(seat));
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Bagagli:"));
        formPanel.add(new JLabel(String.valueOf(baggageCount)));

        bookingPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bookingSavePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBookingButton = new JButton("Salva Prenotazione");
        saveBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingsTable.setValueAt(nameField.getText(), row, 1);
                bookingsTable.setValueAt(seatField.getText(), row, 3);

                StatoPrenotazione selectedStatus = (StatoPrenotazione) statusComboBox.getSelectedItem();

                bookingsTable.setValueAt(selectedStatus, row, 4);

                controller.aggiornaPrenotazione(selectedStatus, ticketNumber);
                refreshFlightsTable();

                JOptionPane.showMessageDialog(dialog,
                        "Prenotazione aggiornata con successo",
                        SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bookingSavePanel.add(saveBookingButton);
        bookingPanel.add(bookingSavePanel, BorderLayout.SOUTH);

        JPanel baggagePanel = new JPanel(new BorderLayout());
        baggagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel baggageModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        baggageModel.addColumn("Codice Bagaglio");
        baggageModel.addColumn("Stato");
        baggageModel.addColumn("Azioni");

        List<Bagaglio> bagagli = controller.getBagagliByPrenotazione(ticketNumber);

        for (Bagaglio b : bagagli) {
            baggageModel.addRow(new Object[]{b.getCodice(), b.getStato(), MODIFICA});
        }

        JTable baggageTable = new JTable(baggageModel);

        JComboBox<StatoBagaglio> statusEditor = new JComboBox<>(StatoBagaglio.values());
        baggageTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(statusEditor));

        baggageTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    StatoBagaglio status = (StatoBagaglio) value;
                    switch (status) {
                        case smarrito -> {
                            c.setBackground(Color.RED);
                            c.setForeground(Color.WHITE);
                        }
                        case inElaborazione -> {
                            c.setBackground(Color.YELLOW);
                            c.setForeground(Color.BLACK);
                        }
                        case caricato -> {
                            c.setBackground(Color.GREEN);
                            c.setForeground(Color.BLACK);
                        }
                        case ritirabile -> {
                            c.setBackground(new Color(173, 216, 230));
                            c.setForeground(Color.BLACK);
                        }
                        default -> {
                            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                            c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                        }
                    }
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }


                return c;
            }
        });

        baggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = baggageTable.rowAtPoint(evt.getPoint());
                int col = baggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 2) {
                    String baggageCode = (String) baggageTable.getValueAt(row, 0);
                    StatoBagaglio currentStatus = (StatoBagaglio) baggageTable.getValueAt(row, 1);
                    Bagaglio bagaglio = new Bagaglio(baggageCode, currentStatus);
                    showBaggageStatusDialog(bagaglio, row, baggageTable);
                }
            }
        });

        JScrollPane baggageScrollPane = new JScrollPane(baggageTable);
        baggageScrollPane.setBorder(BorderFactory.createTitledBorder("Bagagli"));
        baggagePanel.add(baggageScrollPane, BorderLayout.CENTER);

        dialogTabs.addTab("Prenotazione", bookingPanel);
        dialogTabs.addTab("Bagagli", baggagePanel);

        mainPanel.add(dialogTabs, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog to modify baggage status
     *
     * @param bagaglio     The baggage object
     * @param row          The row in the table
     * @param baggageTable The baggage table
     */
    private void showBaggageStatusDialog(Bagaglio bagaglio, int row, JTable baggageTable) {
        JDialog dialog = new JDialog(this, "Modifica Stato Bagaglio: " + bagaglio.getCodice(), true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel codeLabel = new JLabel("Codice Bagaglio:");
        JLabel codeValueLabel = new JLabel(bagaglio.getCodice());

        JLabel statusLabel = new JLabel("Stato:");
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        statusComboBox.setSelectedItem(bagaglio.getStato());

        formPanel.add(codeLabel);
        formPanel.add(codeValueLabel);
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Salva");
        JButton cancelButton = new JButton("Annulla");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoBagaglio selectedStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

                baggageTable.setValueAt(selectedStatus, row, 1);

                bagaglio.setStato(selectedStatus);

                boolean success = controller.aggiornaBagaglio(bagaglio);

                if (success) {
                    JOptionPane.showMessageDialog(dialog,
                            "Stato del bagaglio aggiornato con successo",
                            SUCCESSO, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Errore durante l'aggiornamento dello stato del bagaglio",
                            ERRORE, JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }


}
