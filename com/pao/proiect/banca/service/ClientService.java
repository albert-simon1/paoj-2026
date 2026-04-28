package com.pao.proiect.banca.service;

import com.pao.proiect.banca.model.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static ClientService instance;
    private List<Client> clienti;

    private ClientService() {
        clienti = new ArrayList<>();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void adaugaClient(Client client) {
        if (client != null && !clienti.contains(client)) {
            clienti.add(client);
        }
    }

    public Client cautaDupaCnp(String cnp) {
        for (Client c : clienti) {
            if (c.getCnp().equals(cnp)) return c;
        }
        return null;
    }

    public List<Client> listeazaTotiClientii() {
        return new ArrayList<>(clienti);
    }
    public void stergeClient(String cnp) {
        Client clientDeSters = cautaDupaCnp(cnp);
        if (clientDeSters != null) {
            clienti.remove(clientDeSters);
            System.out.println("Sistem: Clientul cu CNP-ul " + cnp + " a fost șters.");
        }
    }
}