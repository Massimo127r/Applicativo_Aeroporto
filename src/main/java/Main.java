import model.Amministratore;
import model.Generico;
import model.Volo;
import model.StatoVolo;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        // TEST MAIN | Istanziamento degli oggetti

        // Creazione oggetto Amministratore
        Amministratore u = new Amministratore("Amministratore 1", "123ABC", "Gennaro",
                "Esposito");

        // Creazione oggetto Generico
        Generico g = new Generico("Pippo125", "123BC", "Pippo","Rossi");

        // Creazione oggetto Volo
        Volo v = new Volo("0403A", "Luftansa","Napoli","Dubai",
                "12:20",StatoVolo.programmato, LocalDate.of(2004,04,10),
                14);


        System.out.printf("\n Nome e Cognome amministratore: %s %s ", u.getNome(), u.getCognome());
        System.out.printf("\n Nome utente e Password generico: %s %s", g.getNomeUtente(), g.getPassword());
        System.out.printf("\n Compagnia e Data volo:  %s %s", v.getCompagnia(), v.getData());
    }
}