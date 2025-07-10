package gui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe che implementa l'interfaccia grafica per la dashboard dell'utente generico.
 * Permette agli utenti di visualizzare voli disponibili, effettuare prenotazioni,
 * gestire le proprie prenotazioni e tracciare lo stato dei bagagli.
 * Fornisce funzionalità per la ricerca di voli, la selezione dei posti e la segnalazione di bagagli smarriti.
 */
public class UserDashboard extends JFrame {
    /**
     * Pannello principale che contiene tutti gli elementi dell'interfaccia.
     */
    private JPanel mainPanel;

    /**
     * Pannello a schede che organizza le diverse sezioni della dashboard.
     */
    private JTabbedPane tabbedPane;

    /**
     * Pannello che contiene la visualizzazione dei voli disponibili.
     */
    private JPanel homepagePanel;

    /**
     * Tabella che visualizza l'elenco dei voli disponibili.
     */
    private JTable flightsTable;

    /**
     * Pannello che contiene i controlli per la ricerca dei voli.
     */
    private JPanel searchPanel;

    /**
     * Campo di testo per l'inserimento dei criteri di ricerca.
     */
    private JTextField searchField;

    /**
     * Menu a tendina per selezionare il tipo di ricerca (es. per codice, destinazione).
     */
    private JComboBox<String> searchTypeComboBox;

    /**
     * Pannello che contiene il form per la prenotazione dei voli.
     */
    private JPanel bookingPanel;

    /**
     * Menu a tendina per selezionare il volo da prenotare.
     */
    private JComboBox<String> flightComboBox;

    /**
     * Campo di testo per l'inserimento del nome del passeggero.
     */
    private JTextField passengerNameField;

    /**
     * Campo di testo per l'inserimento del cognome del passeggero.
     */
    private JTextField passengerSurnameField;

    /**
     * Campo di testo per l'inserimento del numero di documento del passeggero.
     */
    private JTextField passengerDocumentField;

    /**
     * Controllo numerico per selezionare il numero di bagagli da registrare.
     */
    private JSpinner baggageCountSpinner;


    /**
     * Tabella che visualizza i bagagli associati alle prenotazioni.
     */
    private JTable baggageTable;

    /**
     * Pannello che contiene la visualizzazione dei voli prenotati dall'utente.
     */
    private JPanel myFlightsPanel;

    /**
     * Tabella che visualizza i voli prenotati dall'utente.
     */
    private JTable myFlightsTable;

    /**
     * Campo di testo per filtrare i voli prenotati.
     */
    private JTextField myFlightsSearchField;

    /**
     * Oggetto che gestisce l'ordinamento e il filtraggio della tabella dei voli prenotati.
     */
    private TableRowSorter<DefaultTableModel> myFlightsSorter;

    /**
     * Pannello che contiene i controlli per il tracciamento dei bagagli.
     */
    private JPanel baggageTrackingPanel;

    /**
     * Campo di testo per l'inserimento del codice del bagaglio da tracciare.
     */
    private JTextField baggageCodeField;



    /**
     * Lista di tutti i voli disponibili nel sistema.
     */
    private List<Volo> flights;

    /**
     * Lista delle prenotazioni effettuate dall'utente.
     */
    private List<Prenotazione> bookings;

    /**
     * Lista dei bagagli associati alle prenotazioni dell'utente.
     */
    private List<Bagaglio> baggages;

    /**
     * Utente attualmente loggato.
     */
    private Utente user;

    /**
     * Riferimento al controller che gestisce la logica dell'applicazione.
     */
    private controller.Controller controller;

    /**
     * Costruttore della dashboard utente.
     * Inizializza l'interfaccia grafica e carica i dati necessari per l'utente.
     * Configura i pannelli per la visualizzazione dei voli, prenotazioni e monitoraggio bagagli.
     * 
     * @param user L'utente generico che sta utilizzando la dashboard
     * @throws IllegalArgumentException Se l'utente non ha il ruolo di utente generico
     */
    public UserDashboard(Utente user) {

        if (!user.isGenerico()) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo di utente generico");
        }
        this.user = user;

        this.controller = new controller.Controller();

        initializeTestData();

        setTitle("Dashboard Utente - Aeroporto di Napoli");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.styleFrame(this);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIManager.BACKGROUND_COLOR);

        tabbedPane = new JTabbedPane();
        UIManager.styleTabbedPane(tabbedPane);

        createHomepagePanel();
        createBookingPanel();
        createMyFlightsPanel();
        createBaggageTrackingPanel();

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIManager.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Benvenuto, " + user.getNome() + " " + user.getCognome() + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(UIManager.TITLE_FONT);
        welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(UIManager.BACKGROUND_COLOR);
        JButton logoutButton = new JButton("Log Out");
        UIManager.styleButton(logoutButton);
        logoutButton.addActionListener(e -> {
            dispose();
            Login loginFrame = new Login(controller);
            loginFrame.setVisible(true);
        });

        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        setContentPane(mainPanel);
    }

    /**
     * Conta il numero di caratteri non spazio in una stringa.
     * Utilizzato per la validazione degli input dell'utente.
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
     * Inizializza i dati necessari per la dashboard utente.
     * Carica tutti i voli disponibili, le prenotazioni dell'utente e i suoi bagagli dal controller.
     * Se i dati non sono disponibili, inizializza liste vuote.
     */
    private void initializeTestData() {

        flights = controller.getAllVoli();
        if (flights == null) flights = new ArrayList<>();
        bookings = controller.getPrenotazioneByUtente(user);
        baggages = controller.getBagagliByUtente(user);
    }


    /**
     * Crea il pannello principale della homepage.
     * Contiene una barra di ricerca e una tabella che mostra tutti i voli disponibili.
     * Permette all'utente di filtrare i voli in base a diversi criteri.
     */
    private void createHomepagePanel() {
        homepagePanel = new JPanel(new BorderLayout());
        homepagePanel.setBackground(UIManager.BACKGROUND_COLOR);

        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
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

        searchTypeComboBox = new JComboBox<>(new String[]{
                "Data", "Compagnia", "Codice Volo", "Aeroporto di Partenza", "Aeroporto di Arrivo"
        });
        UIManager.styleComboBox(searchTypeComboBox);

        searchField = new JTextField(20);
        UIManager.styleTextField(searchField);

        JButton searchButton = new JButton("Cerca");
        UIManager.styleButton(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterFlights();
            }
        });

        JLabel searchLabel = new JLabel("Cerca per:");
        searchLabel.setFont(UIManager.NORMAL_FONT);
        searchLabel.setForeground(UIManager.TEXT_COLOR);

        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        homepagePanel.add(searchPanel, BorderLayout.NORTH);

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
        model.addColumn("Orario");
        model.addColumn("Stato");
        model.addColumn("Data");
        model.addColumn("Ritardo (min)");

        for (Volo volo : flights) {
            model.addRow(new Object[]{
                    volo.getCodiceVolo(),
                    volo.getCompagnia(),
                    volo.getOrigine(),
                    volo.getDestinazione(),
                    volo.getOrarioPrevisto(),
                    volo.getStato(),
                    volo.getData(),
                    volo.getTempoRitardo()
            });
        }

        flightsTable = new JTable(model);

        UIManager.styleTable(flightsTable);

        flightsTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
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
                }

                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(UIManager.BACKGROUND_COLOR);
        homepagePanel.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIManager.BACKGROUND_COLOR);
        homepagePanel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Home", homepagePanel);
    }

    /**
     * Filtra i voli nella tabella in base ai criteri di ricerca specificati dall'utente.
     * Utilizza il testo inserito nel campo di ricerca e il tipo di ricerca selezionato
     * per filtrare i voli visualizzati nella tabella.
     */
    private void filterFlights() {
        String searchText = searchField.getText().toLowerCase();
        String searchType = (String) searchTypeComboBox.getSelectedItem();

        DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();
        model.setRowCount(0);

        for (Volo volo : flights) {
            boolean match = false;

            if (searchText.isEmpty()) {
                match = true;
            } else if (searchType.equals("Data") && volo.getData().toString().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Compagnia") && volo.getCompagnia().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Codice Volo") && volo.getCodiceVolo().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Aeroporto di Partenza") && volo.getOrigine().toLowerCase().contains(searchText)) {
                match = true;
            } else if (searchType.equals("Aeroporto di Arrivo") && volo.getDestinazione().toLowerCase().contains(searchText)) {
                match = true;
            }

            if (match) {
                model.addRow(new Object[]{
                        volo.getCodiceVolo(),
                        volo.getCompagnia(),
                        volo.getOrigine(),
                        volo.getDestinazione(),
                        volo.getOrarioPrevisto(),
                        volo.getStato(),
                        volo.getData(),
                        volo.getTempoRitardo()
                });
            }
        }
    }


    /**
     * Crea il pannello per la prenotazione dei voli.
     * Contiene un form con campi per selezionare il volo, inserire i dati del passeggero
     * e specificare il numero di bagagli. Include un pulsante per effettuare la prenotazione.
     */
    private void createBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nuova Prenotazione"));

        flightComboBox = new JComboBox<>();
        for (Volo volo : flights) {
            if (volo.getStato() != StatoVolo.cancellato && volo.getStato() != StatoVolo.atterrato && volo.getPostiDisponibili() > 0) {
                flightComboBox.addItem(volo.getCodiceVolo() + " - " + volo.getCompagnia() + " (" +
                        volo.getOrigine() + " -> " + volo.getDestinazione() + ")");
            }
        }

        passengerNameField = new JTextField();
        passengerSurnameField = new JTextField();
        passengerDocumentField = new JTextField();
        baggageCountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));

        formPanel.add(new JLabel("Volo:"));
        formPanel.add(flightComboBox);
        formPanel.add(new JLabel("Nome Passeggero:"));
        formPanel.add(passengerNameField);
        formPanel.add(new JLabel("Cognome Passeggero:"));
        formPanel.add(passengerSurnameField);
        formPanel.add(new JLabel("Numero Documento:"));
        formPanel.add(passengerDocumentField);
        formPanel.add(new JLabel("Numero Bagagli:"));
        formPanel.add(baggageCountSpinner);

        JButton bookButton = new JButton("Prenota");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bookFlight();
            }
        });

        JPanel bookButtonPanel = new JPanel();
        bookButtonPanel.add(bookButton);
        formPanel.add(new JLabel(""));
        formPanel.add(bookButtonPanel);

        bookingPanel.add(formPanel, BorderLayout.NORTH);


        tabbedPane.addTab("Prenotazione Voli", bookingPanel);

    }

    /**
     * Gestisce la prenotazione di un volo.
     * Raccoglie i dati inseriti dall'utente, valida i campi, seleziona un posto,
     * crea una nuova prenotazione e registra eventuali bagagli associati.
     * Mostra messaggi di conferma o errore all'utente.
     */
    private void bookFlight() {
        String flightString = (String) flightComboBox.getSelectedItem();
        if (flightString == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un volo", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String flightCode = flightString.split(" - ")[0];
        String name = passengerNameField.getText();
        String surname = passengerSurnameField.getText();
        String document = passengerDocumentField.getText();
        int baggageCount = (int) baggageCountSpinner.getValue();

        if (name.isEmpty() || surname.isEmpty() || document.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, cognome e documento sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (countNonSpaceChars(name) < 3 || countNonSpaceChars(surname) < 3 || countNonSpaceChars(document) < 3) {
            JOptionPane.showMessageDialog(this, "Nome, cognome e documento devono contenere almeno 3 caratteri diversi dallo spazio", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String seat = selectSeat(flightCode);
        if (seat == null || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "È necessario selezionare un posto", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ticketNumber = "TKT" + System.currentTimeMillis() % 10000;

        Passeggero passenger = new Passeggero(name, surname, document);
        Prenotazione booking = new Prenotazione(flightCode, ticketNumber, seat, StatoPrenotazione.inAttesa, passenger);


        StringBuilder baggageInfo = new StringBuilder();
        if (baggageCount > 0) {
            List<Bagaglio> bookingBaggages = new ArrayList<>();
            baggageInfo.append("\n\nID Bagagli:");
            for (int i = 0; i < baggageCount; i++) {
                String baggageCode = "BAG" + ticketNumber + "-" + (i + 1);
                Bagaglio baggage = new Bagaglio(baggageCode, StatoBagaglio.inElaborazione);
                bookingBaggages.add(baggage);
                baggages.add(baggage);
                baggageInfo.append("\n- ").append(baggageCode);
            }
            booking.setBagagli(bookingBaggages);
        }

        bookings.add(booking);
        controller.creaPrenotazione(booking, flightCode, user);

        passengerNameField.setText("");
        passengerSurnameField.setText("");
        passengerDocumentField.setText("");
        baggageCountSpinner.setValue(0);
        updateMyFlightsTable();
        updateBaggageTable(baggageTable);
        baggages = controller.getBagagliByUtente(user);
        JOptionPane.showMessageDialog(this,
                "Prenotazione effettuata con successo!\nNumero Biglietto: " + ticketNumber + baggageInfo.toString(),
                "Prenotazione Confermata", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra una finestra di dialogo per la selezione del posto a sedere.
     * Visualizza una rappresentazione grafica dei posti disponibili e occupati,
     * permettendo all'utente di selezionare un posto libero.
     * 
     * @param flightCode Il codice del volo per cui selezionare il posto
     * @return Il posto selezionato o null se nessun posto è stato selezionato
     */
    private String selectSeat(String flightCode) {
        Volo selectedFlight = flights.stream()
                .filter(v -> v.getCodiceVolo().equals(flightCode))
                .findFirst()
                .orElse(null);

        if (selectedFlight == null) {
            JOptionPane.showMessageDialog(this,
                    "Volo non trovato",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        List<Posto> posti;
        try {
            posti = controller.getPostiByVolo(flightCode);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento posti: " + ex.getMessage(),
                    "Errore DB",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        posti.sort(Comparator.comparing(Posto::getSeatNumber, (a, b) -> {
            int filaA = Integer.parseInt(a.replaceAll("\\D", ""));
            int filaB = Integer.parseInt(b.replaceAll("\\D", ""));
            if (filaA != filaB) return filaA - filaB;
            return a.replaceAll("\\d", "").charAt(0) - b.replaceAll("\\d", "").charAt(0);
        }));

        JDialog seatDialog = new JDialog(this, "Seleziona Posto - Volo " + flightCode, true);
        seatDialog.setSize(700, 600);
        seatDialog.setLocationRelativeTo(this);

        JPanel seatPanel = new JPanel(new BorderLayout(10, 10));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        seatPanel.add(new JLabel(
                        "Volo: " + flightCode + " - Posti disponibili: " + selectedFlight.getPostiDisponibili()),
                BorderLayout.NORTH);

        JPanel airplanePanel = new JPanel(new BorderLayout(10, 10));
        airplanePanel.setBorder(BorderFactory.createTitledBorder("Seleziona un posto disponibile"));

        int cols = 6;
        int rows = (int) Math.ceil(posti.size() / (double) cols);
        JPanel seatGrid = new JPanel(new GridLayout(rows, cols + 1, 5, 5));

        final String[] selectedSeat = {null};

        List<JButton> buttons = new ArrayList<>();
        for (Posto p : posti) {
            String seatNumber = p.getSeatNumber();
            JButton btn = new JButton(seatNumber);
            btn.setPreferredSize(new Dimension(60, 40));
            if (p.isOccupato()) {
                btn.setEnabled(false);
                btn.setBackground(Color.RED);
                btn.setText(seatNumber + " (X)");
            } else {
                btn.setBackground(Color.GREEN);
                btn.addActionListener(e -> {
                    if (selectedSeat[0] != null) {
                        for (Component c : seatGrid.getComponents()) {
                            if (c instanceof JButton old &&
                                    old.getText().startsWith(selectedSeat[0]) &&
                                    old.isEnabled()) {
                                old.setBackground(Color.GREEN);
                                old.setText(selectedSeat[0]);
                            }
                        }
                    }
                    selectedSeat[0] = seatNumber;
                    btn.setBackground(Color.BLUE);
                    btn.setText(seatNumber + " (✓)");
                });
            }
            buttons.add(btn);
        }

        int btnIndex = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols + 1; c++) {
                if (c == 3) {
                    JPanel aisle = new JPanel();
                    aisle.setBackground(Color.LIGHT_GRAY);
                    seatGrid.add(aisle);
                } else if (btnIndex < buttons.size()) {
                    seatGrid.add(buttons.get(btnIndex));
                    btnIndex++;
                } else {
                    JPanel empty = new JPanel();
                    empty.setOpaque(false);
                    seatGrid.add(empty);
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(seatGrid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        airplanePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legend.add(new JLabel("Libero", JLabel.LEFT));
        legend.add(createLegendBox(Color.GREEN));
        legend.add(new JLabel("Occupato", JLabel.LEFT));
        legend.add(createLegendBox(Color.RED));
        legend.add(new JLabel("Selezionato", JLabel.LEFT));
        legend.add(createLegendBox(Color.BLUE));
        airplanePanel.add(legend, BorderLayout.NORTH);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton confirm = new JButton("Conferma");
        confirm.addActionListener(e -> {
            if (selectedSeat[0] != null) {
                seatDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(seatDialog,
                        "Seleziona un posto prima di confermare",
                        "Avviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton cancel = new JButton("Annulla");
        cancel.addActionListener(e -> {
            selectedSeat[0] = null;
            seatDialog.dispose();
        });
        footer.add(confirm);
        footer.add(cancel);

        seatPanel.add(airplanePanel, BorderLayout.CENTER);
        seatPanel.add(footer, BorderLayout.SOUTH);
        seatDialog.setContentPane(seatPanel);
        seatDialog.setVisible(true);

        return selectedSeat[0];
    }


    /**
     * Crea il pannello per la visualizzazione dei voli prenotati dall'utente.
     * Contiene una barra di ricerca e una tabella che mostra tutte le prenotazioni dell'utente.
     * Permette di filtrare le prenotazioni, visualizzare dettagli e modificare informazioni.
     */
    private void createMyFlightsPanel() {
        myFlightsPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Prenotazioni"));
        myFlightsSearchField = new JTextField(20);
        JButton searchButton = new JButton("Cerca");
        UIManager.styleButton(searchButton);
        searchButton.addActionListener(e -> filterMyFlights());
        searchPanel.add(new JLabel("Cerca per nome passeggero o numero volo:"));
        searchPanel.add(myFlightsSearchField);
        searchPanel.add(searchButton);
        myFlightsPanel.add(searchPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Codice Volo", "Numero Biglietto", "Passeggero", "N° Documento", "Posto", "Stato", "Bagagli", "Azioni"},
                0
        ) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 7) return String.class;
                return super.getColumnClass(col);
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        myFlightsTable = new JTable(model);
        myFlightsSorter = new TableRowSorter<>(model);
        myFlightsTable.setRowSorter(myFlightsSorter);

        myFlightsTable.getColumnModel().getColumn(5)
                .setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        JLabel c = (JLabel) super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        if (value instanceof StatoPrenotazione st) {
                            if (st == StatoPrenotazione.confermata) {
                                c.setBackground(UIManager.SUCCESS_COLOR);
                                c.setForeground(Color.WHITE);
                            } else if (st == StatoPrenotazione.inAttesa) {
                                c.setBackground(Color.YELLOW);
                                c.setForeground(Color.BLACK);
                            } else if (st == StatoPrenotazione.cancellato) {
                                c.setBackground(Color.RED);
                                c.setForeground(Color.WHITE);
                            }
                        }
                        c.setHorizontalAlignment(JLabel.CENTER);
                        return c;
                    }
                });

        myFlightsTable.getColumnModel().getColumn(7)
                .setCellRenderer((JTable table, Object value, boolean isSelected,
                                  boolean hasFocus, int row, int column) -> {
                    String text = value != null ? value.toString() : "";


                    return new JLabel(text, SwingConstants.CENTER);
                });

        myFlightsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = myFlightsTable.rowAtPoint(e.getPoint());
                int viewCol = myFlightsTable.columnAtPoint(e.getPoint());
                if (viewRow < 0) return;
                int modelRow = myFlightsTable.convertRowIndexToModel(viewRow);
                Prenotazione booking = bookings.get(modelRow);

                if (viewCol == 7 && "Modifica".equals(
                        myFlightsTable.getValueAt(viewRow, 7))) {
                    if (booking.getStato() == StatoPrenotazione.inAttesa) {
                        editPassengerInfo(booking);
                    }
                } else if (viewCol != 7) {
                    showBaggageDetails(booking);
                }
            }
        });

        myFlightsPanel.add(new JScrollPane(myFlightsTable), BorderLayout.CENTER);
        tabbedPane.addTab("I Miei Voli", myFlightsPanel);

        updateMyFlightsTable();
    }


    /**
     * Mostra una finestra di dialogo per modificare le informazioni del passeggero.
     * Permette di aggiornare nome, cognome e numero di documento del passeggero
     * associato a una prenotazione esistente.
     * 
     * @param booking La prenotazione di cui modificare le informazioni del passeggero
     */
    private void editPassengerInfo(Prenotazione booking) {
        JDialog dialog = new JDialog(this, "Modifica Informazioni Passeggero", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Passeggero passeggero = booking.getPasseggero();

        JTextField nomeField = new JTextField(passeggero.getNome());
        JTextField cognomeField = new JTextField(passeggero.getCognome());
        JTextField documentoField = new JTextField(passeggero.getnDocumento());

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Cognome:"));
        formPanel.add(cognomeField);
        formPanel.add(new JLabel("Numero Documento:"));
        formPanel.add(documentoField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salva");
        JButton cancelButton = new JButton("Annulla");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText().trim();
                String cognome = cognomeField.getText().trim();
                String documento = documentoField.getText().trim();

                if (nome.isEmpty() || cognome.isEmpty() || documento.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tutti i campi sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = controller.updatePassengerInfo(booking.getNumeroBiglietto(), nome, cognome, documento);

                if (success) {
                    passeggero.setNome(nome);
                    passeggero.setCognome(cognome);
                    passeggero.setnDocumento(documento);

                    updateMyFlightsTable();

                    JOptionPane.showMessageDialog(dialog, "Informazioni passeggero aggiornate con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Errore durante l'aggiornamento delle informazioni", "Errore", JOptionPane.ERROR_MESSAGE);
                }
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

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    /**
     * Filtra le prenotazioni nella tabella dei voli prenotati.
     * Utilizza il testo inserito nel campo di ricerca per filtrare le prenotazioni
     * in base al numero del biglietto o al nome del passeggero.
     */
    private void filterMyFlights() {
        String text = myFlightsSearchField.getText().trim();
        if (text.isEmpty()) {
            myFlightsSorter.setRowFilter(null);
        } else {
            myFlightsSorter.setRowFilter(
                    RowFilter.regexFilter("(?i)" + Pattern.quote(text), 1, 2)
            );
        }
    }

    /**
     * Aggiorna la tabella dei voli prenotati con i dati più recenti.
     * Recupera tutte le prenotazioni dell'utente dal controller e aggiorna la tabella dell'interfaccia.
     * Mostra informazioni su codice volo, numero biglietto, passeggero, posto, stato e bagagli.
     */
    private void updateMyFlightsTable() {
        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();
        model.setRowCount(0);
        bookings = controller.getPrenotazioneByUtente(user);
        for (Prenotazione b : bookings) {
            Passeggero p = b.getPasseggero();
            int bagCount = b.getBagagli() != null ? b.getBagagli().size() : 0;
            String action = b.getStato() == StatoPrenotazione.inAttesa ? "Modifica" : "";
            model.addRow(new Object[]{
                    b.getCodiceVolo(),
                    b.getNumeroBiglietto(),
                    p.getNome() + " " + p.getCognome(),
                    p.getnDocumento(),
                    b.getPosto(),
                    b.getStato(),
                    bagCount,
                    action
            });
        }
    }

    /**
     * Crea il pannello per il monitoraggio dei bagagli.
     * Contiene una barra di ricerca e una tabella che mostra tutti i bagagli dell'utente.
     * Permette di cercare bagagli, visualizzare il loro stato e segnalare bagagli smarriti.
     */
    private void createBaggageTrackingPanel() {
        baggageTrackingPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Bagagli"));

        JTextField flightCodeField = new JTextField();
        baggageCodeField = new JTextField();

        JButton searchButton = new JButton("Cerca");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flightCode = flightCodeField.getText().trim();
                String baggageCode = baggageCodeField.getText().trim();

                DefaultTableModel model = (DefaultTableModel) baggageTable.getModel();
                model.setRowCount(0);

                for (Bagaglio baggage : baggages) {
                    String flightInfo = "N/A";
                    String tckInfo = "N/A";
                    for (Prenotazione booking : bookings) {
                        if (booking.getBagagli() != null) {
                            for (Bagaglio b : booking.getBagagli()) {
                                if (b.getCodice().equals(baggage.getCodice())) {
                                    tckInfo = booking.getCodiceVolo();
                                    flightInfo = booking.getCodiceVolo();
                                    break;
                                }
                            }
                        }
                    }

                    boolean matchesFlightCode = flightCode.isEmpty() || flightInfo.toLowerCase().contains(flightCode.toLowerCase());
                    boolean matchesBaggageCode = baggageCode.isEmpty() || baggage.getCodice().toLowerCase().contains(baggageCode.toLowerCase());

                    if (matchesFlightCode && matchesBaggageCode) {
                        String status = baggage.getStato().toString();
                        if (baggage.getStato().toString().equals("smarrito")) {
                            status = "Smarrimento segnalato";
                        }

                        String actionText = (baggage.getStato() == StatoBagaglio.caricato || baggage.getStato() == StatoBagaglio.ritirabile) ? "Segnala smarrimento" : "";

                        model.addRow(new Object[]{
                                flightInfo,
                                tckInfo,
                                baggage.getCodice(),
                                status,
                                actionText
                        });
                    }
                }
            }
        });

        formPanel.add(new JLabel("Codice Volo:"));
        formPanel.add(flightCodeField);
        formPanel.add(new JLabel("Codice Bagaglio:"));
        formPanel.add(baggageCodeField);
        formPanel.add(new JLabel(""));
        formPanel.add(searchButton);

        baggageTrackingPanel.add(formPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Volo");
        model.addColumn("Codice Biglietto");

        model.addColumn("Codice Bagaglio");
        model.addColumn("Stato");
        model.addColumn("Azioni");

        baggageTable = new JTable(model);
        updateBaggageTable(baggageTable);
        baggageTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) value;
                if (status != null) {
                    if (status.contains("Smarrimento segnalato") || status.equals("smarrito")) {
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

        baggageTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String status = (String) table.getValueAt(row, 3);

                if (status != null && (status.equals("caricato") || status.equals("ritirabile"))) {
                    JButton button = new JButton("Segnala Smarrimento");
                    button.setBackground(new Color(255, 200, 200));
                    return button;
                }

                return new JLabel("");
            }
        });

        baggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = baggageTable.rowAtPoint(evt.getPoint());
                int col = baggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 4) {
                    String baggageCode = (String) baggageTable.getValueAt(row, 2);
                    reportLostBaggageFromTable(baggageCode, baggageTable);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(baggageTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tutti i Bagagli"));
        baggageTrackingPanel.add(scrollPane, BorderLayout.CENTER);
        updateBaggageTable(baggageTable);
        JButton refreshButton = new JButton("Aggiorna");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBaggageTable(baggageTable);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        baggageTrackingPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateBaggageTable(baggageTable);

        tabbedPane.addTab("Monitoraggio Bagagli", baggageTrackingPanel);
    }

    /**
     * Aggiorna la tabella dei bagagli con i dati più recenti.
     * Recupera tutti i bagagli dell'utente e aggiorna la tabella dell'interfaccia
     * con informazioni su volo, biglietto, codice bagaglio e stato.
     * 
     * @param baggageTable La tabella da aggiornare
     */
    private void updateBaggageTable(JTable baggageTable) {
        DefaultTableModel model = (DefaultTableModel) baggageTable.getModel();
        model.setRowCount(0);

        for (Bagaglio baggage : baggages) {
            String flightInfo = "N/A";
            String tckInfo = "N/A";

            for (Prenotazione booking : bookings) {

                if (booking.getBagagli() != null) {
                    for (Bagaglio b : booking.getBagagli()) {
                        if (b.getCodice().equals(baggage.getCodice())) {
                            flightInfo = booking.getCodiceVolo();
                            tckInfo = booking.getNumeroBiglietto();
                            break;
                        }
                    }
                }
            }

            String status = baggage.getStato().toString();
            if (baggage.getStato().toString().equals("smarrito")) {
                status = "Smarrimento segnalato";
            }

            String actionText = (baggage.getStato() == StatoBagaglio.caricato || baggage.getStato() == StatoBagaglio.ritirabile) ? "Segnala Smarrimento" : "";

            model.addRow(new Object[]{
                    flightInfo,
                    tckInfo,
                    baggage.getCodice(),
                    status,
                    actionText
            });
        }
    }

    /**
     * Segnala un bagaglio come smarrito dalla tabella dei bagagli.
     * Verifica lo stato attuale del bagaglio, chiede conferma all'utente
     * e aggiorna lo stato del bagaglio nel sistema.
     * 
     * @param baggageCode Il codice del bagaglio da segnalare come smarrito
     * @param baggageTable La tabella contenente il bagaglio
     */
    private void reportLostBaggageFromTable(String baggageCode, JTable baggageTable) {
        Bagaglio baggage = null;
        for (Bagaglio b : baggages) {
            if (b.getCodice().equals(baggageCode)) {
                baggage = b;
                break;
            }
        }

        if (baggage == null) {
            JOptionPane.showMessageDialog(this,
                    "Bagaglio non trovato.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (baggage.getStato() != StatoBagaglio.caricato && baggage.getStato() != StatoBagaglio.ritirabile) {
            JOptionPane.showMessageDialog(this,
                    "Il bagaglio può essere segnalato come smarrito solo se è nello stato 'caricato' o 'ritirabile'.",
                    "Stato non valido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int scelta = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler segnalare questo bagaglio come smarrito?", "Conferma", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (scelta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Segnalazione inviata con successo!\nUn operatore ti contatterà al più presto.",
                    "Segnalazione Inviata",
                    JOptionPane.INFORMATION_MESSAGE);

            baggage.setStato(StatoBagaglio.smarrito);
            controller.aggiornaBagaglio(baggage);

            updateBaggageTable(baggageTable);
        }
    }


    /**
     * Mostra una finestra di dialogo con i dettagli dei bagagli associati a una prenotazione.
     * Elenca tutti i bagagli con i relativi codici e stati.
     * Se non ci sono bagagli associati, mostra un messaggio informativo.
     * 
     * @param booking La prenotazione di cui visualizzare i dettagli dei bagagli
     */
    private void showBaggageDetails(Prenotazione booking) {
        if (booking.getBagagli() == null || booking.getBagagli().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nessun bagaglio associato a questa prenotazione.",
                    "Dettagli Bagagli",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Bagaglio baggage : booking.getBagagli()) {
            JLabel label = new JLabel("Codice: " + baggage.getCodice() + " - Stato: " + baggage.getStato());
            panel.add(label);
            panel.add(Box.createVerticalStrut(5));
        }

        JOptionPane.showMessageDialog(this,
                panel,
                "Dettagli Bagagli",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Crea un componente grafico che rappresenta un quadrato colorato.
     * Utilizzato per creare la legenda nella selezione dei posti.
     * 
     * @param c Il colore da utilizzare per il quadrato
     * @return Un componente grafico che rappresenta un quadrato colorato
     */
    private Component createLegendBox(Color c) {
        JPanel box = new JPanel();
        box.setBackground(c);
        box.setPreferredSize(new Dimension(15, 15));
        return box;
    }


}
