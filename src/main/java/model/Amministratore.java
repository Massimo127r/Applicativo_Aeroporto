package model;

// Amministratore.java
public class Amministratore extends Utente {
    public Amministratore(String nomeUtente, String password, String nome, String cognome) {
        super(nomeUtente, password, nome, cognome);
    }

    // Logica dei metodi da implementare
    public void inserisciVolo() {}
    public void modificaVolo() {}
    public void assegnaGate() {}
    public void aggiornaBagaglio() {}
    public void visualizzaSmarriti() {}
}
