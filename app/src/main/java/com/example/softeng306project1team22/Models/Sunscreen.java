package com.example.softeng306project1team22.Models;



public class Sunscreen extends Items {
    private String spf;
    private String sunscreenType;
    public Sunscreen(String id, String name, String brand, String[] imageUri, double price, String usage, Category category, String[] skinType, String sunscreenType, String spf, String howToUse, String timeToUse){
        super(id, name, brand, imageUri, price, usage, category, skinType, howToUse, timeToUse);
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
