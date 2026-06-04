package com.pao.proiect.banca.repository;

import com.pao.proiect.banca.model.Adresa;
import com.pao.proiect.banca.model.Client;
import com.pao.proiect.banca.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Client entity) {
        String sql = "INSERT INTO clienti (cnp, nume, oras, strada) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getCnp());
            stmt.setString(2, entity.getNume());
            stmt.setString(3, entity.getAdresa().getOras());
            stmt.setString(4, entity.getAdresa().getStrada());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> findById(String cnp) {
        String sql = "SELECT * FROM clienti WHERE cnp = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnp);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Adresa adresa = new Adresa(rs.getString("oras"), rs.getString("strada"));
                    return Optional.of(new Client(rs.getString("cnp"), rs.getString("nume"), adresa));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override public List<Client> findAll() { return new ArrayList<>(); }
    @Override public void update(Client entity) {}
    @Override public void delete(String id) {}
}