package controller;

import DAO.PostgresDAO;
import ImplementazionePostgresDAO.ImplementazionePostgresDAO;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final PostgresDAO dao;

    public Controller() {
        this.dao = new ImplementazionePostgresDAO();
    }



    /**
     * Effettua il login verificando anche il tipo di utente
     * @param login Nome utente
     * @param password Password
     * @param tipo Tipo di utente (Amministratore o Utente)
     * @return Oggetto Utente se le credenziali sono valide e il tipo corrisponde, null altrimenti
     */
    public Utente login(String login, String password, String tipo) {
        // Converti il tipo in formato database (amministratore o generico)
        String tipoDb = "Amministratore".equals(tipo) ? "amministratore" : "generico";
        return dao.getUtenteByCredentialsAndType(login, password, tipoDb);
    }

    /*
    public boolean registraUtente(Utente utente, boolean isAdmin) {
        String tipo = isAdmin ? "amministratore" : "generico";
        return dao.insertUtente(utente, tipo);
    }
*/
    // Metodi per la gestione dei voli
    public List<Volo> getAllVoli() {
        return dao.getAllVoli();
    }

    public Volo getVoloByCodice(String codiceVolo) {
        return dao.getVoloByCodice(codiceVolo);
    }

    public boolean inserisciVolo(String codiceVolo, String compagnia, String origine, String destinazione,
                                String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo,  int totalSeats, int availableSeats) {
        Volo volo = new Volo(codiceVolo, compagnia, origine, destinazione, orarioPrevisto, stato, data, tempoRitardo,totalSeats,availableSeats, 0  );
        return dao.insertVolo(volo);
    }

    public boolean modificaVolo(Volo volo) {
        return dao.updateVolo(volo);
    }

    // Metodi per la gestione dei gate
    public List<Gate> getAllGates() {
        return dao.getAllGates();
    }

    public boolean assegnaGate(int codiceGate, String codiceVolo) {
        return dao.assignGateToFlight(codiceGate, codiceVolo);
    }


    // Metodi per la gestione delle prenotazioni
    public List<Prenotazione> getAllPrenotazioni() {
        return dao.getAllPrenotazioni();
    }

    public Prenotazione getPrenotazioneByNumeroBiglietto(String numeroBiglietto) {
        return dao.getPrenotazioneByNumeroBiglietto(numeroBiglietto);
    }

    public List<Prenotazione> getPrenotazioniByPasseggero(String nome, String cognome) {
        return dao.getPrenotazioniByPasseggero(nome, cognome);
    }

    public boolean creaPrenotazione(String numeroBiglietto, String posto, StatoPrenotazione stato,
                                   Passeggero passeggero, String codiceVolo) {
        Prenotazione prenotazione = new Prenotazione(numeroBiglietto, posto, stato, passeggero);
        return dao.insertPrenotazione(prenotazione, codiceVolo);
    }

    public boolean aggiornaPrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto) {
        return dao.updatePrenotazione(prenotazione, numeroBiglietto);
    }

    // Metodi per la gestione dei bagagli
    public List<Bagaglio> getAllBagagli() {
        return dao.getAllBagagli();
    }

    public List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto) {
        return dao.getBagagliByPrenotazione(numeroBiglietto);
    }

    public Bagaglio getBagaglioByCodice(String codice) {
        return dao.getBagaglioByCodice(codice);
    }

    public boolean aggiungiBagaglio(String codice, StatoBagaglio stato, String numeroBiglietto) {
        Bagaglio bagaglio = new Bagaglio(codice, stato);
        return dao.insertBagaglio(bagaglio, numeroBiglietto);
    }

    public boolean aggiornaBagaglio(Bagaglio bagaglio) {
        return dao.updateBagaglio(bagaglio);
    }

    public List<Bagaglio> getBagagliSmarriti() {
        return dao.getBagagliSmarriti();
    }
}
