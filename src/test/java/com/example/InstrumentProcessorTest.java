package com.example;

import com.example.model.Instrument;
import com.example.model.InstrumentPriceModifier;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstrumentProcessorTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d-MMM-yyyy");

    // Create a mock CalculationEngine for testing
    CalculationEngine mockCalculationEngine = new CalculationEngine();

    InstrumentProcessor instrumentProcessor = new InstrumentProcessor(mockCalculationEngine);

    // Create sample modifiers
    List<InstrumentPriceModifier> modifiers = Arrays.asList(
            new InstrumentPriceModifier("INSTRUMENT1", 2.0),
            new InstrumentPriceModifier("INSTRUMENT3", 0.5)
    );


    @Test
    public void testProcessInstrument1() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("INSTRUMENT1", LocalDate.now(), 10.0)
        );

        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals("Mean for INSTRUMENT1: 10.0"+ System.lineSeparator()  +
                "Final value for INSTRUMENT1: 20.0" + System.lineSeparator() , expectedOutput);
    }

    @Test
    public void testProcessInstrument2() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("INSTRUMENT2", LocalDate.parse("01-Nov-2012", DATE_FORMATTER), 20.0)
        );

        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals(
                "Mean for INSTRUMENT2 in November 2014: 20.0"+ System.lineSeparator()  +
                "Final value for INSTRUMENT2: 20.0" + System.lineSeparator() , expectedOutput);
    }

    @Test
    public void testProcessInstrument3() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("INSTRUMENT3", LocalDate.now(), 30.0)
        );

        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals("Max value for INSTRUMENT3: 30.0" + System.lineSeparator()  +
                "Count of INSTRUMENT3 instruments: 1"+ System.lineSeparator() +
                "Final value for INSTRUMENT3: 15.0" + System.lineSeparator() , expectedOutput);
    }

    @Test
    public void testProcessOtherInstrument() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("OTHER_INSTRUMENT", LocalDate.now(), 40.0)
        );

        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals("Sum of the newest 10 values for OTHER_INSTRUMENT: 40.0" + System.lineSeparator()  +
                "Final value for OTHER_INSTRUMENT: 40.0" + System.lineSeparator() , expectedOutput);
    }

    @Test
    public void testProcessInstrument1PriceModifierChanged() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("INSTRUMENT1", LocalDate.now(), 10.0)
        );
        modifiers = Arrays.asList(
                new InstrumentPriceModifier("INSTRUMENT1", 20.0)
        );
        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals("Mean for INSTRUMENT1: 10.0" + System.lineSeparator()  +
                "Final value for INSTRUMENT1: 200.0" + System.lineSeparator() , expectedOutput);
    }

    @Test
    public void testProcessInstrument3PriceModifierChanged() {
        List<Instrument> instruments = Arrays.asList(
                new Instrument("INSTRUMENT3", LocalDate.now(), 30.0)
        );

        modifiers = Arrays.asList(
                new InstrumentPriceModifier("INSTRUMENT3", 15.0)
        );

        String expectedOutput = captureSystemOut(() -> instrumentProcessor.processInstruments(instruments, modifiers));

        assertEquals("Max value for INSTRUMENT3: 30.0" + System.lineSeparator()  +
                "Count of INSTRUMENT3 instruments: 1"+ System.lineSeparator() +
                "Final value for INSTRUMENT3: 450.0" + System.lineSeparator() , expectedOutput);
    }

    // Helper method to capture the standard output
    private String captureSystemOut(Runnable runnable) {
        return TestUtils.captureSystemOut(runnable);
    }
}
