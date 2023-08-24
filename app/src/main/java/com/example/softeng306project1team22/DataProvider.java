package com.example.softeng306project1team22;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataProvider {
    static List<Category> categories = new ArrayList<>();
    static List<IItem> allItems = new ArrayList<>();

    public static List<Category> getCategories() {
        return categories;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static CompletableFuture<List<Category>> fetchCategoryData() {
        categories.clear();
        CompletableFuture<List<Category>> future = new CompletableFuture<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("category");

        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Data retrieved successfully");
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                categories.add(new Category(document.get("name", String.class), document.getId(), document.get("imageName", String.class)));
                System.out.println("Data in categories: " + categories.get(0).getName());
            }
            future.complete(categories);

        }).addOnFailureListener(e -> {
            System.out.println("Category Data Retrieval Failure");
        });


        return future;
    }

    public static CompletableFuture<List<IItem>> fetchAllItems() {
        CompletableFuture<List<IItem>> future = new CompletableFuture<>();
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();

        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");
        
        CompletableFuture<Void> fetchCleanser = retrieveFromCollection(colRef1, Cleanser.class);
        CompletableFuture<Void> fetchMoisturiser = retrieveFromCollection(colRef2, Moisturiser.class);
        CompletableFuture<Void> fetchSunscreen = retrieveFromCollection(colRef3, Sunscreen.class);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(fetchCleanser, fetchMoisturiser, fetchSunscreen);
        combinedFuture.thenRun(() -> {
            future.complete(allItems);
        });

        return future;
    }

    private static CompletableFuture<Void> retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        CompletableFuture<Void> fetchFuture = new CompletableFuture<>();
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "All Items Retrieved");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                allItems.add(item);

            }
            fetchFuture.complete(null);
        });
        return fetchFuture;
    }

}
