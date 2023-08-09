package com.example.softeng306project1team22.Models;

public class Moisturiser extends Item {
    private String moisturiserType;
    private String timeToUse;

    public Moisturiser() {

    }

    public Moisturiser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String moisturiserType, String howToUse, String timeToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageUri = imageUri;
        this.price = price;
        this.category = category;
        this.skinType = skinType;
        this.howToUse = howToUse;
        this.moisturiserType = moisturiserType;
        this.timeToUse = timeToUse;
    }

    @Override
    public String getMoisturiserType() {
        return this.moisturiserType;
    }

    @Override
    public String getTimeToUse() {
        return this.timeToUse;
    }
}

