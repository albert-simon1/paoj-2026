package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // 1. Definim minim 10 tranzacții hardcodate, acoperind 3 luni (ian, feb, mar) și conturi multiple
        List<Tranzactie> tranzactii = Arrays.asList(
                new Tranzactie(1, 1500.00, "2024-01-05", TipTranzactie.CREDIT, "RO01_BANC"),
                new Tranzactie(2,  350.50, "2024-01-12", TipTranzactie.DEBIT,  "RO01_BANC"),
                new Tranzactie(3, 1200.00, "2024-01-20", TipTranzactie.CREDIT, "RO02_INGB"),
                new Tranzactie(4,   50.00, "2024-02-01", TipTranzactie.DEBIT,  "RO01_BANC"),
                new Tranzactie(5, 4500.00, "2024-02-14", TipTranzactie.CREDIT, "RO03_BT"),
                new Tranzactie(6,  750.25, "2024-02-28", TipTranzactie.DEBIT,  "RO04_BCR"),
                new Tranzactie(7,  100.00, "2024-03-05", TipTranzactie.DEBIT,  "RO02_INGB"),
                new Tranzactie(8,  200.00, "2024-03-10", TipTranzactie.DEBIT,  "RO01_BANC"),
                new Tranzactie(9, 2100.00, "2024-03-15", TipTranzactie.CREDIT, "RO04_BCR"),
                new Tranzactie(10, 850.75, "2024-03-25", TipTranzactie.DEBIT,  "RO03_BT")
        );

        // --- 1. Filtrare pe criteriu ---
        System.out.println("=== 1. Lista tuturor tranzactiilor CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        // --- 2. Agregare numerică ---
        System.out.println("\n=== 2. Total procesat ===");
        double totalSuma = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON\n", totalSuma);

        // --- 3. Grupare + sumare per grup ---
        System.out.println("\n=== 3. Suma totala per luna ===");
        // Folosim TreeMap pentru a avea rezultatul ordonat natural (după lună)
        Map<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7), // extragem yyyy-MM
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        sumaPerLuna.forEach((luna, suma) ->
                System.out.printf(Locale.US, "%s: %.2f RON\n", luna, suma)
        );

        // --- 4. Sortare și trunchiere ---
        System.out.println("\n=== 4. Top 3 tranzactii (dupa suma) ===");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // --- 5. Proiecție + Deduplicare ---
        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        // --- 6. Medie ---
        System.out.println("\n=== 6. Suma medie per tranzactie ===");
        double averageSuma = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON\n", averageSuma);

        // --- 7. Grupare avansată (Extras de cont) ---
        System.out.println("\n=== 7. EXTRAS DE CONT ===");
        Map<String, List<Tranzactie>> tranzactiiPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));

        tranzactiiPerLuna.forEach((luna, listaLuna) -> {
            double totalLuna = listaLuna.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n",
                    luna, listaLuna.size(), totalLuna);
        });
    }
}