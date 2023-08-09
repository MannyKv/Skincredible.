package com.example.softeng306project1team22.Models;

import java.util.NoSuchElementException;

public class Moisturiser extends Items{
private String moisturiserType;
private String  timeToUse;
    public Moisturiser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String moisturiserType, String howToUse, String timeToUse) {
    super(id, name, brand, imageUri, price, category, skinType, howToUse);
    this.moisturiserType=moisturiserType;
    this.timeToUse = timeToUse;
    }
    @Override
    public  String getMoisturiserType(){
        return this.moisturiserType;
    }
    @Override
    public String getTimeToUse(){
        return this.timeToUse;
    }
}

