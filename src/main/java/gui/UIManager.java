package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe che gestisce l'aspetto grafico dell'interfaccia utente dell'applicazione.
 * Fornisce costanti per colori, font e bordi, e metodi per applicare stili coerenti
 * a vari componenti dell'interfaccia grafica.
 * Implementa un design coerente in tutta l'applicazione.
 */
public class UIManager {
    /**
     * Costruttore privato per evitare l'istanziazione della classe UIManager
     */
    private UIManager() {}

    /**
     * Colore primario utilizzato per elementi principali dell'interfaccia come pulsanti e intestazioni.
     */
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204);

    /**
     * Colore secondario utilizzato per elementi di supporto e hover states.
     */
    public static final Color SECONDARY_COLOR = new Color(51, 153, 255);

    /**
     * Colore di sfondo utilizzato per pannelli e finestre.
     */
    public static final Color BACKGROUND_COLOR = new Color(240, 240, 245);

    /**
     * Colore utilizzato per il testo standard nell'interfaccia.
     */
    public static final Color TEXT_COLOR = new Color(50, 50, 50);

    /**
     * Colore utilizzato per messaggi di successo e conferma.
     */
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);

    /**
     * Colore utilizzato per messaggi di errore e avvisi critici.
     */
    public static final Color ERROR_COLOR = new Color(231, 76, 60);

    /**
     * Colore utilizzato per messaggi di avvertimento.
     */
    public static final Color WARNING_COLOR = new Color(241, 196, 15);

    /**
     * Font utilizzato per i titoli principali dell'interfaccia.
     */
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);

    /**
     * Font utilizzato per le intestazioni di sezioni e sottotitoli.
     */
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    /**
     * Font utilizzato per il testo standard nell'interfaccia.
     */
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Bordo standard utilizzato per i pannelli dell'interfaccia.
     */
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );

    /**
     * Bordo standard utilizzato per i campi di input dell'interfaccia.
     */
    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    /**
     * Applica il tema personalizzato all'interfaccia utente Swing.
     * Imposta il look and feel del sistema e configura i colori e i font predefiniti
     * per vari componenti dell'interfaccia grafica.
     */
    public static void applyTheme() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.UIManager.put("Panel.background", BACKGROUND_COLOR);
        javax.swing.UIManager.put("Label.font", NORMAL_FONT);
        javax.swing.UIManager.put("Label.foreground", TEXT_COLOR);
        javax.swing.UIManager.put("TextField.font", NORMAL_FONT);
        javax.swing.UIManager.put("PasswordField.font", NORMAL_FONT);
        javax.swing.UIManager.put("ComboBox.font", NORMAL_FONT);
        javax.swing.UIManager.put("Button.font", NORMAL_FONT);
        javax.swing.UIManager.put("TabbedPane.font", NORMAL_FONT);
        javax.swing.UIManager.put("Table.font", NORMAL_FONT);
        javax.swing.UIManager.put("TableHeader.font", HEADER_FONT);
    }

    /**
     * Applica lo stile standard a un JFrame.
     * Imposta il colore di sfondo e l'icona dell'applicazione.
     * 
     * @param frame Il JFrame a cui applicare lo stile
     */
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setIconImage(createAirportIcon().getImage());
    }

    /**
     * Applica lo stile standard a un JPanel.
     * Imposta il colore di sfondo e il bordo.
     * 
     * @param panel Il JPanel a cui applicare lo stile
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(PANEL_BORDER);
    }

    /**
     * Applica lo stile standard a un JButton.
     * Imposta colori, font, bordi e aggiunge effetti di hover.
     * 
     * @param button Il JButton a cui applicare lo stile
     */
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    /**
     * Applica lo stile standard a una JTable.
     * Configura font, dimensioni delle righe, colori di selezione e allineamento delle celle.
     * Personalizza anche l'aspetto dell'intestazione della tabella.
     * 
     * @param table La JTable a cui applicare lo stile
     */
    public static void styleTable(JTable table) {
        table.setFont(NORMAL_FONT);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(false);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(SECONDARY_COLOR);
        header.setFont(HEADER_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(PRIMARY_COLOR);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Applica lo stile standard a un JTabbedPane.
     * Imposta il font, il colore di sfondo e il colore del testo per il pannello a schede.
     * 
     * @param tabbedPane Il JTabbedPane a cui applicare lo stile
     */
    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
    }

    /**
     * Applica lo stile standard a un JTextField.
     * Imposta il font e il bordo per il campo di testo.
     * 
     * @param textField Il JTextField a cui applicare lo stile
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(NORMAL_FONT);
        textField.setBorder(FIELD_BORDER);
    }

    /**
     * Applica lo stile standard a un JPasswordField.
     * Imposta il font e il bordo per il campo password.
     * 
     * @param passwordField Il JPasswordField a cui applicare lo stile
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(NORMAL_FONT);
        passwordField.setBorder(FIELD_BORDER);
    }

    /**
     * Applica lo stile standard a un JComboBox.
     * Imposta il font, il colore di sfondo e il bordo per il menu a tendina.
     * 
     * @param comboBox Il JComboBox a cui applicare lo stile
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(FIELD_BORDER);
    }

    /**
     * Crea un'icona personalizzata per l'applicazione aeroportuale.
     * Genera un'immagine con un cerchio blu e un simbolo di aereo stilizzato in bianco.
     * 
     * @return Un oggetto ImageIcon contenente l'icona dell'aeroporto
     */
    public static ImageIcon createAirportIcon() {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(0, 0, size, size);

        g2d.setColor(Color.WHITE);
        int[] xPoints = {size/4, size/2, 3*size/4, size/2};
        int[] yPoints = {size/4, size/8, size/4, 3*size/4};
        g2d.fillPolygon(xPoints, yPoints, 4);

        g2d.dispose();
        return new ImageIcon(image);
    }
}
