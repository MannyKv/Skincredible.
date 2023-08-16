package com.example.softeng306project1team22.Models;

public class Category {
    private String name;
    private String imageName;
    private String id;

    public Category() {

    }

    public Category(String name, String id, String imageName) {
        this.name = name;
        this.id = id;
        this.imageName = imageName;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getImageName() {
        return this.imageName;
    }
}
