package com.example;

import com.example.model.Instrument;
import com.example.model.InstrumentPriceModifier;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class App {

    private static final String LARGE_DATASET_PATH = "large_dataset.csv";

    public static void main(String[] args) {
        // Initialize H2 database and create the table
        initializeDatabase();

        boolean useGeneratedDataset = args.length > 0 && args[0].equals("-generatedDataset");
        int numInstruments =  100;
        int numEntriesPerInstrument = 100;

        if (args.length > 1){
            numInstruments = isInteger(args[1]) ? Integer.parseInt(args[1]) : numInstruments;
            numEntriesPerInstrument = isInteger(args[2]) ? Integer.parseInt(args[2]) : numEntriesPerInstrument;
        }
        if(!Files.exists(Paths.get(LARGE_DATASET_PATH))) {
            // Use the generated dataset
            LargeDatasetGenerator.generateLargeDataset(LARGE_DATASET_PATH, numInstruments, numEntriesPerInstrument);
        }

        // Process instruments based on the specified input file
        InstrumentProcessor processor = new InstrumentProcessor(new CalculationEngine());
        List<Instrument> parsedInstruments = FileParser.parseInstruments(getInputFilePath(useGeneratedDataset, args));
        // Read modifiers from the database
        List<InstrumentPriceModifier> modifiers = DatabaseReader.readModifiers();

        processor.processInstruments(parsedInstruments, modifiers);


    }

    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }

    private static String getInputFilePath(boolean useGeneratedDataset, String[] args) {
        // Check if a file path is provided as a command-line argument
        if (useGeneratedDataset) {
            return LARGE_DATASET_PATH;
        } else {
            // Use the default input file
            return "input.txt";
        }
    }

    private static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./data/mydatabase", "sa", "")) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS INSTRUMENT_PRICE_MODIFIER (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY," +
                    "NAME VARCHAR(255) NOT NULL," +
                    "MULTIPLIER DOUBLE NOT NULL)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
                preparedStatement.executeUpdate();
            }

            // Insert some sample modifiers for testing
            String insertDataQuery = "INSERT INTO INSTRUMENT_PRICE_MODIFIER (NAME, MULTIPLIER) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataQuery)) {
                preparedStatement.setString(1, "INSTRUMENT1");
                preparedStatement.setDouble(2, 1.5);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "INSTRUMENT2");
                preparedStatement.setDouble(2, 0.8);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

