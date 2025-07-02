package ImplementazionePostgresDAO;

import DAO.PostgresDAO;
import Database.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementazionePostgresDAO implements PostgresDAO {
    // Metodi per LOGIN

    public Utente getUtenteByCredentialsAndType(String login, String password, String tipo) {
        String query = "SELECT * FROM Utente WHERE login = ? AND password = ? AND ruolo = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, tipo.toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");

                    return new Utente(login, password, nome, cognome, tipo.toLowerCase());
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dell'utente: " + e.getMessage());
        }
        return null;
    }
// PER SIGNUP TODO
    @Override
    public boolean insertUtente(Utente utente, String tipo) {
        String query = "INSERT INTO Utente (login, password, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getLogin());
            stmt.setString(2, utente.getPassword());
            stmt.setString(3, utente.getNome());
            stmt.setString(4, utente.getCognome());
            stmt.setString(5, tipo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
            return false;
        }
    }

    // Metodi per Volo
    @Override
    public List<Volo> getAllVoli() {
        List<Volo> voli = new ArrayList<>();
        String query = "SELECT * FROM Volo ORDER BY  data DESC";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String codiceVolo = rs.getString("codice");
                String compagnia = rs.getString("compagnia");
                String origine = rs.getString("origine");
                String destinazione = rs.getString("destinazione");
                String orarioPrevisto = rs.getString("orario");
                StatoVolo stato = StatoVolo.valueOf(rs.getString("stato"));
                LocalDate data = rs.getDate("data").toLocalDate();
                int tempoRitardo = rs.getInt("ritardo");
                int postiTotali = rs.getInt("posti_totali");
                int postiDisponibili = rs.getInt("posti_disponibili");
                int  gate = rs.getInt("gate");

                Volo volo = new Volo(codiceVolo, compagnia, origine, destinazione, orarioPrevisto, stato, data, tempoRitardo, postiTotali, postiDisponibili, gate);
                voli.add(volo);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei voli: " + e.getMessage());
        }
        return voli;
    }

    //TODO Non utilizzata
    @Override
    public Volo getVoloByCodice(String codiceVolo) {
        String query = "SELECT * FROM Volo WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codiceVolo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String compagnia = rs.getString("compagnia");
                    String origine = rs.getString("origine");
                    String destinazione = rs.getString("destinazione");
                    String orarioPrevisto = rs.getString("orario");
                    StatoVolo stato = StatoVolo.valueOf(rs.getString("stato"));
                    LocalDate data = rs.getDate("data").toLocalDate();
                    int tempoRitardo = rs.getInt("ritardo");
                    int postiTotali = rs.getInt("posti_totali");
                    int postiDisponibili = rs.getInt("posti_disponibili");
                    int  gate =rs.getInt("gate");
                    return new Volo(codiceVolo, compagnia, origine, destinazione, orarioPrevisto, stato, data, tempoRitardo, postiTotali, postiDisponibili, gate);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del volo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean insertVolo(Volo volo) {
        String sqlVolo =
                "INSERT INTO Volo " +
                        "(codice, compagnia, origine, destinazione, orario, stato, data, ritardo, posti_totali, posti_disponibili) " +
                        "VALUES (?, ?, ?, ?, ?::time, ?::statovolo, ?, ?, ?, ?)";
        String sqlPosto =
                "INSERT INTO posto (codice_volo, posto, occupato) VALUES (?, ?, false)";

        try (Connection conn = ConnessioneDatabase.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psVolo = conn.prepareStatement(sqlVolo);
                 PreparedStatement psPosto = conn.prepareStatement(sqlPosto)) {

                // 1) Inserisco il volo
                psVolo.setString(1, volo.getCodiceVolo());
                psVolo.setString(2, volo.getCompagnia());
                psVolo.setString(3, volo.getOrigine());
                psVolo.setString(4, volo.getDestinazione());
                psVolo.setString(5, volo.getOrarioPrevisto());
                psVolo.setString(6, volo.getStato().toString());
                psVolo.setDate(7, Date.valueOf(volo.getData()));
                psVolo.setInt(8, volo.getTempoRitardo());
                psVolo.setInt(9, volo.getPostiTotali());
                psVolo.setInt(10, volo.getPostiTotali());

                if (psVolo.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                // 2) Genero e inserisco uno alla volta i posti
                int totalSeats   = volo.getPostiTotali();
                final int perRow = 6;
                int fullRows     = totalSeats / perRow;
                int remainder    = totalSeats % perRow;

                // File complete
                for (int row = 1; row <= fullRows; row++) {
                    for (int s = 0; s < perRow; s++) {
                        String seatLabel = row + String.valueOf((char)('A' + s));
                        psPosto.setString(1, volo.getCodiceVolo());
                        psPosto.setString(2, seatLabel);
                        // Inserimento immediato, senza batch
                        if (psPosto.executeUpdate() == 0) {
                            throw new SQLException("Fallito insert posto " + seatLabel);
                        }
                    }
                }
                // Fila parziale
                if (remainder > 0) {
                    int row = fullRows + 1;
                    for (int s = 0; s < remainder; s++) {
                        String seatLabel = row + String.valueOf((char)('A' + s));
                        psPosto.setString(1, volo.getCodiceVolo());
                        psPosto.setString(2, seatLabel);
                        if (psPosto.executeUpdate() == 0) {
                            throw new SQLException("Fallito insert posto " + seatLabel);
                        }
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Errore inserimento volo/posti: " + ex.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Errore connessione/preparazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateVolo(Volo volo) {
        String query = "UPDATE Volo SET compagnia= ?, origine = ?, destinazione = ?, orario = ?::time, stato = ?::statovolo, data = ?, ritardo = ?, posti_totali = ?, posti_disponibili = ? WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, volo.getCompagnia());
            stmt.setString(2, volo.getOrigine());
            stmt.setString(3, volo.getDestinazione());
            stmt.setString(4, volo.getOrarioPrevisto());
            stmt.setString(5, volo.getStato().toString());
            stmt.setDate(6, Date.valueOf(volo.getData()));
            stmt.setInt(7, volo.getTempoRitardo());
            stmt.setInt(8, volo.getPostiTotali());
            stmt.setInt(9, volo.getPostiDisponibili());
            stmt.setString(10, volo.getCodiceVolo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del volo: " + e.getMessage());
            return false;
        }
    }

    // Metodi per Gate
    @Override
    public List<Gate> getAllGates() {
        List<Gate> gates = new ArrayList<>();
        String query = "SELECT * FROM gate";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int codice = rs.getInt("numero");
                Gate gate = new Gate(codice);
                gates.add(gate);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei gate: " + e.getMessage());
        }
        return gates;
    }

    @Override
    public boolean assignGateToFlight(int codiceGate, String codiceVolo) {
        String query = "UPDATE volo SET gate = ? WHERE codice =?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, codiceGate);
            stmt.setString(2, codiceVolo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'assegnazione del gate al volo: " + e.getMessage());
            return false;
        }
    }




    // Metodi per Passeggero
    @Override
    public Passeggero getPasseggeroByDocumento(String nDocumento) {
        String query = "SELECT * FROM passeggeri WHERE numero_documento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nDocumento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    return new Passeggero(nome, cognome, nDocumento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del passeggero: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean insertPasseggero(Passeggero passeggero) {
        String query = "INSERT INTO passeggeri (nome, cognome, numero_documento) VALUES (?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, passeggero.getNome());
            stmt.setString(2, passeggero.getCognome());
            stmt.setString(3, passeggero.getnDocumento());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del passeggero: " + e.getMessage());
            return false;
        }
    }

    // Metodi per Prenotazione
    @Override
    public List<Prenotazione> getAllPrenotazioni() {
        List<Prenotazione> prenotazioniList = new ArrayList<>();

        String query = "SELECT p.numero_biglietto, p., p.stato as stato_prenotazione, " +
                "pa.nome, pa.cognome, pa.numero_documento, " +
                "b.codice as codice_bagaglio, b.stato as stato_bagaglio " +
                "FROM prenotazioni p " +
                "JOIN passeggeri pa ON p.id_passeggero = pa.id_passeggero " +
                "LEFT JOIN prenotazioni_bagagli pb ON p.numero_biglietto = pb.numero_biglietto " +
                "LEFT JOIN bagagli b ON pb.codice_bagaglio = b.codice " +
                "ORDER BY p.numero_biglietto";

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String numeroBiglietto = rs.getString("numero_biglietto");

                // Cerco in lista una prenotazione già creata per questo biglietto
                Prenotazione prenotazione = null;
                for (Prenotazione p : prenotazioniList) {
                    if (p.getNumeroBiglietto().equals(numeroBiglietto)) {
                        prenotazione = p;
                        break;
                    }
                }

                // Se non esiste, la creo e la aggiungo
                if (prenotazione == null) {
                    String posto = rs.getString("posto");
                    StatoPrenotazione stato = StatoPrenotazione.valueOf(rs.getString("stato_prenotazione"));
                    String nDocumento = rs.getString("n_documento");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");

                    Passeggero passeggero = new Passeggero(nome, cognome, nDocumento);
                    prenotazione = new Prenotazione(numeroBiglietto, posto, stato, passeggero);
                    prenotazione.setBagagli(new ArrayList<>());

                    prenotazioniList.add(prenotazione);
                }

                // Aggiungo eventuale bagaglio
                String codiceBagaglio = rs.getString("codice_bagaglio");
                if (codiceBagaglio != null) {
                    String statoBagaglioStr = rs.getString("stato_bagaglio");
                    if (statoBagaglioStr != null) {
                        StatoBagaglio statoBagaglio = StatoBagaglio.valueOf(statoBagaglioStr);
                        Bagaglio bagaglio = new Bagaglio(codiceBagaglio, statoBagaglio);

                        prenotazione.getBagagli().add(bagaglio);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni: " + e.getMessage());
        }

        return prenotazioniList;
    }

    @Override
    public List<Prenotazione> getPrenotazioniByVolo(Volo volo) {
        List<Prenotazione> prenotazioniList = new ArrayList<>();

        String sql = "SELECT p.numero_biglietto, p.posto, p.stato AS stato_prenotazione, " +
                "pa.nome, pa.cognome, pa.numero_documento, " +
                "b.codice AS codice_bagaglio, b.stato AS stato_bagaglio " +
                "FROM prenotazione p " +
                "JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero " +
                "LEFT JOIN bagaglio b ON b.id_prenotazione = p.id_prenotazione " +
                "WHERE p.codice_volo = ? " +
                "ORDER BY p.numero_biglietto";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Imposto il parametro sul codice del volo
            ps.setString(1, volo.getCodiceVolo());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String numeroBiglietto = rs.getString("numero_biglietto");

                    // Cerco in lista una prenotazione già creata per questo biglietto
                    Prenotazione prenotazione = null;
                    for (Prenotazione p : prenotazioniList) {
                        if (p.getNumeroBiglietto().equals(numeroBiglietto)) {
                            prenotazione = p;
                            break;
                        }
                    }

                    // Se non esiste, la creo e la aggiungo
                    if (prenotazione == null) {
                        String posto = rs.getString("posto");
                        StatoPrenotazione statoPren =
                                StatoPrenotazione.valueOf(rs.getString("stato_prenotazione"));
                        String nDocumento = rs.getString("numero_documento");
                        String nome = rs.getString("nome");
                        String cognome = rs.getString("cognome");

                        Passeggero passeggero = new Passeggero(nome, cognome, nDocumento);
                        prenotazione = new Prenotazione(numeroBiglietto, posto, statoPren, passeggero);
                        prenotazione.setBagagli(new ArrayList<>());

                        prenotazioniList.add(prenotazione);
                    }

                    // Aggiungo eventuale bagaglio
                    String codiceBagaglio = rs.getString("codice_bagaglio");
                    if (codiceBagaglio != null) {
                        String statoBagaglioStr = rs.getString("stato_bagaglio");
                        if (statoBagaglioStr != null) {
                            StatoBagaglio statoBag =
                                    StatoBagaglio.valueOf(statoBagaglioStr);
                            Bagaglio bag = new Bagaglio(codiceBagaglio, statoBag);
                            prenotazione.getBagagli().add(bag);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore recupero prenotazioni per volo "
                    + volo.getCodiceVolo() + ": " + e.getMessage());
        }

        return prenotazioniList;
    }

    @Override
    public Prenotazione getPrenotazioneByNumeroBiglietto(String numeroBiglietto) {
        Prenotazione prenotazione = null;

        // Query per ottenere prenotazione, passeggero e bagagli in un'unica query
        String query = "SELECT p.numero_biglietto, p.posto, p.stato as stato_prenotazione, " +
                       "pa.nome, pa.cognome, pa.n_documento, " +
                       "b.codice as codice_bagaglio, b.stato as stato_bagaglio " +
                       "FROM prenotazioni p " +
                       "JOIN passeggeri pa ON p.passeggero_id = pa.n_documento " +
                       "LEFT JOIN prenotazioni_bagagli pb ON p.numero_biglietto = pb.numero_biglietto " +
                       "LEFT JOIN bagagli b ON pb.codice_bagaglio = b.codice " +
                       "WHERE p.numero_biglietto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, numeroBiglietto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Se è la prima riga, crea l'oggetto prenotazione
                    if (prenotazione == null) {
                        String posto = rs.getString("posto");
                        StatoPrenotazione stato = StatoPrenotazione.valueOf(rs.getString("stato_prenotazione"));
                        String nDocumento = rs.getString("n_documento");
                        String nome = rs.getString("nome");
                        String cognome = rs.getString("cognome");

                        Passeggero passeggero = new Passeggero(nome, cognome, nDocumento);
                        prenotazione = new Prenotazione(numeroBiglietto, posto, stato, passeggero);
                        prenotazione.setBagagli(new ArrayList<>());
                    }

                    // Aggiungi il bagaglio alla prenotazione se presente
                    String codiceBagaglio = rs.getString("codice_bagaglio");
                    if (codiceBagaglio != null) {
                        String statoBagaglioStr = rs.getString("stato_bagaglio");
                        if (statoBagaglioStr != null) {
                            StatoBagaglio statoBagaglio = StatoBagaglio.valueOf(statoBagaglioStr);
                            Bagaglio bagaglio = new Bagaglio(codiceBagaglio, statoBagaglio);

                            // Aggiungi il bagaglio alla lista dei bagagli della prenotazione
                            prenotazione.getBagagli().add(bagaglio);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della prenotazione: " + e.getMessage());
        }

        return prenotazione;
    }

    @Override
    public List<Prenotazione> getPrenotazioniByPasseggero(String nome, String cognome) {
        Map<String, Prenotazione> prenotazioniMap = new HashMap<>();

        // Query per ottenere prenotazioni, passeggeri e bagagli in un'unica query
        String query = "SELECT p.numero_biglietto, p.posto, p.stato as stato_prenotazione, " +
                       "pa.nome, pa.cognome, pa.n_documento, " +
                       "b.codice as codice_bagaglio, b.stato as stato_bagaglio " +
                       "FROM prenotazioni p " +
                       "JOIN passeggeri pa ON p.passeggero_id = pa.n_documento " +
                       "LEFT JOIN prenotazioni_bagagli pb ON p.numero_biglietto = pb.numero_biglietto " +
                       "LEFT JOIN bagagli b ON pb.codice_bagaglio = b.codice " +
                       "WHERE pa.nome = ? AND pa.cognome = ? " +
                       "ORDER BY p.numero_biglietto";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String numeroBiglietto = rs.getString("numero_biglietto");

                    // Se la prenotazione non è ancora nella mappa, creala
                    if (!prenotazioniMap.containsKey(numeroBiglietto)) {
                        String posto = rs.getString("posto");
                        StatoPrenotazione stato = StatoPrenotazione.valueOf(rs.getString("stato_prenotazione"));
                        String nDocumento = rs.getString("n_documento");

                        Passeggero passeggero = new Passeggero(nome, cognome, nDocumento);
                        Prenotazione prenotazione = new Prenotazione(numeroBiglietto, posto, stato, passeggero);
                        prenotazione.setBagagli(new ArrayList<>());

                        prenotazioniMap.put(numeroBiglietto, prenotazione);
                    }

                    // Aggiungi il bagaglio alla prenotazione se presente
                    String codiceBagaglio = rs.getString("codice_bagaglio");
                    if (codiceBagaglio != null) {
                        String statoBagaglioStr = rs.getString("stato_bagaglio");
                        if (statoBagaglioStr != null) {
                            StatoBagaglio statoBagaglio = StatoBagaglio.valueOf(statoBagaglioStr);
                            Bagaglio bagaglio = new Bagaglio(codiceBagaglio, statoBagaglio);

                            // Aggiungi il bagaglio alla lista dei bagagli della prenotazione
                            prenotazioniMap.get(numeroBiglietto).getBagagli().add(bagaglio);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni per passeggero: " + e.getMessage());
        }

        // Converti la mappa in una lista
        return new ArrayList<>(prenotazioniMap.values());
    }

    @Override
    public boolean insertPrenotazione(Prenotazione prenotazione, String codiceVolo) {
        Connection conn = null;
        boolean success = false;

        try {
            // Ottieni la connessione
            conn = ConnessioneDatabase.getConnection();

            // Disabilita l'autocommit per gestire la transazione manualmente
            conn.setAutoCommit(false);

            // Prima inserisci il passeggero se non esiste già
            Passeggero passeggero = prenotazione.getPasseggero();
            if (getPasseggeroByDocumento(passeggero.getnDocumento()) == null) {
                String passeggeroQuery = "INSERT INTO passeggeri (nome, cognome, n_documento) VALUES (?, ?, ?)";
                try (PreparedStatement passeggeroStmt = conn.prepareStatement(passeggeroQuery)) {
                    passeggeroStmt.setString(1, passeggero.getNome());
                    passeggeroStmt.setString(2, passeggero.getCognome());
                    passeggeroStmt.setString(3, passeggero.getnDocumento());
                    passeggeroStmt.executeUpdate();
                }
            }

            // Inserisci la prenotazione
            String prenotazioneQuery = "INSERT INTO prenotazioni (numero_biglietto, posto, stato, passeggero_id, codice) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement prenotazioneStmt = conn.prepareStatement(prenotazioneQuery)) {
                prenotazioneStmt.setString(1, prenotazione.getNumeroBiglietto());
                prenotazioneStmt.setString(2, prenotazione.getPosto());
                prenotazioneStmt.setString(3, prenotazione.getStato().toString());
                prenotazioneStmt.setString(4, passeggero.getnDocumento());
                prenotazioneStmt.setString(5, codiceVolo);
                prenotazioneStmt.executeUpdate();
            }

            // Se ci sono bagagli, inseriscili
            if (prenotazione.getBagagli() != null && !prenotazione.getBagagli().isEmpty()) {
                for (Bagaglio bagaglio : prenotazione.getBagagli()) {
                    // Inserisci il bagaglio
                    String bagaglioQuery = "INSERT INTO bagagli (codice, stato) VALUES (?, ?)";
                    try (PreparedStatement bagaglioStmt = conn.prepareStatement(bagaglioQuery)) {
                        bagaglioStmt.setString(1, bagaglio.getCodice());
                        bagaglioStmt.setString(2, bagaglio.getStato().toString());
                        bagaglioStmt.executeUpdate();
                    }

                    // Associa il bagaglio alla prenotazione
                    String associazioneQuery = "INSERT INTO prenotazioni_bagagli (numero_biglietto, codice_bagaglio) VALUES (?, ?)";
                    try (PreparedStatement associazioneStmt = conn.prepareStatement(associazioneQuery)) {
                        associazioneStmt.setString(1, prenotazione.getNumeroBiglietto());
                        associazioneStmt.setString(2, bagaglio.getCodice());
                        associazioneStmt.executeUpdate();
                    }
                }
            }

            // Commit della transazione
            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento della prenotazione: " + e.getMessage());
            // Rollback in caso di errore
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Errore durante il rollback: " + ex.getMessage());
                }
            }
        } finally {
            // Ripristina l'autocommit e chiudi la connessione
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
                }
            }
        }

        return success;
    }

    @Override
    public boolean updatePrenotazione(StatoPrenotazione prenotazione, String numeroBiglietto) {
        String query = "UPDATE prenotazione SET stato = ? WHERE numerobiglietto = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, String.valueOf(prenotazione));
            stmt.setString(2, numeroBiglietto);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della prenotazione: " + e.getMessage());
            return false;
        }
    }

    // Metodi per Bagaglio
    @Override
    public List<Bagaglio> getAllBagagli() {
        List<Bagaglio> bagagli = new ArrayList<>();
        String query = "SELECT * FROM bagaglio WHERE stato = 'smarrito'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String codice = rs.getString("codice");
                StatoBagaglio stato = StatoBagaglio.valueOf(rs.getString("stato"));
                Bagaglio bagaglio = new Bagaglio(codice, stato);
                bagagli.add(bagaglio);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei bagagli: " + e.getMessage());
        }
        return bagagli;
    }

    @Override
    public List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto) {
        List<Bagaglio> bagagli = new ArrayList<>();
        String query = "SELECT b.* FROM bagaglio b JOIN prenotazione pb ON b.id_prenotazione = pb.id_prenotazione WHERE pb.numero_biglietto = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, numeroBiglietto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String codice = rs.getString("codice");
                    StatoBagaglio stato = StatoBagaglio.valueOf(rs.getString("stato"));
                    Bagaglio bagaglio = new Bagaglio(codice, stato);
                    bagagli.add(bagaglio);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei bagagli per prenotazione: " + e.getMessage());
        }
        return bagagli;
    }

    @Override
    public Bagaglio getBagaglioByCodice(String codice) {
        String query = "SELECT * FROM bagaglio WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codice);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StatoBagaglio stato = StatoBagaglio.valueOf(rs.getString("stato"));
                    return new Bagaglio(codice, stato);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del bagaglio: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean insertBagaglio(Bagaglio bagaglio, String numeroBiglietto) {
        String queryBagaglio = "INSERT INTO bagagli (codice, stato) VALUES (?, ?)";
        String queryRelazione = "INSERT INTO prenotazioni_bagagli (numero_biglietto, codice_bagaglio) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConnessioneDatabase.getConnection();
            conn.setAutoCommit(false);

            // Inserisci il bagaglio
            try (PreparedStatement stmtBagaglio = conn.prepareStatement(queryBagaglio)) {
                stmtBagaglio.setString(1, bagaglio.getCodice());
                stmtBagaglio.setString(2, bagaglio.getStato().toString());
                stmtBagaglio.executeUpdate();
            }

            // Inserisci la relazione con la prenotazione
            try (PreparedStatement stmtRelazione = conn.prepareStatement(queryRelazione)) {
                stmtRelazione.setString(1, numeroBiglietto);
                stmtRelazione.setString(2, bagaglio.getCodice());
                stmtRelazione.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del bagaglio: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Errore durante il rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean updateBagaglio(Bagaglio bagaglio) {
        String query = "UPDATE bagaglio SET stato = ? WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bagaglio.getStato().toString());
            stmt.setString(2, bagaglio.getCodice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del bagaglio: " + e.getMessage());
            return false;
        }
    }


    public boolean updateBagagliByVolo(String codiceVolo, StatoBagaglio nuovoStato) {
        String sql =
                "UPDATE bagaglio b " +
                        "SET stato = ? " +
                        "FROM prenotazione p " +
                        "WHERE p.id = b.id_prenotazione " +
                        "  AND p.codice_volo = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1 = nuovo stato, 2 = codice del volo
            ps.setString(1, nuovoStato.toString());
            ps.setString(2, codiceVolo);

            // esegue e ritorna quante righe sono state aggiornate
            return ps.executeUpdate() >0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento dei bagagli per volo "
                    + codiceVolo + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public List<Bagaglio> getBagagliSmarriti() {
        List<Bagaglio> bagagliSmarriti = new ArrayList<>();
        String query = "SELECT * FROM bagaglio WHERE stato = 'smarrito'";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String codice = rs.getString("codice");
                StatoBagaglio stato = StatoBagaglio.valueOf(rs.getString("stato"));
                Bagaglio bagaglio = new Bagaglio(codice, stato);
                bagagliSmarriti.add(bagaglio);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei bagagli smarriti: " + e.getMessage());
        }
        return bagagliSmarriti;
    }
}
