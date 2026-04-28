package com.pao.proiect.banca.model;

import java.math.BigDecimal;

public class ContEconomii extends ContBancar {
    private BigDecimal rataDobanda;

    public ContEconomii(String iban, Client titular, BigDecimal rataDobanda) {
        super(iban, titular);
        this.rataDobanda = rataDobanda;
    }

    public void aplicaDobanda() {
        BigDecimal dobanda = sold.multiply(rataDobanda);
        depune(dobanda);
    }

    @Override
    public String getTipCont() { return "Economii"; }

    @Override
    public String toString() {
        return "ContEconomii{IBAN='" + iban + "', Sold=" + sold + ", Dobanda=" + rataDobanda + "}";
    }
}