package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;


public class UIManager {
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    public static final Color SECONDARY_COLOR = new Color(51, 153, 255);
    public static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    public static final Color TEXT_COLOR = new Color(50, 50, 50);
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color ERROR_COLOR = new Color(231, 76, 60);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );

    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

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

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setIconImage(createAirportIcon().getImage());
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(PANEL_BORDER);
    }

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

    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(NORMAL_FONT);
        textField.setBorder(FIELD_BORDER);
    }

    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(NORMAL_FONT);
        passwordField.setBorder(FIELD_BORDER);
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(FIELD_BORDER);
    }

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
