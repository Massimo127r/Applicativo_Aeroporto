package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserDashboard extends BaseDashboard {
    private JTable voliTable;
    private JTable bagagliTable;
    private JTable prenotazioniTable;

    public UserDashboard() {
        super();
        setTitle("Dashboard Utente");
    }

    @Override
    protected void createTabs() {
        // Tab Voli
        JPanel voliPanel = new JPanel(new GridLayout(1, 1));
        String[] voliColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario", "Stato", "Gate"};
        voliTable = createTable(voliColumns);
        addTableToPanel(voliPanel, voliTable);
        updateVoliTable();
        tabbedPane.addTab("Voli", voliPanel);

        // Tab Bagagli
        JPanel bagagliPanel = new JPanel(new GridLayout(1, 1));
        String[] bagagliColumns = {"Codice",  "Stato"};
        bagagliTable = createTable(bagagliColumns);
        addTableToPanel(bagagliPanel, bagagliTable);
        updateBagagliTable();
        tabbedPane.addTab("Bagagli", bagagliPanel);

        // Tab Prenotazioni
        JPanel prenotazioniPanel = new JPanel(new GridLayout(1, 1));
        String[] prenotazioniColumns = {"N°Biglietto", "Passeggero", "Stato", "Posto", "Bagaglio"};
        prenotazioniTable = createTable(prenotazioniColumns);
        addTableToPanel(prenotazioniPanel, prenotazioniTable);
        updatePrenotazioniTable();
        tabbedPane.addTab("Prenotazioni", prenotazioniPanel);
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
                    prenotazione.getPasseggero().getNome() + " " + prenotazione.getPasseggero().getCognome(),
                    prenotazione.getStato(),
                    prenotazione.getPosto(),
                    prenotazione.getBagagli()

            };
        }
        return new Object[0];
    }
}
