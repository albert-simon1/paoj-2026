package com.pao.proiect.banca.model;

import com.pao.proiect.banca.exception.FonduriInsuficienteException;
import java.math.BigDecimal;

public interface Tranzactionabil {
    void depune(BigDecimal suma);
    void retrage(BigDecimal suma) throws FonduriInsuficienteException;
}