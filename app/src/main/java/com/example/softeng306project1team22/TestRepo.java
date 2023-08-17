package com.example.softeng306project1team22;

import android.util.Log;

import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TestRepo {
    static List<IItem> allItems = new ArrayList<>();

    public static List<IItem> getAllItems() {
        return allItems;
    }

    public static void allItemRetriever() {
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");

        retrieveFromCollection(colRef1, Cleanser.class);
        retrieveFromCollection(colRef2, Moisturiser.class);
        retrieveFromCollection(colRef3, Sunscreen.class);
    }

    private static void retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                allItems.add(item);
            }

        });
    }
}
