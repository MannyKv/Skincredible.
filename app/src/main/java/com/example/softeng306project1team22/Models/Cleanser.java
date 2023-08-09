package com.example.softeng306project1team22.Models;

import java.util.NoSuchElementException;

public class Cleanser extends Items {
    private String ph;
    private String cleanserType;

    public Cleanser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String ph, String cleanserType, String howToUse) {
        super(id, name, brand, imageUri, price, category, skinType, howToUse);
        this.ph=ph;
        this.cleanserType=cleanserType;
    }
    @Override
    public String getCleanserType(){
        return this.cleanserType;
    }
    @Override
    public  String getPh(){
        return this.ph;
    }
}
