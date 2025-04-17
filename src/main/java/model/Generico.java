package model;

// Generico.java
public class Generico extends Utente {
    public Generico(String nomeUtente, String password, String nome, String cognome) {
        super(nomeUtente, password, nome, cognome);
    }

    public void prenota() {}
    public void cercaPrenotazione(String numeroVolo) {}
    public void cercaPrenotazionePerPasseggero(String nomePasseggero) {}
    public void segnalaSmarrimento() {}
}
