package com.pao.proiect.banca.service;

import com.pao.proiect.banca.exception.ContNegasitException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.model.*;
import com.pao.proiect.banca.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ContService {
    private static ContService instance;

    private Map<String, ContBancar> conturi;

    private final Connection connection;
    private final AuditService auditService;

    private ContService() {
        this.conturi = new HashMap<>();
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.auditService = AuditService.getInstance();
    }

    public static synchronized ContService getInstance() {
        if (instance == null) {
            instance = new ContService();
        }
        return instance;
    }


    public void deschideCont(ContBancar cont) {
        if (cont != null && cont.getIban() != null) {
            conturi.put(cont.getIban(), cont);
            auditService.logAction("deschide_cont_" + cont.getTipCont());
        }
    }

    public ContBancar cautaCont(String iban) throws ContNegasitException {
        auditService.logAction("cauta_cont");
        if (!conturi.containsKey(iban)) {
            throw new ContNegasitException("Contul cu IBAN-ul " + iban + " nu există!");
        }
        return conturi.get(iban);
    }

    public List<ContBancar> obtineConturiClient(String cnpClient) {
        auditService.logAction("obtine_conturi_client");
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
        auditService.logAction("transfer_in_memory");
    }

    public void emiteCard(String iban, Card card) throws ContNegasitException {
        ContBancar cont = cautaCont(iban);
        if (cont instanceof ContCurent) {
            ((ContCurent) cont).ataseazaCard(card);
            auditService.logAction("emite_card");
        }
    }

    public void inchideCont(String iban) throws ContNegasitException {
        if (!conturi.containsKey(iban)) {
            throw new ContNegasitException("Nu se poate închide un cont inexistent (IBAN: " + iban + ").");
        }
        conturi.remove(iban);
        auditService.logAction("inchide_cont");
        System.out.println("Sistem: Contul " + iban + " a fost închis și șters din sistem.");
    }


    public void transferaBaniDb(String ibanSursa, String ibanDestinatie, BigDecimal suma) throws SQLException {
        String updateSursa = "UPDATE conturi SET sold = sold - ? WHERE iban = ?";
        String updateDestinatie = "UPDATE conturi SET sold = sold + ? WHERE iban = ?";
        String insertTranzactieSursa = "INSERT INTO tranzactii (iban_cont, suma, tip_tranzactie) VALUES (?, ?, 'TRANSFER_OUT')";
        String insertTranzactieDestinatie = "INSERT INTO tranzactii (iban_cont, suma, tip_tranzactie) VALUES (?, ?, 'TRANSFER_IN')";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement psSursa = connection.prepareStatement(updateSursa)) {
                psSursa.setBigDecimal(1, suma);
                psSursa.setString(2, ibanSursa);
                psSursa.executeUpdate();
            }

            try (PreparedStatement psDest = connection.prepareStatement(updateDestinatie)) {
                psDest.setBigDecimal(1, suma);
                psDest.setString(2, ibanDestinatie);
                psDest.executeUpdate();
            }

            try (PreparedStatement psLogSursa = connection.prepareStatement(insertTranzactieSursa);
                 PreparedStatement psLogDest = connection.prepareStatement(insertTranzactieDestinatie)) {
                psLogSursa.setString(1, ibanSursa);
                psLogSursa.setBigDecimal(2, suma);

                psLogDest.setString(1, ibanDestinatie);
                psLogDest.setBigDecimal(2, suma);

                psLogSursa.executeUpdate();
                psLogDest.executeUpdate();
            }

            connection.commit();
            auditService.logAction("transferaBani_JDBC");
            System.out.println("Transfer în baza de date efectuat cu succes!");

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Eroare la transfer. Tranzacția a fost anulată.");
            throw e;
        } finally {

            connection.setAutoCommit(true);
        }
    }
}