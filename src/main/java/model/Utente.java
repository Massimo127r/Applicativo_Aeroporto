package model;

public class Utente {
    protected String login;
    protected String password;
    protected String nome;
    protected String cognome;
    protected String ruolo;

    public Utente(String login, String password, String nome, String cognome, String ruolo) {
        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNomeUtente() {
        return login;
    }

    public void setNomeUtente(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public boolean isAmministratore() {
        return "amministratore".equalsIgnoreCase(ruolo);
    }

    public boolean isGenerico() {
        return "generico".equalsIgnoreCase(ruolo);
    }


}
