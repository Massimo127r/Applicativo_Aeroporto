// Pacchetto principale
package model;

// Utente.java
public class Utente {
    protected String login;
    protected String password;
    protected String nome;
    protected String cognome;
    protected String ruolo; // Ruolo dell'utente (amministratore o generico)

    public Utente(String login, String password, String nome, String cognome, String ruolo) {
        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    // Mantengo i metodi vecchi per retrocompatibilità
    public String getNomeUtente() { return login; }
    public void setNomeUtente(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public boolean isAmministratore() {
        return "amministratore".equalsIgnoreCase(ruolo);
    }

    public boolean isGenerico() {
        return "generico".equalsIgnoreCase(ruolo);
    }

    public void visualizzaVoli() {
        // Logica per visualizzare voli da implementare
    }

    // Metodi specifici per amministratore
    public void inserisciVolo() {
        if (!isAmministratore()) {
            throw new UnsupportedOperationException("Solo gli amministratori possono inserire voli");
        }
        // Implementazione del metodo
    }

    public void modificaVolo() {
        if (!isAmministratore()) {
            throw new UnsupportedOperationException("Solo gli amministratori possono modificare voli");
        }
        // Implementazione del metodo
    }

    public void assegnaGate() {
        if (!isAmministratore()) {
            throw new UnsupportedOperationException("Solo gli amministratori possono assegnare gate");
        }
        // Implementazione del metodo
    }

    public void aggiornaBagaglio() {
        if (!isAmministratore()) {
            throw new UnsupportedOperationException("Solo gli amministratori possono aggiornare bagagli");
        }
        // Implementazione del metodo
    }

    public void visualizzaSmarriti() {
        if (!isAmministratore()) {
            throw new UnsupportedOperationException("Solo gli amministratori possono visualizzare bagagli smarriti");
        }
        // Implementazione del metodo
    }

    // Metodi specifici per utente generico
    public void prenota() {
        if (!isGenerico()) {
            throw new UnsupportedOperationException("Solo gli utenti generici possono effettuare prenotazioni");
        }
        // Implementazione del metodo
    }

    public void cercaPrenotazione(String codiceVolo) {
        if (!isGenerico()) {
            throw new UnsupportedOperationException("Solo gli utenti generici possono cercare prenotazioni");
        }
        // Implementazione del metodo
    }

    public void cercaPrenotazione(String nomePasseggero, String cognomePasseggero) {
        if (!isGenerico()) {
            throw new UnsupportedOperationException("Solo gli utenti generici possono cercare prenotazioni");
        }
        // Implementazione del metodo
    }

    public void segnalaSmarrimento() {
        if (!isGenerico()) {
            throw new UnsupportedOperationException("Solo gli utenti generici possono segnalare smarrimenti");
        }
        // Implementazione del metodo
    }
}
