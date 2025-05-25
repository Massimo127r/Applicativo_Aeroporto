package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends BaseDashboard {
    private JTable voliTable;
    private JTable bagagliTable;
    private JTable prenotazioniTable;
    private JTable passeggeriTable;
    private JTable gatesTable;

    public AdminDashboard() {
        super();
        setTitle("Dashboard Amministratore");
    }

    @Override
    protected void createTabs() {
        // Tab Voli
        JPanel voliPanel = new JPanel(new GridLayout(1,1) );
        String[] voliColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario", "Stato", "Gate"};
        voliTable = createTable(voliColumns);
        addTableToPanel(voliPanel, voliTable);
        updateVoliTable();
        tabbedPane.addTab("Voli", voliPanel);

        // Tab Bagagli
        JPanel bagagliPanel = new JPanel(new GridLayout(1,1) );
        String[] bagagliColumns = {"Codice",  "Stato"};
        bagagliTable = createTable(bagagliColumns);
        addTableToPanel(bagagliPanel, bagagliTable);
        updateBagagliTable();
        tabbedPane.addTab("Bagagli", bagagliPanel);

        // Tab Prenotazioni
        JPanel prenotazioniPanel = new JPanel(new GridLayout(1,1) );
        String[] prenotazioniColumns = {"N°Biglietto", "Passeggero", "Stato", "Posto", "Bagaglio"};
        prenotazioniTable = createTable(prenotazioniColumns);
        addTableToPanel(prenotazioniPanel, prenotazioniTable);
        updatePrenotazioniTable();
        tabbedPane.addTab("Prenotazioni", prenotazioniPanel);

        // Tab Passeggeri
        JPanel passeggeriPanel = new JPanel(new GridLayout(1,1) );
        String[] passeggeriColumns = {"Nome", "Cognome", "Codice Fiscale", "Passaporto"};
        passeggeriTable = createTable(passeggeriColumns);
        addTableToPanel(passeggeriPanel, passeggeriTable);
        updatePasseggeriTable();
        tabbedPane.addTab("Passeggeri", passeggeriPanel);

        // Tab Gates
        JPanel gatesPanel = new JPanel(new GridLayout(1,1) );
        String[] gatesColumns = {"Codice"};
        gatesTable = createTable(gatesColumns);
        addTableToPanel(gatesPanel, gatesTable);
        updateGatesTable();
        tabbedPane.addTab("Gates", gatesPanel);
    }

    private void updateVoliTable() {
        updateTable(voliTable, controller.getVoli());
    }

    private void updateBagagliTable() {
        updateTable(bagagliTable, controller.getBagagli());
    }

    private void updatePrenotazioniTable() {
        updateTable(prenotazioniTable, controller.getPrenotazioni());
    }

    private void updatePasseggeriTable() {
        updateTable(passeggeriTable, controller.getPasseggeri());
    }

    private void updateGatesTable() {
        updateTable(gatesTable, controller.getGates());
    }

    @Override
    protected Object[] getTableRow(Object item) {
        if (item instanceof Volo) {
            Volo volo = (Volo) item;
            return new Object[]{
                    volo.getCodiceVolo(),
                    volo.getCompagnia(),
                    volo.getOrigine(),
                    volo.getDestinazione(),
                    volo.getData(),
                    volo.getOrarioPrevisto(),
                    volo.getStato(),
                    volo.getTempoRitardo(),
            };
        } else if (item instanceof Bagaglio) {
            Bagaglio bagaglio = (Bagaglio) item;
            return new Object[]{
                    bagaglio.getCodice(),
                    bagaglio.getStato()
            };
        } else if (item instanceof Prenotazione) {
            Prenotazione prenotazione = (Prenotazione) item;
            return new Object[]{
                    prenotazione.getNumeroBiglietto(),
                    prenotazione.getPosto(),
                    prenotazione.getPasseggero().getNome() + " " + prenotazione.getPasseggero().getCognome(),
                    prenotazione.getStato(),
            };
        } else if (item instanceof Passeggero) {
            Passeggero passeggero = (Passeggero) item;
            return new Object[]{
                    passeggero.getNome(),
                    passeggero.getCognome(),
                    passeggero.getnDocumento()
            };
        } else if (item instanceof Gate) {
            Gate gate = (Gate) item;
            return new Object[]{
                    gate.getCodice()
            };
        }
        return new Object[0];
    }
}
