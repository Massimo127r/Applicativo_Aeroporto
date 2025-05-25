package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public abstract class BaseDashboard extends JFrame {
    protected Controller controller;
    protected JTabbedPane tabbedPane;
    protected JPanel mainPanel;

    public BaseDashboard() {
        controller = Controller.getInstance();
        setupFrame();
        createMainPanel();
        createTabs();
    }

    private void setupFrame() {
        setTitle("Dashboard Aeroporto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane);
        setContentPane(mainPanel);
    }

    protected abstract void createTabs();

    protected JTable createTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return new JTable(model);
    }

    protected void addTableToPanel(JPanel panel, JTable table) {
        panel.setLayout(new GridLayout(1, 1));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
    }

    protected void updateTable(JTable table, List<?> data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object item : data) {
            model.addRow(getTableRow(item));
        }
    }

    protected abstract Object[] getTableRow(Object item);
}

