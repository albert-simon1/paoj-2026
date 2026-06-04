package com.pao.proiect.banca;

import com.pao.proiect.banca.exception.ContNegasitException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.model.*;
import com.pao.proiect.banca.service.ClientService;
import com.pao.proiect.banca.service.ContService;
import com.pao.proiect.banca.service.AuditService;
import com.pao.proiect.banca.repository.StatisticiRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        AuditService audit = AuditService.getInstance();

        try {
            Adresa adresaIon = new Adresa("Bucuresti", "Bulevardul Unirii, nr. 10");
            Client ion = new Client("1950101123456", "Ion Popescu", adresaIon);

            clientService.adaugaClient(ion);
            audit.logAction("adauga_client");
            System.out.println("Client adăugat cu succes în sistem: " + ion.getNume());

            System.out.println("\n--- Deschidere Conturi ---");
            ContCurent contCurentIon = new ContCurent("RO01BTRL1111222233334444", ion);
            contService.deschideCont(contCurentIon);
            System.out.println("S-a deschis contul curent cu IBAN-ul: " + contCurentIon.getIban());

            ContEconomii contEconomiiIon = new ContEconomii("RO02BTRL9999888877776666", ion, new BigDecimal("0.05"));
            contService.deschideCont(contEconomiiIon);
            System.out.println("S-a deschis contul de economii cu IBAN-ul: " + contEconomiiIon.getIban());

            Card cardIon = new Card("1234-5678-9012-3456");
            contService.emiteCard(contCurentIon.getIban(), cardIon);
            System.out.println("Cardul " + cardIon.getNumar() + " a fost atașat cu succes la contul curent.");

            System.out.println("\n--- Procesare Tranzacții ---");
            ContBancar contCurent = contService.cautaCont(contCurentIon.getIban());
            contCurent.depune(new BigDecimal("1500.50"));
            System.out.println("S-au depus 1500.50 RON în contul curent.");

            contCurent.retrage(new BigDecimal("200.50"));
            System.out.println("S-au retras 200.50 RON din contul curent.");

            System.out.println("\n--- Inițiere Transfer Bancar (JDBC) ---");
            try {
                java.sql.Connection conn = com.pao.proiect.banca.util.DatabaseConnection.getInstance().getConnection();

                try (java.sql.PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO clienti (cnp, nume, oras, strada) VALUES (?, ?, ?, ?)")) {
                    ps.setString(1, ion.getCnp());
                    ps.setString(2, ion.getNume());
                    ps.setString(3, ion.getAdresa().getOras());
                    ps.setString(4, ion.getAdresa().getStrada());
                    ps.executeUpdate();
                }

                try (java.sql.PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO conturi (iban, cnp_client, tip_cont, sold) VALUES (?, ?, 'CURENT', 1300.00)")) {
                    ps.setString(1, contCurentIon.getIban());
                    ps.setString(2, ion.getCnp());
                    ps.executeUpdate();
                }

                try (java.sql.PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO conturi (iban, cnp_client, tip_cont, sold) VALUES (?, ?, 'ECONOMII', 0.00)")) {
                    ps.setString(1, contEconomiiIon.getIban());
                    ps.setString(2, ion.getCnp());
                    ps.executeUpdate();
                }
                System.out.println("Datele clientului au fost sincronizate cu baza de date MySQL!");

                contService.transferaBaniDb(contCurentIon.getIban(), contEconomiiIon.getIban(), new BigDecimal("300.00"));

            } catch (SQLException e) {
                System.err.println("Eroare la baza de date în timpul transferului: " + e.getMessage());
            }

            System.out.println("\n--- Statistici Baza de Date ---");
            StatisticiRepository statsRepo = new StatisticiRepository();
            statsRepo.afiseazaClientiSiNumarConturi();
            statsRepo.topClientiDupaSoldTotal();
            audit.logAction("vizualizare_statistici_bancare");

            System.out.println("\n--- Situație Conturi pentru " + ion.getNume() + " ---");
            List<ContBancar> conturileLuiIon = contService.obtineConturiClient(ion.getCnp());
            for (ContBancar cont : conturileLuiIon) {
                System.out.println(" -> " + cont.getTipCont() + " | IBAN: " + cont.getIban() + " | Sold: " + cont.getSold() + " RON");
            }

            System.out.println("\n--- Ștergere date din sistem ---");
            contService.inchideCont(contEconomiiIon.getIban());

        } catch (ContNegasitException e) {
            System.err.println("Eroare de sistem (Cont): " + e.getMessage());
        } catch (FonduriInsuficienteException e) {
            System.err.println("Tranzacție refuzată: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("A apărut o eroare neașteptată: " + e.getMessage());
        }
    }
}