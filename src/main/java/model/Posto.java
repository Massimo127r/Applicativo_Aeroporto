package model;

/**
 * Classe che rappresenta un posto a sedere su un aereo nel sistema aeroportuale.
 * Ogni posto è associato a un volo specifico, ha un numero identificativo
 * e può essere occupato o libero.
 */
public class Posto {
    /**
     * Codice identificativo del volo a cui appartiene il posto.
     */
    private String codiceVolo;

    /**
     * Numero identificativo del posto (es. "A1", "B12", ecc.).
     */
    private String seatNumber;

    /**
     * Flag che indica se il posto è occupato (true) o libero (false).
     */
    private boolean occupato;

    /**
     * Costruttore vuoto della classe Posto.
     * Crea un nuovo oggetto Posto senza inizializzare i campi.
     */
    public Posto() {
    }

    /**
     * Costruttore della classe Posto.
     * Inizializza un nuovo oggetto Posto con i parametri specificati.
     *
     * @param codiceVolo Codice identificativo del volo
     * @param seatNumber Numero identificativo del posto
     * @param occupato   Flag che indica se il posto è occupato
     */
    public Posto(String codiceVolo, String seatNumber, boolean occupato) {
        this.codiceVolo = codiceVolo;
        this.seatNumber = seatNumber;
        this.occupato = occupato;
    }

    /**
     * Restituisce il codice del volo a cui appartiene il posto.
     *
     * @return Il codice del volo
     */
    public String getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Imposta il codice del volo a cui appartiene il posto.
     *
     * @param codiceVolo Il nuovo codice del volo
     */
    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    /**
     * Restituisce il numero identificativo del posto.
     *
     * @return Il numero del posto
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Imposta il numero identificativo del posto.
     *
     * @param seatNumber Il nuovo numero del posto
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Verifica se il posto è occupato.
     *
     * @return true se il posto è occupato, false se è libero
     */
    public boolean isOccupato() {
        return occupato;
    }

    /**
     * Imposta lo stato di occupazione del posto.
     *
     * @param occupato true per segnare il posto come occupato, false per segnarlo come libero
     */
    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }
}
