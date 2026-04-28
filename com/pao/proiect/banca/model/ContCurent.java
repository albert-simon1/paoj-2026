package com.pao.proiect.banca.model;

import java.util.ArrayList;
import java.util.List;

public class ContCurent extends ContBancar {
    private List<Card> carduri;

    public ContCurent(String iban, Client titular) {
        super(iban, titular);
        this.carduri = new ArrayList<>();
    }

    public void ataseazaCard(Card card) {
        if (card != null) {
            carduri.add(card);
        }
    }

    public List<Card> getCarduri() { return carduri; }

    @Override
    public String getTipCont() { return "Curent"; }

    @Override
    public String toString() {
        return "ContCurent{IBAN='" + iban + "', Sold=" + sold + "}";
    }
}