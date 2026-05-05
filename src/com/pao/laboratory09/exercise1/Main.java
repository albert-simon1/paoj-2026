package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        // Ne asigurăm că directorul de output există înainte de a scrie fișierul
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) return;

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        // 1. Citim cele N tranzacții
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            // Evităm probleme de parsing cauzate de setările de limbă (virgulă vs punct)
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);

            // 2. Setăm câmpul transient
            t.setNote("procesat");
            tranzactii.add(t);
        }

        // 3. Serializare (try-with-resources)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(tranzactii);
        }

        // 4. Deserializare (try-with-resources)
        List<Tranzactie> tranzactiiDeserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        }

        // 5. Procesare comenzi (LIST, FILTER, NOTE)
        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "LIST":
                    for (Tranzactie t : tranzactiiDeserializate) {
                        System.out.println(t);
                    }
                    break;

                case "FILTER":
                    String prefix = scanner.next();
                    boolean gasitFilter = false;

                    for (Tranzactie t : tranzactiiDeserializate) {
                        if (t.getData().startsWith(prefix)) {
                            System.out.println(t);
                            gasitFilter = true;
                        }
                    }

                    if (!gasitFilter) {
                        System.out.println("Niciun rezultat.");
                    }
                    break;

                case "NOTE":
                    int idCautat = scanner.nextInt();
                    boolean gasitNote = false;

                    for (Tranzactie t : tranzactiiDeserializate) {
                        if (t.getId() == idCautat) {
                            // t.getNote() va fi `null` aici datorită cuvântului cheie `transient`
                            System.out.println("NOTE[" + idCautat + "]: " + t.getNote());
                            gasitNote = true;
                            break;
                        }
                    }

                    if (!gasitNote) {
                        System.out.println("NOTE[" + idCautat + "]: not found");
                    }
                    break;
            }
        }

        scanner.close();
    }
}