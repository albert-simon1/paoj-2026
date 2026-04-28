package com.pao.proiect.banca.model;

public class Card {
    private String numar;
    private boolean activ;

    public Card(String numar) {
        this.numar = numar;
        this.activ = true;
    }

    public String getNumar() { return numar; }
    public boolean isActiv() { return activ; }
    public void setActiv(boolean activ) { this.activ = activ; }

    @Override
    public String toString() {
        return "Card{numar='" + numar + "', activ=" + activ + "}";
    }
}