package com.pao.proiect.banca;

import com.pao.proiect.banca.exception.ContNegasitException;
import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import com.pao.proiect.banca.model.*;
import com.pao.proiect.banca.service.ClientService;
import com.pao.proiect.banca.service.ContService;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inițializăm serviciile de bază (Singleton)
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();

        try {
            // 1. Înregistrarea unui client nou
            Adresa adresaIon = new Adresa("Bucuresti", "Bulevardul Unirii, nr. 10");
            Client ion = new Client("1950101123456", "Ion Popescu", adresaIon);

            clientService.adaugaClient(ion);
            System.out.println("Client adăugat cu succes în sistem: " + ion.getNume());

            // 2. Deschiderea unui cont curent
            System.out.println("\n--- Deschidere Conturi ---");
            ContCurent contCurentIon = new ContCurent("RO01BTRL1111222233334444", ion);
            contService.deschideCont(contCurentIon);
            System.out.println("S-a deschis contul curent cu IBAN-ul: " + contCurentIon.getIban());

            // 3. Deschiderea unui cont de economii (cu o dobândă de 5%)
            ContEconomii contEconomiiIon = new ContEconomii("RO02BTRL9999888877776666", ion, new BigDecimal("0.05"));
            contService.deschideCont(contEconomiiIon);
            System.out.println("S-a deschis contul de economii cu IBAN-ul: " + contEconomiiIon.getIban());

            // 4. Emiterea unui card bancar
            Card cardIon = new Card("1234-5678-9012-3456");
            contService.emiteCard(contCurentIon.getIban(), cardIon);
            System.out.println("Cardul " + cardIon.getNumar() + " a fost atașat cu succes la contul curent.");

            // 5. Depunere numerar
            System.out.println("\n--- Procesare Tranzacții ---");
            ContBancar contCurent = contService.cautaCont(contCurentIon.getIban());
            contCurent.depune(new BigDecimal("1500.50"));
            System.out.println("S-au depus 1500.50 RON în contul curent.");

            // 6. Retragere numerar
            contCurent.retrage(new BigDecimal("200.50"));
            System.out.println("S-au retras 200.50 RON din contul curent.");

            // 7. Transfer între conturi (din contul curent în cel de economii)
            contService.transferaBani(contCurentIon.getIban(), contEconomiiIon.getIban(), new BigDecimal("300.00"));
            System.out.println("Transfer finalizat: 300.00 RON trimiși către contul de economii.");

            // 8. Blocarea cardului (simulăm cazul în care clientul îl pierde)
            cardIon.setActiv(false);
            System.out.println("Atenție: Cardul " + cardIon.getNumar() + " a fost blocat!");

            // 9. Afișarea tuturor conturilor clientului
            System.out.println("\n--- Situație Conturi pentru " + ion.getNume() + " ---");
            List<ContBancar> conturileLuiIon = contService.obtineConturiClient(ion.getCnp());

            for (ContBancar cont : conturileLuiIon) {
                System.out.println(" -> " + cont.getTipCont() + " | IBAN: " + cont.getIban() + " | Sold: " + cont.getSold() + " RON");
            }

            // 10. Extrasul de cont (afișează istoricul tranzacțiilor, sortat descrescător din Set)
            System.out.println("\n--- Extras de cont (Cont Curent) ---");
            for (Tranzactie tranzactie : contCurentIon.getIstoricTranzactii()) {
                System.out.println(tranzactie.toString());
            }
            // 11. Testare operație de ștergere
            System.out.println("\n--- Ștergere date din sistem ---");
            contService.inchideCont(contEconomiiIon.getIban());

            List<ContBancar> conturiRamase = contService.obtineConturiClient(ion.getCnp());
            System.out.println("Conturi active rămase pentru " + ion.getNume() + ": " + conturiRamase.size());

        } catch (ContNegasitException e) {
            System.err.println("Eroare de sistem (Cont): " + e.getMessage());
        } catch (FonduriInsuficienteException e) {
            System.err.println("Tranzacție refuzată: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("A apărut o eroare neașteptată: " + e.getMessage());
        }
    }
}