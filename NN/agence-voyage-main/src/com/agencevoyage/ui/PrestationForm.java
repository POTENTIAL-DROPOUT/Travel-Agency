package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

public class PrestationForm extends JFrame {
    private JTextField designationField, typeField, hotelField, villeField, paysField, prixField, placesField;
    private JDatePickerImpl dateDebutPicker, dateFinPicker;
    private JButton enregistrerButton;
    private String prestationId;
    private PrestationList parent;

    public PrestationForm(PrestationList parent) {
        this(parent, null);
    }

    public PrestationForm(PrestationList parent, String id) {
        this.parent = parent;
        this.prestationId = id;

        setTitle(id == null ? "Nouvelle prestation" : "Modifier prestation");
        setSize(500, 400);
        setLayout(new GridLayout(10, 2));

        designationField = new JTextField();
        typeField = new JTextField();
        hotelField = new JTextField();
        villeField = new JTextField();
        paysField = new JTextField();
        prixField = new JTextField();
        placesField = new JTextField();
        dateDebutPicker = createDatePicker();
        dateFinPicker = createDatePicker();
        enregistrerButton = new JButton("Enregistrer");

        add(new JLabel("Désignation:")); add(designationField);
        add(new JLabel("Type:")); add(typeField);
        add(new JLabel("Hôtel:")); add(hotelField);
        add(new JLabel("Ville:")); add(villeField);
        add(new JLabel("Pays:")); add(paysField);
        add(new JLabel("Date début:")); add(dateDebutPicker);
        add(new JLabel("Date fin:")); add(dateFinPicker);
        add(new JLabel("Prix:")); add(prixField);
        add(new JLabel("Places dispo:")); add(placesField);
        add(enregistrerButton);

        if (id != null) chargerPrestation();

        enregistrerButton.addActionListener(this::enregistrer);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd'hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void chargerPrestation() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM PRESTATION WHERE ID=?")) {
            ps.setString(1, prestationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                designationField.setText(rs.getString("DESIGNATION"));
                typeField.setText(rs.getString("TYPE"));
                hotelField.setText(rs.getString("HOTEL"));
                villeField.setText(rs.getString("VILLE"));
                paysField.setText(rs.getString("PAYS"));
                prixField.setText(String.valueOf(rs.getDouble("PRIX")));
                placesField.setText(String.valueOf(rs.getInt("PLACESDISPONIBLES")));

                java.util.Date utilDateDebut = new java.util.Date(rs.getDate("DATEDEBUT").getTime());
                Calendar calDebut = Calendar.getInstance();
                calDebut.setTime(utilDateDebut);
                dateDebutPicker.getModel().setDate(calDebut.get(Calendar.YEAR), calDebut.get(Calendar.MONTH), calDebut.get(Calendar.DAY_OF_MONTH));
                dateDebutPicker.getModel().setSelected(true);

                java.util.Date utilDateFin = new java.util.Date(rs.getDate("DATEFIN").getTime());
                Calendar calFin = Calendar.getInstance();
                calFin.setTime(utilDateFin);
                dateFinPicker.getModel().setDate(calFin.get(Calendar.YEAR), calFin.get(Calendar.MONTH), calFin.get(Calendar.DAY_OF_MONTH));
                dateFinPicker.getModel().setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enregistrer(ActionEvent e) {
        try {
            String designation = designationField.getText();
            String type = typeField.getText();
            String hotel = hotelField.getText();
            String ville = villeField.getText();
            String pays = paysField.getText();
            double prix = Double.parseDouble(prixField.getText());
            int places = Integer.parseInt(placesField.getText());

            java.util.Date dateDebut = (java.util.Date) dateDebutPicker.getModel().getValue();
            java.util.Date dateFin = (java.util.Date) dateFinPicker.getModel().getValue();

            if (dateDebut == null || dateFin == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner les dates.");
                return;
            }

            LocalDate ldDebut = dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ldFin = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            try (Connection conn = DatabaseManager.getConnection()) {
                if (prestationId == null) {
                    String sql = "INSERT INTO PRESTATION (ID, DESIGNATION, TYPE, HOTEL, VILLE, PAYS, DATEDEBUT, DATEFIN, PRIX, PLACESDISPONIBLES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, UUID.randomUUID().toString());
                    ps.setString(2, designation);
                    ps.setString(3, type);
                    ps.setString(4, hotel);
                    ps.setString(5, ville);
                    ps.setString(6, pays);
                    ps.setDate(7, java.sql.Date.valueOf(ldDebut));
                    ps.setDate(8, java.sql.Date.valueOf(ldFin));
                    ps.setDouble(9, prix);
                    ps.setInt(10, places);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✅ Prestation ajoutée !");
                } else {
                    String sql = "UPDATE PRESTATION SET DESIGNATION=?, TYPE=?, HOTEL=?, VILLE=?, PAYS=?, DATEDEBUT=?, DATEFIN=?, PRIX=?, PLACESDISPONIBLES=? WHERE ID=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, designation);
                    ps.setString(2, type);
                    ps.setString(3, hotel);
                    ps.setString(4, ville);
                    ps.setString(5, pays);
                    ps.setDate(6, java.sql.Date.valueOf(ldDebut));
                    ps.setDate(7, java.sql.Date.valueOf(ldFin));
                    ps.setDouble(8, prix);
                    ps.setInt(9, places);
                    ps.setString(10, prestationId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, " Prestation modifiée !");
                }
                parent.rafraichir();
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, " Erreur : vérifiez les champs entrés.");
        }
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                return dateFormatter.format((java.util.Date) value);
            }
            return "";
        }
    }
}