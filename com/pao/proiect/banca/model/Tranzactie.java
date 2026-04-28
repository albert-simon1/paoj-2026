package com.pao.proiect.banca.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Tranzactie implements Comparable<Tranzactie> {
    private final String id;
    private final BigDecimal suma;
    private final String tip;
    private final LocalDateTime dataTimp;

    public Tranzactie(String id, BigDecimal suma, String tip) {
        this.id = id;
        this.suma = suma;
        this.tip = tip;
        this.dataTimp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public BigDecimal getSuma() { return suma; }
    public String getTip() { return tip; }
    public LocalDateTime getDataTimp() { return dataTimp; }

    @Override
    public int compareTo(Tranzactie o) {
        return o.dataTimp.compareTo(this.dataTimp);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s RON (ID: %s)", dataTimp, tip, suma, id);
    }
}