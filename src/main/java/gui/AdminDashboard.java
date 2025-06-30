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

public class AdminDashboard extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable flightsTable;
    private JPanel flightsPanel;
    private JPanel addFlightPanel;
    private JPanel updateFlightPanel;
    private JPanel gateAssignmentPanel;
    private JPanel baggageStatusPanel;
    private JPanel updateBaggagePanel;
    private JPanel lostBaggagePanel;

    // Data
    private List<Volo> flights;
    private List<Gate> gates;
    private List<Bagaglio> baggages;

    private Utente admin;
    private controller.Controller controller;

    public AdminDashboard(Utente admin) {
        if (!admin.isAmministratore()) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo di amministratore");
        }
        this.admin = admin;

        // Initialize controller
        this.controller = new controller.Controller();

        // Initialize data
        initializeTestData();

        // Set up the frame
        setTitle("Dashboard Amministratore - Aeroporto di Napoli");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply theme to frame
        UIManager.styleFrame(this);

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIManager.BACKGROUND_COLOR);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        UIManager.styleTabbedPane(tabbedPane);

        // Create and add panels for each tab
        createFlightsPanel();
        createAddFlightPanel();
        createLostBaggagePanel();

        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Create top panel with welcome label and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIManager.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add welcome label
        JLabel welcomeLabel = new JLabel("Benvenuto, " + admin.getNome() + " " + admin.getCognome() + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(UIManager.TITLE_FONT);
        welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(UIManager.BACKGROUND_COLOR);
        JButton logoutButton = new JButton("Log Out");
        UIManager.styleButton(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close this window
                dispose();

                // Open login window
                Login loginFrame = new Login(controller);
                loginFrame.setVisible(true);
            }
        });
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Set main panel as content pane
        setContentPane(mainPanel);
    }

    private void initializeTestData() {
        // Retrieve flights from database
        flights = controller.getAllVoli();
        // If no flights in database, create an empty list
        if (flights == null) {
            flights = new ArrayList<>();
        }

        // Initialize gates
        gates = controller.getAllGates();
        if (gates == null) {
            gates = new ArrayList<>();
        }

        // Initialize baggages
        baggages = controller.getAllBagagli();
        if (baggages == null) {
            baggages = new ArrayList<>();
        }
    }

    private void createFlightsPanel() {
        flightsPanel = new JPanel(new BorderLayout());
        flightsPanel.setBackground(UIManager.BACKGROUND_COLOR);

        // Create search panel
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

        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[] {
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

        // Create table model with column names
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
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

        // Add data to table model
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

        // Create table with model
        flightsTable = new JTable(model);

        // Apply table styling
        UIManager.styleTable(flightsTable);

        // Set custom renderer for status column
        flightsTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    StatoVolo stato = (StatoVolo) value;
                    if (stato == StatoVolo.inRitardo) {
                        c.setBackground(UIManager.WARNING_COLOR);
                        c.setForeground(Color.WHITE);
                    } else if (stato == StatoVolo.cancellato) {
                        c.setBackground(UIManager.ERROR_COLOR);
                        c.setForeground(Color.BLACK);
                    } else if (stato == StatoVolo.atterrato || stato == StatoVolo.decollato) {
                        c.setBackground(UIManager.SUCCESS_COLOR);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        c.setForeground(isSelected ? table.getSelectionForeground() : UIManager.TEXT_COLOR);
                    }
                }

                ((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Add mouse listener to show bookings when a flight is clicked
        flightsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click
                    int row = flightsTable.rowAtPoint(evt.getPoint());
                    if (row >= 0 && row < flights.size()) {
                        showFlightDetailsDialog(flights.get(row));
                    }
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(UIManager.BACKGROUND_COLOR);
        flightsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIManager.BACKGROUND_COLOR);
        flightsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Voli", flightsPanel);
    }

    private void refreshFlightsTable() {
        // Retrieve updated flights from database
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

    private void createAddFlightPanel() {
        addFlightPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields
        JTextField airlineField = new JTextField();
        JComboBox<String> flightTypeComboBox = new JComboBox<>(new String[]{"Partenza", "Arrivo"});
        JTextField originField = new JTextField("Napoli");
        JTextField destinationField = new JTextField();
        JTextField timeField = new JTextField();
        JComboBox<StatoVolo> statusComboBox = new JComboBox<>(StatoVolo.values());
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField delayField = new JTextField("0");
        JTextField totalSeatsField = new JTextField("0");
        JTextField seatsField = new JTextField("0");

        // Set up flight type combo box to control origin/destination fields
        flightTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) flightTypeComboBox.getSelectedItem();
                if ("Partenza".equals(selectedType)) {
                    originField.setText("Napoli");
                    originField.setEditable(false);
                    destinationField.setEditable(true);
                    destinationField.setText("");
                } else { // Arrivo
                    destinationField.setText("Napoli");
                    destinationField.setEditable(false);
                    originField.setEditable(true);
                    originField.setText("");
                }
            }
        });

        // Trigger the action listener to set initial state
        flightTypeComboBox.setSelectedIndex(0);

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
        formPanel.add(new JLabel("Posti Disponibili:"));
        formPanel.add(seatsField);

        // Add form panel to center
        addFlightPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi Volo");
        JButton clearButton = new JButton("Pulisci");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String airline = airlineField.getText();
                    String flightType = (String) flightTypeComboBox.getSelectedItem();
                    String origin = originField.getText();
                    String destination = destinationField.getText();
                    String time = timeField.getText();
                    StatoVolo status = (StatoVolo) statusComboBox.getSelectedItem();
                    LocalDate date = LocalDate.parse(dateField.getText());
                    int delay = Integer.parseInt(delayField.getText());
                    int totalSeats = Integer.parseInt(totalSeatsField.getText());
                    int availableSeats = Integer.parseInt(seatsField.getText());

                    if (airline.isEmpty() || origin.isEmpty() || destination.isEmpty() || time.isEmpty() || 
                        totalSeats <= 0 || availableSeats <= 0 || availableSeats > totalSeats) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Tutti i campi sono obbligatori. I posti totali e disponibili devono essere maggiori di zero, e i posti disponibili non possono superare i posti totali.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate that either origin or destination is Napoli
                    if (!origin.equals("Napoli") && !destination.equals("Napoli")) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "L'origine o la destinazione deve essere Napoli",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Generate a unique flight code
                    String code = generateFlightCode(airline);
                    boolean success = false;

                    try {
                        // Create new flight
                        Volo newFlight = new Volo(code, airline, origin, destination, time, status, date, delay, totalSeats, availableSeats, 0);

                        // Save to database
                        success = controller.inserisciVolo(code, airline, origin, destination, time, status, date, delay, totalSeats, availableSeats);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            ex.getMessage(),
                            "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (success) {
                        // Refresh flights from database
                        refreshFlightsTable();

                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Volo aggiunto con successo\nCodice Volo: " + code,
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Errore durante l'inserimento del volo nel database",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                    // Clear form
                    airlineField.setText("");
                    flightTypeComboBox.setSelectedIndex(0); // This will trigger the action listener
                    timeField.setText("");
                    statusComboBox.setSelectedIndex(0);
                    dateField.setText(LocalDate.now().toString());
                    delayField.setText("0");
                    totalSeatsField.setText("0");
                    seatsField.setText("0");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Errore: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                airlineField.setText("");
                flightTypeComboBox.setSelectedIndex(0); // This will trigger the action listener
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

        // Add panel to tabbed pane
        tabbedPane.addTab("Inserisci Volo", addFlightPanel);
    }



    /**
     * Generates a unique flight code based on the airline name
     * @param airline The airline name
     * @return A unique flight code
     */
    private String generateFlightCode(String airline) {
        // Get the first two letters of the airline name (or one if it's a single letter)
        String prefix = airline.length() > 1 ?
                        airline.substring(0, 2).toUpperCase() :
                        airline.substring(0, 1).toUpperCase();

        // Generate a random 4-digit number
        int randomNum = 1000 + new Random().nextInt(9000);

        // Combine to form the flight code
        String code = prefix + randomNum;

        // Check if the code already exists, if so, generate a new one
        for (Volo volo : flights) {
            if (volo.getCodiceVolo().equals(code)) {
                return generateFlightCode(airline); // Recursive call to generate a new code
            }
        }

        return code;
    }

    private void createLostBaggagePanel() {
        lostBaggagePanel = new JPanel(new BorderLayout());

        // Create table model with column names
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        model.addColumn("Codice Bagaglio");
        model.addColumn("Stato");
        model.addColumn("Azioni");

        // Create table with model
        JTable lostBaggageTable = new JTable(model);

        // Set preferred column widths
        lostBaggageTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Add some test data for lost baggage
        for (Bagaglio bagaglio : baggages) {
            if(bagaglio.getStato() == StatoBagaglio.smarrito){
                model.addRow(new Object[]{bagaglio.getCodice(), bagaglio.getStato(),  "Modifica"});

            }
        }


        // Add mouse listener to handle button clicks in the action column
        lostBaggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = lostBaggageTable.rowAtPoint(evt.getPoint());
                int col = lostBaggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 2) { // Action column
                    String baggageCode = (String) lostBaggageTable.getValueAt(row, 0);
                    showBaggageStatusDialog(baggageCode, row, lostBaggageTable);
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(lostBaggageTable);
        lostBaggagePanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button
        JButton refreshButton = new JButton("Aggiorna");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // In a real application, this would query the database for lost baggage
                updateLostBaggageTable(lostBaggageTable);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        lostBaggagePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Bagagli Smarriti", lostBaggagePanel);
    }

    /**
     * Updates the lost baggage table with the latest data
     * @param lostBaggageTable The table to update
     */
    private void updateLostBaggageTable(JTable lostBaggageTable) {
        DefaultTableModel model = (DefaultTableModel) lostBaggageTable.getModel();
        model.setRowCount(0);

        // In a real application, this would query the database for lost baggage
        // For now, we'll just add some test data
        for (Bagaglio bagaglio : baggages) {
            if (bagaglio.getStato() == StatoBagaglio.smarrito) {
                model.addRow(new Object[]{
                    bagaglio.getCodice(),
                    bagaglio.getStato(),
                    "Modifica"
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
     * @param baggageCode The code of the baggage to modify
     * @param row The row in the table
     * @param lostBaggageTable The table containing the baggage
     */
    private void showBaggageStatusDialog(String baggageCode, int row, JTable lostBaggageTable) {
        // Create a dialog with a combo box for status
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        // Make the status selector smaller, with at most 20px extra padding compared to text
        statusComboBox.setPreferredSize(new Dimension(150, 25));

        // Find the baggage and set the current status
        for (Bagaglio bagaglio : baggages) {
            if (bagaglio.getCodice().equals(baggageCode)) {
                statusComboBox.setSelectedItem(bagaglio.getStato());
                break;
            }
        }

        panel.add(new JLabel("Codice Bagaglio:"));
        panel.add(new JLabel(baggageCode));
        panel.add(new JLabel("Nuovo Stato:"));
        panel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifica Stato Bagaglio",
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            StatoBagaglio newStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

            // Update the baggage status
            for (Bagaglio bagaglio : baggages) {
                if (bagaglio.getCodice().equals(baggageCode)) {
                    bagaglio.setStato(newStatus);

                    // Update the table
                    lostBaggageTable.setValueAt(newStatus, row, 1);

                    JOptionPane.showMessageDialog(this,
                        "Stato del bagaglio aggiornato con successo",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);

                    // If the status is no longer "smarrito", remove only this row from the table
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
     * @param flight The flight to show details for
     */
    private void showFlightDetailsDialog(Volo flight) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Dettagli Volo: " + flight.getCodiceVolo(), true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create tabbed pane for different sections
        JTabbedPane dialogTabs = new JTabbedPane();

        // Create info panel with flight details and edit button
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Flight details section
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

        // Get the seat capacity from the flight
        int totalSeats = flight.getPostiTotali();
        int availableSeats = flight.getPostiDisponibili();
        int occupiedSeats = totalSeats - availableSeats;

        detailsPanel.add(new JLabel("Posti Totali:"));
        detailsPanel.add(new JLabel(String.valueOf(totalSeats)));
        detailsPanel.add(new JLabel("Posti Disponibili:"));
        detailsPanel.add(new JLabel(String.valueOf(availableSeats)));
        detailsPanel.add(new JLabel("Posti Occupati:"));
        detailsPanel.add(new JLabel(occupiedSeats + " / " + totalSeats));

        // Add gate information if available
        detailsPanel.add(new JLabel("Gate:"));
        detailsPanel.add(new JLabel(flight.getGate() > 0 ? String.valueOf(flight.getGate()) : "Non assegnato"));

        infoPanel.add(detailsPanel, BorderLayout.CENTER);

        // Add edit button and gate assignment button if applicable
        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Modifica");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateFlightDialog(flight, dialog);
            }
        });
        editButtonPanel.add(editButton);

        // Add gate assignment button if the flight is departing from Napoli
        if (flight.getOrigine().equalsIgnoreCase("NAPOLI")) {
            JButton assignGateButton = new JButton("Assegna Gate");
            assignGateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a dialog for gate assignment
                    JDialog gateDialog = new JDialog(dialog, "Assegna Gate", true);
                    gateDialog.setSize(400, 200);
                    gateDialog.setLocationRelativeTo(dialog);

                    JPanel gatePanel = new JPanel(new BorderLayout());
                    gatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                    JLabel gateLabel = new JLabel("Seleziona Gate:");
                    JComboBox<Integer> gateComboBox = new JComboBox<>();

                    // Add gate options
                    for (Gate gate : gates) {
                        gateComboBox.addItem(gate.getCodice());
                    }

                    // Set current gate if assigned
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

                            // Update the flight's gate
                            flight.setGate(selectedGate);
                            controller.assegnaGate(selectedGate, flight.getCodiceVolo());

                            // Update the gate display in the details panel
                            for (int i = 0; i < detailsPanel.getComponentCount(); i++) {
                                Component comp = detailsPanel.getComponent(i);
                                if (comp instanceof JLabel && ((JLabel) comp).getText().equals("Gate:")) {
                                    // The next component is the gate value label
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

                            // Refresh the flights table to show the updated gate
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

        // Gate assignment functionality has been moved to a button next to "Modifica"
        JPanel gatePanel = null;

        // Create bookings panel
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create bookings table
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        model.addColumn("Numero Biglietto");
        model.addColumn("Passeggero");
        model.addColumn("Posto");
        model.addColumn("Stato");
        model.addColumn("Bagagli");
        model.addColumn("Azioni");

        // In a real application, we would get the bookings for this flight from the database
        // For now, we'll just add some example bookings
        model.addRow(new Object[]{"TKT1001", "Mario Rossi", "12A", "Confermato", "2", "Modifica"});
        model.addRow(new Object[]{"TKT1002", "Luigi Verdi", "14B", "In Attesa", "1", "Modifica"});
        model.addRow(new Object[]{"TKT1003", "Anna Bianchi", "16C", "Confermato", "0", "Modifica"});

        JTable bookingsTable = new JTable(model);

        // Add mouse listener to handle button clicks in the action column
        bookingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookingsTable.rowAtPoint(evt.getPoint());
                int col = bookingsTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 5) { // Action column
                    String ticketNumber = (String) bookingsTable.getValueAt(row, 0);
                    showBookingDetailsDialog(ticketNumber, row, bookingsTable);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prenotazioni"));
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add collective baggage management button
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

        // Add tabs
        dialogTabs.addTab("Dettagli Volo", infoPanel);
        dialogTabs.addTab("Prenotazioni", bookingsPanel);

        mainPanel.add(dialogTabs, BorderLayout.CENTER);

        // Add close button
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
     * @param flight The flight to update
     * @param parentDialog The parent dialog
     */
    private void showUpdateFlightDialog(Volo flight, JDialog parentDialog) {
        // Create a dialog
        JDialog dialog = new JDialog(parentDialog, "Aggiorna Volo: " + flight.getCodiceVolo(), true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(parentDialog);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Aggiorna Volo"));

        // Add form fields
        JTextField airlineField = new JTextField(flight.getCompagnia());
        JTextField originField = new JTextField(flight.getOrigine());
        JTextField destinationField = new JTextField(flight.getDestinazione());
        JTextField timeField = new JTextField(flight.getOrarioPrevisto());
        JComboBox<StatoVolo> statusComboBox = new JComboBox<>(StatoVolo.values());
        statusComboBox.setSelectedItem(flight.getStato());
        JTextField dateField = new JTextField(flight.getData().toString());
        JTextField delayField = new JTextField(String.valueOf(flight.getTempoRitardo()));
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
        formPanel.add(new JLabel("Posti Totali:"));
        formPanel.add(totalSeatsField);
        formPanel.add(new JLabel("Posti Disponibili:"));
        formPanel.add(availableSeatsField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
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

                    // Validate input
                    if (airline.isEmpty() || origin.isEmpty() || destination.isEmpty() || time.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                            "Tutti i campi sono obbligatori",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (delay < 0) {
                        JOptionPane.showMessageDialog(dialog,
                            "Errore, inserire un valore di ritardo maggiore o uguale a zero",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (availableSeats > totalSeats) {
                        JOptionPane.showMessageDialog(dialog,
                            "I posti disponibili non possono essere maggiori dei posti totali",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        // Update flight
                        flight.setCompagnia(airline);
                        flight.setOrigine(origin);
                        flight.setDestinazione(destination);
                        flight.setOrarioPrevisto(time);
                        flight.setStato(status);
                        flight.setData(date);
                        flight.setTempoRitardo(delay);
                        flight.setPostiTotali(totalSeats);
                        flight.setPostiDisponibili(availableSeats);

                        // In a real application, we would update the database here
                        controller.modificaVolo(flight);

                        JOptionPane.showMessageDialog(dialog,
                            "Volo aggiornato con successo",
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog,
                            ex.getMessage(),
                            "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Refresh the flights table
                    refreshFlightsTable();

                    // Close the dialog
                    dialog.dispose();

                    // Close the parent dialog and reopen it with updated flight details
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
     * @param flight The flight
     * @param bookingsTable The bookings table
     */
    private void showCollectiveBaggageManagementDialog(Volo flight, JTable bookingsTable) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Gestione Collettiva Bagagli: " + flight.getCodiceVolo(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni"));

        infoPanel.add(new JLabel("Volo: " + flight.getCodiceVolo() + " - " + flight.getCompagnia()));
        infoPanel.add(new JLabel("Tratta: " + flight.getOrigine() + " -> " + flight.getDestinazione()));
        infoPanel.add(new JLabel("Data: " + flight.getData().toString()));

        // Get total baggage count from the bookings table
        int totalBaggage = 0;
        for (int i = 0; i < bookingsTable.getRowCount(); i++) {
            totalBaggage += Integer.parseInt(bookingsTable.getValueAt(i, 4).toString());
        }

        infoPanel.add(new JLabel("Totale Bagagli: " + totalBaggage));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Create action panel
        JPanel actionPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Azioni"));

        JLabel statusLabel = new JLabel("Imposta stato per tutti i bagagli:");
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());

        actionPanel.add(statusLabel);
        actionPanel.add(statusComboBox);

        mainPanel.add(actionPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Applica");
        JButton cancelButton = new JButton("Annulla");

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoBagaglio selectedStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

                // In a real application, we would update all baggage status in the database

                JOptionPane.showMessageDialog(dialog,
                    "Stato '" + selectedStatus + "' applicato a tutti i bagagli del volo " + flight.getCodiceVolo(),
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

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
     * @param ticketNumber The ticket number of the booking to modify
     * @param row The row in the table
     * @param bookingsTable The table containing the booking
     */
    private void showBookingDetailsDialog(String ticketNumber, int row, JTable bookingsTable) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Modifica Prenotazione: " + ticketNumber, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create tabbed pane for different sections
        JTabbedPane dialogTabs = new JTabbedPane();

        // Create booking details panel
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dettagli Prenotazione"));

        // Get booking details from the table
        String passengerName = (String) bookingsTable.getValueAt(row, 1);
        String seat = (String) bookingsTable.getValueAt(row, 2);
        String status = (String) bookingsTable.getValueAt(row, 3);
        String baggageCount = (String) bookingsTable.getValueAt(row, 4);

        // Create form fields
        JTextField nameField = new JTextField(passengerName);
        JTextField seatField = new JTextField(seat);
        JComboBox<StatoPrenotazione> statusComboBox = new JComboBox<>(StatoPrenotazione.values());

        // Set selected status based on the string value
        if (status.equalsIgnoreCase("Confermato")) {
            statusComboBox.setSelectedItem(StatoPrenotazione.confermato);
        } else if (status.equalsIgnoreCase("In Attesa")) {
            statusComboBox.setSelectedItem(StatoPrenotazione.inAttesa);
        } else if (status.equalsIgnoreCase("Cancellato")) {
            statusComboBox.setSelectedItem(StatoPrenotazione.cancellato);
        }

        formPanel.add(new JLabel("Numero Biglietto:"));
        formPanel.add(new JLabel(ticketNumber));
        formPanel.add(new JLabel("Passeggero:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Posto:"));
        formPanel.add(seatField);
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Bagagli:"));
        formPanel.add(new JLabel(baggageCount));

        bookingPanel.add(formPanel, BorderLayout.CENTER);

        // Add save button for booking details
        JPanel bookingSavePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBookingButton = new JButton("Salva Prenotazione");
        saveBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the booking in the table
                bookingsTable.setValueAt(nameField.getText(), row, 1);
                bookingsTable.setValueAt(seatField.getText(), row, 2);

                // Convert enum to display string
                String statusDisplay;
                StatoPrenotazione selectedStatus = (StatoPrenotazione) statusComboBox.getSelectedItem();
                if (selectedStatus == StatoPrenotazione.confermato) {
                    statusDisplay = "Confermato";
                } else if (selectedStatus == StatoPrenotazione.inAttesa) {
                    statusDisplay = "In Attesa";
                } else {
                    statusDisplay = "Cancellato";
                }

                bookingsTable.setValueAt(statusDisplay, row, 3);

                controller.aggiornaPrenotazione(selectedStatus, ticketNumber);

                JOptionPane.showMessageDialog(dialog,
                    "Prenotazione aggiornata con successo",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bookingSavePanel.add(saveBookingButton);
        bookingPanel.add(bookingSavePanel, BorderLayout.SOUTH);

        // Create baggage panel
        JPanel baggagePanel = new JPanel(new BorderLayout());
        baggagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create baggage table
        DefaultTableModel baggageModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only status column is editable
            }
        };

        baggageModel.addColumn("Codice Bagaglio");
        baggageModel.addColumn("Stato");
        baggageModel.addColumn("Azioni");

        // In a real application, we would get the baggage for this booking from the database
        // For now, we'll just add some example baggage
        int numBaggage = Integer.parseInt(baggageCount);
        for (int i = 0; i < numBaggage; i++) {
            baggageModel.addRow(new Object[]{"BAG" + ticketNumber + "-" + (i + 1), StatoBagaglio.caricato, "Modifica"});
        }

        JTable baggageTable = new JTable(baggageModel);

        // Create a combo box editor for the status column
        JComboBox<StatoBagaglio> statusEditor = new JComboBox<>(StatoBagaglio.values());
        baggageTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(statusEditor));

        // Add mouse listener for the action column
        baggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = baggageTable.rowAtPoint(evt.getPoint());
                int col = baggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 2) { // Action column
                    String baggageCode = (String) baggageTable.getValueAt(row, 0);
                    StatoBagaglio currentStatus = (StatoBagaglio) baggageTable.getValueAt(row, 1);
                    showBaggageStatusDialog(baggageCode, currentStatus, row, baggageTable);
                }
            }
        });

        JScrollPane baggageScrollPane = new JScrollPane(baggageTable);
        baggageScrollPane.setBorder(BorderFactory.createTitledBorder("Bagagli"));
        baggagePanel.add(baggageScrollPane, BorderLayout.CENTER);

        // Add save button for baggage
        JPanel baggageSavePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBaggageButton = new JButton("Salva Modifiche Bagagli");
        saveBaggageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // In a real application, we would update the database here

                JOptionPane.showMessageDialog(dialog,
                    "Stato dei bagagli aggiornato con successo",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        baggageSavePanel.add(saveBaggageButton);
        baggagePanel.add(baggageSavePanel, BorderLayout.SOUTH);

        // Create lost baggage panel
        JPanel lostBaggagePanel = new JPanel(new BorderLayout());
        lostBaggagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create lost baggage table
        DefaultTableModel lostBaggageModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        lostBaggageModel.addColumn("Codice Bagaglio");
        lostBaggageModel.addColumn("Data Smarrimento");
        lostBaggageModel.addColumn("Stato");
        lostBaggageModel.addColumn("Azioni");

        // Add example lost baggage (in a real app, we would get this from the database)
        // Only add if there are baggage
        if (numBaggage > 0) {
            lostBaggageModel.addRow(new Object[]{"BAG" + ticketNumber + "-1", "2023-06-15", StatoBagaglio.smarrito, "Modifica"});
        }

        JTable lostBaggageTable = new JTable(lostBaggageModel);

        // Add mouse listener for the action column
        lostBaggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = lostBaggageTable.rowAtPoint(evt.getPoint());
                int col = lostBaggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 3) { // Action column
                    String baggageCode = (String) lostBaggageTable.getValueAt(row, 0);
                    showLostBaggageStatusDialog(baggageCode, row, lostBaggageTable);
                }
            }
        });

        JScrollPane lostBaggageScrollPane = new JScrollPane(lostBaggageTable);
        lostBaggageScrollPane.setBorder(BorderFactory.createTitledBorder("Bagagli Smarriti"));
        lostBaggagePanel.add(lostBaggageScrollPane, BorderLayout.CENTER);

        // Add tabs
        dialogTabs.addTab("Prenotazione", bookingPanel);
        dialogTabs.addTab("Bagagli", baggagePanel);
        dialogTabs.addTab("Bagagli Smarriti", lostBaggagePanel);

        mainPanel.add(dialogTabs, BorderLayout.CENTER);

        // Add close button
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
     * @param baggageCode The baggage code
     * @param currentStatus The current status
     * @param row The row in the table
     * @param baggageTable The baggage table
     */
    private void showBaggageStatusDialog(String baggageCode, StatoBagaglio currentStatus, int row, JTable baggageTable) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Modifica Stato Bagaglio: " + baggageCode, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel codeLabel = new JLabel("Codice Bagaglio:");
        JLabel codeValueLabel = new JLabel(baggageCode);

        JLabel statusLabel = new JLabel("Stato:");
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        statusComboBox.setSelectedItem(currentStatus);

        formPanel.add(codeLabel);
        formPanel.add(codeValueLabel);
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Salva");
        JButton cancelButton = new JButton("Annulla");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoBagaglio selectedStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

                // Update the baggage status in the table
                baggageTable.setValueAt(selectedStatus, row, 1);

                // In a real application, we would update the database here

                JOptionPane.showMessageDialog(dialog,
                    "Stato del bagaglio aggiornato con successo",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

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

    /**
     * Shows a dialog to modify lost baggage status
     * @param baggageCode The baggage code
     * @param row The row in the table
     * @param lostBaggageTable The lost baggage table
     */
    private void showLostBaggageStatusDialog(String baggageCode, int row, JTable lostBaggageTable) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Modifica Stato Bagaglio Smarrito: " + baggageCode, true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel codeLabel = new JLabel("Codice Bagaglio:");
        JLabel codeValueLabel = new JLabel(baggageCode);

        JLabel dateLabel = new JLabel("Data Smarrimento:");
        JLabel dateValueLabel = new JLabel((String) lostBaggageTable.getValueAt(row, 1));

        JLabel statusLabel = new JLabel("Stato:");
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        statusComboBox.setSelectedItem(lostBaggageTable.getValueAt(row, 2));

        JLabel notesLabel = new JLabel("Note:");
        JTextField notesField = new JTextField();

        formPanel.add(codeLabel);
        formPanel.add(codeValueLabel);
        formPanel.add(dateLabel);
        formPanel.add(dateValueLabel);
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);
        formPanel.add(notesLabel);
        formPanel.add(notesField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Salva");
        JButton cancelButton = new JButton("Annulla");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatoBagaglio selectedStatus = (StatoBagaglio) statusComboBox.getSelectedItem();

                // Update the baggage status in the table
                lostBaggageTable.setValueAt(selectedStatus, row, 2);

                // In a real application, we would update the database here

                JOptionPane.showMessageDialog(dialog,
                    "Stato del bagaglio smarrito aggiornato con successo",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

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
