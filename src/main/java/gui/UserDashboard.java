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
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class UserDashboard extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    // Homepage tab components
    private JPanel homepagePanel;
    private JTable flightsTable;
    private JPanel searchPanel;
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;

    // Booking tab components
    private JPanel bookingPanel;
    private JComboBox<String> flightComboBox;
    private JTextField passengerNameField;
    private JTextField passengerSurnameField;
    private JTextField passengerDocumentField;
    private JSpinner baggageCountSpinner;
    private JTable bookingsTable;
    private JTable baggageTable;
    // My Flights tab components
    private JPanel myFlightsPanel;
    private JTable myFlightsTable;
    private JTextField myFlightsSearchField;

    // Baggage tracking tab components
    private JPanel baggageTrackingPanel;
    private JTextField baggageCodeField;
    private JLabel baggageStatusLabel;
    private JButton reportLostButton;

    // Data
    private List<Volo> flights;
    private List<Prenotazione> bookings;
    private List<Bagaglio> baggages;
    private Utente user;
    private controller.Controller controller;

    public UserDashboard(Utente user) {
        if (!user.isGenerico()) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo di utente generico");
        }
        this.user = user;

        // Initialize controller
        this.controller = new controller.Controller();

        // Initialize data
        initializeTestData();

        // Set up the frame
        setTitle("Dashboard Utente - Aeroporto di Napoli");
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
        createHomepagePanel();
        createBookingPanel();
        createMyFlightsPanel();
        createBaggageTrackingPanel();

        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Create top panel with welcome label and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIManager.BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add welcome label
        JLabel welcomeLabel = new JLabel("Benvenuto, " + user.getNome() + " " + user.getCognome() + "!");
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

        if (flights == null) {
            flights = new ArrayList<>();
        }

        // Initialize bookings
        bookings = controller.getPrenotazioneByUtente(user);

        // Initialize baggages
        baggages = controller.getBagagliByUtente(user);
    }

    private void createHomepagePanel() {
        homepagePanel = new JPanel(new BorderLayout());
        homepagePanel.setBackground(UIManager.BACKGROUND_COLOR);

        // Create search panel
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

        searchTypeComboBox = new JComboBox<>(new String[] {
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
                volo.getTempoRitardo()
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

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(UIManager.BACKGROUND_COLOR);
        homepagePanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(UIManager.BACKGROUND_COLOR);
        homepagePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("Home", homepagePanel);
    }

    private void filterFlights() {
        String searchText = searchField.getText().toLowerCase();
        String searchType = (String) searchTypeComboBox.getSelectedItem();

        DefaultTableModel model = (DefaultTableModel) flightsTable.getModel();
        model.setRowCount(0);

        for (Volo volo : flights) {
            boolean match = false;

            if ( searchText.isEmpty()) {
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
                volo.getTempoRitardo()
            });
        }
    }

    private void createBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nuova Prenotazione"));

        // Add form fields
        flightComboBox = new JComboBox<>();
        for (Volo volo : flights) {
            if (volo.getStato() != StatoVolo.cancellato && volo.getStato() != StatoVolo.atterrato && volo.getPostiDisponibili() >0) {
                flightComboBox.addItem(volo.getCodiceVolo() + " - " + volo.getCompagnia() + " (" +
                                      volo.getOrigine() + " -> " + volo.getDestinazione() + ")");
            }
        }

        passengerNameField = new JTextField();
        passengerSurnameField = new JTextField();
        passengerDocumentField = new JTextField();
        // Remove seatField as we'll use a dialog for seat selection
        baggageCountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));

        formPanel.add(new JLabel("Volo:"));
        formPanel.add(flightComboBox);
        formPanel.add(new JLabel("Nome Passeggero:"));
        formPanel.add(passengerNameField);
        formPanel.add(new JLabel("Cognome Passeggero:"));
        formPanel.add(passengerSurnameField);
        formPanel.add(new JLabel("Numero Documento:"));
        formPanel.add(passengerDocumentField);
        // Remove seat field from form as we'll use a dialog
        formPanel.add(new JLabel("Numero Bagagli:"));
        formPanel.add(baggageCountSpinner);

        // Add book button
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

        // Add form panel to top
        bookingPanel.add(formPanel, BorderLayout.NORTH);

        // Create bookings table
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };


       /* bookingsTable = new JTable(model);

        // Add mouse listener to show baggage details when a booking is clicked
        bookingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookingsTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    showBaggageDetails(bookings.get(row));
                }
            }
        });

        // Add table to scroll pane with title
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Le Mie Prenotazioni"));
        bookingPanel.add(scrollPane, BorderLayout.CENTER);
        updateBookingsTable();

        */
        // Add panel to tabbed pane
        tabbedPane.addTab("Prenotazione Voli", bookingPanel);

    }

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

        // Open seat selection dialog
        String seat = selectSeat(flightCode);
        if (seat == null || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "È necessario selezionare un posto", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate ticket number
        String ticketNumber = "TKT" + System.currentTimeMillis() % 10000;

        // Create passenger
        Passeggero passenger = new Passeggero(name, surname, document);
        // Create booking
        Prenotazione booking = new Prenotazione(flightCode, ticketNumber, seat, StatoPrenotazione.inAttesa, passenger);


        // Add baggage if needed
        StringBuilder baggageInfo = new StringBuilder();
        if (baggageCount > 0) {
            List<Bagaglio> bookingBaggages = new ArrayList<>();
            baggageInfo.append("\n\nID Bagagli:");
            for (int i = 0; i < baggageCount; i++) {
                String baggageCode = "BAG" + ticketNumber + "-" + (i + 1);
                Bagaglio baggage = new Bagaglio(baggageCode, StatoBagaglio.caricato);
                bookingBaggages.add(baggage);
                baggages.add(baggage);
                baggageInfo.append("\n- ").append(baggageCode);
            }
            booking.setBagagli(bookingBaggages);
        }

        // Add booking to list
        bookings.add(booking);
        controller.creaPrenotazione(booking, flightCode, user);
        // Update bookings table

        // Clear form
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

    private void updateBookingsTable() {
        DefaultTableModel model = (DefaultTableModel) bookingsTable.getModel();
        model.setRowCount(0);

        for (Prenotazione booking : bookings) {
            Passeggero passenger = booking.getPasseggero();
            int baggageCount = booking.getBagagli() != null ? booking.getBagagli().size() : 0;

            model.addRow(new Object[]{
                booking.getNumeroBiglietto(),
                passenger.getNome() + " " + passenger.getCognome(),
                booking.getPosto(),
                booking.getStato(),
                baggageCount
            });
        }

        // Also update My Flights table if it exists
        if (myFlightsTable != null) {
            updateMyFlightsTable();
        }
    }

    private void createMyFlightsPanel() {
        myFlightsPanel = new JPanel(new BorderLayout());

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Prenotazioni"));

        myFlightsSearchField = new JTextField(20);
        JButton searchButton = new JButton("Cerca");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterMyFlights();
            }
        });

        searchPanel.add(new JLabel("Cerca per nome passeggero o numero volo:"));
        searchPanel.add(myFlightsSearchField);
        searchPanel.add(searchButton);

        myFlightsPanel.add(searchPanel, BorderLayout.NORTH);

        // Create table model with column names
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        model.addColumn("Codice Volo");

        model.addColumn("Numero Biglietto");
        model.addColumn("Passeggero");
        model.addColumn("N° Documento");
        model.addColumn("Posto");
        model.addColumn("Stato");
        model.addColumn("Bagagli");

        myFlightsTable = new JTable(model);

        // Add mouse listener to show baggage details when a booking is clicked
        myFlightsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = myFlightsTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    showBaggageDetails(bookings.get(row));
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(myFlightsTable);
        myFlightsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button

        updateMyFlightsTable();
        JPanel buttonPanel = new JPanel();
        myFlightsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to tabbed pane
        tabbedPane.addTab("I Miei Voli", myFlightsPanel);
    }

    private void filterMyFlights() {
        String searchText = myFlightsSearchField.getText().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();
        model.setRowCount(0);

        for (Prenotazione booking : bookings) {
            Passeggero passenger = booking.getPasseggero();
            String passengerName = passenger.getNome() + " " + passenger.getCognome();
            int baggageCount = booking.getBagagli() != null ? booking.getBagagli().size() : 0;

            if (searchText.isEmpty() ||
                passengerName.toLowerCase().contains(searchText) ||
                booking.getNumeroBiglietto().toLowerCase().contains(searchText)) {

                model.addRow(new Object[]{
                    booking.getNumeroBiglietto(),
                    passengerName,
                    booking.getPosto(),
                    booking.getStato(),
                    baggageCount
                });
            }
        }
    }

    private void updateMyFlightsTable() {
        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();
        model.setRowCount(0);

        for (Prenotazione booking : bookings) {
            Passeggero passenger = booking.getPasseggero();
            int baggageCount = booking.getBagagli() != null ? booking.getBagagli().size() : 0;

            model.addRow(new Object[]{
                    booking.getCodiceVolo(),
                booking.getNumeroBiglietto(),
                passenger.getNome() + " " + passenger.getCognome(),
                    passenger.getnDocumento(),
                booking.getPosto(),
                booking.getStato(),
                baggageCount
            });
        }
    }

    private void createBaggageTrackingPanel() {
        baggageTrackingPanel = new JPanel(new BorderLayout());

        // Create form panel for searching baggage by flight code and baggage code
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

                // Get the baggage table from the scroll pane


                // Filter the table based on search criteria
                DefaultTableModel model = (DefaultTableModel) baggageTable.getModel();
                model.setRowCount(0);

                for (Bagaglio baggage : baggages) {
                    // Find the booking that contains this baggage
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

                    // Check if baggage matches search criteria
                    boolean matchesFlightCode = flightCode.isEmpty() || flightInfo.toLowerCase().contains(flightCode.toLowerCase());
                    boolean matchesBaggageCode = baggageCode.isEmpty() || baggage.getCodice().toLowerCase().contains(baggageCode.toLowerCase());

                    if (matchesFlightCode && matchesBaggageCode) {
                        // Check if baggage is marked as lost
                        String status = baggage.getStato().toString();
                        if (baggage.getStato().toString().equals("smarrito")) {
                            status = "Smarrimento segnalato";
                        }

                        model.addRow(new Object[]{
                                flightInfo,
                                tckInfo,
                                baggage.getCodice(),
                                status,
                                "Segnala Smarrimento"
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

        // Create table to show all baggage
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        model.addColumn("Volo");
        model.addColumn("Codice Biglietto");

        model.addColumn("Codice Bagaglio");
        model.addColumn("Stato");
        model.addColumn("Azioni");

         baggageTable = new JTable(model);
        updateBaggageTable(baggageTable);
        // Set custom renderer for the status column
        baggageTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) value;
                if (status != null && status.contains("Smarrimento segnalato")) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }

                return c;
            }
        });

        // Set custom renderer for the action column
        baggageTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Segnala Smarrimento");
                button.setBackground(new Color(255, 200, 200));
                return button;
            }
        });

        // Add mouse listener to handle button clicks in the action column
        baggageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = baggageTable.rowAtPoint(evt.getPoint());
                int col = baggageTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 4) { // Action column
                    String baggageCode = (String) baggageTable.getValueAt(row, 2);
                    reportLostBaggageFromTable(baggageCode, row, baggageTable);
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(baggageTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tutti i Bagagli"));
        baggageTrackingPanel.add(scrollPane, BorderLayout.CENTER);
        updateBaggageTable(baggageTable);
        // Add refresh button





        JPanel buttonPanel = new JPanel();

        baggageTrackingPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Initialize the table
        updateBaggageTable(baggageTable);

        // Add panel to tabbed pane
        tabbedPane.addTab("Monitoraggio Bagagli", baggageTrackingPanel);
    }

    /**
     * Updates the baggage table with the latest data
     * @param baggageTable The table to update
     */
    private void updateBaggageTable(JTable baggageTable) {
        DefaultTableModel model = (DefaultTableModel) baggageTable.getModel();
        model.setRowCount(0);

        for (Bagaglio baggage : baggages) {
            // Find the booking that contains this baggage
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

            // Check if baggage is marked as lost
            String status = baggage.getStato().toString();
            if (baggage.getStato().toString().equals("smarrito")) {
                status = "Smarrimento segnalato";
            }

            model.addRow(new Object[]{
                    flightInfo,
                    tckInfo,
                baggage.getCodice(),
                status,
                "Segnala Smarrimento"
            });
        }
    }

    /**
     * Reports a baggage as lost from the baggage table
     * @param baggageCode The code of the baggage to report as lost
     * @param row The row in the table
     * @param baggageTable The table containing the baggage
     */
    private void reportLostBaggageFromTable(String baggageCode, int row, JTable baggageTable) {
        // Show dialog for description
        int description = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler segnalare questo bagaglio come smarrito?");

        if (description == 0 ) {
            JOptionPane.showMessageDialog(this,
                "Segnalazione inviata con successo!\nUn operatore ti contatterà al più presto.",
                "Segnalazione Inviata",
                JOptionPane.INFORMATION_MESSAGE);

            // Mark the baggage as lost
            for (Bagaglio baggage : baggages) {
                if (baggage.getCodice().equals(baggageCode)) {
                    baggage.setStato(StatoBagaglio.smarrito);
                    // Update the table
                    controller.aggiornaBagaglio(baggage);

                    updateBaggageTable(baggageTable);
                    break;
                }
            }
        }
    }

    private void trackBaggage() {
        String code = baggageCodeField.getText();

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il codice del bagaglio", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        for (Bagaglio baggage : baggages) {
            if (baggage.getCodice().equals(code)) {
                baggageStatusLabel.setText("Stato: " + baggage.getStato());
                reportLostButton.setEnabled(true);
                found = true;
                break;
            }
        }

        if (!found) {
            baggageStatusLabel.setText("Stato: Non trovato");
            reportLostButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Bagaglio non trovato", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reportLostBaggage() {
        String code = baggageCodeField.getText();

        // Show dialog for description
        String description = JOptionPane.showInputDialog(this,
            "Inserisci una descrizione del bagaglio smarrito:",
            "Segnalazione Smarrimento",
            JOptionPane.PLAIN_MESSAGE);

        if (description != null && !description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Segnalazione inviata con successo!\nUn operatore ti contatterà al più presto.",
                "Segnalazione Inviata",
                JOptionPane.INFORMATION_MESSAGE);

            // Update the baggage status in the database
            for (Bagaglio baggage : baggages) {
                if (baggage.getCodice().equals(code)) {
                    // Set the baggage status to "smarrito"
                    baggage.setStato(StatoBagaglio.smarrito);
                    // Update the status label
                    baggageStatusLabel.setText("Stato: Smarrimento segnalato");
                    baggageStatusLabel.setForeground(Color.RED);
                    break;
                }
            }
        }
    }

    /**
     * Shows a dialog with baggage details for a booking
     * @param booking The booking to show baggage details for
     */
    private void showBaggageDetails(Prenotazione booking) {
        if (booking.getBagagli() == null || booking.getBagagli().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nessun bagaglio associato a questa prenotazione.",
                "Dettagli Bagagli",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a panel to display baggage details
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add a label for each baggage
        for (Bagaglio baggage : booking.getBagagli()) {
            JLabel label = new JLabel("Codice: " + baggage.getCodice() + " - Stato: " + baggage.getStato());
            panel.add(label);
            panel.add(Box.createVerticalStrut(5)); // Add some spacing
        }

        // Show the dialog
        JOptionPane.showMessageDialog(this,
            panel,
            "Dettagli Bagagli",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog for seat selection and returns the selected seat
     * @param flightCode The code of the flight to select a seat for
     * @return The selected seat or null if no seat was selected
     */
    private String selectSeat(String flightCode) {
        // 1) Trova il volo in memoria
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

        // 2) Carica i posti dal controller
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

        // 3) Ordina per fila e lettera
        posti.sort(Comparator.comparing(Posto::getSeatNumber, (a, b) -> {
            int filaA = Integer.parseInt(a.replaceAll("\\D", ""));
            int filaB = Integer.parseInt(b.replaceAll("\\D", ""));
            if (filaA != filaB) return filaA - filaB;
            return a.replaceAll("\\d", "").charAt(0) - b.replaceAll("\\d", "").charAt(0);
        }));

        // 4) Costruisci il dialog
        JDialog seatDialog = new JDialog(this, "Seleziona Posto - Volo " + flightCode, true);
        seatDialog.setSize(700, 600);
        seatDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(new JLabel(
                        "Volo: " + flightCode + " - Posti disponibili: " + selectedFlight.getPostiDisponibili()),
                BorderLayout.NORTH);

        // --- Qui dichiariamo di nuovo airplanePanel ---
        JPanel airplanePanel = new JPanel(new BorderLayout(10,10));
        airplanePanel.setBorder(BorderFactory.createTitledBorder("Seleziona un posto disponibile"));

        // 5) Griglia con corridoio
        int cols = 6;
        int rows = (int) Math.ceil(posti.size() / (double) cols);
        JPanel seatGrid = new JPanel(new GridLayout(rows, cols + 1, 5, 5));

        final String[] selectedSeat = {null};
        int countInRow = 0;

        for (Posto p : posti) {
            if (countInRow == 3) {
                seatGrid.add(Box.createHorizontalStrut(20));
            }

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

            seatGrid.add(btn);
            countInRow++;
            if (countInRow == cols) countInRow = 0;
        }

        // 6) Scroll pane per la griglia
        JScrollPane scrollPane = new JScrollPane(seatGrid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        airplanePanel.add(scrollPane, BorderLayout.CENTER);

        // 7) Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legend.add(new JLabel("Libero", JLabel.LEFT));
        legend.add(createLegendBox(Color.GREEN));
        legend.add(new JLabel("Occupato", JLabel.LEFT));
        legend.add(createLegendBox(Color.RED));
        legend.add(new JLabel("Selezionato", JLabel.LEFT));
        legend.add(createLegendBox(Color.BLUE));
        airplanePanel.add(legend, BorderLayout.NORTH);

        // 8) Footer con pulsanti
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

        // Assemblo tutto
        mainPanel.add(airplanePanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);
        seatDialog.setContentPane(mainPanel);
        seatDialog.setVisible(true);

        return selectedSeat[0];
    }

    /**
     * Aiuto per creare un quadratino colorato
     */
    private Component createLegendBox(Color c) {
        JPanel box = new JPanel();
        box.setBackground(c);
        box.setPreferredSize(new Dimension(15, 15));
        return box;
    }


}
