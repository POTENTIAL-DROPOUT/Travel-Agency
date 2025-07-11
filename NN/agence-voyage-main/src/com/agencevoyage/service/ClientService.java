package com.agencevoyage.service;

import com.agencevoyage.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private List<Client> clients = new ArrayList<>();

    public void ajouterClient(Client client) {
        clients.add(client);
    }

    public void modifierClient(Client client, String nom, String prenom, String email, String telephone) {
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setTelephone(telephone);
    }

    public void archiverClient(Client client) {
        client.setArchive(true);
    }

    public void restaurerClient(Client client) {
        client.setArchive(false);
    }

    public List<Client> listerClientsActifs() {
        List<Client> actifs = new ArrayList<>();
        for (Client c : clients) {
            if (!c.isArchive()) {
                actifs.add(c);
            }
        }
        return actifs;
    }

    public List<Client> listerClientsArchives() {
        List<Client> archives = new ArrayList<>();
        for (Client c : clients) {
            if (c.isArchive()) {
                archives.add(c);
            }
        }
        return archives;
    }

    public Client rechercherParId(String id) {
        for (Client c : clients) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
}