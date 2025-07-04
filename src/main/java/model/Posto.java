package model;

import java.util.Objects;

/**
 * Rappresenta un posto a sedere associato a un volo.
 * La chiave primaria è composta da codiceVolo e seatNumber.
 */
public class Posto {
    private String codiceVolo;
    private String seatNumber;
    private boolean occupato;


    public Posto() {
    }


    public Posto(String codiceVolo, String seatNumber, boolean occupato) {
        this.codiceVolo = codiceVolo;
        this.seatNumber = seatNumber;
        this.occupato = occupato;
    }

    public String getCodiceVolo() {
        return codiceVolo;
    }

    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isOccupato() {
        return occupato;
    }

    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }






}
