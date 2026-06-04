package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.util.DatabaseConnection;
import java.sql.*;

public class StatisticiRepository {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public void afiseazaClientiSiNumarConturi() {
        String sql = "SELECT c.nume, COUNT(co.iban) as nr_conturi " +
                "FROM clienti c LEFT JOIN conturi co ON c.cnp = co.cnp_client " +
                "GROUP BY c.cnp";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("--- Situație Clienți vs Conturi ---");
            while (rs.next()) {
                System.out.println("Client: " + rs.getString("nume") + " | Conturi: " + rs.getInt("nr_conturi"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void afiseazaTranzactiiClient(String cnpClient) {
        String sql = "SELECT t.data_tranzactiei, t.tip_tranzactie, t.suma, c.nume, co.iban " +
                "FROM tranzactii t " +
                "JOIN conturi co ON t.iban_cont = co.iban " +
                "JOIN clienti c ON co.cnp_client = c.cnp " +
                "WHERE c.cnp = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnpClient);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("nume") + " a efectuat " + rs.getString("tip_tranzactie") +
                            " de " + rs.getBigDecimal("suma") + " RON in contul " + rs.getString("iban"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void topClientiDupaSoldTotal() {
        String sql = "SELECT c.nume, SUM(co.sold) as sold_total " +
                "FROM clienti c JOIN conturi co ON c.cnp = co.cnp_client " +
                "GROUP BY c.cnp ORDER BY sold_total DESC LIMIT 3";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("--- Top 3 Clienți (Sold Total) ---");
            while (rs.next()) {
                System.out.println("Client: " + rs.getString("nume") + " | Sold total: " + rs.getBigDecimal("sold_total") + " RON");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}