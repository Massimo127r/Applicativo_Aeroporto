package dao;

import model.*;

import java.util.List;

public interface PostgresDao {

    Utente getUtenteByCredentialsAndType(String login, String password, String tipo);

    boolean insertUtente(Utente utente, String tipo);

    List<Volo> getAllVoli();

    boolean insertVolo(Volo volo);

    boolean updateVolo(Volo volo);

    List<Gate> getAllGates();

    boolean assignGateToFlight(int codiceGate, String codiceVolo);

    boolean updatePasseggeroInPrenotazione(String numeroBiglietto, String nome, String cognome, String nDocumento);

    List<Prenotazione> getPrenotazioniByVolo(Volo volo);

    boolean insertPrenotazione(Prenotazione prenotazione, String codiceVolo, Utente utente);

    boolean updatePrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto);

    List<Bagaglio> getAllBagagli();

    List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto);

    Bagaglio getBagaglioByCodice(String codice);

    boolean updateBagaglio(Bagaglio bagaglio);

    boolean updateBagagliByVolo(String codiceVolo, StatoBagaglio nuovoStato);

    List<Bagaglio> getBagagliByUtente(Utente user);

    List<Prenotazione> getPrenotazioneByUtente(Utente utente);

    List<Posto> getPostiByVolo(String coidceVolo);

    Prenotazione getPrenotazioneByBagaglio(String codcieBagaglio);


}
