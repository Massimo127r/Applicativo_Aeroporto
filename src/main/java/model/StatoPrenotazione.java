package model;

/**
 * Enumerazione che rappresenta i possibili stati di una prenotazione nel sistema aeroportuale.
 * Gli stati riflettono il ciclo di vita di una prenotazione, dalla creazione alla conferma
 * o eventuale cancellazione.
 */
public enum StatoPrenotazione {
    /**
     * La prenotazione è stata confermata e il posto è riservato per il passeggero.
     */
    confermata, 

    /**
     * La prenotazione è in attesa di conferma o di elaborazione.
     */
    inAttesa, 

    /**
     * La prenotazione è stata cancellata e il posto è nuovamente disponibile.
     */
    cancellato;
}
