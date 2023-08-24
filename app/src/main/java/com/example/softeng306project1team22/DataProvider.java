package com.example.softeng306project1team22;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.IItem;
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

    }

}
