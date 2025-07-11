# Aeroporto Napoli - Applicazione Desktop

## Descrizione del Progetto
L’applicazione desktop, sviluppata in Java con Swing, fornisce una gestione centralizzata ed efficiente di tutte le operazioni aeroportuali dell’aeroporto di Napoli. Grazie a un database relazionale PostgreSQL, l’app consente il monitoraggio in tempo reale di voli, prenotazioni, bagagli.

## Funzionalità Principali
- **Autenticazione**  
  Accesso tramite login e password; due ruoli utente (generico e amministratore) con privilegi differenziati.

- **Utenti Generici**  
  - Consultazione voli programmati  
  - Prenotazione biglietti (inclusi numero del biglietto, posto e stato)  
  - Ricerca e modifica prenotazioni per passeggero o codice volo  

- **Amministratori**  
  - Tutte le operazioni degli utenti generici  
  - Inserimento e aggiornamento voli (compagnia, origini/destinazioni, orario, stato, gate)  
  - Gestione delle prenotazioni e dei bagagli

- **Gestione Bagagli**  
  - Creazione e tracciamento bagagli associati a prenotazioni  
  - Aggiornamento stato (in Elaborazione, caricato, disponibile, smarrito)  
  - Segnalazione e gestione dei bagagli smarriti  

- **Homepage Operativa**  
  Panoramica in tempo reale di voli in arrivo/partenza, con evidenza di ritardi e cancellazioni.

- **Ricerca Avanzata**  
  Filtri rapidi su voli, prenotazioni, passeggeri e bagagli per scenari ad alto traffico.

## Requisiti
- Java 14 (o superiore)  
- PostgreSQL  
- pgAdmin (per importare il backup)  
- Maven (o altro tool di build compatibile)

## Installazione e Avvio
1. **Clona il repository**  
   ```bash
   git clone https://github.com/Massimo127r/Applicativo_Aeroporto.git
   cd Applicativo_Aeroporto
   ```

2. **Importa il database**  
   - Apri pgAdmin.  
   - Crea un nuovo database (es. `aeroporto_na`).  
   - Esegui lo script di backup presente in `database/database.sql` per ripristinare tutte le tabelle e i dati.

3. **Configura la connessione**  
   Apri il file:
   ```
   src/main/java/database/ConnessioneDatabase.java
   ```
   e sostituisci la stringa di connessione con la tua password:
   ```java
   private String url = "jdbc:postgresql://localhost:5432/aeroporto_na";
   private String user = "postgres";
   private String password = "<LA_TUA_PASSWORD>";
   ```

4. **Compila e avvia**  
   Con Maven:
   ```bash
   mvn clean package
   java -jar target/aeroporto-app.jar
   ```

## Contribuire
Per bug, suggerimenti o contributi, apri un *issue* o invia una *pull request* su GitHub.
