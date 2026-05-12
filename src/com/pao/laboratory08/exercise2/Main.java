package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();

                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);
                    studenti.add(student);
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului de intrare: " + e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            System.err.println("Eroare: Nu s-a introdus un număr întreg valid.");
            scanner.close();
            return;
        }
        int prag = scanner.nextInt();
        scanner.close();
        List<Student> studentiFiltrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                studentiFiltrati.add(s);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Student s : studentiFiltrati) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea fișierului: " + e.getMessage());
            return;
        }
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + studentiFiltrati.size() + " studenti\n");

        for (Student s : studentiFiltrati) {
            System.out.println(s);
        }

        System.out.println("\nScris in: " + OUTPUT_FILE);
    }
}