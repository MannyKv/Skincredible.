package com.example.softeng306project1team22.Models;

import java.util.ArrayList;

public class Cleanser extends Item {
    private String ph;
    private String cleanserType;

    public Cleanser() {

    }

    public Cleanser(String id, String name, String brand, ArrayList<String> imageName, double price, Category category, ArrayList<String> skinType, String ph, String cleanserType, String howToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageName = imageName;
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
