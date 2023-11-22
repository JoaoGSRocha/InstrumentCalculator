package com.example.model;

public class InstrumentPriceModifier {
    private long id;
    private String name;
    private double multiplier;

    public InstrumentPriceModifier(String name, double multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}

