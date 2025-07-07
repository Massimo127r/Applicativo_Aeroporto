package model;

public class Bagaglio {
    private String codice;
    private StatoBagaglio stato;

    public Bagaglio(String codice, StatoBagaglio stato) {
        this.codice = codice;
        this.stato = stato;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public StatoBagaglio getStato() {
        return stato;
    }

    public void setStato(StatoBagaglio stato) {
        this.stato = stato;
    }
}
