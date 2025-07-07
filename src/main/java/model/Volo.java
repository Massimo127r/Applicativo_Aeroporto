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
    private int postiTotali;
    private int postiDisponibili;
    private static final String NAPOLI = "Napoli";

    private int gate;

    public Volo(String codiceVolo, String compagnia, String origine, String destinazione,
                String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo,
                int postiTotali, int postiDisponibili, int gate) {
        if (!origine.equalsIgnoreCase(NAPOLI) && !destinazione.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.codiceVolo = codiceVolo;
        this.compagnia = compagnia;
        this.origine = origine;
        this.destinazione = destinazione;
        this.orarioPrevisto = orarioPrevisto;
        this.stato = stato;
        this.data = data;
        setTempoRitardo(tempoRitardo);
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
        this.gate = gate;
    }


    public String getCodiceVolo() {
        return codiceVolo;
    }

    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    public String getCompagnia() {
        return compagnia;
    }

    public void setCompagnia(String compagnia) {
        this.compagnia = compagnia;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        if (this.origine != null && this.origine.equalsIgnoreCase(NAPOLI) && !origine.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("L'origine non può essere cambiata da Napoli ad un'altra città");
        }

        if (this.destinazione != null && !this.destinazione.equalsIgnoreCase(NAPOLI) && !origine.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.origine = origine;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        if (this.destinazione != null && this.destinazione.equalsIgnoreCase(NAPOLI) && !destinazione.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("La destinazione non può essere cambiata da Napoli ad un'altra città");
        }

        if (this.origine != null && !this.origine.equalsIgnoreCase(NAPOLI) && !destinazione.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.destinazione = destinazione;
    }

    public String getOrarioPrevisto() {
        return orarioPrevisto;
    }

    public void setOrarioPrevisto(String orarioPrevisto) {
        this.orarioPrevisto = orarioPrevisto;
    }

    public StatoVolo getStato() {
        return stato;
    }

    public void setStato(StatoVolo stato) {
        this.stato = stato;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getTempoRitardo() {
        return tempoRitardo;
    }

    public void setTempoRitardo(int tempoRitardo) {
        this.tempoRitardo = Math.max(0, tempoRitardo);
    }

    public int getPostiTotali() {
        return postiTotali;
    }

    public void setPostiTotali(int postiTotali) {
        this.postiTotali = postiTotali;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public int getGate() {
        return gate;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }
}
