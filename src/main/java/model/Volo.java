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
private int gate;

    public Volo(String codiceVolo, String compagnia, String origine, String destinazione,
                String orarioPrevisto, StatoVolo stato, LocalDate data, int tempoRitardo,
                int postiTotali, int postiDisponibili, int gate) {
        // Validazione: almeno uno tra origine e destinazione deve essere Napoli
        if (!origine.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.codiceVolo = codiceVolo;
        this.compagnia = compagnia;
        this.origine = origine;
        this.destinazione = destinazione;
        this.orarioPrevisto = orarioPrevisto;
        this.stato = stato;
        this.data = data;
        setTempoRitardo(tempoRitardo); // Using setter to ensure validation
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
        this.gate = gate;
    }



    public String getCodiceVolo() { return codiceVolo; }
    public void setCodiceVolo(String codiceVolo) { this.codiceVolo = codiceVolo; }

    public String getCompagnia() { return compagnia; }
    public void setCompagnia(String compagnia) { this.compagnia = compagnia; }

    public String getOrigine() { return origine; }
    public void setOrigine(String origine) { 
        // Se l'origine era Napoli, non può essere cambiata
        if (this.origine != null && this.origine.equalsIgnoreCase("Napoli") && !origine.equalsIgnoreCase("Napoli")) {
            throw new IllegalArgumentException("L'origine non può essere cambiata da Napoli ad un'altra città");
        }

        // Se la destinazione non è Napoli, l'origine deve essere Napoli
        if (this.destinazione != null && !this.destinazione.equalsIgnoreCase("Napoli") && !origine.equalsIgnoreCase("Napoli")) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.origine = origine; 
    }

    public String getDestinazione() { return destinazione; }
    public void setDestinazione(String destinazione) { 
        // Se la destinazione era Napoli, non può essere cambiata
        if (this.destinazione != null && this.destinazione.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
            throw new IllegalArgumentException("La destinazione non può essere cambiata da Napoli ad un'altra città");
        }

        // Se l'origine non è Napoli, la destinazione deve essere Napoli
        if (this.origine != null && !this.origine.equalsIgnoreCase("Napoli") && !destinazione.equalsIgnoreCase("Napoli")) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.destinazione = destinazione; 
    }

    public String getOrarioPrevisto() { return orarioPrevisto; }
    public void setOrarioPrevisto(String orarioPrevisto) { this.orarioPrevisto = orarioPrevisto; }

    public StatoVolo getStato() { return stato; }
    public void setStato(StatoVolo stato) { this.stato = stato; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getTempoRitardo() { return tempoRitardo; }
    public void setTempoRitardo(int tempoRitardo) { 
        // Il ritardo di un volo non può essere negativo ma deve essere ≥ 0
        this.tempoRitardo = tempoRitardo < 0 ? 0 : tempoRitardo; 
    }

    public int getPostiTotali() { return postiTotali; }
    public void setPostiTotali(int postiTotali) { this.postiTotali = postiTotali; }

    public int getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(int postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public int getGate() { return gate; }
    public void setGate(int gate) { this.gate = gate; }
}
