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
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataProvider {
    static List<Category> categories = new ArrayList<>();
    static List<IItem> allItems = new ArrayList<>();

    /**
     * returns the list of categories
     *
     * @return list of categories
     */
    public static List<Category> getCategories() {
        return categories;
    }

    /**
     * Retrieves a category by id from the list of categories
     *
     * @param id
     * @return the category specified by id
     */
    public static Category getCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    /**
     * A getter for the allItem list variable.
     * If the item list is empty then it will make the fetchAllItems() call and return that as a completeable future
     *
     * @return list of ALL items future
     */
    public static CompletableFuture<List<IItem>> getAllItems() {
        CompletableFuture<List<IItem>> future = new CompletableFuture<>();
        if (allItems.size() > 0) {
            future.complete(allItems); //return if list is full
            return future;
        } else {
            fetchAllItems().thenAccept(allItems -> future.complete(allItems)); //make the database query
        }
        return future;
    }

    /**
     * Fetches all the category data in the category collection on firebase
     *
     * @return a list of categories as a completeable future
     */
    public static CompletableFuture<List<Category>> fetchCategoryData() {
        categories.clear(); //clear the list if its not already
        CompletableFuture<List<Category>> future = new CompletableFuture<>(); //new completeable future
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("category");
        //make the database call using Task
        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Data retrieved successfully");
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                categories.add(new Category(document.get("name", String.class), document.getId(), document.get("imageName", String.class)));
                //added category to list
            }
            future.complete(categories); //complete the future

        }).addOnFailureListener(e -> {
            System.out.println("Category Data Retrieval Failure");
        });


        return future;
    }

    /**
     * A method to fetch all items from all category collections in the database
     *
     * @return CompleteableFuture with a list of ALL items
     */
    public static CompletableFuture<List<IItem>> fetchAllItems() {

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        //create a ref for all the collections
        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");
        //create a completeable future for all items and call the retriever
        CompletableFuture<List<IItem>> fetchCleanser = retrieveFromCollection(colRef1, Cleanser.class);
        CompletableFuture<List<IItem>> fetchMoisturiser = retrieveFromCollection(colRef2, Moisturiser.class);
        CompletableFuture<List<IItem>> fetchSunscreen = retrieveFromCollection(colRef3, Sunscreen.class);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(fetchCleanser, fetchMoisturiser, fetchSunscreen);
        CompletableFuture<List<IItem>> future = combinedFuture.thenApply(voidResult -> { //create a new combined future to the future only returns once everything has been retrieved
            allItems.addAll(fetchCleanser.join());
            allItems.addAll(fetchMoisturiser.join());
            allItems.addAll(fetchSunscreen.join());
            return allItems;
        });

        return future;
    }

    /**
     * Retrieves a collection of items based on the reference and the Item class type
     * Only works for categories not collections with a mix of object types
     *
     * @param colRef    a collection reference of the items
     * @param itemClass the type of item class
     * @return a compeleteable future of a list of items from that category
     */
    public static CompletableFuture<List<IItem>> retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        CompletableFuture<List<IItem>> fetchFuture = new CompletableFuture<>();
        List<IItem> items = new ArrayList<>();
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "All Items Retrieved");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                items.add(item);
                //add the retrived item to the list
            }
            fetchFuture.complete(items); //complete the future
        });
        return fetchFuture;
    }

    /**
     * Fetches an item from firebase based on a product category and product id
     * Passes it back as a completable future which the calling method should prepare for
     *
     * @param productCategory the category the product is in
     * @param productId       the id of the product
     * @return a completeable future IItem
     */
    public static CompletableFuture<IItem> fetchItemById(String productCategory, String productId) {
        CompletableFuture<IItem> future = new CompletableFuture<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(productCategory.toLowerCase()).document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    switch (productCategory.toLowerCase()) { //fetching by the type
                        case "sunscreen":
                            IItem recievedItems = (IItem) documentSnapshot.toObject(Sunscreen.class);
                            future.complete(recievedItems);
                            break;
                        case "cleanser":
                            IItem recievedItems1 = (IItem) documentSnapshot.toObject(Cleanser.class);
                            future.complete(recievedItems1);
                            break;
                        case "moisturiser":
                            IItem recievedItems2 = (IItem) documentSnapshot.toObject(Moisturiser.class);
                            future.complete(recievedItems2);
                    }

                });
        return future;
    }

    /**
     * Returns a CompletableFuture to the calling class with a List of the recently viewed Items
     * Utilises fetchItemById() to get each specific document within the firetore collection
     *
     * @return A completeable future that will contain a list of recently viewed items
     */
    public static CompletableFuture<List<IItem>> fetchRecentlyViewed(String collectionName) {
        CompletableFuture<List<IItem>> future = new CompletableFuture<>();
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef = dbs.collection(collectionName);
        List<IItem> listToReturn = new ArrayList<>();
        colRef.orderBy("timeAdded", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            int numDocsInCollection = queryDocumentSnapshots.size();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                String id = documentSnapshot.getString("itemId");
                String categoryName = documentSnapshot.getString("categoryName");

                fetchItemById(categoryName, id).thenAccept(item -> {
                    listToReturn.add(item);
                    System.out.println("Added item");
                    if (listToReturn.size() == numDocsInCollection) {
                        future.complete(listToReturn);
                    }
                });
            }

        });
        return future;
    }


}
