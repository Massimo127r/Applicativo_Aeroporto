package model;

/**
 * Enumerazione che rappresenta i possibili stati di un bagaglio nel sistema aeroportuale.
 * Gli stati seguono il ciclo di vita di un bagaglio, dalla registrazione al ritiro,
 * includendo anche stati eccezionali come lo smarrimento.
 */
public enum StatoBagaglio {
    /**
     * Il bagaglio è in fase di elaborazione o registrazione.
     */
    inElaborazione, 

    /**
     * Il bagaglio è stato caricato sull'aereo.
     */
    caricato, 

    /**
     * Il bagaglio è arrivato a destinazione ed è disponibile per il ritiro.
     */
    ritirabile, 

    /**
     * Il bagaglio è stato smarrito durante il trasporto.
     */
    smarrito;
}
