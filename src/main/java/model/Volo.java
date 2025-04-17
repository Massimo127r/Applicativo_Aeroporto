package model;

import java.time.LocalDate;


public class Volo {
    private String codiceVolo;
    private String compagnia;
    private String origine;
    private String destinazione;
    private String orarioPrevisto;
    private StatoVolo stato;
    private LocalDate data;
    private int tempoRitardo;


    public Volo(String codiceVolo, String compagnia, String origine, String destinazione,
                String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo) {
        this.codiceVolo = codiceVolo;
        this.compagnia = compagnia;
        this.origine = origine;
        this.destinazione = destinazione;
        this.orarioPrevisto = orarioPrevisto;
        this.stato = stato;
        this.data = data;
        this.tempoRitardo = tempoRitardo;
    }

    public String getCodiceVolo() { return codiceVolo; }
    public void setCodiceVolo(String codiceVolo) { this.codiceVolo = codiceVolo; }

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

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getTempoRitardo() { return tempoRitardo; }
    public void setTempoRitardo(int tempoRitardo) { this.tempoRitardo = tempoRitardo; }
}
