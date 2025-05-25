package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {
    private static DataInitializer instance;
    private List<Volo> voli;
    private List<Gate> gates;
    private List<Bagaglio> bagagli;
    private List<Prenotazione> prenotazioni;
    private List<Passeggero> passeggeri;

    private DataInitializer() {
        initializeData();
    }

    public static DataInitializer getInstance() {
        if (instance == null) {
            instance = new DataInitializer();
        }
        return instance;
    }

    private void initializeData() {
        // Inizializzazione Gates
        gates = new ArrayList<>();
        gates.add(new Gate(1));
        gates.add(new Gate(2));
        gates.add(new Gate(1));
        gates.add(new Gate(2));

        // Inizializzazione Voli
        voli = new ArrayList<>();
        voli.add(new Volo("AZ123", "Alitalia", "Roma", "Milano",
                "10:00", StatoVolo.programmato, LocalDate.now(), 1));
        voli.add(new Volo("AZ456", "Alitalia", "Milano", "Roma",
                "14:30", StatoVolo.inRitardo, LocalDate.now(), 2));
        voli.add(new Volo("LH789", "Lufthansa", "Roma", "Francoforte",
                "16:45", StatoVolo.cancellato, LocalDate.now(), 3));
        voli.add(new Volo("BA321", "British Airways", "Roma", "Londra",
                "09:15", StatoVolo.programmato, LocalDate.now(), 4));

        // Inizializzazione Passeggeri
        passeggeri = new ArrayList<>();
        passeggeri.add(new Passeggero("MARIO", "ROSSI", "MR12345"));
        passeggeri.add(new Passeggero("LUCA", "BIANCHI", "LB67890"));
        passeggeri.add(new Passeggero("ANNA", "VERDI", "AV54321"));

        // Inizializzazione Bagagli
        bagagli = new ArrayList<>();
        bagagli.add(new Bagaglio("BAG001", StatoBagaglio.ritirabile));
        bagagli.add(new Bagaglio("BAG002", StatoBagaglio.caricato));
        bagagli.add(new Bagaglio("BAG003", StatoBagaglio.ritirabile));
        bagagli.add(new Bagaglio("BAG004", StatoBagaglio.caricato));

        // Inizializzazione Prenotazioni
        prenotazioni = new ArrayList<>();
        prenotazioni.add(new Prenotazione("PREN001", "B1",
                StatoPrenotazione.confermato, passeggeri.get(0)));
        prenotazioni.add(new Prenotazione("PREN002", "B2",
                StatoPrenotazione.confermato, passeggeri.get(1)));
        prenotazioni.add(new Prenotazione("PREN003", "B5",
                StatoPrenotazione.confermato, passeggeri.get(2)));
    }

    // Getters per accedere ai dati
    public List<Volo> getVoli() {
        return voli;
    }

    public List<Gate> getGates() {
        return gates;
    }

    public List<Bagaglio> getBagagli() {
        return bagagli;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public List<Passeggero> getPasseggeri() {
        return passeggeri;
    }

    // Metodi per aggiungere nuovi elementi
    public void addVolo(Volo volo) {
        voli.add(volo);
    }

    public void addGate(Gate gate) {
        gates.add(gate);
    }

    public void addBagaglio(Bagaglio bagaglio) {
        bagagli.add(bagaglio);
    }

    public void addPrenotazione(Prenotazione prenotazione) {
        prenotazioni.add(prenotazione);
    }

    public void addPasseggero(Passeggero passeggero) {
        passeggeri.add(passeggero);
    }
}
