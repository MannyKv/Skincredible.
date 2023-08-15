package com.example.softeng306project1team22;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softeng306project1team22.Data.CleanserDataProvider;
import com.example.softeng306project1team22.Data.MoisturiserDataProvider;
import com.example.softeng306project1team22.Data.SunscreenDataProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CleanserDataProvider.addCleansersToFirestore();
        MoisturiserDataProvider.addMoisturisersToFirestore();
        SunscreenDataProvider.addSunscreenToFirestore();
    }
}