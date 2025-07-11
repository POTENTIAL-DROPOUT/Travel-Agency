package com.agencevoyage.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // URL de connexion : fichier local ./data/agenceDB
    private static final String JDBC_URL = "jdbc:h2:./data/agenceDB";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Création de la table CLIENT si elle n'existe pas
            String sql = "CREATE TABLE IF NOT EXISTS CLIENT (" +
                    "ID VARCHAR(255) PRIMARY KEY," +
                    "NOM VARCHAR(255)," +
                    "PRENOM VARCHAR(255)," +
                    "EMAIL VARCHAR(255)," +
                    "TELEPHONE VARCHAR(255)," +
                    "ARCHIVE BOOLEAN" +
                    ")";
            // Création de la table PRESTATION
            String sql2 = "CREATE TABLE IF NOT EXISTS PRESTATION (" +
                    "ID VARCHAR(255) PRIMARY KEY," +
                    "DESIGNATION VARCHAR(255)," +
                    "TYPE VARCHAR(255)," +
                    "HOTEL VARCHAR(255)," +
                    "VILLE VARCHAR(255)," +
                    "PAYS VARCHAR(255)," +
                    "DATEDEBUT DATE," +
                    "DATEFIN DATE," +
                    "PRIX DOUBLE," +
                    "PLACESDISPONIBLES INT" +
                    ")";
            stmt.execute(sql2);
            stmt.execute(sql);

            System.out.println("✅ Base initialisée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}