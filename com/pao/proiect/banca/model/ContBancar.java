package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public abstract class ContBancar implements Tranzactionabil {
    protected String iban;
    protected BigDecimal sold;
    protected Client titular;
    protected Set<Tranzactie> istoricTranzactii;

    public ContBancar(String iban, Client titular) {
        this.iban = iban;
        this.titular = titular;
        this.sold = BigDecimal.ZERO;
        this.istoricTranzactii = new TreeSet<>();
    }

    public String getIban() { return iban; }
    public BigDecimal getSold() { return sold; }
    public Client getTitular() { return titular; }
    public Set<Tranzactie> getIstoricTranzactii() { return istoricTranzactii; }

    protected void inregistreazaTranzactie(BigDecimal suma, String tip) {
        istoricTranzactii.add(new Tranzactie(UUID.randomUUID().toString(), suma, tip));
    }

    @Override
    public void depune(BigDecimal suma) {
        if (suma.compareTo(BigDecimal.ZERO) > 0) {
            sold = sold.add(suma);
            inregistreazaTranzactie(suma, "DEPUNERE");
        }
    }

    @Override
    public void retrage(BigDecimal suma) throws FonduriInsuficienteException {
        if (sold.compareTo(suma) < 0) {
            throw new FonduriInsuficienteException("Fonduri insuficiente în contul " + iban);
        }
        sold = sold.subtract(suma);
        inregistreazaTranzactie(suma, "RETRAGERE");
    }

    public abstract String getTipCont();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContBancar that = (ContBancar) o;
        return Objects.equals(iban, that.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }
}