package com.agencevoyage.model;

import java.time.LocalDate;
import java.util.UUID;


public class Prestation {
    private String id;
    private String designation;
    private String type;
    private String hotel;
    private String ville;
    private String pays;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double prix;
    private int placesDisponibles;

    public Prestation(String designation, String type, String hotel, String ville, String pays,
                      LocalDate dateDebut, LocalDate dateFin, double prix, int placesDisponibles) {
        this.id = UUID.randomUUID().toString();
        this.designation = designation;
        this.type = type;
        this.hotel = hotel;
        this.ville = ville;
        this.pays = pays;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
    }

    // Getters et Setters
    public String getId() { return id; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }
}