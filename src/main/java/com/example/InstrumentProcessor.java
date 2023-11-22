package com.example;

import com.example.model.Instrument;
import com.example.model.InstrumentPriceModifier;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InstrumentProcessor {
    private CalculationEngine calculationEngine;

    public InstrumentProcessor(CalculationEngine calculationEngine) {
        this.calculationEngine = calculationEngine;
    }


    public void processInstruments(List<Instrument> instruments, List<InstrumentPriceModifier> modifiers) {
        // Filter out instruments with non-business dates
        List<Instrument> businessDayInstruments = instruments.stream()
                .filter(this::isBusinessDay)
                .collect(Collectors.toList());

        // Group instruments by name for further processing
        Map<String, List<Instrument>> instrumentGroups = businessDayInstruments.stream()
                .collect(Collectors.groupingBy(Instrument::getName));

        // Process each instrument group
        instrumentGroups.forEach((name, group) -> {
            List<Double> values = group.stream().map(Instrument::getValue).collect(Collectors.toList());

            // Apply modifiers if present
            double finalValue = applyModifiers(name, values, modifiers);

            // Perform calculations based on the instrument name
            switch (name) {
                case "INSTRUMENT1":
                    System.out.println("Mean for " + name + ": " + calculationEngine.calculateMean(values));
                    break;
                case "INSTRUMENT2":
                    List<Instrument> novemberInstruments = group.stream()
                            .filter(instrument -> instrument.getDate().getMonthValue() == 11) // November
                            .collect(Collectors.toList());
                    System.out.println("Mean for " + name + " in November 2014: " +
                            calculationEngine.calculateMean(novemberInstruments.stream().map(Instrument::getValue).collect(Collectors.toList())));
                    break;
                case "INSTRUMENT3":
                    // Your custom statistical calculation for INSTRUMENT3
                    // Example: Print the maximum value
                    double max = values.stream().max(Double::compareTo).orElse(0.0);
                    System.out.println("Max value for " + name + ": " + max);
                    System.out.println("Count of "+ name +" instruments: " + values.size());
                    break;
                default:
                    // Sum of the newest 10 elements
                    List<Double> newestValues = group.stream()
                            .sorted((i1, i2) -> i2.getDate().compareTo(i1.getDate()))
                            .limit(10)
                            .map(Instrument::getValue)
                            .collect(Collectors.toList());
                    double sumNewestValues = newestValues.stream().mapToDouble(Double::doubleValue).sum();
                    System.out.println("Sum of the newest 10 values for " + name + ": " + sumNewestValues);
                    break;
            }

            // Print the final value after applying modifiers
            System.out.println("Final value for " + name + ": " + finalValue);
        });
    }

    private boolean isBusinessDay(Instrument instrument) {
        return instrument.getDate().getDayOfWeek().getValue() <= 5;
    }

    private double applyModifiers(String instrumentName, List<Double> values, List<InstrumentPriceModifier> modifiers) {
        // Find the modifier for the instrument
        InstrumentPriceModifier modifier = modifiers.stream()
                .filter(m -> m.getName().equals(instrumentName))
                .findFirst()
                .orElse(null);

        // Apply the modifier if present
        return modifier != null ? values.stream().mapToDouble(value -> value * modifier.getMultiplier()).sum() :
                values.stream().mapToDouble(Double::doubleValue).sum();
    }
}