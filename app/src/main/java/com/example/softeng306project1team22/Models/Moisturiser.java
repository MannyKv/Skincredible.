package com.example.softeng306project1team22.Models;

import java.util.NoSuchElementException;

public class Moisturiser extends Items{
private String moisturiserType;
    public Moisturiser(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String moisturiserType, String howToUse, String timeToUse) {
    super(id, name, brand, imageUri, price, category, skinType, howToUse, timeToUse);
    this.moisturiserType=moisturiserType;
    }
    @Override
    public  String getMoisturiserType(){
        return this.moisturiserType;
    }
}
