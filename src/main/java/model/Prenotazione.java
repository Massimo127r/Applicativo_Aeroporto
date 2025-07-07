package model;

import java.util.List;

/**
 * Classe che rappresenta una prenotazione di volo nel sistema aeroportuale.
 * Ogni prenotazione è associata a un volo, un passeggero, un posto specifico
 * e può includere uno o più bagagli.
 */
public class Prenotazione {
    /**
     * Numero identificativo univoco del biglietto.
     */
    private String numeroBiglietto;

    /**
     * Codice identificativo del volo associato alla prenotazione.
     */
    private String codiceVolo;

    /**
     * Identificativo del posto assegnato al passeggero.
     */
    private String posto;

    /**
     * Stato attuale della prenotazione (es. CONFERMATA, CANCELLATA, ecc.).
     */
    private StatoPrenotazione stato;

    /**
     * Informazioni sul passeggero associato alla prenotazione.
     */
    private Passeggero passeggero;

    /**
     * Lista dei bagagli associati alla prenotazione.
     */
    private List<Bagaglio> bagagli;

    /**
     * Costruttore della classe Prenotazione.
     * Inizializza una nuova prenotazione senza specificare il codice del volo.
     *
     * @param numeroBiglietto Numero identificativo del biglietto
     * @param posto           Identificativo del posto assegnato
     * @param stato           Stato della prenotazione
     * @param passeggero      Informazioni sul passeggero
     */
    public Prenotazione(String numeroBiglietto, String posto, StatoPrenotazione stato, Passeggero passeggero) {
        this.numeroBiglietto = numeroBiglietto;
        this.posto = posto;
        this.stato = stato;
        this.passeggero = passeggero;
    }

    /**
     * Costruttore della classe Prenotazione.
     * Inizializza una nuova prenotazione specificando anche il codice del volo.
     *
     * @param codiceVolo      Codice identificativo del volo
     * @param numeroBiglietto Numero identificativo del biglietto
     * @param posto           Identificativo del posto assegnato
     * @param stato           Stato della prenotazione
     * @param passeggero      Informazioni sul passeggero
     */
    public Prenotazione(String codiceVolo, String numeroBiglietto, String posto, StatoPrenotazione stato, Passeggero passeggero) {
        this.codiceVolo = codiceVolo;
        this.numeroBiglietto = numeroBiglietto;
        this.posto = posto;
        this.stato = stato;
        this.passeggero = passeggero;
    }

    /**
     * Restituisce il numero del biglietto associato alla prenotazione.
     *
     * @return Il numero del biglietto
     */
    public String getNumeroBiglietto() {
        return numeroBiglietto;
    }

    /**
     * Imposta il numero del biglietto associato alla prenotazione.
     *
     * @param numeroBiglietto Il nuovo numero del biglietto
     */
    public void setNumeroBiglietto(String numeroBiglietto) {
        this.numeroBiglietto = numeroBiglietto;
    }

    /**
     * Restituisce l'identificativo del posto assegnato al passeggero.
     *
     * @return L'identificativo del posto
     */
    public String getPosto() {
        return posto;
    }

    /**
     * Imposta l'identificativo del posto assegnato al passeggero.
     *
     * @param posto Il nuovo identificativo del posto
     */
    public void setPosto(String posto) {
        this.posto = posto;
    }

    /**
     * Restituisce lo stato attuale della prenotazione.
     *
     * @return Lo stato della prenotazione
     */
    public StatoPrenotazione getStato() {
        return stato;
    }

    /**
     * Imposta lo stato della prenotazione.
     *
     * @param stato Il nuovo stato della prenotazione
     */
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    /**
     * Restituisce le informazioni sul passeggero associato alla prenotazione.
     *
     * @return L'oggetto Passeggero con le informazioni sul passeggero
     */
    public Passeggero getPasseggero() {
        return passeggero;
    }

    /**
     * Imposta le informazioni sul passeggero associato alla prenotazione.
     *
     * @param passeggero Il nuovo oggetto Passeggero
     */
    public void setPasseggero(Passeggero passeggero) {
        this.passeggero = passeggero;
    }

    /**
     * Restituisce la lista dei bagagli associati alla prenotazione.
     *
     * @return La lista dei bagagli
     */
    public List<Bagaglio> getBagagli() {
        return bagagli;
    }

    /**
     * Imposta la lista dei bagagli associati alla prenotazione.
     *
     * @param bagagli La nuova lista dei bagagli
     */
    public void setBagagli(List<Bagaglio> bagagli) {
        this.bagagli = bagagli;
    }

    /**
     * Restituisce il codice del volo associato alla prenotazione.
     *
     * @return Il codice del volo
     */
    public String getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Imposta il codice del volo associato alla prenotazione.
     *
     * @param codiceVolo Il nuovo codice del volo
     */
    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }
}
