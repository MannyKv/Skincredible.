package com.example.softeng306project1team22.Models;


import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public abstract class Item implements IItem {

    protected String id;
    protected String name;
    protected String brand;
    protected ArrayList<String> imageUri;
    protected double price;
    protected Category category;
    protected ArrayList<String> skinType;
    protected String howToUse;


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBrand() {
        return this.brand;
    }

    public double getPrice() {
        return this.price;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getHowToUse() {
        return this.howToUse;
    }

    public ArrayList<String> getSkinType() {
        return this.skinType;
    }

    public ArrayList<String> getImageUri() {
        return this.imageUri;
    }

    @Exclude
    public String getCleanserType() {
        throw new NoSuchElementException();
    }

    @Exclude
    public String getPh() {
        throw new NoSuchElementException();
    }

    @Exclude
    public String getSunscreenType() {
        throw new NoSuchElementException();
    }

    @Exclude
    public String getSpf() {
        throw new NoSuchElementException();
    }

    @Exclude
    public String getMoisturiserType() {
        throw new NoSuchElementException();
    }

    @Exclude

    public String getTimeToUse() {
        throw new NoSuchElementException();
    }
}