package controller;

import dao.PostgresDao;
import implementazionePostgresDao.ImplementazionePostgresDao;
import model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe Controller che gestisce la logica dell'applicazione aeroportuale.
 * Funge da intermediario tra l'interfaccia utente e il livello di accesso ai dati.
 * Gestisce tutte le operazioni relative a voli, prenotazioni, bagagli, gate e utenti.
 */
public class Controller {
    /**
     * Riferimento all'oggetto DAO che gestisce l'accesso al database.
     */
    private final PostgresDao dao;

    /**
     * Costruttore della classe Controller.
     * Inizializza il DAO per l'accesso al database.
     */
    public Controller() {
        this.dao = new ImplementazionePostgresDao();
    }

    /**
     * Effettua il login di un utente nel sistema.
     *
     * @param login    Nome utente o email dell'utente
     * @param password Password dell'utente
     * @param tipo     Tipo di utente ("Amministratore" o altro per utente generico)
     * @return L'oggetto Utente se le credenziali sono valide, altrimenti null
     */
    public Utente login(String login, String password, String tipo) {
        String tipoDb = "Amministratore".equals(tipo) ? "amministratore" : "generico";
        return dao.getUtenteByCredentialsAndType(login, password, tipoDb);
    }

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param utente  L'oggetto Utente da registrare
     * @param isAdmin Flag che indica se l'utente è un amministratore
     * @return true se la registrazione è avvenuta con successo, false altrimenti
     */
    public boolean registraUtente(Utente utente, boolean isAdmin) {
        String tipo = isAdmin ? "amministratore" : "generico";
        return dao.insertUtente(utente, tipo);
    }

    /**
     * Recupera tutti i voli presenti nel sistema.
     *
     * @return Lista di tutti i voli
     */
    public List<Volo> getAllVoli() {
        return dao.getAllVoli();
    }

    /**
     * Inserisce un nuovo volo nel sistema.
     *
     * @param codiceVolo     Codice identificativo del volo
     * @param compagnia      Nome della compagnia aerea
     * @param origine        Aeroporto di partenza
     * @param destinazione   Aeroporto di arrivo
     * @param orarioPrevisto Orario previsto di partenza
     * @param stato          Stato attuale del volo
     * @param data           Data del volo
     * @param tempoRitardo   Tempo di ritardo in minuti
     * @param totalSeats     Numero totale di posti disponibili
     * @param availableSeats Numero di posti ancora disponibili
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean inserisciVolo(String codiceVolo, String compagnia, String origine, String destinazione,
                                 String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo, int totalSeats, int availableSeats) {
        Volo volo = new Volo(codiceVolo, compagnia, origine, destinazione, orarioPrevisto, stato, data, tempoRitardo, totalSeats, availableSeats, 0);
        return dao.insertVolo(volo);
    }

    /**
     * Modifica i dati di un volo esistente.
     *
     * @param volo L'oggetto Volo con i dati aggiornati
     * @return true se la modifica è avvenuta con successo, false altrimenti
     */
    public boolean modificaVolo(Volo volo) {
        return dao.updateVolo(volo);
    }

    /**
     * Recupera tutti i gate dell'aeroporto.
     *
     * @return Lista di tutti i gate
     */
    public List<Gate> getAllGates() {
        return dao.getAllGates();
    }

    /**
     * Assegna un gate a un volo specifico.
     *
     * @param codiceGate  Codice identificativo del gate
     * @param codiceVolo  Codice identificativo del volo
     * @return true se l'assegnazione è avvenuta con successo, false altrimenti
     */
    public boolean assegnaGate(int codiceGate, String codiceVolo) {
        return dao.assignGateToFlight(codiceGate, codiceVolo);
    }


    /**
     * Recupera tutte le prenotazioni associate a un volo specifico.
     *
     * @param volo Il volo di cui si vogliono recuperare le prenotazioni
     * @return Lista delle prenotazioni associate al volo
     */
    public List<Prenotazione> getPrenotazioniByVolo(Volo volo) {
        return dao.getPrenotazioniByVolo(volo);
    }

    /**
     * Crea una nuova prenotazione nel sistema.
     *
     * @param prenotazione L'oggetto Prenotazione da inserire
     * @param codiceVolo   Codice identificativo del volo associato alla prenotazione
     * @param utente       L'utente che effettua la prenotazione
     * @return true se la creazione è avvenuta con successo, false altrimenti
     */
    public boolean creaPrenotazione(Prenotazione prenotazione, String codiceVolo, Utente utente) {
        return dao.insertPrenotazione(prenotazione, codiceVolo, utente);
    }

    /**
     * Aggiorna lo stato di una prenotazione esistente.
     *
     * @param prenotazione    Nuovo stato della prenotazione
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaPrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto) {
        return dao.updatePrenotazione(prenotazione, numeroBiglietto);
    }

    /**
     * Recupera tutti i bagagli registrati nel sistema.
     *
     * @return Lista di tutti i bagagli
     */
    public List<Bagaglio> getAllBagagli() {
        return dao.getAllBagagli();
    }

    /**
     * Recupera tutti i bagagli associati a una prenotazione specifica.
     *
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @return Lista dei bagagli associati alla prenotazione
     */
    public List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto) {
        return dao.getBagagliByPrenotazione(numeroBiglietto);
    }

    /**
     * Recupera un bagaglio specifico tramite il suo codice.
     *
     * @param codice Codice identificativo del bagaglio
     * @return L'oggetto Bagaglio corrispondente al codice, o null se non trovato
     */
    public Bagaglio getBagaglioByCodice(String codice) {
        return dao.getBagaglioByCodice(codice);
    }

    /**
     * Aggiorna i dati di un bagaglio esistente.
     *
     * @param bagaglio L'oggetto Bagaglio con i dati aggiornati
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaBagaglio(Bagaglio bagaglio) {
        return dao.updateBagaglio(bagaglio);
    }

    /**
     * Aggiorna lo stato di tutti i bagagli associati a un volo specifico.
     *
     * @param codiceVolo  Codice identificativo del volo
     * @param nuovoStato  Nuovo stato da assegnare ai bagagli
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaAllBagagli(String codiceVolo, StatoBagaglio nuovoStato) {
        return dao.updateBagagliByVolo(codiceVolo, nuovoStato);
    }

    /**
     * Recupera tutte le prenotazioni effettuate da un utente specifico.
     *
     * @param utente L'utente di cui si vogliono recuperare le prenotazioni
     * @return Lista delle prenotazioni effettuate dall'utente
     */
    public List<Prenotazione> getPrenotazioneByUtente(Utente utente) {
        return dao.getPrenotazioneByUtente(utente);
    }

    /**
     * Recupera tutti i posti disponibili per un volo specifico.
     *
     * @param coidceVolo Codice identificativo del volo
     * @return Lista dei posti disponibili per il volo
     */
    public List<Posto> getPostiByVolo(String coidceVolo) {
        return dao.getPostiByVolo(coidceVolo);
    }

    /**
     * Recupera tutti i bagagli associati a un utente specifico.
     *
     * @param user L'utente di cui si vogliono recuperare i bagagli
     * @return Lista dei bagagli associati all'utente
     */
    public List<Bagaglio> getBagagliByUtente(Utente user) {
        return dao.getBagagliByUtente(user);
    }

    /**
     * Recupera la prenotazione associata a un bagaglio specifico.
     *
     * @param codiceBagaglio Codice identificativo del bagaglio
     * @return La prenotazione associata al bagaglio, o null se non trovata
     */
    public Prenotazione getPrenotazioneByBagaglio(String codiceBagaglio) {
        return dao.getPrenotazioneByBagaglio(codiceBagaglio);
    }

    /**
     * Aggiorna le informazioni del passeggero in una prenotazione esistente.
     *
     * @param numeroBiglietto Numero del biglietto associato alla prenotazione
     * @param nome            Nuovo nome del passeggero
     * @param cognome         Nuovo cognome del passeggero
     * @param nDocumento      Nuovo numero di documento del passeggero
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean updatePassengerInfo(String numeroBiglietto, String nome, String cognome, String nDocumento) {
        return dao.updatePasseggeroInPrenotazione(numeroBiglietto, nome, cognome, nDocumento);
    }
}
