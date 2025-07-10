package model;

import java.time.LocalDate;

/**
 * Classe che rappresenta un volo aereo nel sistema aeroportuale.
 * Ogni volo ha un codice identificativo, una compagnia aerea, un'origine, una destinazione,
 * un orario previsto, uno stato, una data, un tempo di ritardo, informazioni sui posti disponibili
 * e un gate assegnato.
 * 
 * Nota: Per policy dell'aeroporto, almeno uno tra origine e destinazione deve essere "Napoli".
 */
public class Volo {
    /**
     * Codice identificativo univoco del volo.
     */
    private String codiceVolo;

    /**
     * Nome della compagnia aerea che opera il volo.
     */
    private String compagnia;

    /**
     * Aeroporto di partenza del volo.
     */
    private String origine;

    /**
     * Aeroporto di arrivo del volo.
     */
    private String destinazione;

    /**
     * Orario previsto di partenza del volo in formato stringa.
     */
    private String orarioPrevisto;

    /**
     * Stato attuale del volo (es. IN_ORARIO, IN_RITARDO, CANCELLATO, ecc.).
     */
    private StatoVolo stato;

    /**
     * Data di partenza del volo.
     */
    private LocalDate data;

    /**
     * Tempo di ritardo del volo in minuti.
     */
    private int tempoRitardo;

    /**
     * Numero totale di posti disponibili sul volo.
     */
    private int postiTotali;

    /**
     * Numero di posti ancora disponibili per la prenotazione.
     */
    private int postiDisponibili;

    /**
     * Costante che rappresenta l'aeroporto di Napoli.
     */
    private static final String NAPOLI = "Napoli";

    /**
     * Numero del gate assegnato al volo.
     */
    private int gate;

    /**
     * Costruttore della classe Volo.
     * Inizializza un nuovo oggetto Volo con i parametri specificati.
     * Verifica che almeno uno tra origine e destinazione sia "Napoli".
     *
     * @param codiceVolo      Codice identificativo del volo
     * @param compagnia       Nome della compagnia aerea
     * @param origine         Aeroporto di partenza
     * @param destinazione    Aeroporto di arrivo
     * @param orarioPrevisto  Orario previsto di partenza
     * @param stato           Stato attuale del volo
     * @param data            Data di partenza
     * @param tempoRitardo    Tempo di ritardo in minuti
     * @param postiTotali     Numero totale di posti disponibili
     * @param postiDisponibili Numero di posti ancora disponibili
     * @param gate            Numero del gate assegnato
     * @throws IllegalArgumentException Se né l'origine né la destinazione sono "Napoli"
     */
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


    /**
     * Restituisce il codice identificativo del volo.
     *
     * @return Il codice del volo
     */
    public String getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Imposta il codice identificativo del volo.
     *
     * @param codiceVolo Il nuovo codice del volo
     */
    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    /**
     * Restituisce il nome della compagnia aerea che opera il volo.
     *
     * @return Il nome della compagnia aerea
     */
    public String getCompagnia() {
        return compagnia;
    }

    /**
     * Imposta il nome della compagnia aerea che opera il volo.
     *
     * @param compagnia Il nuovo nome della compagnia aerea
     */
    public void setCompagnia(String compagnia) {
        this.compagnia = compagnia;
    }

    /**
     * Restituisce l'aeroporto di partenza del volo.
     *
     * @return L'aeroporto di partenza
     */
    public String getOrigine() {
        return origine;
    }

    /**
     * Imposta l'aeroporto di partenza del volo.
     * Verifica che se l'origine attuale è "Napoli", non possa essere cambiata in un'altra città.
     * Inoltre, verifica che almeno uno tra origine e destinazione sia "Napoli".
     *
     * @param origine Il nuovo aeroporto di partenza
     * @throws IllegalArgumentException Se si tenta di cambiare l'origine da "Napoli" a un'altra città,
     *                                  o se né l'origine né la destinazione sarebbero "Napoli" dopo la modifica
     */
    public void setOrigine(String origine) {
        if (this.origine != null && this.origine.equalsIgnoreCase(NAPOLI) && !origine.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("L'origine non può essere cambiata da Napoli ad un'altra città");
        }

        if (this.destinazione != null && !this.destinazione.equalsIgnoreCase(NAPOLI) && !origine.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.origine = origine;
    }

    /**
     * Restituisce l'aeroporto di arrivo del volo.
     *
     * @return L'aeroporto di arrivo
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Imposta l'aeroporto di arrivo del volo.
     * Verifica che se la destinazione attuale è "Napoli", non possa essere cambiata in un'altra città.
     * Inoltre, verifica che almeno uno tra origine e destinazione sia "Napoli".
     *
     * @param destinazione Il nuovo aeroporto di arrivo
     * @throws IllegalArgumentException Se si tenta di cambiare la destinazione da "Napoli" a un'altra città,
     *                                  o se né l'origine né la destinazione sarebbero "Napoli" dopo la modifica
     */
    public void setDestinazione(String destinazione) {
        if (this.destinazione != null && this.destinazione.equalsIgnoreCase(NAPOLI) && !destinazione.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("La destinazione non può essere cambiata da Napoli ad un'altra città");
        }

        if (this.origine != null && !this.origine.equalsIgnoreCase(NAPOLI) && !destinazione.equalsIgnoreCase(NAPOLI)) {
            throw new IllegalArgumentException("Almeno uno tra origine e destinazione deve essere Napoli");
        }

        this.destinazione = destinazione;
    }

    /**
     * Restituisce l'orario previsto di partenza del volo.
     *
     * @return L'orario previsto di partenza
     */
    public String getOrarioPrevisto() {
        return orarioPrevisto;
    }

    /**
     * Imposta l'orario previsto di partenza del volo.
     *
     * @param orarioPrevisto Il nuovo orario previsto di partenza
     */
    public void setOrarioPrevisto(String orarioPrevisto) {
        this.orarioPrevisto = orarioPrevisto;
    }

    /**
     * Restituisce lo stato attuale del volo.
     *
     * @return Lo stato attuale del volo
     */
    public StatoVolo getStato() {
        return stato;
    }

    /**
     * Imposta lo stato attuale del volo.
     *
     * @param stato Il nuovo stato del volo
     */
    public void setStato(StatoVolo stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la data di partenza del volo.
     *
     * @return La data di partenza
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Imposta la data di partenza del volo.
     *
     * @param data La nuova data di partenza
     */
    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * Restituisce il tempo di ritardo del volo in minuti.
     *
     * @return Il tempo di ritardo in minuti
     */
    public int getTempoRitardo() {
        return tempoRitardo;
    }

    /**
     * Imposta il tempo di ritardo del volo in minuti.
     * Se il valore fornito è negativo, viene impostato a 0.
     *
     * @param tempoRitardo Il nuovo tempo di ritardo in minuti
     */
    public void setTempoRitardo(int tempoRitardo) {
        this.tempoRitardo = Math.max(0, tempoRitardo);
    }

    /**
     * Restituisce il numero totale di posti disponibili sul volo.
     *
     * @return Il numero totale di posti
     */
    public int getPostiTotali() {
        return postiTotali;
    }

    /**
     * Imposta il numero totale di posti disponibili sul volo.
     *
     * @param postiTotali Il nuovo numero totale di posti
     */
    public void setPostiTotali(int postiTotali) {
        this.postiTotali = postiTotali;
    }

    /**
     * Restituisce il numero di posti ancora disponibili per la prenotazione.
     *
     * @return Il numero di posti disponibili
     */
    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    /**
     * Imposta il numero di posti ancora disponibili per la prenotazione.
     *
     * @param postiDisponibili Il nuovo numero di posti disponibili
     */
    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    /**
     * Restituisce il numero del gate assegnato al volo.
     *
     * @return Il numero del gate
     */
    public int getGate() {
        return gate;
    }

    /**
     * Imposta il numero del gate assegnato al volo.
     *
     * @param gate Il nuovo numero del gate
     */
    public void setGate(int gate) {
        this.gate = gate;
    }
}
