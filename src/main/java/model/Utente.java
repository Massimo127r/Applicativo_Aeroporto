package model;

/**
 * Classe che rappresenta un utente del sistema aeroportuale.
 * Ogni utente ha credenziali di accesso (login e password), dati personali (nome e cognome)
 * e un ruolo che ne determina i permessi all'interno del sistema.
 */
public class Utente {
    /**
     * Nome utente o email utilizzato per l'accesso al sistema.
     */
    private String login;

    /**
     * Password utilizzata per l'autenticazione dell'utente.
     */
    private String password;

    /**
     * Nome dell'utente.
     */
    private String nome;

    /**
     * Cognome dell'utente.
     */
    private String cognome;

    /**
     * Ruolo dell'utente nel sistema (es. "amministratore", "generico").
     */
    private String ruolo;

    /**
     * Costruttore della classe Utente.
     * Inizializza un nuovo oggetto Utente con i parametri specificati.
     *
     * @param login    Nome utente o email per l'accesso
     * @param password Password per l'autenticazione
     * @param nome     Nome dell'utente
     * @param cognome  Cognome dell'utente
     * @param ruolo    Ruolo dell'utente nel sistema
     */
    public Utente(String login, String password, String nome, String cognome, String ruolo) {
        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il login dell'utente.
     *
     * @return Il login dell'utente
     */
    public String getLogin() {
        return login;
    }

    /**
     * Imposta il login dell'utente.
     *
     * @param login Il nuovo login dell'utente
     */
    public void setLogin(String login) {
        this.login = login;
    }


    /**
     * Restituisce la password dell'utente.
     *
     * @return La password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password La nuova password dell'utente
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome Il nuovo nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome Il nuovo cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il ruolo dell'utente nel sistema.
     *
     * @return Il ruolo dell'utente
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo dell'utente nel sistema.
     *
     * @param ruolo Il nuovo ruolo dell'utente
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Verifica se l'utente ha il ruolo di amministratore.
     *
     * @return true se l'utente è un amministratore, false altrimenti
     */
    public boolean isAmministratore() {
        return "amministratore".equalsIgnoreCase(ruolo);
    }

    /**
     * Verifica se l'utente ha il ruolo generico.
     *
     * @return true se l'utente è un utente generico, false altrimenti
     */
    public boolean isGenerico() {
        return "generico".equalsIgnoreCase(ruolo);
    }


}
