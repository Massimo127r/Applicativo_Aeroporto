package controller;

import dao.PostgresDao;
import implementazionePostgresDao.ImplementazionePostgresDao;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final PostgresDao dao;

    public Controller() {
        this.dao = new ImplementazionePostgresDao();
    }

    public Utente login(String login, String password, String tipo) {
        String tipoDb = "Amministratore".equals(tipo) ? "amministratore" : "generico";
        return dao.getUtenteByCredentialsAndType(login, password, tipoDb);
    }


    public boolean registraUtente(Utente utente, boolean isAdmin) {
        String tipo = isAdmin ? "amministratore" : "generico";
        return dao.insertUtente(utente, tipo);
    }

    public List<Volo> getAllVoli() {
        return dao.getAllVoli();
    }


    public boolean inserisciVolo(String codiceVolo, String compagnia, String origine, String destinazione,
                                 String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo, int totalSeats, int availableSeats) {
        Volo volo = new Volo(codiceVolo, compagnia, origine, destinazione, orarioPrevisto, stato, data, tempoRitardo, totalSeats, availableSeats, 0);
        return dao.insertVolo(volo);
    }

    public boolean modificaVolo(Volo volo) {
        return dao.updateVolo(volo);
    }

    public List<Gate> getAllGates() {
        return dao.getAllGates();
    }

    public boolean assegnaGate(int codiceGate, String codiceVolo) {
        return dao.assignGateToFlight(codiceGate, codiceVolo);
    }


    public List<Prenotazione> getPrenotazioniByVolo(Volo volo) {
        return dao.getPrenotazioniByVolo(volo);
    }

    public boolean creaPrenotazione(Prenotazione prenotazione, String codiceVolo, Utente utente) {
        return dao.insertPrenotazione(prenotazione, codiceVolo, utente);
    }

    public boolean aggiornaPrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto) {
        return dao.updatePrenotazione(prenotazione, numeroBiglietto);
    }

    public List<Bagaglio> getAllBagagli() {
        return dao.getAllBagagli();
    }

    public List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto) {
        return dao.getBagagliByPrenotazione(numeroBiglietto);
    }

    public Bagaglio getBagaglioByCodice(String codice) {
        return dao.getBagaglioByCodice(codice);
    }


    public boolean aggiornaBagaglio(Bagaglio bagaglio) {
        return dao.updateBagaglio(bagaglio);
    }

    public boolean aggiornatAllBagagli(String codiceVolo, StatoBagaglio nuovoStato) {
        return dao.updateBagagliByVolo(codiceVolo, nuovoStato);
    }

    public List<Prenotazione> getPrenotazioneByUtente(Utente utente) {
        return dao.getPrenotazioneByUtente(utente);

    }

    public List<Posto> getPostiByVolo(String coidceVolo) {
        return dao.getPostiByVolo(coidceVolo);
    }

    public List<Bagaglio> getBagagliByUtente(Utente user) {
        return dao.getBagagliByUtente(user);
    }

    public Prenotazione getPrenotazioneByBagaglio(String codiceBagaglio) {
        return dao.getPrenotazioneByBagaglio(codiceBagaglio);
    }


    public boolean updatePassengerInfo(String numeroBiglietto, String nome, String cognome, String nDocumento) {
        return dao.updatePasseggeroInPrenotazione(numeroBiglietto, nome, cognome, nDocumento);
    }
}
