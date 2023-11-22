package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LargeDatasetGenerator {

    private static final LocalDate MAX_DATE = LocalDate.of(2014, 12, 19);
    private static final int YEARS_BEFORE_MAX_DATE = 5; // Set the desired number of years before 19-Dec-2014
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("d-MMM-yyyy");


    protected static void generateLargeDataset(String fileName, int numInstruments, int numEntriesPerInstrument) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();

            for (int i = 1; i <= numInstruments; i++) {
                String instrumentName = "INSTRUMENT" + i;

                for (int j = 1; j <= numEntriesPerInstrument; j++) {
                    // Generate a random number of years before 19-Dec-2014
                    int yearsBeforeMaxDate = random.nextInt(YEARS_BEFORE_MAX_DATE + 1);

                    // Subtract the random number of years to get a date
                    LocalDate date = MAX_DATE.minusYears(yearsBeforeMaxDate);
                    date = date.minusDays(random.nextInt(365)); // Random day within that year

                    double value = random.nextDouble() * 1000; // Random value

                    String line = String.format("%s,%s,%.2f%n", instrumentName, date.format(OUTPUT_FORMATTER), value);
                    writer.write(line);
                }
            }

            System.out.println("Large dataset generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
