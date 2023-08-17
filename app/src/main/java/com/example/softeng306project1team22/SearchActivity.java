package com.example.softeng306project1team22;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.Item;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<Item> allItems = new ArrayList<>();
    List<Item> filtered = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        retrieveAllItems();

        
    }

    protected void retrieveAllItems() {
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");

        colRef1.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Cleanser cleanser = documentSnapshot.toObject(Cleanser.class);
                allItems.add(cleanser);
            }
        });
        colRef2.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Moisturiser moisturiser = documentSnapshot.toObject(Moisturiser.class);
                allItems.add(moisturiser);
            }

        });
        colRef3.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Sunscreen sunscreen = documentSnapshot.toObject(Sunscreen.class);
                allItems.add(sunscreen);
            }

        });
    }

}