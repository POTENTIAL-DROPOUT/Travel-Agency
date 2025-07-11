package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class PrestationList extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField dateDebutField;
    private JTextField dateFinField;
    private JButton rechercheButton;
    private JButton nouvellePrestationButton;

    public PrestationList() {
        setTitle("Gestion des prestations");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel haut avec recherche et boutons
        JPanel topPanel = new JPanel(new FlowLayout());
        dateDebutField = new JTextField(8);
        dateFinField = new JTextField(8);
        rechercheButton = new JButton("Rechercher par période");
        nouvellePrestationButton = new JButton("Nouvelle prestation");

        topPanel.add(new JLabel("Date début (AAAA-MM-JJ):"));
        topPanel.add(dateDebutField);
        topPanel.add(new JLabel("Date fin:"));
        topPanel.add(dateFinField);
        topPanel.add(rechercheButton);
        topPanel.add(nouvellePrestationButton);

        add(topPanel, BorderLayout.NORTH);

        // Table des prestations
        String[] columns = {"ID", "Désignation", "Type", "Hôtel", "Ville", "Pays",
                "Date début", "Date fin", "Prix", "Places", "Actions"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 10;
            }
        };

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getColumn("Actions").setCellRenderer(new ButtonRendererPrestations());
        table.getColumn("Actions").setCellEditor(new ButtonEditorPrestations(new JCheckBox(), this));

        // Charger toutes les prestations au départ
        chargerPrestations(null, null);

        // Actions
        rechercheButton.addActionListener(e -> rechercher());
        nouvellePrestationButton.addActionListener(e -> new PrestationForm(this).setVisible(true));
    }

    public void chargerPrestations(String debut, String fin) {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM PRESTATION";
        if (debut != null && fin != null) {
            sql += " WHERE DATEDEBUT >= ? AND DATEFIN <= ?";
        }
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (debut != null && fin != null) {
                ps.setDate(1, Date.valueOf(debut));
                ps.setDate(2, Date.valueOf(fin));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("DESIGNATION"));
                row.add(rs.getString("TYPE"));
                row.add(rs.getString("HOTEL"));
                row.add(rs.getString("VILLE"));
                row.add(rs.getString("PAYS"));
                row.add(rs.getDate("DATEDEBUT").toString());
                row.add(rs.getDate("DATEFIN").toString());
                row.add(rs.getDouble("PRIX"));
                row.add(rs.getInt("PLACESDISPONIBLES"));
                row.add("Actions");
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rechercher() {
        String debut = dateDebutField.getText().trim();
        String fin = dateFinField.getText().trim();
        chargerPrestations(debut, fin);
    }

    public void rafraichir() {
        chargerPrestations(null, null);
    }

    public void modifierPrestation(String id) {
        new PrestationForm(this, id).setVisible(true);
    }

    public void supprimerPrestation(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette prestation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM PRESTATION WHERE ID=?")) {
                ps.setString(1, id);
                ps.executeUpdate();
                rafraichir();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}