package com.example.softeng306project1team22.Models;

public class Cleanser extends Item {
    private String ph;
    private String cleanserType;

    public Cleanser() {

    }

    public Cleanser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String ph, String cleanserType, String howToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageUri = imageUri;
        this.price = price;
        this.category = category;
        this.skinType = skinType;
        this.howToUse = howToUse;
        this.ph = ph;
        this.cleanserType = cleanserType;
    }

    @Override
    public String getCleanserType() {
        return this.cleanserType;
    }

    @Override
    public String getPh() {
        return this.ph;
    }
}
