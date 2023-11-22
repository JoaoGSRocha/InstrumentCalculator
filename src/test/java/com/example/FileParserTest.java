package com.example;

import com.example.model.Instrument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileParserTest {

    @Test
    public void testParseInstruments() throws IOException {
        // Create a temporary file with sample data
        Path tempFile = Files.createTempFile("testFile", ".csv");
        Files.write(tempFile, List.of(
                "INSTRUMENT1,12-Mar-2015,12.21",
                "INSTRUMENT2,15-Nov-2014,8.5",
                "INVALID_LINE" // Invalid line that should be skipped
        ));

        // Test parseInstruments method
        List<Instrument> instruments = FileParser.parseInstruments(tempFile.toString());
        assertEquals(2, instruments.size());

        // Clean up the temporary file
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testParseLineNotNull() {
        // Test valid line
        Instrument instrument = FileParser.parseLine("INSTRUMENT1,12-Mar-2015,12.21");
        assertNotNull(instrument);
    }
    @Test
    public void testParseLineRightName() {
        // Test valid line
        Instrument instrument = FileParser.parseLine("INSTRUMENT1,12-Mar-2015,12.21");
        assertEquals("INSTRUMENT1", instrument.getName());
    }
    @Test
    public void testParseLineRightDate() {
        // Test valid line
        Instrument instrument = FileParser.parseLine("INSTRUMENT1,12-Mar-2015,12.21");
        assertEquals(LocalDate.of(2015, 3, 12), instrument.getDate());
    }
    @Test
    public void testParseLineRightValue() {
        Instrument instrument = FileParser.parseLine("INSTRUMENT1,12-Mar-2015,12.21");
        assertEquals(12.21, instrument.getValue());
    }
    @Test
    public void testParseInvalidLine() {
        // Test invalid line
        Instrument instrument = FileParser.parseLine("INVALID_LINE");
        assertEquals(null, instrument);
    }
    @Test
    public void testParseDateNotNull() {
        LocalDate date = FileParser.parseDate("12-Mar-2015");
        assertNotNull(date);
    }
    @Test
    public void testParseDate() {
        LocalDate date = FileParser.parseDate("12-Mar-2015");
        assertEquals(LocalDate.of(2015, 3, 12), date);
    }
    @Test
    public void testParseInvalidDate() {
        LocalDate date = FileParser.parseDate("InvalidDate");
        assertEquals(null, date);
    }
    @Test
    public void testParseDouble() {
        double value = FileParser.parseDouble("12.34");
        assertEquals(12.34, value);
    }
    @Test
    public void testParseInvalidDouble() {
        double value = FileParser.parseDouble("InvalidDouble");
        assertEquals(0.0, value);
    }
}