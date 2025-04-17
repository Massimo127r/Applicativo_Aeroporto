package model;

import java.util.Date;

public class Volo {
    private String numeroVolo;
    private String compagnia;
    private String origine;
    private String destinazione;
    private String orarioPrevisto;
    private StatoVolo stato;
    private Date data;
    private int ritardo;

    public Volo(String numeroVolo, String compagnia, String origine, String destinazione,
                String orarioPrevisto, StatoVolo stato, Date data, int ritardo) {
        this.numeroVolo = numeroVolo;
        this.compagnia = compagnia;
        this.origine = origine;
        this.destinazione = destinazione;
        this.orarioPrevisto = orarioPrevisto;
        this.stato = stato;
        this.data = data;
        this.ritardo = ritardo;
    }

    public String getNumeroVolo() { return numeroVolo; }
    public void setNumeroVolo(String numeroVolo) { this.numeroVolo = numeroVolo; }

    public String getCompagnia() { return compagnia; }
    public void setCompagnia(String compagnia) { this.compagnia = compagnia; }

    public String getOrigine() { return origine; }
    public void setOrigine(String origine) { this.origine = origine; }

    public String getDestinazione() { return destinazione; }
    public void setDestinazione(String destinazione) { this.destinazione = destinazione; }

    public String getOrarioPrevisto() { return orarioPrevisto; }
    public void setOrarioPrevisto(String orarioPrevisto) { this.orarioPrevisto = orarioPrevisto; }

    public StatoVolo getStato() { return stato; }
    public void setStato(StatoVolo stato) { this.stato = stato; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public int getRitardo() { return ritardo; }
    public void setRitardo(int ritardo) { this.ritardo = ritardo; }
}
