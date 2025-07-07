package implementazionePostgresDao;

import dao.PostgresDao;
import database.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia PostgresDao che fornisce l'accesso ai dati
 * del sistema aeroportuale attraverso un database PostgreSQL.
 * 
 * Questa classe gestisce tutte le operazioni di lettura e scrittura sul database,
 * inclusa la gestione di utenti, voli, prenotazioni, bagagli, gate e posti.
 * Utilizza la classe ConnessioneDatabase per stabilire connessioni al database.
 */
public class ImplementazionePostgresDao implements PostgresDao {

    /**
     * {@inheritDoc}
     * 
     * Implementazione che recupera un utente dal database in base alle credenziali fornite.
     * Esegue una query SQL per verificare l'esistenza dell'utente con le credenziali
     * e il tipo specificati.
     * 
     * @throws RuntimeException Se si verifica un errore di caricamento del driver del database
     */
    public Utente getUtenteByCredentialsAndType(String login, String password, String tipo) {
        String query = "SELECT * FROM Utente WHERE username = ? AND password = ? AND ruolo = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Override
    public boolean insertUtente(Utente utente, String tipo) {
        String query = "INSERT INTO Utente (username, password, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Volo> getAllVoli() {
        List<Volo> voli = new ArrayList<>();
        String query = "SELECT * FROM Volo ORDER BY  data DESC";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return voli;
    }


    /**
     * {@inheritDoc}
     * 
     * Implementazione che inserisce un nuovo volo nel database e crea automaticamente
     * i posti associati al volo. L'operazione viene eseguita come una transazione atomica:
     * se una parte fallisce, tutte le modifiche vengono annullate.
     * 
     * Il metodo esegue i seguenti passaggi:
     * 1. Inserisce i dati del volo nella tabella Volo
     * 2. Crea i posti per il volo nella tabella posto, organizzati in file e lettere
     *    (es. 1A, 1B, 1C, 1D, 1E, 1F, 2A, ecc.)
     * 
     * @throws RuntimeException Se si verifica un errore di caricamento del driver del database
     */
    @Override
    public boolean insertVolo(Volo volo) {
        String sqlVolo =
                "INSERT INTO Volo " +
                        "(codice, compagnia, origine, destinazione, orario, stato, data, ritardo, posti_totali, posti_disponibili) " +
                        "VALUES (?, ?, ?, ?, ?::time, ?::statovolo, ?, ?, ?, ?)";
        String sqlPosto =
                "INSERT INTO posto (codice_volo, posto, occupato) VALUES (?, ?, false)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateVolo(Volo volo) {
        String query = "UPDATE Volo SET compagnia= ?, origine = ?, destinazione = ?, orario = ?::time, stato = ?::statovolo, data = ?, ritardo = ?, posti_totali = ?, posti_disponibili = ? WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodi per Gate
    @Override
    public List<Gate> getAllGates() {
        List<Gate> gates = new ArrayList<>();
        String query = "SELECT * FROM gate";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int codice = rs.getInt("numero");
                Gate gate = new Gate(codice);
                gates.add(gate);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei gate: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return gates;
    }

    @Override
    public boolean assignGateToFlight(int codiceGate, String codiceVolo) {
        String query = "UPDATE volo SET gate = ? WHERE codice =?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, codiceGate);
            stmt.setString(2, codiceVolo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'assegnazione del gate al volo: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Prenotazione> getPrenotazioneByUtente(Utente utente) {
        List<Prenotazione> prenotazioniList = new ArrayList<>();

        String sql = ""
                + "SELECT "
                + "  p.id_prenotazione AS id_prenotazione, "
                + "  p.numero_biglietto, "
                + "  p.posto, "
                +"p.codice_volo,"
                + "  p.stato AS stato_prenotazione, "
                + "  pa.id_passeggero AS id_passeggero, "
                + "  pa.nome, "
                + "  pa.cognome, "
                + "  pa.numero_documento, "
                + "  b.codice AS codice_bagaglio, "
                + "  b.stato AS stato_bagaglio "
                + "FROM prenotazione p "
                + "  JOIN passeggero pa ON p.id_passeggero = pa.id_passeggero "
                + "  LEFT JOIN bagaglio b ON b.id_prenotazione = p.id_prenotazione "
                + "WHERE p.username = ? "
                + "ORDER BY p.codice_volo";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utente.getLogin());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String numeroBiglietto = rs.getString("numero_biglietto");
                    Prenotazione prenotazione = null;
                    // cerco se l'ho già creato
                    for (Prenotazione p : prenotazioniList) {
                        if (p.getNumeroBiglietto().equals(numeroBiglietto)) {
                            prenotazione = p;
                            break;
                        }
                    }
                    if (prenotazione == null) {
                        // creo la nuova prenotazione
                        String posto = rs.getString("posto");
                        StatoPrenotazione stato =
                                StatoPrenotazione.valueOf(rs.getString("stato_prenotazione"));
                        String documento = rs.getString("numero_documento");
                        String nome = rs.getString("nome");
                        String cognome = rs.getString("cognome");

                        String codiceVolo= rs.getString("codice_volo");
                        Passeggero passeggero = new Passeggero(nome, cognome, documento);
                        prenotazione = new Prenotazione(
                                codiceVolo,
                                numeroBiglietto,
                                posto,
                                stato,
                                passeggero
                        );
                        prenotazione.setBagagli(new ArrayList<>());
                        prenotazioniList.add(prenotazione);
                    }

                    // aggiungo il bagaglio se esiste
                    String codiceBag = rs.getString("codice_bagaglio");
                    if (codiceBag != null) {
                        String statoBagStr = rs.getString("stato_bagaglio");
                        if (statoBagStr != null) {
                            StatoBagaglio statoBag = StatoBagaglio.valueOf(statoBagStr);
                            Bagaglio b = new Bagaglio(codiceBag, statoBag);
                            prenotazione.getBagagli().add(b);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle prenotazioni: "
                    + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return prenotazioniList;
    }

    @Override
    public boolean insertPrenotazione(Prenotazione prenotazione, String codiceVolo, Utente utente) {
        boolean success = false;

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                // --- 1) Cerco il passeggero per numero_documento ---
                String selectP = "SELECT id_passeggero FROM passeggero WHERE numero_documento = ?";
                Integer passengerId = null;
                try (PreparedStatement psSel = conn.prepareStatement(selectP)) {
                    psSel.setString(1, prenotazione.getPasseggero().getnDocumento());
                    try (ResultSet rs = psSel.executeQuery()) {
                        if (rs.next()) {
                            passengerId = rs.getInt("id_passeggero");
                        }
                    }
                }

                // --- 2) Se non esiste lo inserisco ---
                if (passengerId == null) {
                    String insertP =
                            "INSERT INTO passeggero (nome, cognome, numero_documento) VALUES (?, ?, ?)";
                    try (PreparedStatement psIns = conn.prepareStatement(
                            insertP, Statement.RETURN_GENERATED_KEYS)) {

                        psIns.setString(1, prenotazione.getPasseggero().getNome());
                        psIns.setString(2, prenotazione.getPasseggero().getCognome());
                        psIns.setString(3, prenotazione.getPasseggero().getnDocumento());
                        psIns.executeUpdate();

                        try (ResultSet rs = psIns.getGeneratedKeys()) {
                            if (rs.next()) {
                                passengerId = rs.getInt(1);
                            } else {
                                throw new SQLException("Impossibile recuperare ID passeggero");
                            }
                        }
                    }
                }

                // --- 3) Inserisco la prenotazione e ne recupero l'id ---
                String insertPr = ""
                        + "INSERT INTO prenotazione "
                        + "(codice_volo, id_passeggero, numero_biglietto, posto, stato, username) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                int bookingId;
                try (PreparedStatement psInsPr = conn.prepareStatement(
                        insertPr, Statement.RETURN_GENERATED_KEYS)) {

                    psInsPr.setString(1, codiceVolo);
                    psInsPr.setInt(2, passengerId);
                    psInsPr.setString(3, prenotazione.getNumeroBiglietto());
                    psInsPr.setString(4, prenotazione.getPosto());
                    psInsPr.setString(5, prenotazione.getStato().toString());
                    psInsPr.setString(6, utente.getLogin());
                    psInsPr.executeUpdate();

                    try (ResultSet rs = psInsPr.getGeneratedKeys()) {
                        if (rs.next()) {
                            bookingId = rs.getInt(1);
                        } else {
                            throw new SQLException("Impossibile recuperare ID prenotazione");
                        }
                    }
                }

                // --- 4) Inserisco eventuali bagagli con FK sull'id della prenotazione ---
                if (prenotazione.getBagagli() != null && !prenotazione.getBagagli().isEmpty()) {
                    String insertB =
                            "INSERT INTO bagaglio (codice, stato, id_prenotazione) VALUES (?, ?, ?)";
                    try (PreparedStatement psInsB = conn.prepareStatement(insertB)) {
                        for (Bagaglio b : prenotazione.getBagagli()) {
                            psInsB.setString(1, b.getCodice());
                            psInsB.setString(2, b.getStato().toString());
                            psInsB.setInt(3, bookingId);
                            psInsB.addBatch();
                        }
                        psInsB.executeBatch();
                    }

                }
                    String updateV = ""
                            + "UPDATE volo "
                            + "SET posti_disponibili = posti_disponibili - 1 "
                            + "WHERE codice= ? AND posti_disponibili > 0";
                    try (PreparedStatement psUpdV = conn.prepareStatement(updateV)) {
                        psUpdV.setString(1, codiceVolo);
                        int rows = psUpdV.executeUpdate();
                        if (rows != 1) {
                            throw new SQLException("Impossibile scalare i posti: nessun volo valido o posti esauriti");
                        }
                    }

                    // --- 6) Aggiorno la tabella posto: imposto occupato = true ---
                    String updateP = ""
                            + "UPDATE posto "
                            + "SET occupato = TRUE "
                            + "WHERE codice_volo = ? AND posto = ? AND occupato = FALSE";
                    try (PreparedStatement psUpdP = conn.prepareStatement(updateP)) {
                        psUpdP.setString(1, codiceVolo);
                        psUpdP.setString(2, prenotazione.getPosto());
                        int rows = psUpdP.executeUpdate();
                        if (rows != 1) {
                            throw new SQLException("Impossibile occupare il posto: già occupato o non esistente");
                        }
                    }

                conn.commit();
                success = true;

            } catch (SQLException tx) {
                conn.rollback();
                throw tx;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return success;
    }



    @Override
    public boolean updatePrenotazione(StatoPrenotazione nuovoStato, String numeroBiglietto) {
        // query principali
        String sqlUpdatePren =
                "UPDATE prenotazione SET stato = ? WHERE numero_biglietto = ?";
        String sqlSelectInfo =
                "SELECT codice_volo, posto FROM prenotazione WHERE numero_biglietto = ?";
        String sqlUpdateVolo =
                "UPDATE volo SET posti_disponibili = posti_disponibili + ? WHERE codice = ?";
        String sqlUpdatePosto =
                "UPDATE posto SET occupato = ? WHERE codice_volo = ? AND posto = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psPren = conn.prepareStatement(sqlUpdatePren);
                    PreparedStatement psInfo = conn.prepareStatement(sqlSelectInfo);
                    PreparedStatement psVolo = conn.prepareStatement(sqlUpdateVolo);
                    PreparedStatement psPosto = conn.prepareStatement(sqlUpdatePosto);
            ) {
                psPren.setString(1, nuovoStato.name());
                psPren.setString(2, numeroBiglietto);
                int updated = psPren.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    return false;
                }

                // ---- se lo stato è cancellato ----
                if (nuovoStato == StatoPrenotazione.cancellato) {
                    // leggo codice_volo e posto dalla prenotazione
                    psInfo.setString(1, numeroBiglietto);
                    try (ResultSet rs = psInfo.executeQuery()) {
                        if (rs.next()) {
                            String codiceVolo = rs.getString("codice_volo");
                            String seat      = rs.getString("posto");

                            // aggiorno volo: +1 posto disponibile
                            psVolo.setInt(1, 1);
                            psVolo.setString(2, codiceVolo);
                            psVolo.executeUpdate();

                            // aggiorno posto: occupato = false
                            psPosto.setBoolean(1, false);
                            psPosto.setString(2, codiceVolo);
                            psPosto.setString(3, seat);
                            psPosto.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return true;
            }
            catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
        catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della prenotazione: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updatePasseggeroInPrenotazione(String numeroBiglietto, String nome, String cognome, String nDocumento) {
        // Query per ottenere l'ID del passeggero associato alla prenotazione
        String sqlSelectPasseggeroId = 
                "SELECT id_passeggero FROM prenotazione WHERE numero_biglietto = ?";

        // Query per aggiornare i dati del passeggero
        String sqlUpdatePasseggero = 
                "UPDATE passeggero SET nome = ?, cognome = ?, numero_documento = ? WHERE id_passeggero = ?";

        // Query per aggiornare il riferimento al passeggero nella prenotazione se il numero documento è cambiato


        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1) Recupero l'ID del passeggero attuale
                int currentPasseggeroId;
                try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectPasseggeroId)) {
                    psSelect.setString(1, numeroBiglietto);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        currentPasseggeroId = rs.getInt("id_passeggero");
                    }
                }

                // 2) Aggiorno i dati del passeggero
                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdatePasseggero)) {
                    psUpdate.setString(1, nome);
                    psUpdate.setString(2, cognome);
                    psUpdate.setString(3, nDocumento);
                    psUpdate.setInt(4, currentPasseggeroId);
                    int updated = psUpdate.executeUpdate();
                    if (updated == 0) {
                        conn.rollback();
                        return false;
                    }
                }

                // 3) Se il numero documento è cambiato, aggiorno anche il riferimento nella prenotazione


                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del passeggero nella prenotazione: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodi per Bagaglio
    @Override
    public List<Bagaglio> getAllBagagli() {
        List<Bagaglio> bagagli = new ArrayList<>();
        String query = "SELECT * FROM bagaglio WHERE stato = 'smarrito'";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bagagli;
    }

    @Override
    public List<Bagaglio> getBagagliByPrenotazione(String numeroBiglietto) {
        List<Bagaglio> bagagli = new ArrayList<>();
        String query = "SELECT b.* FROM bagaglio b JOIN prenotazione pb ON b.id_prenotazione = pb.id_prenotazione WHERE pb.numero_biglietto = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bagagli;
    }

    @Override
    public Bagaglio getBagaglioByCodice(String codice) {
        String query = "SELECT * FROM bagaglio WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public boolean updateBagaglio(Bagaglio bagaglio) {
        String query = "UPDATE bagaglio SET stato = ? WHERE codice = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, bagaglio.getStato().toString());
            stmt.setString(2, bagaglio.getCodice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del bagaglio: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean updateBagagliByVolo(String codiceVolo, StatoBagaglio nuovoStato) {
        String sql =
                "UPDATE bagaglio b " +
                        "SET stato = ? " +
                        "FROM prenotazione p " +
                        "WHERE p.id_prenotazione = b.id_prenotazione " +
                        "  AND p.codice_volo = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public List<Posto> getPostiByVolo(String coidceVolo) {
        List<Posto> posti = new ArrayList<>();
        String sql =
                "SELECT codice_volo, posto, occupato " +
                        "FROM posto " +
                        "WHERE codice_volo = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, coidceVolo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Posto p = new Posto();
                    p.setCodiceVolo( rs.getString("codice_volo") );
                    p.setSeatNumber( rs.getString("posto") );
                    p.setOccupato( rs.getBoolean("occupato") );
                    posti.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore in getPostiByVolo: " + e.getMessage());
            // se preferisci, rilancia un unchecked:
            // throw new RuntimeException("DB error", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return posti;
    }


@Override
public List<Bagaglio> getBagagliByUtente(Utente user){
        List<Bagaglio> bagagli = new ArrayList<>();
    String sql = ""
            + "SELECT b.codice, b.stato, b.id_prenotazione "
            + "FROM bagaglio b "
            + "JOIN prenotazione p ON b.id_prenotazione = p.id_prenotazione "
            + "WHERE p.username = ? "
            +"ORDER BY p.codice_volo";


    try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // filtro per l'utente passato
        ps.setString(1, user.getLogin());

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bagaglio b = new Bagaglio(rs.getString("codice"),StatoBagaglio.valueOf(rs.getString("stato")));
                // Se StatoBagaglio è un enum, ricava il valore corretto
                // opzionale: se Bagaglio tiene traccia dell'id della prenotazione
                bagagli.add(b);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        // in produzione potresti rilanciare un'eccezione custom
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }

    return bagagli;



}


    @Override
    public Prenotazione getPrenotazioneByBagaglio(String codice_bagaglio) {
        String query = "SELECT * FROM prenotazione p " +
                    "JOIN bagaglio b ON b.id_prenotazione = p.id_prenotazione " +
                "JOIN passeggero pa ON pa.id_passeggero = p.id_passeggero " +
                " WHERE b.codice = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codice_bagaglio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Passeggero pa = new Passeggero(rs.getString("nome"), rs.getString("cognome"), rs.getString("numero_documento"));
                    return new Prenotazione(rs.getString("codice_volo"), rs.getString("numero_biglietto"), rs.getString("posto"), StatoPrenotazione.valueOf(rs.getString("stato") ), pa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del bagaglio: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
