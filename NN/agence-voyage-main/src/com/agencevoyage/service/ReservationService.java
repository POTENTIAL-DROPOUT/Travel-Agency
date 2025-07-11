package com.agencevoyage.service;

import com.agencevoyage.model.Client;
import com.agencevoyage.model.Prestation;
import com.agencevoyage.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();

    public boolean creerReservation(Client client, Prestation prestation) {
        if (prestation.getPlacesDisponibles() > 0) {
            Reservation r = new Reservation(client, prestation);
            reservations.add(r);
            prestation.setPlacesDisponibles(prestation.getPlacesDisponibles() - 1);
            return true;
        } else {
            return false;
        }
    }

    public List<Reservation> listerReservations() {
        return reservations;
    }

    public List<Reservation> listerReservationsParClient(Client client) {
        List<Reservation> resultat = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getClient().getId().equals(client.getId())) {
                resultat.add(r);
            }
        }
        return resultat;
    }
}
