package DAO;

import model.*;

import java.util.List;

public interface PostgresDAO {
    // Metodi per Utente
    Utente getUtenteByCredentialsAndType(String login, String password, String tipo);
    boolean insertUtente(Utente utente, String tipo);

    // Metodi per Volo
    List<Volo> getAllVoli();
    Volo getVoloByCodice(String codiceVolo);
    boolean insertVolo(Volo volo);
    boolean updateVolo(Volo volo);

    // Metodi per Gate
    List<Gate> getAllGates();
    boolean assignGateToFlight(int codiceGate, String codiceVolo);

    // Metodi per Passeggero
    Passeggero getPasseggeroByDocumento(String nDocumento);
    boolean insertPasseggero(Passeggero passeggero);

    // Metodi per Prenotazione
    List<Prenotazione> getAllPrenotazioni();
    Prenotazione getPrenotazioneByNumeroBiglietto(String numeroBiglietto);
    List<Prenotazione> getPrenotazioniByPasseggero(String nome, String cognome);
     List<Prenotazione> getPrenotazioniByVolo(Volo volo);
    boolean insertPrenotazione(Prenotazione prenotazione, String codiceVolo);
    boolean updatePrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto);

    // Metodi per Bagaglio
    List<Bagaglio> getAllBagagli();
    List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto);
    Bagaglio getBagaglioByCodice(String codice);
    boolean insertBagaglio(Bagaglio bagaglio, String numeroBiglietto);
    boolean updateBagaglio(Bagaglio bagaglio);
    boolean updateBagagliByVolo(String codiceVolo, StatoBagaglio nuovoStato) ;

        List<Bagaglio> getBagagliSmarriti();
}
