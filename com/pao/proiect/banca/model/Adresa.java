package com.pao.proiect.banca.model;

public class Adresa {
    private String oras;
    private String strada;

    public Adresa(String oras, String strada) {
        this.oras = oras;
        this.strada = strada;
    }

    public String getOras() { return oras; }
    public void setOras(String oras) { this.oras = oras; }
    public String getStrada() { return strada; }
    public void setStrada(String strada) { this.strada = strada; }

    @Override
    public String toString() {
        return strada + ", " + oras;
    }
}