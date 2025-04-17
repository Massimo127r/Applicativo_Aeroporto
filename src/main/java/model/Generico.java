package model;

// Generico.java
public class Generico extends Utente {
    public Generico(String nomeUtente, String password, String nome, String cognome) {
        super(nomeUtente, password, nome, cognome);
    }

    // Logica dei metodi da implementare
    public void prenota() {}
    public void cercaPrenotazione(String codiceVolo) {}
    public void cercaPrenotazione(String nomePasseggero, String cognomePasseggero) {}
    public void segnalaSmarrimento() {}
}
