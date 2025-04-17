package model;

import java.util.List;

public class Prenotazione {
    private String numeroBiglietto;
    private String posto;
    private StatoPrenotazione stato;
    private Passeggero passeggero;
    private List<Bagaglio> bagagli;

    public Prenotazione(String numeroBiglietto, String posto, StatoPrenotazione stato, Passeggero passeggero) {
        this.numeroBiglietto = numeroBiglietto;
        this.posto = posto;
        this.stato = stato;
        this.passeggero = passeggero;
    }

    public String getNumeroBiglietto() { return numeroBiglietto; }
    public void setNumeroBiglietto(String numeroBiglietto) { this.numeroBiglietto = numeroBiglietto; }

    public String getPosto() { return posto; }
    public void setPosto(String posto) { this.posto = posto; }

    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }

    public Passeggero getPasseggero() { return passeggero; }
    public void setPasseggero(Passeggero passeggero) { this.passeggero = passeggero; }

    public List<Bagaglio> getBagagli() { return bagagli; }
    public void setBagagli(List<Bagaglio> bagagli) { this.bagagli = bagagli; }

    public void checkin() {
        // Logica check-in da implementare
    }
}
