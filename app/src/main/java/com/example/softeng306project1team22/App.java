package com.example.softeng306project1team22;

import static com.example.softeng306project1team22.TestRepo.allItemRetriever;

import android.app.Application;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        allItemRetriever();

    }
}
