package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii();

        // 1. Creează instanțele
        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        ProcessorThread processorTask = new ProcessorThread(coada);
        Thread consumerThread = new Thread(processorTask);

        // 2. & 3. Pornește firele de execuție (consumatorul mai întâi pentru a fi gata imediat)
        consumerThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        // 4. Facem join() pentru a asigura că toți producătorii au trimis tranzacțiile
        atm1.join();
        atm2.join();
        atm3.join();

        // Așteptăm să ne asigurăm că procesorul termină de procesat ultimele tranzacții rămase pe bandă
        while (!coada.isEmpty()) {
            Thread.sleep(50);
        }

        // 5. Oprim gratios bucla consumatorului și îl deblocăm din funcția wait()
        processorTask.activ = false;
        synchronized (coada) {
            coada.notifyAll();
        }
        consumerThread.join();

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}