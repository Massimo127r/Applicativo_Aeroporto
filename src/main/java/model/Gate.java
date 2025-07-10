package model;

/**
 * Classe che rappresenta un gate dell'aeroporto.
 * Ogni gate è identificato da un codice numerico univoco.
 */
public class Gate {
    /**
     * Codice identificativo numerico del gate.
     */
    private int codice;

    /**
     * Costruttore della classe Gate.
     * Inizializza un nuovo oggetto Gate con il codice specificato.
     *
     * @param codice Codice identificativo del gate
     */
    public Gate(int codice) {
        this.codice = codice;
    }

    /**
     * Restituisce il codice identificativo del gate.
     *
     * @return Il codice del gate
     */
    public int getCodice() {
        return codice;
    }

    /**
     * Imposta il codice identificativo del gate.
     *
     * @param codice Il nuovo codice del gate
     */
    public void setCodice(int codice) {
        this.codice = codice;
    }
}
