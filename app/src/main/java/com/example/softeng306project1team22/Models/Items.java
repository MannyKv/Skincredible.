package com.example.softeng306project1team22.Models;


import java.util.NoSuchElementException;
public abstract class Items implements IItem {

    protected String id;
    protected String name;
    protected String brand;
    protected String[] imageUri;
    protected double price;
    protected Category category;
    protected String[] skinType;
    protected String howToUse;
    protected String ph;
    protected String cleanserType;
    protected String spf;
    protected String sunscreenType;
    protected String moisturiserType;
    protected String timeToUse;

    public Items(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String howToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageUri = imageUri;
        this.price = price;
        this.category = category;
        this.skinType = skinType;
        this.howToUse = howToUse;

    }

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getBrand(){
        return this.brand;
    }
    public double getPrice(){
        return this.price;
    }
    public Category getCategory(){
        return this.category;
    }
    public String getHowToUse(){
        return this.howToUse;
    }
    public String[] getSkinType(){
        return this.skinType;
    }
    public String[] getImageUri(){
        return this.imageUri;
    }
    public String getCleanserType(){
        throw new NoSuchElementException();
    }
    public  String getPh(){
        throw new NoSuchElementException();
    }
    public  String getSunscreenType(){
        throw new NoSuchElementException();
    }
    public  String getSpf(){
        throw new NoSuchElementException();
    }
    public  String getMoisturiserType(){
        throw new NoSuchElementException();
    }

    public String getTimeToUse(){
        throw new NoSuchElementException();
    }
}
