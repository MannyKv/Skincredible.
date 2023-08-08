package com.example.softeng306project1team22.Models;

public class Category {
    private String name;
    private String imageUri;
    private String id;

    public Category(String name, String id, String imageUri){
        this.name = name;
        this.id = id;
        this.imageUri = imageUri;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }

    public String getImageUri(){
        return this.imageUri;
    }
}
