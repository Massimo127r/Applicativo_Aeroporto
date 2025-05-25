package controller;

import model.*;
import gui.LoginFrame;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    private static Controller instance;
    private List<Utente> utenti;
    private List<Amministratore> amministratori;
    private Utente utenteCorrente;
    private DataInitializer dataInitializer;

    private Controller() {
        utenti = new ArrayList<>();
        amministratori = new ArrayList<>();
        dataInitializer = DataInitializer.getInstance();

        // Aggiungo alcuni utenti di default per test
        amministratori.add(new Amministratore("admin", "admin", "Admin", "System"));
        utenti.add(new Utente("user", "user", "Utente", "Test"));
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public boolean login(String username, String password, String tipoUtente) {
        if (tipoUtente.equals("Amministratore")) {
            for (Amministratore admin : amministratori) {
                if (admin.getNomeUtente().equals(username) && admin.getPassword().equals(password)) {
                    utenteCorrente = admin;
                    return true;
                }
            }
        } else {
            for (Utente utente : utenti) {
                if (utente.getNomeUtente().equals(username) && utente.getPassword().equals(password)) {
                    utenteCorrente = utente;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean registraUtente(String username, String password, String nome, String cognome) {
        // Verifica se l'username esiste già
        for (Utente utente : utenti) {
            if (utente.getNomeUtente().equals(username)) {
                return false;
            }
        }
        for (Amministratore admin : amministratori) {
            if (admin.getNomeUtente().equals(username)) {
                return false;
            }
        }

        // Crea nuovo utente
        Utente nuovoUtente = new Utente(username, password, nome, cognome);
        utenti.add(nuovoUtente);
        return true;
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public boolean isAmministratore() {
        return utenteCorrente instanceof Amministratore;
    }

    public void logout() {
        utenteCorrente = null;
    }

    // Metodi per gestire i voli
    public List<Volo> getVoli() {
        return dataInitializer.getVoli();
    }

    public void aggiungiVolo(Volo volo) {
        dataInitializer.addVolo(volo);
    }

    // Metodi per gestire i gate
    public List<Gate> getGates() {
        return dataInitializer.getGates();
    }

    public void aggiungiGate(Gate gate) {
        dataInitializer.addGate(gate);
    }

    // Metodi per gestire i bagagli
    public List<Bagaglio> getBagagli() {
        return dataInitializer.getBagagli();
    }

    public void aggiungiBagaglio(Bagaglio bagaglio) {
        dataInitializer.addBagaglio(bagaglio);
    }

    // Metodi per gestire le prenotazioni
    public List<Prenotazione> getPrenotazioni() {
        return dataInitializer.getPrenotazioni();
    }

    public void aggiungiPrenotazione(Prenotazione prenotazione) {
        dataInitializer.addPrenotazione(prenotazione);
    }

    // Metodi per gestire i passeggeri
    public List<Passeggero> getPasseggeri() {
        return dataInitializer.getPasseggeri();
    }

    public void aggiungiPasseggero(Passeggero passeggero) {
        dataInitializer.addPasseggero(passeggero);
    }

    // Metodi per filtrare i dati
    public List<Volo> getVoliByStato(StatoVolo stato) {
        List<Volo> voliFiltrati = new ArrayList<>();
        for (Volo volo : dataInitializer.getVoli()) {
            if (volo.getStato() == stato) {
                voliFiltrati.add(volo);
            }
        }
        return voliFiltrati;
    }

    public List<Bagaglio> getBagagliByStato(StatoBagaglio stato) {
        List<Bagaglio> bagagliFiltrati = new ArrayList<>();
        for (Bagaglio bagaglio : dataInitializer.getBagagli()) {
            if (bagaglio.getStato() == stato) {
                bagagliFiltrati.add(bagaglio);
            }
        }
        return bagagliFiltrati;
    }

    public List<Prenotazione> getPrenotazioniByStato(StatoPrenotazione stato) {
        List<Prenotazione> prenotazioniFiltrate = new ArrayList<>();
        for (Prenotazione prenotazione : dataInitializer.getPrenotazioni()) {
            if (prenotazione.getStato() == stato) {
                prenotazioniFiltrate.add(prenotazione);
            }
        }
        return prenotazioniFiltrate;
    }
}
