package com.pao.proiect.banca.service;

import com.pao.proiect.banca.exception.ContNegasitException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.model.*;
import java.math.BigDecimal;
import java.util.*;

public class ContService {
    private static ContService instance;
    private Map<String, ContBancar> conturi;

    private ContService() {
        conturi = new HashMap<>();
    }

    public static ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }

    public void deschideCont(ContBancar cont) {
        if (cont != null && cont.getIban() != null) {
            conturi.put(cont.getIban(), cont);
        }
    }

    public ContBancar cautaCont(String iban) throws ContNegasitException {
        if (!conturi.containsKey(iban)) {
            throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu există!");
        }
        return conturi.get(iban);
    }

    public List<ContBancar> obtineConturiClient(String cnpClient) {
        List<ContBancar> conturiClient = new ArrayList<>();
        for (ContBancar cont : conturi.values()) {
            if (cont.getTitular().getCnp().equals(cnpClient)) {
                conturiClient.add(cont);
            }
        }
        return conturiClient;
    }

    public void transferaBani(String ibanSursa, String ibanDestinatie, BigDecimal suma) throws ContNegasitException, FonduriInsuficienteException {
        ContBancar sursa = cautaCont(ibanSursa);
        ContBancar destinatie = cautaCont(ibanDestinatie);
        sursa.retrage(suma);
        destinatie.depune(suma);
    }

    public void emiteCard(String iban, Card card) throws ContNegasitException {
        ContBancar cont = cautaCont(iban);
        if (cont instanceof ContCurent) {
            ((ContCurent) cont).ataseazaCard(card);
        }
    }
    public void inchideCont(String iban) throws ContNegasitException {
        if (!conturi.containsKey(iban)) {
            throw new ContNegasitException("Nu se poate închide un cont inexistent (IBAN: " + iban + ").");
        }
        conturi.remove(iban);
        System.out.println("Sistem: Contul " + iban + " a fost închis și șters din sistem.");
    }
}