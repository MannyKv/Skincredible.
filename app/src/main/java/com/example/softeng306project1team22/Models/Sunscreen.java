package com.example.softeng306project1team22.Models;



public class Sunscreen extends Items {
    private String spf;
    private String sunscreenType;
    public Sunscreen(String id, String name, String brand, String[] imageUri, double price, Category category, String[] skinType, String sunscreenType, String spf, String howToUse){
        super(id, name, brand, imageUri, price, category, skinType, howToUse);
        this.sunscreenType = sunscreenType;
        this.spf = spf;

    }

    @Override
    public  String getSunscreenType(){
        return this.sunscreenType;
    }
    @Override
    public  String getSpf(){
        return this.spf;
    }

}
