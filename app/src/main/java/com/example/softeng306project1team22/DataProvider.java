package com.example.softeng306project1team22;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


        //create a ref for all the collections

        //create a completeable future for all items and call the retriever
        CompletableFuture<List<IItem>> fetchCleanser = fetchFromCollection("cleanser", Cleanser.class);
        CompletableFuture<List<IItem>> fetchMoisturiser = fetchFromCollection("moisturiser", Moisturiser.class);
        CompletableFuture<List<IItem>> fetchSunscreen = fetchFromCollection("sunscreen", Sunscreen.class);
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
     * @param categoryName the category name
     * @param itemClass    the type of item class
     * @return a compeleteable future of a list of items from that category
     */
    public static CompletableFuture<List<IItem>> fetchFromCollection(String categoryName, Class<?> itemClass) {
        CompletableFuture<List<IItem>> fetchFuture = new CompletableFuture<>();
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        List<IItem> items = new ArrayList<>();
        CollectionReference colRef = dbs.collection(categoryName);
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

                    if (listToReturn.size() == numDocsInCollection) {
                        future.complete(listToReturn);
                    }
                });
            }

        });
        return future;
    }

    public static CompletableFuture<List<IItem>> getReccomended(String categoryName, Class<?> itemClass, String filter) {
        CompletableFuture<List<IItem>> fetchFuture = new CompletableFuture<>();
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        List<IItem> items = new ArrayList<>();
        CollectionReference colRef = dbs.collection(categoryName);
        colRef.whereArrayContains("skinType", filter).get().addOnSuccessListener(queryDocumentSnapshots -> {
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
     * Adds and item to cart
     *
     * @param productId
     * @param productCategory
     * @param price
     * @param quantity
     */
    public static void addItemToCart(String productId, String productCategory, String price, String quantity) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> itemInfo = new HashMap<>();

        itemInfo.put("itemId", productId);
        itemInfo.put("quantity", quantity);
        itemInfo.put("categoryName", productCategory.toLowerCase());
        itemInfo.put("singleItemPrice", price);

        database.collection("cart").document(productId).set(itemInfo);
    }

    /**
     * adds an item to the recently viewed list
     *
     * @param item
     */
    public static void addItemToRecentlyViewed(IItem item) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Determine the size of the recently-viewed collection
        database.collection("recently-viewed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> documents = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        documents.add(document.getId());
                    }
                    // Format the data to be added to Firestore as a HashMap with key, value pairs
                    Map<String, Object> itemInfo = new HashMap<>();
                    itemInfo.put("itemId", item.getId());
                    itemInfo.put("timeAdded", FieldValue.serverTimestamp());
                    itemInfo.put("categoryName", item.getCategoryName());

                    // If there are more than 5 documents in the recently-viewed collection and the document being added is not already in it, delete the oldest one and then add the newest one
                    if (documents.size() > 5 && !documents.contains(item.getId())) {
                        // Query the collection and return the oldest document
                        Query sortedRecentlyViewed = database.collection("recently-viewed")
                                .orderBy("timeAdded", Query.Direction.ASCENDING)
                                .limit(1);

                        // Delete the oldest document
                        sortedRecentlyViewed.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    database.collection("recently-viewed").document(document.getId()).delete();
                                }
                            }
                        });

                        // Add the new document after the oldest one has been deleted
                        database.collection("recently-viewed").document(item.getId()).set(itemInfo);
                    } else {
                        // If there are 5 or fewer documents in the collection, just add the new one
                        database.collection("recently-viewed").document(item.getId()).set(itemInfo);
                    }
                }
            }
        });
    }

    /**
     * retrieves all cart documents and places it into a hash map with the Item as a key and quantity as a value
     *
     * @return Hashmap IItem,String
     */
    public static CompletableFuture<HashMap<IItem, String>> getCartDocuments() {
        CompletableFuture<HashMap<IItem, String>> future = new CompletableFuture<>();
        HashMap<IItem, String> itemInfo = new HashMap<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("cart").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int noItems = queryDocumentSnapshots.size();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                fetchItemById(document.getString("categoryName"), document.getString("itemId")).thenAccept(item -> {
                    itemInfo.put(item, document.getString("quantity"));
                    if (itemInfo.size() == noItems) {
                        future.complete(itemInfo);
                    }
                });
            }

        });
        return future;
    }

    public static void clearCart() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    database.collection("cart").document(document.getId()).delete();
                }
            }
        });
    }
}
