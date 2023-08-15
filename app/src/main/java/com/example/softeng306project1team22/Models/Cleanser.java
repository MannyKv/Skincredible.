package com.example.softeng306project1team22.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Cleanser extends Item {
    private String ph;
    private String cleanserType;
    private DocumentReference categoryRef;

    public Cleanser() {

    }

    public Cleanser(String id, String name, String brand, ArrayList<String> imageUri, double price, Category category, ArrayList<String> skinType, String ph, String cleanserType, String howToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageUri = imageUri;
        this.price = price;
        this.category = category;
        this.skinType = skinType;
        this.howToUse = howToUse;
        this.ph = ph;
        this.cleanserType = cleanserType;
    }

    public Cleanser(DocumentReference categoryRef, String id, String name, String brand, ArrayList<String> imageUri, double price, ArrayList<String> skinType, String ph, String cleanserType, String howToUse) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.imageUri = imageUri;
        this.price = price;
        this.skinType = skinType;
        this.howToUse = howToUse;
        this.ph = ph;
        this.cleanserType = cleanserType;
        this.categoryRef = categoryRef;
        populateCategoryField();
    }

    @Override
    public String getCleanserType() {
        return this.cleanserType;
    }

    @Override
    public String getPh() {
        return this.ph;
    }

    public void populateCategoryField() {
        if (categoryRef != null) {
            categoryRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    category = documentSnapshot.toObject(Category.class);
                }
            }).addOnFailureListener(e -> {
                // Handle fetch failure
            });
        }
    }
}

