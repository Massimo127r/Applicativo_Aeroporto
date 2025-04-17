// Pacchetto principale
package model;

// Utente.java
public class Utente {
    protected String nomeUtente;
    protected String password;
    protected String nome;
    protected String cognome;

    public Utente(String nomeUtente, String password, String nome, String cognome) {
        this.nomeUtente = nomeUtente;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNomeUtente() { return nomeUtente; }
    public void setNomeUtente(String nomeUtente) { this.nomeUtente = nomeUtente; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public void visualizzaVoli() {
        // Logica per visualizzare voli da implementare
    }
}
