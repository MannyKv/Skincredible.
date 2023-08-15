package com.example.softeng306project1team22.Models;

import java.util.ArrayList;

public interface IItem {
    public String getId();

    public String getName();

    public String getBrand();

    public double getPrice();

    public String getCategoryName();

    public String getHowToUse();

    public ArrayList<String> getSkinType();

    public ArrayList<String> getImageUri();

    public String getCleanserType();

    public String getPh();

    public String getSunscreenType();

    public String getSpf();

    public String getMoisturiserType();

    public String getTimeToUse();
}
