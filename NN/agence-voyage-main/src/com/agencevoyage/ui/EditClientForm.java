package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditClientForm extends JFrame {
    private JTextField nomField, prenomField, emailField, telephoneField;
    private JButton enregistrerButton;
    private String clientId;
    private ClientList parent;

    public EditClientForm(ClientList parent, String id) {
        this.parent = parent;
        this.clientId = id;

        setTitle("Modifier client");
        setSize(400, 300);
        setLayout(new GridLayout(5,2));

        nomField = new JTextField();
        prenomField = new JTextField();
        emailField = new JTextField();
        telephoneField = new JTextField();
        enregistrerButton = new JButton("Enregistrer");

        add(new JLabel("Nom:")); add(nomField);
        add(new JLabel("Prénom:")); add(prenomField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Téléphone:")); add(telephoneField);
        add(enregistrerButton);

        chargerClient();

        enregistrerButton.addActionListener(this::enregistrer);
    }

    private void chargerClient() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM CLIENT WHERE ID=?")) {
            ps.setString(1, clientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nomField.setText(rs.getString("NOM"));
                prenomField.setText(rs.getString("PRENOM"));
                emailField.setText(rs.getString("EMAIL"));
                telephoneField.setText(rs.getString("TELEPHONE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enregistrer(ActionEvent e) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE CLIENT SET NOM=?, PRENOM=?, EMAIL=?, TELEPHONE=? WHERE ID=?")) {
            ps.setString(1, nomField.getText());
            ps.setString(2, prenomField.getText());
            ps.setString(3, emailField.getText());
            ps.setString(4, telephoneField.getText());
            ps.setString(5, clientId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Modifié !");
            parent.rafraichir();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}