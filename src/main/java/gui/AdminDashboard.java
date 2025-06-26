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
        createUpdateFlightPanel();
        createGateAssignmentPanel();
        // createBaggageStatusPanel(); // Removed as per requirement: admin should not insert baggage
        createUpdateBaggagePanel();
        createLostBaggagePanel();

        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add welcome label at the top
        JLabel welcomeLabel = new JLabel("Benvenuto, " + admin.getNome() + " " + admin.getCognome() + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(UIManager.TITLE_FONT);
        welcomeLabel.setForeground(UIManager.PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

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
            gates.add(new Gate(1));
            gates.add(new Gate(2));
            gates.add(new Gate(3));
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
            "Tutti", "Codice Volo", "Compagnia", "Destinazione", "Origine", "Orario", "Stato", "Data", "Ritardo", "Posti Totali", "Posti Disponibili"
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
                volo.getOrarioPrevisto(),
                volo.getStato(),
                volo.getData(),
                volo.getTempoRitardo(),
                volo.getPostiTotali(),
                volo.getPostiDisponibili()
            });
        }

        // Create table with model
        flightsTable = new JTable(model);

        // Apply table styling
        UIManager.styleTable(flightsTable);

        // Set custom renderer for status column
        flightsTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    StatoVolo stato = (StatoVolo) value;
                    if (stato == StatoVolo.inRitardo) {
                        c.setBackground(UIManager.ERROR_COLOR);
                        c.setForeground(Color.WHITE);
                    } else if (stato == StatoVolo.cancellato) {
                        c.setBackground(UIManager.WARNING_COLOR);
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
        JButton refreshButton = new JButton("Aggiorna");
        UIManager.styleButton(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshFlightsTable();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIManager.BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
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

            if (searchType.equals("Tutti") || searchText.isEmpty()) {
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
            } else if (searchType.equals("Ritardo") && String.valueOf(volo.getTempoRitardo()).contains(searchText)) {
                match = true;
            } else if (searchType.equals("Posti Totali") && String.valueOf(volo.getPostiTotali()).contains(searchText)) {
                match = true;
            } else if (searchType.equals("Posti Disponibili") && String.valueOf(volo.getPostiDisponibili()).contains(searchText)) {
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

                    // Create new flight
                    Volo newFlight = new Volo(code, airline, origin, destination, time, status, date, delay, totalSeats, availableSeats);

                    // Save to database
                    boolean success = controller.inserisciVolo(code, airline, origin, destination, time, status, date, delay);

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

    private void createUpdateFlightPanel() {
        updateFlightPanel = new JPanel(new BorderLayout());

        // Create selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel selectLabel = new JLabel("Seleziona Volo:");
        JComboBox<String> flightComboBox = new JComboBox<>();

        // Populate flight combo box
        for (Volo volo : flights) {
            flightComboBox.addItem(volo.getCodiceVolo() + " - " + volo.getCompagnia() + " (" +
                                  volo.getOrigine() + " -> " + volo.getDestinazione() + ")");
        }

        selectionPanel.add(selectLabel);
        selectionPanel.add(flightComboBox);

        updateFlightPanel.add(selectionPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields (excluding code field since it's selected from dropdown)
        JTextField airlineField = new JTextField();
        JTextField originField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField timeField = new JTextField();
        JComboBox<StatoVolo> statusComboBox = new JComboBox<>(StatoVolo.values());
        JTextField dateField = new JTextField();
        JTextField delayField = new JTextField();
        JTextField totalSeatsField = new JTextField();
        JTextField availableSeatsField = new JTextField();

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

        // Add form panel to center
        updateFlightPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Aggiorna Volo");
        // Make the button smaller and more suitable for the screen
        updateButton.setPreferredSize(new Dimension(150, 30));
        updateButton.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add action listener to flight combo box to populate form fields
        flightComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = flightComboBox.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < flights.size()) {
                    Volo selectedFlight = flights.get(selectedIndex);
                    airlineField.setText(selectedFlight.getCompagnia());
                    originField.setText(selectedFlight.getOrigine());
                    destinationField.setText(selectedFlight.getDestinazione());
                    timeField.setText(selectedFlight.getOrarioPrevisto());
                    statusComboBox.setSelectedItem(selectedFlight.getStato());
                    dateField.setText(selectedFlight.getData().toString());
                    delayField.setText(String.valueOf(selectedFlight.getTempoRitardo()));
                    totalSeatsField.setText(String.valueOf(selectedFlight.getPostiTotali()));
                    availableSeatsField.setText(String.valueOf(selectedFlight.getPostiDisponibili()));
                }
            }
        });

        // Trigger the action listener to populate fields initially if there are flights
        if (flightComboBox.getItemCount() > 0) {
            flightComboBox.setSelectedIndex(0);
        }

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedIndex = flightComboBox.getSelectedIndex();
                    if (selectedIndex < 0) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Seleziona un volo da aggiornare",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String airline = airlineField.getText();
                    String origin = originField.getText();
                    String destination = destinationField.getText();
                    String time = timeField.getText();
                    StatoVolo status = (StatoVolo) statusComboBox.getSelectedItem();
                    LocalDate date = LocalDate.parse(dateField.getText());
                    int delay = Integer.parseInt(delayField.getText());
                    int totalSeats = Integer.parseInt(totalSeatsField.getText());
                    int availableSeats = Integer.parseInt(availableSeatsField.getText());

                    if (airline.isEmpty() || origin.isEmpty() || destination.isEmpty() || time.isEmpty() || 
                        totalSeats <= 0 || availableSeats <= 0 || availableSeats > totalSeats) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Tutti i campi sono obbligatori. I posti totali e disponibili devono essere maggiori di zero, e i posti disponibili non possono superare i posti totali.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update the selected flight
                    Volo selectedFlight = flights.get(selectedIndex);

                    // Check if origin or destination is Napoli
                    String originalOrigin = selectedFlight.getOrigine();
                    String originalDestination = selectedFlight.getDestinazione();

                    // Constraint: Admin can only update the city that is different from Napoli
                    if (originalOrigin.equals("Napoli") && !originalDestination.equals("Napoli")) {
                        // If origin is Napoli, only allow updating destination
                        if (!origin.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile modificare l'origine da Napoli ad altra città",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // Prevent setting destination to Napoli (both would be Napoli)
                        if (destination.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile impostare sia origine che destinazione a Napoli",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else if (!originalOrigin.equals("Napoli") && originalDestination.equals("Napoli")) {
                        // If destination is Napoli, only allow updating origin
                        if (!destination.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile modificare la destinazione da Napoli ad altra città",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // Prevent setting origin to Napoli (both would be Napoli)
                        if (origin.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile impostare sia origine che destinazione a Napoli",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else if (originalOrigin.equals("Napoli") && originalDestination.equals("Napoli")) {
                        // If both are Napoli (shouldn't happen normally), don't allow changes
                        if (!origin.equals("Napoli") || !destination.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile modificare questo volo",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        // Neither origin nor destination is Napoli
                        // At least one of them must remain different from Napoli
                        if (origin.equals("Napoli") && destination.equals("Napoli")) {
                            JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Non è possibile impostare sia origine che destinazione a Napoli",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    selectedFlight.setCompagnia(airline);
                    selectedFlight.setOrigine(origin);
                    selectedFlight.setDestinazione(destination);
                    selectedFlight.setOrarioPrevisto(time);
                    selectedFlight.setStato(status);
                    selectedFlight.setData(date);
                    selectedFlight.setTempoRitardo(delay);

                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Volo aggiornato con successo",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);

                    // Refresh flights table and combo box
                    refreshFlightsTable();

                    // Update the combo box
                    flightComboBox.removeItemAt(selectedIndex);
                    flightComboBox.insertItemAt(selectedFlight.getCodiceVolo() + " - " + selectedFlight.getCompagnia() + " (" +
                                              selectedFlight.getOrigine() + " -> " + selectedFlight.getDestinazione() + ")", selectedIndex);
                    flightComboBox.setSelectedIndex(selectedIndex);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Errore: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(updateButton);
        updateFlightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Aggiorna Volo", updateFlightPanel);
    }

    private void createGateAssignmentPanel() {
        gateAssignmentPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields
        JComboBox<String> flightComboBox = new JComboBox<>();

        // Only show flights departing from Napoli
        List<Volo> departingFlights = new ArrayList<>();
        for (Volo volo : flights) {
            if (volo.getOrigine().equals("Napoli")) {
                departingFlights.add(volo);
                flightComboBox.addItem(volo.getCodiceVolo() + " - " + volo.getCompagnia() + " (" +
                                      volo.getOrigine() + " -> " + volo.getDestinazione() + ")");
            }
        }

        JComboBox<Integer> gateComboBox = new JComboBox<>();
        for (Gate gate : gates) {
            gateComboBox.addItem(gate.getCodice());
        }

        // Add information label
        JLabel infoLabel = new JLabel("Solo i voli in partenza da Napoli possono avere un gate assegnato");
        infoLabel.setForeground(Color.BLUE);

        formPanel.add(infoLabel);
        formPanel.add(new JLabel()); // Empty cell for grid alignment
        formPanel.add(new JLabel("Volo:"));
        formPanel.add(flightComboBox);
        formPanel.add(new JLabel("Gate:"));
        formPanel.add(gateComboBox);

        // Create a panel with BoxLayout for better control of vertical spacing
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(formPanel);

        // Add vertical spacing
        centerPanel.add(Box.createVerticalStrut(100)); // Add 100px of vertical space

        // Add the center panel to the main panel
        gateAssignmentPanel.add(centerPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding around buttons
        JButton assignButton = new JButton("Assegna Gate");

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int flightIndex = flightComboBox.getSelectedIndex();
                int gateIndex = gateComboBox.getSelectedIndex();

                if (flightIndex >= 0 && gateIndex >= 0) {
                    if (flightIndex < departingFlights.size()) {
                        Volo selectedFlight = departingFlights.get(flightIndex);
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Gate " + gates.get(gateIndex).getCodice() +
                            " assegnato al volo " + selectedFlight.getCodiceVolo(),
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Seleziona un volo e un gate",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(assignButton);
        gateAssignmentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Assegna Gate", gateAssignmentPanel);
    }

    private void createBaggageStatusPanel() {
        baggageStatusPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields
        JTextField baggageCodeField = new JTextField();
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        // Make the status selector smaller, with at most 20px extra padding compared to text
        statusComboBox.setPreferredSize(new Dimension(150, 25));
        JCheckBox lostCheckBox = new JCheckBox("Segnala come smarrito");

        formPanel.add(new JLabel("Codice Bagaglio:"));
        formPanel.add(baggageCodeField);
        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel(""));
        formPanel.add(lostCheckBox);

        // Add form panel to center
        baggageStatusPanel.add(formPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi Bagaglio");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = baggageCodeField.getText();
                StatoBagaglio status = (StatoBagaglio) statusComboBox.getSelectedItem();
                boolean isLost = lostCheckBox.isSelected();

                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Inserisci il codice del bagaglio",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if baggage already exists
                for (Bagaglio bagaglio : baggages) {
                    if (bagaglio.getCodice().equals(code)) {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Un bagaglio con questo codice esiste già. Usa la scheda 'Aggiorna Bagaglio' per modificarlo.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Add new baggage
                Bagaglio newBaggage = new Bagaglio(code, status);
                baggages.add(newBaggage);

                JOptionPane.showMessageDialog(AdminDashboard.this,
                    "Nuovo bagaglio aggiunto con successo" +
                    (isLost ? " e segnalato come smarrito" : ""),
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                baggageCodeField.setText("");
                statusComboBox.setSelectedIndex(0);
                lostCheckBox.setSelected(false);
            }
        });

        buttonPanel.add(addButton);
        baggageStatusPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Inserisci Bagaglio", baggageStatusPanel);
    }

    private void createUpdateBaggagePanel() {
        updateBaggagePanel = new JPanel(new BorderLayout());

        // Create selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel selectLabel = new JLabel("Seleziona Bagaglio:");
        JComboBox<String> baggageComboBox = new JComboBox<>();
        // Make the baggage selector smaller, similar to the status selector
        baggageComboBox.setPreferredSize(new Dimension(150, 25));

        // Populate baggage combo box
        for (Bagaglio bagaglio : baggages) {
            baggageComboBox.addItem(bagaglio.getCodice() + " - " + bagaglio.getStato());
        }

        selectionPanel.add(selectLabel);
        selectionPanel.add(baggageComboBox);

        updateBaggagePanel.add(selectionPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields (excluding code field since it's selected from dropdown)
        JComboBox<StatoBagaglio> statusComboBox = new JComboBox<>(StatoBagaglio.values());
        // Make the status selector smaller, with at most 20px extra padding compared to text fields
        statusComboBox.setPreferredSize(new Dimension(150, 25));

        formPanel.add(new JLabel("Stato:"));
        formPanel.add(statusComboBox);

        // Create a panel with BoxLayout for better control of vertical spacing
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(formPanel);

        // Add vertical spacing
        centerPanel.add(Box.createVerticalStrut(100)); // Add 100px of vertical space

        // Add the center panel to the main panel
        updateBaggagePanel.add(centerPanel, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add padding around buttons
        JButton updateButton = new JButton("Aggiorna Bagaglio");
        // Make the button smaller and more suitable for the screen
        updateButton.setPreferredSize(new Dimension(150, 30));
        updateButton.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add action listener to baggage combo box to populate form fields
        baggageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = baggageComboBox.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < baggages.size()) {
                    Bagaglio selectedBaggage = baggages.get(selectedIndex);
                    statusComboBox.setSelectedItem(selectedBaggage.getStato());
                }
            }
        });

        // Trigger the action listener to populate fields initially if there are baggages
        if (baggageComboBox.getItemCount() > 0) {
            baggageComboBox.setSelectedIndex(0);
        }

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = baggageComboBox.getSelectedIndex();
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Seleziona un bagaglio da aggiornare",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StatoBagaglio status = (StatoBagaglio) statusComboBox.getSelectedItem();

                // Update the selected baggage
                Bagaglio selectedBaggage = baggages.get(selectedIndex);
                selectedBaggage.setStato(status);

                JOptionPane.showMessageDialog(AdminDashboard.this,
                    "Stato del bagaglio aggiornato con successo",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);

                // Update the combo box
                baggageComboBox.removeItemAt(selectedIndex);
                baggageComboBox.insertItemAt(selectedBaggage.getCodice() + " - " + selectedBaggage.getStato(), selectedIndex);
                baggageComboBox.setSelectedIndex(selectedIndex);
            }
        });

        buttonPanel.add(updateButton);
        updateBaggagePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Aggiorna Bagaglio", updateBaggagePanel);
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
        model.addColumn("Messaggio Utente");
        model.addColumn("Azioni");

        // Create table with model
        JTable lostBaggageTable = new JTable(model);

        // Set preferred column widths
        lostBaggageTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        lostBaggageTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        lostBaggageTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Add some test data for lost baggage
        model.addRow(new Object[]{"BAG001", "smarrito", "Il bagaglio è di colore nero con etichetta rossa", "Modifica"});
        model.addRow(new Object[]{"BAG002", "smarrito", "Valigia blu con adesivi", "Modifica"});

        // Add mouse listener to handle button clicks in the action column
        lostBaggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = lostBaggageTable.rowAtPoint(evt.getPoint());
                int col = lostBaggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 3) { // Action column
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
                    "Messaggio utente per " + bagaglio.getCodice(), // In a real app, this would come from the database
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
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni Volo"));

        infoPanel.add(new JLabel("Codice Volo:"));
        infoPanel.add(new JLabel(flight.getCodiceVolo()));
        infoPanel.add(new JLabel("Compagnia:"));
        infoPanel.add(new JLabel(flight.getCompagnia()));
        infoPanel.add(new JLabel("Origine:"));
        infoPanel.add(new JLabel(flight.getOrigine()));
        infoPanel.add(new JLabel("Destinazione:"));
        infoPanel.add(new JLabel(flight.getDestinazione()));
        infoPanel.add(new JLabel("Orario:"));
        infoPanel.add(new JLabel(flight.getOrarioPrevisto()));
        infoPanel.add(new JLabel("Stato:"));
        infoPanel.add(new JLabel(flight.getStato().toString()));
        infoPanel.add(new JLabel("Data:"));
        infoPanel.add(new JLabel(flight.getData().toString()));
        infoPanel.add(new JLabel("Ritardo:"));
        infoPanel.add(new JLabel(flight.getTempoRitardo() + " min"));

        // Get the seat capacity from the flight
        int totalSeats = flight.getPostiTotali();
        int availableSeats = flight.getPostiDisponibili();
        int occupiedSeats = totalSeats - availableSeats;

        infoPanel.add(new JLabel("Posti Totali:"));
        infoPanel.add(new JLabel(String.valueOf(totalSeats)));
        infoPanel.add(new JLabel("Posti Disponibili:"));
        infoPanel.add(new JLabel(String.valueOf(availableSeats)));
        infoPanel.add(new JLabel("Posti Occupati:"));
        infoPanel.add(new JLabel(occupiedSeats + " / " + totalSeats));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

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
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttons
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
     * Shows a dialog to modify a booking
     * @param ticketNumber The ticket number of the booking to modify
     * @param row The row in the table
     * @param bookingsTable The table containing the booking
     */
    private void showBookingDetailsDialog(String ticketNumber, int row, JTable bookingsTable) {
        // Create a dialog
        JDialog dialog = new JDialog(this, "Modifica Prenotazione: " + ticketNumber, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Confermato", "In Attesa", "Cancellato"});
        statusComboBox.setSelectedItem(status);

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

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create baggage panel
        JPanel baggagePanel = new JPanel(new BorderLayout());
        baggagePanel.setBorder(BorderFactory.createTitledBorder("Bagagli"));

        DefaultTableModel baggageModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        baggageModel.addColumn("Codice Bagaglio");
        baggageModel.addColumn("Stato");

        // In a real application, we would get the baggage for this booking from the database
        // For now, we'll just add some example baggage
        int numBaggage = Integer.parseInt(baggageCount);
        for (int i = 0; i < numBaggage; i++) {
            baggageModel.addRow(new Object[]{"BAG" + ticketNumber + "-" + (i + 1), "caricato"});
        }

        JTable baggageTable = new JTable(baggageModel);
        JScrollPane baggageScrollPane = new JScrollPane(baggageTable);
        baggagePanel.add(baggageScrollPane, BorderLayout.CENTER);

        mainPanel.add(baggagePanel, BorderLayout.SOUTH);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Salva");
        JButton cancelButton = new JButton("Annulla");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the booking in the table
                bookingsTable.setValueAt(nameField.getText(), row, 1);
                bookingsTable.setValueAt(seatField.getText(), row, 2);
                bookingsTable.setValueAt(statusComboBox.getSelectedItem(), row, 3);

                JOptionPane.showMessageDialog(dialog,
                    "Prenotazione aggiornata con successo",
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

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }
}
