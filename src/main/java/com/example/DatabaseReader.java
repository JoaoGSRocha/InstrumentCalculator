package com.example;

import com.example.model.InstrumentPriceModifier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseReader {
    private static final String URL = "jdbc:h2:./data/mydatabase";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static List<InstrumentPriceModifier> readModifiers() {
        List<InstrumentPriceModifier> modifiers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM INSTRUMENT_PRICE_MODIFIER";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    double multiplier = resultSet.getDouble("MULTIPLIER");
                    modifiers.add(new InstrumentPriceModifier(name, multiplier));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modifiers;
    }
}
