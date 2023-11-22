package com.example;

import com.example.model.Instrument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d-MMM-yyyy");

    public static List<Instrument> parseInstruments(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .map(FileParser::parseLine)
                    .filter(instrument -> instrument != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of(); // Return an empty list in case of an exception
    }

    protected static Instrument parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            String name = parts[0];
            LocalDate date = parseDate(parts[1]);
            double value = parseDouble(parts[2]);
            return new Instrument(name, date, value);
        }
        return null; // Skip invalid lines
    }

    protected static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static double parseDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}