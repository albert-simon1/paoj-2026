package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ATMThread extends Thread {
    private final int idATM;
    private final CoadaTranzactii coada;

    public ATMThread(int idATM, CoadaTranzactii coada) {
        this.idATM = idATM;
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                int idTranzactie = idATM * 100 + i;
                double suma = 100 + Math.random() * 900;
                Tranzactie t = new Tranzactie(idTranzactie, suma, "2024-05-12");

                String numeATM = "ATM-" + idATM;

                // Folosim Locale.US pentru a formata suma cu punct (ex: 150.50 RON)
                System.out.printf(Locale.US, "[%s] trimite: Tranzactie #%d %.2f RON\n",
                        numeATM, idTranzactie, suma);

                coada.adauga(t, numeATM);

                Thread.sleep(50); // Simulează timpul de preluare a inputului din ATM
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ATM-" + idATM + "] a fost intrerupt.");
        }
    }
}