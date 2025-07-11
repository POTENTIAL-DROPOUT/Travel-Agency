package com.agencevoyage.ui;

import com.agencevoyage.service.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initDatabase();
        ClientList list = new ClientList();
        list.setVisible(true);
    }
}