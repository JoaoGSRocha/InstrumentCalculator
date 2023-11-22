package com.example;

import java.util.List;

public class CalculationEngine {
    public double calculateMean(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}