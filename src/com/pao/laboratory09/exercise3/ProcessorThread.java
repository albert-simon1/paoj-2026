package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ProcessorThread implements Runnable {
    // Volatile garantează că modificările făcute din firul principal sunt vizibile instantaneu aici
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            while (activ) {
                Tranzactie t = coada.extrage(this);
                if (t != null) {
                    System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s\n",
                            t.getId(), t.getSuma(), t.getData());
                    Thread.sleep(80); // Simulează timpul efectiv de procesare al serverului
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Processor] a fost intrerupt.");
        }
    }
}