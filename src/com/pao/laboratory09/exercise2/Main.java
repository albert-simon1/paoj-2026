package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;

        int n = scanner.nextInt();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = Double.parseDouble(scanner.next());
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                dos.write(idBytes);
                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                dos.write(sumaBytes);
                String dataPadded = String.format("%-10s", data).substring(0, 10);
                dos.write(dataPadded.getBytes());
                dos.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);
                dos.writeByte(0);
                dos.write(new byte[8]);
            }
        }

        // 3. Procesarea comenzilor folosind RandomAccessFile (pentru acces direct la octeți)
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (scanner.hasNext()) {
                String comanda = scanner.next();

                switch (comanda) {
                    case "READ": {
                        int idx = scanner.nextInt();
                        readAndPrintRecord(raf, idx);
                        break;
                    }
                    case "UPDATE": {
                        int idx = scanner.nextInt();
                        String statusStr = scanner.next();
                        byte statusByte = parseStatus(statusStr);

                        // Ne ducem exact la byte-ul de status al înregistrării cu numărul idx
                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.writeByte(statusByte);

                        System.out.println("Updated [" + idx + "]: " + statusStr);
                        break;
                    }
                    case "PRINT_ALL": {
                        // Aflăm câte înregistrări avem calculând fileSize / RECORD_SIZE
                        int totalRecords = (int) (raf.length() / RECORD_SIZE);
                        for (int i = 0; i < totalRecords; i++) {
                            readAndPrintRecord(raf, i);
                        }
                        break;
                    }
                }
            }
        }

        scanner.close();
    }

    /**
     * Metodă auxiliară pentru a citi și afișa o înregistrare completă.
     */
    private static void readAndPrintRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        // Folosim ByteBuffer pentru extragerea datelor din array (mai ușor de setat endianness-ul)
        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt(0);
        double suma = buffer.getDouble(4);

        // Extragem cei 10 bytes pentru "data" și scoatem spațiile de la final
        byte[] dataBytes = new byte[10];
        buffer.position(12);
        buffer.get(dataBytes);
        String data = new String(dataBytes).trim();

        byte tipByte = buffer.get(22);
        String tip = (tipByte == 0) ? "CREDIT" : "DEBIT";

        byte statusByte = buffer.get(23);
        String status = statusToString(statusByte);

        // Folosim Locale.US pentru a formata suma cu punct (ex: 1500.00), conform output-ului cerut
        System.out.println(String.format(Locale.US,
                "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status));
    }

    /**
     * Parsează starea din String în byte.
     */
    private static byte parseStatus(String statusStr) {
        switch (statusStr) {
            case "PROCESSED": return 1;
            case "REJECTED": return 2;
            case "PENDING":
            default: return 0;
        }
    }

    /**
     * Parsează starea din byte în String.
     */
    private static String statusToString(byte statusByte) {
        switch (statusByte) {
            case 1: return "PROCESSED";
            case 2: return "REJECTED";
            case 0:
            default: return "PENDING";
        }
    }
}