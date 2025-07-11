package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ClientList extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField rechercheField;
    private JButton rechercheButton;
    private JButton nouveauClientButton;
    private JButton prestationButton;
    private JButton archivedClientsButton;
    private JButton refreshButton; // NEW

    public ClientList() {
        setTitle("Liste des clients");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        rechercheField = new JTextField(20);
        rechercheButton = new JButton("Rechercher");
        nouveauClientButton = new JButton("Nouveau client");
        prestationButton = new JButton("Prestation");
        archivedClientsButton = new JButton("Voir clients archivés");
        refreshButton = new JButton("Rafraîchir"); // NEW

        topPanel.add(new JLabel("Recherche :"));
        topPanel.add(rechercheField);
        topPanel.add(rechercheButton);
        topPanel.add(nouveauClientButton);
        topPanel.add(prestationButton);
        topPanel.add(archivedClientsButton);
        topPanel.add(refreshButton); // NEW

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table.setRowHeight((int) (table.getRowHeight() * 2.3));

        chargerClients();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        rechercheButton.addActionListener(e -> rechercherClients());
        refreshButton.addActionListener(e -> rafraichir()); // NEW
        nouveauClientButton.addActionListener(e -> new ClientForm().setVisible(true));
        prestationButton.addActionListener(e -> new PrestationList().setVisible(true));
        archivedClientsButton.addActionListener(e -> new ArchivedClientList().setVisible(true));
    }

    public void chargerClients() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENT WHERE ARCHIVE = false");

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

    public void modifierClient(String id) {
        new EditClientForm(this, id).setVisible(true);
    }

    public void archiverClient(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer l'archivage ?", "Archiver", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE CLIENT SET ARCHIVE = true WHERE ID = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
                rafraichir();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void rechercherClients() {
        String recherche = rechercheField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM CLIENT WHERE ARCHIVE = false AND (LOWER(NOM) LIKE ? OR LOWER(PRENOM) LIKE ?)")) {
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
