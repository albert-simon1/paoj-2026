package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        // 1. Citire studenți din FILE_PATH cu BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Dacă fișierul este valid, fiecare linie va avea 4 componente
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
        } catch (FileNotFoundException e) {
            System.err.println("Fișierul nu a fost găsit la calea: " + FILE_PATH);
            return;
        }

        // 2. Citește comanda din stdin
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String commandLine = scanner.nextLine().trim();

            // 3. Execută comanda
            if (commandLine.equals("PRINT")) {
                for (Student s : studenti) {
                    System.out.println(s);
                }
            } else if (commandLine.startsWith("SHALLOW")) {
                String[] splitCommand = commandLine.split(" ", 2);
                if (splitCommand.length == 2) {
                    String nume = splitCommand[1].trim();
                    Student foundStudent = gasesteStudent(studenti, nume);

                    if (foundStudent != null) {
                        Student clona = foundStudent.shallowClone();
                        clona.getAdresa().setOras("MODIFICAT");

                        System.out.println("Original: " + foundStudent);
                        System.out.println("Clona: " + clona);
                    }
                }
            } else if (commandLine.startsWith("DEEP")) {
                String[] splitCommand = commandLine.split(" ", 2);
                if (splitCommand.length == 2) {
                    String nume = splitCommand[1].trim();
                    Student foundStudent = gasesteStudent(studenti, nume);

                    if (foundStudent != null) {
                        Student clona = foundStudent.deepClone();
                        clona.getAdresa().setOras("MODIFICAT");

                        System.out.println("Original: " + foundStudent);
                        System.out.println("Clona: " + clona);
                    }
                }
            }
        }
        scanner.close();
    }

    // Metodă auxiliară pentru a găsi studentul după nume
    private static Student gasesteStudent(List<Student> studenti, String nume) {
        for (Student s : studenti) {
            if (s.getNume().equalsIgnoreCase(nume)) {
                return s;
            }
        }
        return null;
    }
}