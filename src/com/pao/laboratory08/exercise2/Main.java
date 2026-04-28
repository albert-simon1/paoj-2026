package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

import java.io.*;
import java.util.*;

public class Main {
    private static final String INPUT_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Citirea pragului de vârstă
        if (!scanner.hasNextInt()) return;
        int prag = scanner.nextInt();

        // 2. Citirea studenților din fișierul sursă
        List<Student> totiStudentii = citesteStudenti();

        // 3. Filtrarea studenților
        List<Student> filtrati = new ArrayList<>();
        for (Student s : totiStudentii) {
            // Presupunem că ai adăugat getVarsta() în clasa Student la Ex 1
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        // 4. Scrierea în fișierul de ieșire și afișarea la consolă
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {

            System.out.println("Filtru: varsta >= " + prag);
            System.out.println("Rezultate: " + filtrati.size() + " studenti");
            System.out.println(); // Linie goală conform cerinței

            for (Student s : filtrati) {
                String line = s.toString();

                // Scriere în fișier
                writer.write(line);
                writer.newLine();

                // Afișare la consolă
                System.out.println(line);
            }

            System.out.println(); // Linie goală conform cerinței
            System.out.println("Scris in: " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Eroare la scrierea fișierului: " + e.getMessage());
        }
    }

    /**
     * Metodă utilitară pentru citirea studenților (reutilizată de la Ex 1)
     */
    private static List<Student> citesteStudenti() {
        List<Student> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] p = linie.split(",");
                if (p.length == 4) {
                    Adresa addr = new Adresa(p[2].trim(), p[3].trim());
                    // Asigură-te că Student are un constructor public accesibil
                    lista.add(new Student(p[0].trim(), Integer.parseInt(p[1].trim()), addr));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Eroare la citirea datelor: " + e.getMessage());
        }
        return lista;
    }
}