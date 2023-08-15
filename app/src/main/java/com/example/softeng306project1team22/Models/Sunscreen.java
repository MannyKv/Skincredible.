package com.example.softeng306project1team22.Models;


import java.util.ArrayList;

public class Sunscreen extends Item {
    private String spf;
    private String sunscreenType;

    public Sunscreen() {
    }

    public Sunscreen(String id, String name, String brand, ArrayList<String> imageNames, double price, String categoryName, ArrayList<String> skinType, String sunscreenType, String spf, String howToUse) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageNames = imageNames;
        this.price = price;
        this.categoryName = categoryName;
        this.skinType = skinType;
        this.howToUse = howToUse;
        this.sunscreenType = sunscreenType;
        this.spf = spf;

    }

    @Override
    public String getSunscreenType() {
        return this.sunscreenType;
    }

    @Override
    public String getSpf() {
        return this.spf;
    }

}
