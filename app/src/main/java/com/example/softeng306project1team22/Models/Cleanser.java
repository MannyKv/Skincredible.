package com.example.softeng306project1team22.Models;

import java.util.ArrayList;

public class Cleanser extends Item {
    private String ph;
    private String cleanserType;


    public Cleanser() {

    }


    public Cleanser(String id, String name, String brand, ArrayList<String> imageNames, String price, String categoryName, ArrayList<String> skinType, String ph, String cleanserType, String howToUse) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageNames = imageNames;
        this.price = price;
        this.categoryName = categoryName;
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

