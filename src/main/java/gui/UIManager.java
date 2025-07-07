package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * UIManager class provides consistent styling across the application
 * with methods to apply styles to various UI components.
 */
public class UIManager {
    // Color scheme
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204);      // Deep blue
    public static final Color SECONDARY_COLOR = new Color(51, 153, 255);   // Lighter blue
    public static final Color ACCENT_COLOR = new Color(255, 153, 0);       // Orange
    public static final Color BACKGROUND_COLOR = new Color(240, 240, 245); // Light gray with blue tint
    public static final Color TEXT_COLOR = new Color(50, 50, 50);          // Dark gray
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);     // Green
    public static final Color ERROR_COLOR = new Color(231, 76, 60);        // Red
    public static final Color WARNING_COLOR = new Color(241, 196, 15);     // Yellow

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Borders
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );

    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    /**
     * Applies the application theme to the entire application
     */
    public static void applyTheme() {
        try {
            // Set system look and feel as base
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Override with our custom styling
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
     * Styles a JFrame with the application theme
     * @param frame The JFrame to style
     */
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setIconImage(createAirportIcon().getImage());
    }

    /**
     * Styles a JPanel with the application theme
     * @param panel The JPanel to style
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(PANEL_BORDER);
    }

    /**
     * Styles a JButton with the application theme
     * @param button The JButton to style
     */
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(NORMAL_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    /**
     * Styles a JTable with the application theme
     * @param table The JTable to style
     */
    public static void styleTable(JTable table) {
        table.setFont(NORMAL_FONT);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(false);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(SECONDARY_COLOR);  // Changed from Color.WHITE to SECONDARY_COLOR (blue)
        header.setFont(HEADER_FONT);

        // Center align cell contents and set text color to blue
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(PRIMARY_COLOR);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Styles a JTabbedPane with the application theme
     * @param tabbedPane The JTabbedPane to style
     */
    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
    }

    /**
     * Styles a JTextField with the application theme
     * @param textField The JTextField to style
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(NORMAL_FONT);
        textField.setBorder(FIELD_BORDER);
    }

    /**
     * Styles a JPasswordField with the application theme
     * @param passwordField The JPasswordField to style
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(NORMAL_FONT);
        passwordField.setBorder(FIELD_BORDER);
    }

    /**
     * Styles a JComboBox with the application theme
     * @param comboBox The JComboBox to style
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(FIELD_BORDER);
    }

    /**
     * Creates an airport icon for the application
     * @return The airport icon as an ImageIcon
     */
    public static ImageIcon createAirportIcon() {
        // Create a simple airplane icon
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a circle background
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(0, 0, size, size);

        // Draw a simple airplane shape
        g2d.setColor(Color.WHITE);
        int[] xPoints = {size/4, size/2, 3*size/4, size/2};
        int[] yPoints = {size/4, size/8, size/4, 3*size/4};
        g2d.fillPolygon(xPoints, yPoints, 4);

        g2d.dispose();
        return new ImageIcon(image);
    }
}
