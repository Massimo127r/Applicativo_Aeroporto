package model;

/**
 * Enumerazione che rappresenta i possibili stati di un volo nel sistema aeroportuale.
 * Gli stati seguono il ciclo di vita di un volo, dalla programmazione all'atterraggio,
 * includendo anche stati eccezionali come il ritardo o la cancellazione.
 */
public enum StatoVolo {
    /**
     * Il volo è programmato e dovrebbe partire secondo l'orario previsto.
     */
    programmato, 

    /**
     * Il volo è in ritardo rispetto all'orario previsto.
     */
    inRitardo, 

    /**
     * Il volo è decollato ed è attualmente in volo.
     */
    decollato, 

    /**
     * Il volo è atterrato a destinazione.
     */
    atterrato, 

    /**
     * Il volo è stato cancellato e non partirà.
     */
    cancellato;
}
