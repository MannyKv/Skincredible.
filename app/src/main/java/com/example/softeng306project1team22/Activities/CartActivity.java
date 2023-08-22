package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.CategoryAdapter;
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Item;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private class ViewHolder {
        Button backButton;
        TextView noItemsTextView, recommendedItemsHeader;
        RecyclerView cartItemsRecyclerView;
        CardView cartTotalContainer;
        RecyclerView recommendedItemsRecyclerView;
        TextView totalPriceTextView;
        Button checkoutButton;

        public ViewHolder() {
            backButton = findViewById(R.id.back_button);
            noItemsTextView = findViewById(R.id.noItemsTextView);
            recommendedItemsHeader = findViewById(R.id.recommendedItemsHeader);
            cartItemsRecyclerView = findViewById(R.id.rvCartItems);
            cartTotalContainer = findViewById(R.id.cartTotalContainer);
            recommendedItemsRecyclerView = findViewById(R.id.rvRecommendedItems);
            totalPriceTextView = findViewById(R.id.totalPriceTextView);
            checkoutButton = findViewById(R.id.checkoutButton);
        }
    }

    private ViewHolder viewHolder;

    private ArrayList<IItem> itemList;

    private ArrayList<Item> recommendedItemList;

    private ItemListAdapter listAdapter;

    private CompactItemAdapter itemAdapter;

    private Map<String, String> itemQuantities;

    private ArrayList<String> productSkinTypes;

    private ArrayList<String> productIds;

    private boolean onResumeCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        onResumeCalled = false;

        viewHolder = new ViewHolder();

        viewHolder.backButton.setOnClickListener(v -> finish());

        itemList = new ArrayList<>();

        recommendedItemList = new ArrayList<>();

        itemQuantities = new HashMap<>();

        productSkinTypes = new ArrayList<>();

        productIds = new ArrayList<>();

        // Setting onclick functionality for the checkout button
        viewHolder.checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemList.size() > 0) {
                    // Clear all the cart information and set the total price to $0.00
                    itemList.clear();
                    productSkinTypes.clear();
                    productIds.clear();
                    clearCart();
                    viewHolder.totalPriceTextView.setText("$0.00");
                    viewHolder.noItemsTextView.setVisibility(View.VISIBLE);
                    viewHolder.cartItemsRecyclerView.setVisibility(View.GONE);
                    viewHolder.cartTotalContainer.setVisibility(View.GONE);
                    viewHolder.recommendedItemsHeader.setVisibility(View.GONE);
                    viewHolder.recommendedItemsRecyclerView.setVisibility(View.GONE);
                    viewHolder.checkoutButton.setVisibility(View.GONE);

                    // Display popup dialog confirming purchase
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(CartActivity.this, R.style.alert_dialog);
                    dialogBuilder
                            .setTitle("Thank you!")
                            .setMessage("Your order has been confirmed!")
                            .setPositiveButton("ok", null)
                            .setIcon(R.drawable.alert_success_icon)
                            .setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_search_rounded, null))
                            .show();
                    propagateListAdapter();
                }
            }
        });

        propagateListAdapter();

        loadData();
    }

    // This function is called when the activity is navigated back to from another activity
    @Override
    protected void onResume() {
        super.onResume();
        if (onResumeCalled) {
            // Clear the cart and recommended info and load the data
            itemList.clear();
            recommendedItemList.clear();
            productSkinTypes.clear();
            productIds.clear();
            loadData();
        } else {
            onResumeCalled = true;
        }
    }

    // This function retrieves and sets the data from the database
    private void loadData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Retrieve the cart information
        database.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Display the "cart empty" message if the cart is empty
                if (queryDocumentSnapshots.getDocuments().size() == 0) {
                    viewHolder.noItemsTextView.setVisibility(View.VISIBLE);
                    viewHolder.cartItemsRecyclerView.setVisibility(View.GONE);
                    viewHolder.cartTotalContainer.setVisibility(View.GONE);
                    viewHolder.recommendedItemsHeader.setVisibility(View.GONE);
                    viewHolder.recommendedItemsRecyclerView.setVisibility(View.GONE);
                    viewHolder.checkoutButton.setVisibility(View.GONE);
                } else {
                    viewHolder.noItemsTextView.setVisibility(View.GONE);
                    viewHolder.cartItemsRecyclerView.setVisibility(View.VISIBLE);
                    viewHolder.cartTotalContainer.setVisibility(View.VISIBLE);
                    viewHolder.recommendedItemsHeader.setVisibility(View.VISIBLE);
                    viewHolder.recommendedItemsRecyclerView.setVisibility(View.VISIBLE);
                    viewHolder.checkoutButton.setVisibility(View.VISIBLE);
                }
                double totalPrice = 0;
                int documentPosition = 1;

                // For each item in the cart, fetch it's data and load it
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String categoryName = (String) document.get("categoryName");
                    String productId = document.getId();
                    totalPrice += (Double.parseDouble(document.get("singleItemPrice").toString())) * Double.parseDouble(document.get("quantity").toString());
                    String itemQuantity = document.get("quantity").toString();
                    fetchItemData(categoryName, productId, itemQuantity, documentPosition, queryDocumentSnapshots.getDocuments().size());
                    documentPosition++;
                }
                String totalPriceString = "$" + String.format("%.2f", totalPrice);
                viewHolder.totalPriceTextView.setText(totalPriceString);
            }
        });
    }

    // This function fetches the data for a specific item and loads the views on the activity page
    private void fetchItemData(String cartItemCategoryName, String productId, String itemQuantity, int documentPosition, int numberOfDocuments) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(cartItemCategoryName).document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = (String) documentSnapshot.get("name");
                String brand = (String) documentSnapshot.get("brand");
                ArrayList<String> imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                String price = (String) "$" + documentSnapshot.get("price");
                String categoryName = (String) documentSnapshot.get("categoryName");
                ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
                String howToUse = (String) documentSnapshot.get("howToUse");

                productSkinTypes.addAll(skinType);
                productIds.add(productId);
                itemQuantities.put(productId, itemQuantity);

                switch (cartItemCategoryName) {
                    case "sunscreen":
                        String sunscreenType = (String) documentSnapshot.get("sunscreenType");
                        String spf = (String) documentSnapshot.get("spf");
                        itemList.add(new Sunscreen(productId, name, brand, imageNames, price, categoryName, skinType, sunscreenType, spf, howToUse));
                        break;
                    case "cleanser":
                        String cleanserType = (String) documentSnapshot.get("cleanserType");
                        String ph = (String) documentSnapshot.get("ph");
                        itemList.add(new Cleanser(productId, name, brand, imageNames, price, categoryName, skinType, ph, cleanserType, howToUse));
                        break;
                    case "moisturiser":
                        String moisturiserType = (String) documentSnapshot.get("moisturiserType");
                        String timeToUse = (String) documentSnapshot.get("timeToUse");
                        itemList.add(new Moisturiser(productId, name, brand, imageNames, price, categoryName, skinType, moisturiserType, howToUse, timeToUse));
                        break;
                }
                // If all the documents have been accessed, propagate the list adapters
                if (documentPosition == numberOfDocuments) {
                    getRecommendedItems(productSkinTypes, productIds);
                    propagateListAdapter();
                }
            }
        });
    }

    // This function calculates the recommended items to display to the user based on the items in their cart
    private void getRecommendedItems(ArrayList<String> productSkinTypes, ArrayList<String> productIds) {
        String mostCommonSkinType = findMostCommonElement(productSkinTypes);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Find two sunscreens based on the most commonly occurring skin type in the cart
        database.collection("sunscreen")
                .whereArrayContains("skinType", mostCommonSkinType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int sunscreensFound = 0;
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            String productId = (String) documentSnapshot.getId();
                            String name = (String) documentSnapshot.get("name");
                            String brand = (String) documentSnapshot.get("brand");
                            ArrayList<String> imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                            String price = (String) "$" + documentSnapshot.get("price");
                            String categoryName = (String) documentSnapshot.get("categoryName");
                            ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
                            String howToUse = (String) documentSnapshot.get("howToUse");
                            String sunscreenType = (String) documentSnapshot.get("sunscreenType");
                            String spf = (String) documentSnapshot.get("spf");

                            if (sunscreensFound > 1) {
                                break;
                            }

                            if (!productIds.contains(productId)) {
                                recommendedItemList.add(new Sunscreen(productId, name, brand, imageNames, price, categoryName, skinType, sunscreenType, spf, howToUse));
                                sunscreensFound++;
                            }
                        }
                        propagateItemAdapter();
                    }
                });

        // Find two cleansers based on the most commonly occurring skin type in the cart
        database.collection("cleanser")
                .whereArrayContains("skinType", mostCommonSkinType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int cleansersFound = 0;
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            String productId = (String) documentSnapshot.getId();
                            String name = (String) documentSnapshot.get("name");
                            String brand = (String) documentSnapshot.get("brand");
                            ArrayList<String> imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                            String price = (String) "$" + documentSnapshot.get("price");
                            String categoryName = (String) documentSnapshot.get("categoryName");
                            ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
                            String howToUse = (String) documentSnapshot.get("howToUse");
                            String cleanserType = (String) documentSnapshot.get("cleanserType");
                            String ph = (String) documentSnapshot.get("ph");

                            if (cleansersFound > 1) {
                                break;
                            }

                            if (!productIds.contains(productId)) {
                                recommendedItemList.add(new Cleanser(productId, name, brand, imageNames, price, categoryName, skinType, ph, cleanserType, howToUse));
                                cleansersFound++;
                            }
                        }
                        propagateItemAdapter();
                    }
                });

        // Find two moisturisers based on the most commonly occurring skin type in the cart
        database.collection("moisturiser")
                .whereArrayContains("skinType", mostCommonSkinType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int moisturisersFound = 0;
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            String productId = (String) documentSnapshot.getId();
                            String name = (String) documentSnapshot.get("name");
                            String brand = (String) documentSnapshot.get("brand");
                            ArrayList<String> imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                            String price = (String) "$" + documentSnapshot.get("price");
                            String categoryName = (String) documentSnapshot.get("categoryName");
                            ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
                            String howToUse = (String) documentSnapshot.get("howToUse");
                            String moisturiserType = (String) documentSnapshot.get("moisturiserType");
                            String timeToUse = (String) documentSnapshot.get("timeToUse");

                            if (moisturisersFound > 1) {
                                break;
                            }

                            if (!productIds.contains(productId)) {
                                recommendedItemList.add(new Moisturiser(productId, name, brand, imageNames, price, categoryName, skinType, moisturiserType, howToUse, timeToUse));
                                moisturisersFound++;
                            }
                        }
                        propagateItemAdapter();
                    }
                });
    }

    // This helper function finds the most commonly occurring element in a given ArrayList
    private String findMostCommonElement(ArrayList<String> arrayList) {
        Map<String, Integer> elementMap = new HashMap<>();

        // Find occurrences of each element in the ArrayList
        for (String element : arrayList) {
            elementMap.put(element, elementMap.get(element) == null ? 1 : elementMap.get(element) + 1);
        }

        String mostCommonElement = "";
        int maxValue = 0;

        // Find the most commonly occurring element based on the occurrences in the map
        for (String key : elementMap.keySet()) {
            int value = elementMap.get(key);
            if (value > maxValue) {
                maxValue = value;
                mostCommonElement = key;
            }
        }

        return mostCommonElement;
    }

    // This function clears the cart of all items
    private void clearCart() {
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

    // This function propagates the cart item list based on the data retrieved from the cart collection
    private void propagateListAdapter() {
        listAdapter = new ItemListAdapter(itemList, itemQuantities);
        listAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        double totalPrice = 0;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            totalPrice += (Double.parseDouble(document.get("singleItemPrice").toString())) * Double.parseDouble(document.get("quantity").toString());
                        }
                        String totalPriceString = "$" + String.format("%.2f", totalPrice);
                        viewHolder.totalPriceTextView.setText(totalPriceString);
                    }
                });
            }
        });
        viewHolder.cartItemsRecyclerView.setAdapter(listAdapter);
        viewHolder.cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // This function propagates the recommended item list based on the calculated recommended items to display
    private void propagateItemAdapter() {
        itemAdapter = new CompactItemAdapter(recommendedItemList, getApplicationContext(), new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewItem(position);
            }
        });
        viewHolder.recommendedItemsRecyclerView.setAdapter(itemAdapter);
        viewHolder.recommendedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    // This function defines the onclick activity to open DetailsActivity for clicking an item in the recommended list
    public void viewItem(int position) {
        Item clickedItem = recommendedItemList.get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        startActivity(intent);
    }
}