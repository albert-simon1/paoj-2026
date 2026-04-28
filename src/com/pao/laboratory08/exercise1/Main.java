package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;

        String input = scanner.nextLine();
        String[] commandParts = input.split(" ", 2);
        String command = commandParts[0];

        List<Student> studenti = incarcăStudenti();

        try {
            switch (command) {
                case "PRINT":
                    for (Student s : studenti) System.out.println(s);
                    break;

                case "SHALLOW":
                    proceseazaClonare(studenti, commandParts[1], true);
                    break;

                case "DEEP":
                    proceseazaClonare(studenti, commandParts[1], false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> incarcăStudenti() {
        List<Student> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] p = linie.split(",");
                if (p.length == 4) {
                    Adresa addr = new Adresa(p[2].trim(), p[3].trim());
                    lista.add(new Student(p[0].trim(), Integer.parseInt(p[1].trim()), addr));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului!");
        }
        return lista;
    }

    private static void proceseazaClonare(List<Student> studenti, String nume, boolean isShallow) throws CloneNotSupportedException {
        for (Student s : studenti) {
            if (s.getNume().equalsIgnoreCase(nume)) {
                Student clona = isShallow ? s.shallowClone() : s.deepClone();
                clona.getAdresa().setOras("MODIFICAT");

                System.out.println("Original: " + s);
                System.out.println("Clona: " + clona);
                return;
            }
        }
    }
}