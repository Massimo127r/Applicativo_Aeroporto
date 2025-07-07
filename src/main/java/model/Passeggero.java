package model;

// Passeggero.java
public class Passeggero {
    private int id;
    private String nome;
    private String cognome;
    private String nDocumento;

    public Passeggero(int id, String nome, String cognome, String nDocumento) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.nDocumento = nDocumento;
    }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
