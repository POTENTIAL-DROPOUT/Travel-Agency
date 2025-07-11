package com.agencevoyage.model;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {
    private String id;
    private Client client;
    private Prestation prestation;
    private LocalDate dateReservation;

    public Reservation(Client client, Prestation prestation) {
        this.id = UUID.randomUUID().toString();
        this.client = client;
        this.prestation = prestation;
        this.dateReservation = LocalDate.now();
    }

    // Getters
    public String getId() { return id; }
    public Client getClient() { return client; }
    public Prestation getPrestation() { return prestation; }
    public LocalDate getDateReservation() { return dateReservation;}
}