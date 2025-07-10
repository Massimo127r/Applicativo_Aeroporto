package model;

/**
 * Classe che rappresenta un bagaglio nel sistema aeroportuale.
 * Ogni bagaglio ha un codice identificativo e uno stato che ne indica la posizione
 * e la condizione attuale nel processo di gestione bagagli.
 */
public class Bagaglio {
    /**
     * Codice identificativo univoco del bagaglio.
     */
    private String codice;

    /**
     * Stato attuale del bagaglio (es. REGISTRATO, IMBARCATO, RITIRATO, ecc.).
     */
    private StatoBagaglio stato;

    /**
     * Costruttore della classe Bagaglio.
     * Inizializza un nuovo oggetto Bagaglio con i parametri specificati.
     *
     * @param codice Codice identificativo del bagaglio
     * @param stato  Stato iniziale del bagaglio
     */
    public Bagaglio(String codice, StatoBagaglio stato) {
        this.codice = codice;
        this.stato = stato;
    }

    /**
     * Restituisce il codice identificativo del bagaglio.
     *
     * @return Il codice del bagaglio
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il codice identificativo del bagaglio.
     *
     * @param codice Il nuovo codice del bagaglio
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * Restituisce lo stato attuale del bagaglio.
     *
     * @return Lo stato del bagaglio
     */
    public StatoBagaglio getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del bagaglio.
     *
     * @param stato Il nuovo stato del bagaglio
     */
    public void setStato(StatoBagaglio stato) {
        this.stato = stato;
    }
}
