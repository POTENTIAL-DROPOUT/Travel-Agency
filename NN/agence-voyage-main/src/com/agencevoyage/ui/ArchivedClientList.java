package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ArchivedClientList extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField rechercheField;
    private JButton rechercheButton;

    public ArchivedClientList() {
        setTitle("Liste des clients archivés");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // So it doesn't close the whole app
        setLayout(new BorderLayout());

        // Top panel with search bar
        JPanel topPanel = new JPanel(new FlowLayout());
        rechercheField = new JTextField(20);
        rechercheButton = new JButton("Rechercher");

        topPanel.add(new JLabel("Recherche :"));
        topPanel.add(rechercheField);
        topPanel.add(rechercheButton);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the action column
            }
        };

        table.setRowHeight((int) (table.getRowHeight() * 2.3));

        // Load archived clients
        chargerClients();

        // Table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Hook up the "Restaurer" button
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditorArchive(this));

        // Action: search
        rechercheButton.addActionListener(e -> rechercherClients());
    }

    public void chargerClients() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENT WHERE ARCHIVE = true");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("NOM"));
                row.add(rs.getString("PRENOM"));
                row.add(rs.getString("EMAIL"));
                row.add(rs.getString("TELEPHONE"));
                row.add("Actions");
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rafraichir() {
        chargerClients();
    }

    private void rechercherClients() {
        String recherche = rechercheField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM CLIENT WHERE ARCHIVE = true AND (LOWER(NOM) LIKE ? OR LOWER(PRENOM) LIKE ?)")) {
            String pattern = "%" + recherche + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("NOM"));
                row.add(rs.getString("PRENOM"));
                row.add(rs.getString("EMAIL"));
                row.add(rs.getString("TELEPHONE"));
                row.add("Actions");
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
