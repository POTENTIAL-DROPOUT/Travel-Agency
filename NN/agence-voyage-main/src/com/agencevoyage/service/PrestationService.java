package com.agencevoyage.service;

import com.agencevoyage.model.Prestation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestationService {
    private List<Prestation> prestations = new ArrayList<>();

    public void ajouterPrestation(Prestation prestation) {
        prestations.add(prestation);
    }

    public void supprimerPrestation(Prestation prestation) {
        prestations.remove(prestation);
    }

    public void modifierPrestation(Prestation prestation, String designation, String type, String hotel, String ville, String pays,
                                   LocalDate dateDebut, LocalDate dateFin, double prix, int placesDisponibles) {
        prestation.setDesignation(designation);
        prestation.setType(type);
        prestation.setHotel(hotel);
        prestation.setVille(ville);
        prestation.setPays(pays);
        prestation.setDateDebut(dateDebut);
        prestation.setDateFin(dateFin);
        prestation.setPrix(prix);
        prestation.setPlacesDisponibles(placesDisponibles);
    }

    public List<Prestation> listerPrestationsDisponibles() {
        return prestations;
    }

    public List<Prestation> rechercherParPeriode(LocalDate debut, LocalDate fin) {
        List<Prestation> resultat = new ArrayList<>();
        for (Prestation p : prestations) {
            if (!p.getDateFin().isBefore(debut) && !p.getDateDebut().isAfter(fin)) {
                resultat.add(p);
            }
        }
        return resultat;
    }

    public Prestation rechercherParId(String id) {
        for (Prestation p : prestations) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
