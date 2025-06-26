package model;

/**
 * @deprecated Questa classe è stata deprecata. 
 * La funzionalità è stata spostata nella classe Utente con un approccio basato su ruoli.
 * Utilizzare la classe Utente con il ruolo "amministratore" invece.
 */
@Deprecated
public class Amministratore extends Utente {
    /**
     * @deprecated Utilizzare il costruttore Utente con il parametro ruolo="amministratore" invece.
     */
    @Deprecated
    public Amministratore(String login, String password, String nome, String cognome) {
        super(login, password, nome, cognome, "amministratore");
    }
}
