package com.example.softeng306project1team22.Models;

import java.util.NoSuchElementException;

public class Cleanser extends Items {
    protected String ph;
    protected String cleanserType;

    public Cleanser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String ph, String cleanserType, String howToUse, String timeToUse) {
        super(id, name, brand, imageUri, price, category, skinType, howToUse, timeToUse);
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
