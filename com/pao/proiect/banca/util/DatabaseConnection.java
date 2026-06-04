package com.pao.proiect.banca.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try (java.io.InputStream input = getClass().getClassLoader().getResourceAsStream("com/pao/proiect/banca/resources/db.properties")) {
            java.util.Properties prop = new java.util.Properties();
            if (input == null) {
                throw new RuntimeException("isierul db.properties nu a fost gasit in folderul de resurse");
            }
            prop.load(input);
            this.connection = java.sql.DriverManager.getConnection(
                    prop.getProperty("db.url"),
                    prop.getProperty("db.user"),
                    prop.getProperty("db.password")
            );
            System.out.println("Conexiune la baza de date realizata cu succes");
        } catch (java.sql.SQLException | java.io.IOException e) {
            throw new RuntimeException("Eroare la conectarea cu MySQL: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}