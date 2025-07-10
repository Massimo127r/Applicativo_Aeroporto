package model;

/**
 * Classe che rappresenta un passeggero nel sistema aeroportuale.
 * Ogni passeggero ha un identificativo, dati personali (nome e cognome)
 * e un numero di documento per l'identificazione.
 */
public class Passeggero {
    /**
     * Identificativo numerico univoco del passeggero nel sistema.
     */
    private int id;

    /**
     * Nome del passeggero.
     */
    private String nome;

    /**
     * Cognome del passeggero.
     */
    private String cognome;

    /**
     * Numero del documento di identità del passeggero.
     */
    private String nDocumento;

    /**
     * Costruttore della classe Passeggero.
     * Inizializza un nuovo oggetto Passeggero con i parametri specificati.
     *
     * @param nome       Nome del passeggero
     * @param cognome    Cognome del passeggero
     * @param nDocumento Numero del documento di identità
     */
    public Passeggero(String nome, String cognome, String nDocumento) {
        this.nome = nome;
        this.cognome = cognome;
        this.nDocumento = nDocumento;
    }

    /**
     * Restituisce il nome del passeggero.
     *
     * @return Il nome del passeggero
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del passeggero.
     *
     * @param nome Il nuovo nome del passeggero
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome del passeggero.
     *
     * @return Il cognome del passeggero
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome del passeggero.
     *
     * @param cognome Il nuovo cognome del passeggero
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il numero del documento di identità del passeggero.
     *
     * @return Il numero del documento
     */
    public String getnDocumento() {
        return nDocumento;
    }

    /**
     * Imposta il numero del documento di identità del passeggero.
     *
     * @param nDocumento Il nuovo numero del documento
     */
    public void setnDocumento(String nDocumento) {
        this.nDocumento = nDocumento;
    }

    /**
     * Restituisce l'identificativo numerico del passeggero.
     *
     * @return L'ID del passeggero
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo numerico del passeggero.
     *
     * @param id Il nuovo ID del passeggero
     */
    public void setId(int id) {
        this.id = id;
    }
}
