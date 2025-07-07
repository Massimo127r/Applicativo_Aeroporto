package dao;

import model.*;

import java.util.List;

/**
 * Interfaccia che definisce i metodi di accesso ai dati per il sistema aeroportuale.
 * Fornisce metodi per la gestione di utenti, voli, gate, prenotazioni, bagagli e posti.
 */
public interface PostgresDao {

    /**
     * Recupera un utente in base alle credenziali e al tipo.
     *
     * @param login    Nome utente o email dell'utente
     * @param password Password dell'utente
     * @param tipo     Tipo di utente (es. "amministratore", "generico")
     * @return L'oggetto Utente se le credenziali sono valide, altrimenti null
     */
    Utente getUtenteByCredentialsAndType(String login, String password, String tipo);

    /**
     * Inserisce un nuovo utente nel sistema.
     *
     * @param utente L'oggetto Utente da inserire
     * @param tipo   Tipo di utente (es. "amministratore", "generico")
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    boolean insertUtente(Utente utente, String tipo);

    /**
     * Recupera tutti i voli presenti nel sistema.
     *
     * @return Lista di tutti i voli
     */
    List<Volo> getAllVoli();

    /**
     * Inserisce un nuovo volo nel sistema.
     *
     * @param volo L'oggetto Volo da inserire
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    boolean insertVolo(Volo volo);

    /**
     * Aggiorna i dati di un volo esistente.
     *
     * @param volo L'oggetto Volo con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updateVolo(Volo volo);

    /**
     * Recupera tutti i gate dell'aeroporto.
     *
     * @return Lista di tutti i gate
     */
    List<Gate> getAllGates();

    /**
     * Assegna un gate a un volo specifico.
     *
     * @param codiceGate  Codice identificativo del gate
     * @param codiceVolo  Codice identificativo del volo
     * @return true se l'assegnazione è avvenuta con successo, false altrimenti
     */
    boolean assignGateToFlight(int codiceGate, String codiceVolo);

    /**
     * Aggiorna le informazioni del passeggero in una prenotazione esistente.
     *
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @param nome            Nuovo nome del passeggero
     * @param cognome         Nuovo cognome del passeggero
     * @param nDocumento      Nuovo numero di documento del passeggero
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updatePasseggeroInPrenotazione(String numeroBiglietto, String nome, String cognome, String nDocumento);

    /**
     * Recupera tutte le prenotazioni associate a un volo specifico.
     *
     * @param volo Il volo di cui si vogliono recuperare le prenotazioni
     * @return Lista delle prenotazioni associate al volo
     */
    List<Prenotazione> getPrenotazioniByVolo(Volo volo);

    /**
     * Inserisce una nuova prenotazione nel sistema.
     *
     * @param prenotazione L'oggetto Prenotazione da inserire
     * @param codiceVolo   Codice identificativo del volo associato alla prenotazione
     * @param utente       L'utente che effettua la prenotazione
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    boolean insertPrenotazione(Prenotazione prenotazione, String codiceVolo, Utente utente);

    /**
     * Aggiorna lo stato di una prenotazione esistente.
     *
     * @param prenotazione    Nuovo stato della prenotazione
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updatePrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto);

    /**
     * Recupera tutti i bagagli registrati nel sistema.
     *
     * @return Lista di tutti i bagagli
     */
    List<Bagaglio> getAllBagagli();

    /**
     * Recupera tutti i bagagli associati a una prenotazione specifica.
     *
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @return Lista dei bagagli associati alla prenotazione
     */
    List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto);

    /**
     * Recupera un bagaglio specifico tramite il suo codice.
     *
     * @param codice Codice identificativo del bagaglio
     * @return L'oggetto Bagaglio corrispondente al codice, o null se non trovato
     */
    Bagaglio getBagaglioByCodice(String codice);

    /**
     * Aggiorna i dati di un bagaglio esistente.
     *
     * @param bagaglio L'oggetto Bagaglio con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updateBagaglio(Bagaglio bagaglio);

    /**
     * Aggiorna lo stato di tutti i bagagli associati a un volo specifico.
     *
     * @param codiceVolo  Codice identificativo del volo
     * @param nuovoStato  Nuovo stato da assegnare ai bagagli
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    boolean updateBagagliByVolo(String codiceVolo, StatoBagaglio nuovoStato);

    /**
     * Recupera tutti i bagagli associati a un utente specifico.
     *
     * @param user L'utente di cui si vogliono recuperare i bagagli
     * @return Lista dei bagagli associati all'utente
     */
    List<Bagaglio> getBagagliByUtente(Utente user);

    /**
     * Recupera tutte le prenotazioni effettuate da un utente specifico.
     *
     * @param utente L'utente di cui si vogliono recuperare le prenotazioni
     * @return Lista delle prenotazioni effettuate dall'utente
     */
    List<Prenotazione> getPrenotazioneByUtente(Utente utente);

    /**
     * Recupera tutti i posti disponibili per un volo specifico.
     *
     * @param coidceVolo Codice identificativo del volo
     * @return Lista dei posti disponibili per il volo
     */
    List<Posto> getPostiByVolo(String coidceVolo);

    /**
     * Recupera la prenotazione associata a un bagaglio specifico.
     *
     * @param codcieBagaglio Codice identificativo del bagaglio
     * @return La prenotazione associata al bagaglio, o null se non trovata
     */
    Prenotazione getPrenotazioneByBagaglio(String codcieBagaglio);
}
