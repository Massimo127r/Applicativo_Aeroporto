# Naples Airport Management System — Applicazione Desktop Java
![Type](https://img.shields.io/badge/type-University%20Project-orange)

Progetto di gruppo sviluppato per il corso di **Object-Oriented Programming**  
Corso di Laurea in Informatica — Università degli Studi di Napoli Federico II (A.A. 2024/2025)

Il repository contiene un’applicazione desktop sviluppata in Java (Swing) e supportata da
un database relazionale PostgreSQL per la gestione delle principali operazioni aeroportuali:
voli, prenotazioni, gestione dei bagagli e controllo degli accessi.

> Nota: la struttura della repository segue il template ufficiale richiesto dall’assegnazione accademica.

## Panoramica del Progetto

L’applicazione supporta un sistema di accesso basato su ruoli:

- **Utenti generici**:
  - consultazione dei voli programmati
  - creazione e modifica delle prenotazioni
  - consultazione dello stato dei bagagli
- **Amministratori**:
  - tutte le funzionalità degli utenti generici
  - inserimento e aggiornamento dei dettagli dei voli
  - gestione delle prenotazioni e dei bagagli

Funzionalità aggiuntive includono:
- panoramica operativa dei voli in tempo reale con evidenza di ritardi e cancellazioni
- funzionalità di ricerca e filtraggio su voli, prenotazioni, passeggeri e bagagli

## Funzionalità Principali

### Autenticazione e Ruoli
- Login tramite credenziali
- Due ruoli distinti: utente generico e amministratore

### Gestione dei Voli
- Consultazione dei voli
- Creazione e aggiornamento delle informazioni di volo
- Assegnazione dei gate e aggiornamento dello stato

### Sistema di Prenotazioni
- Creazione dei biglietti con assegnazione del posto
- Ricerca e modifica delle prenotazioni

### Gestione dei Bagagli
- Tracciamento dello stato del bagaglio (in elaborazione, caricato, disponibile, smarrito)
- Segnalazione e gestione dei bagagli smarriti

### Vista Operativa
- Dashboard con arrivi e partenze
- Evidenziazione di ritardi e cancellazioni

## Tecnologie Utilizzate

- Java (Swing) — interfaccia desktop
- PostgreSQL — database
- JDBC — connettività al database
- Maven — build e gestione delle dipendenze
- UML — diagrammi e documentazione tecnica
  
## Livello Database

Lo schema del database e la logica PL/pgSQL utilizzati dall’applicazione sono stati
progettati nell’ambito di un progetto separato per il corso di Basi di Dati.

Repository del progetto DB:
https://github.com/CarmineSgariglia/BDD_Project

## Documentazione

Il repository include documentazione completa redatta in lingua italiana:
- diagrammi UML (concettuali e logici, class diagram, sequence diagram)
- manuale di utilizzo dell’interfaccia grafica
- note sulla gestione degli errori

I file sono disponibili nella cartella:
```bash
documentazione/
```

## Contributo al Progetto

Il progetto è stato sviluppato in modo collaborativo dal team, con responsabilità condivise
su tutte le componenti del sistema.

Le principali aree di responsabilità hanno incluso:
- logica di interazione con il database e query SQL
- gestione della connessione al database tramite JDBC
- progettazione e implementazione dei componenti dell’interfaccia grafica

Tutti i membri del gruppo hanno contribuito allo sviluppo complessivo dell’applicazione.

## Autori

- Carmine Sgariglia  
- Mattia Lemma  
- Massimo Russo
