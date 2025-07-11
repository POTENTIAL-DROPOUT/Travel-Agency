package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ClientForm extends JFrame {
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField telephoneField;
    private JButton enregistrerButton;

    public ClientForm() {
        setTitle("Ajouter un client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Labels et champs
        add(new JLabel("Nom :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        add(prenomField);

        add(new JLabel("Email :"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Téléphone :"));
        telephoneField = new JTextField();
        add(telephoneField);

        enregistrerButton = new JButton("Enregistrer");
        add(enregistrerButton);

        // Action bouton
        enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerClient();
            }
        });
    }

    private void enregistrerClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO CLIENT (ID, NOM, PRENOM, EMAIL, TELEPHONE, ARCHIVE) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, nom);
            ps.setString(3, prenom);
            ps.setString(4, email);
            ps.setString(5, telephone);
            ps.setBoolean(6, false);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Client enregistré !");
            nomField.setText("");
            prenomField.setText("");
            emailField.setText("");
            telephoneField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'enregistrement.");
        }
    }
}