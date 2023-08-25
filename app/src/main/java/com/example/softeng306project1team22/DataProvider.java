package com.example.softeng306project1team22;

import android.util.Log;

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
    //static IItem recievedItems;

    public static List<Category> getCategories() {
        return categories;
    }

    public static Category getCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    public static CompletableFuture<List<IItem>> getAllItems() {
        CompletableFuture<List<IItem>> future = new CompletableFuture<>();
        if (allItems.size() > 0) {

            future.complete(allItems);
            return future;
        } else {
            fetchAllItems().thenAccept(allItems -> future.complete(allItems));
        }
        return future;
    }


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

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();

        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");

        CompletableFuture<List<IItem>> fetchCleanser = retrieveFromCollection(colRef1, Cleanser.class);
        CompletableFuture<List<IItem>> fetchMoisturiser = retrieveFromCollection(colRef2, Moisturiser.class);
        CompletableFuture<List<IItem>> fetchSunscreen = retrieveFromCollection(colRef3, Sunscreen.class);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(fetchCleanser, fetchMoisturiser, fetchSunscreen);
        CompletableFuture<List<IItem>> future = combinedFuture.thenApply(voidResult -> {
            //List<IItem> allItems = new ArrayList<>();
            allItems.addAll(fetchCleanser.join());
            allItems.addAll(fetchMoisturiser.join());
            allItems.addAll(fetchSunscreen.join());
            return allItems;
        });

        return future;
    }

    public static CompletableFuture<List<IItem>> retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        CompletableFuture<List<IItem>> fetchFuture = new CompletableFuture<>();
        List<IItem> items = new ArrayList<>();
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "All Items Retrieved");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                items.add(item);

            }
            fetchFuture.complete(items);
        });
        return fetchFuture;
    }

    public static CompletableFuture<IItem> fetchItemById(String productCategory, String productId) {
        CompletableFuture<IItem> future = new CompletableFuture<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(productCategory.toLowerCase()).document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    switch (productCategory) {
                        case "Sunscreen":
                            IItem recievedItems = (IItem) documentSnapshot.toObject(Sunscreen.class);
                            future.complete(recievedItems);
                            break;
                        case "Cleanser":
                            IItem recievedItems1 = (IItem) documentSnapshot.toObject(Cleanser.class);
                            future.complete(recievedItems1);
                            break;
                        case "Moisturiser":
                            IItem recievedItems2 = (IItem) documentSnapshot.toObject(Moisturiser.class);
                            future.complete(recievedItems2);
                    }

                    //future.complete(recievedItems);
                });
        return future;
    }

}
