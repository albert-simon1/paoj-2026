package com.pao.proiect.banca.model;

import java.util.Objects;

public class Client {
    private String cnp;
    private String nume;
    private Adresa adresa;

    public Client(String cnp, String nume, Adresa adresa) {
        this.cnp = cnp;
        this.nume = nume;
        this.adresa = adresa;
    }

    public String getCnp() { return cnp; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public Adresa getAdresa() { return adresa; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(cnp, client.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }

    @Override
    public String toString() {
        return "Client{CNP='" + cnp + "', Nume='" + nume + "'}";
    }
}