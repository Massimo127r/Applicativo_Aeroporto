package model;

// Passeggero.java
public class Passeggero {
    private String nome;
    private String cognome;
    private String nDocumento;

    public Passeggero(String nome, String cognome, String nDocumento) {
        this.nome = nome;
        this.cognome = cognome;
        this.nDocumento = nDocumento;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getnDocumento() { return nDocumento; }
    public void setnDocumento(String nDocumento) { this.nDocumento = nDocumento; }
}
