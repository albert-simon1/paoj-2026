package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final int capacitate = 5;
    private final Queue<Tranzactie> coada = new LinkedList<>();

    // Metodă pentru producători
    public synchronized void adauga(Tranzactie t, String numeATM) throws InterruptedException {
        while (coada.size() == capacitate) {
            System.out.println("[" + numeATM + "] astept loc...");
            wait(); // Eliberează lock-ul și așteaptă ca banda să aibă loc
        }
        coada.add(t);
        notifyAll(); // Anunță consumatorul (și alți producători) că starea cozii s-a schimbat
    }

    // Metodă pentru consumator
    public synchronized Tranzactie extrage(ProcessorThread p) throws InterruptedException {
        while (coada.isEmpty()) {
            // Dacă am fost treziți doar pentru oprire, nu mai așteptăm
            if (!p.activ) {
                return null;
            }
            wait(); // Eliberează lock-ul și așteaptă să fie adăugat un element
        }
        Tranzactie t = coada.poll();
        notifyAll(); // Anunță producătorii că s-a eliberat un loc
        return t;
    }

    // Metodă ajutătoare pentru a verifica dacă totul a fost procesat înainte de închidere
    public synchronized boolean isEmpty() {
        return coada.isEmpty();
    }
}