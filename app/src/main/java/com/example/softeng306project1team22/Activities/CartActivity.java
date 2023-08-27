package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.CartAdapter;
import com.example.softeng306project1team22.Adapters.CategoryAdapter;
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.Data.DataProvider;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private class ViewHolder {
        TextView noItemsTextView, recommendedItemsHeader;
        RecyclerView cartItemsRecyclerView;
        CardView cartTotalContainer;
        RecyclerView recommendedItemsRecyclerView;
        TextView totalPriceTextView;
        Button checkoutButton;
        BottomNavigationView navigationView;

        public ViewHolder() {
            noItemsTextView = findViewById(R.id.noItemsTextView);
            recommendedItemsHeader = findViewById(R.id.recommendedItemsHeader);
            cartItemsRecyclerView = findViewById(R.id.rvCartItems);
            cartTotalContainer = findViewById(R.id.cartTotalContainer);
            recommendedItemsRecyclerView = findViewById(R.id.rvRecommendedItems);
            totalPriceTextView = findViewById(R.id.totalPriceTextView);
            checkoutButton = findViewById(R.id.checkoutButton);
            navigationView = findViewById(R.id.nav_buttons);
        }
    }

    private ViewHolder viewHolder;

    private ArrayList<IItem> itemList;

    private ArrayList<IItem> recommendedItemList;

    private CartAdapter cartAdapter;

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
                    propagateCartAdapter();
                }
            }
        });

        propagateCartAdapter();

        loadData();

        setNavigationViewLinks();
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

    // This function sets the navigation links for the navigation bar
    private void setNavigationViewLinks() {
        viewHolder.navigationView.setSelectedItemId(R.id.cart);
        viewHolder.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finish();
            } else if (itemId == R.id.search) {
                startActivity(new Intent(CartActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(CartActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }

    // This function retrieves and sets the data from the database
    private void loadData() {
        // Retrieve the cart information
        DataProvider.getCartDocuments().thenAccept(itemsMap -> {

            // Display the "cart empty" message if the cart is empty
            if (itemsMap.size() == 0) {
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

            // For each item in the cart, fetch it's data and load it
            for (IItem i : itemsMap.keySet()) {
                totalPrice += (Double.parseDouble(i.getPrice())) * Double.parseDouble(itemsMap.get(i));
                productSkinTypes.addAll(i.getSkinType());
                productIds.add(i.getId());
                itemQuantities.put(i.getId(), itemsMap.get(i));
                itemList.add(i);
                if (itemList.size() == itemsMap.size()) {
                    getRecommendedItems(productSkinTypes, productIds);
                    propagateCartAdapter();
                }
            }
            String totalPriceString = "$" + String.format("%.2f", totalPrice);
            viewHolder.totalPriceTextView.setText(totalPriceString);
        });
    }


    // This function calculates the recommended items to display to the user based on the items in their cart
    private void getRecommendedItems(ArrayList<String> productSkinTypes, ArrayList<String> productIds) {
        String mostCommonSkinType = findMostCommonElement(productSkinTypes);

        // Find two sunscreens based on the most commonly occurring skin type in the cart

        DataProvider.getReccomended("sunscreen", Sunscreen.class, mostCommonSkinType).thenAccept(item -> {
            int sunscreensFound = 0;
            for (IItem singleItem : item) {
                if (sunscreensFound > 1) {
                    break;
                }
                if (!productIds.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    sunscreensFound++;
                }
            }
            propagateItemAdapter();
        });

        DataProvider.getReccomended("cleanser", Cleanser.class, mostCommonSkinType).thenAccept(item -> {
            int cleanserFound = 0;
            for (IItem singleItem : item) {
                if (cleanserFound > 1) {
                    break;
                }
                if (!productIds.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    cleanserFound++;
                }
            }
            propagateItemAdapter();
        });

        DataProvider.getReccomended("moisturiser", Moisturiser.class, mostCommonSkinType).thenAccept(item -> {
            int moisturiserFound = 0;
            System.out.println("This is number of recc items: " + item.size());
            for (IItem singleItem : item) {
                if (moisturiserFound > 1) {
                    break;
                }
                if (!productIds.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    moisturiserFound++;
                }
            }
            propagateItemAdapter();
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
        DataProvider.clearCart();
    }

    // This function propagates the cart item list based on the data retrieved from the cart collection
    private void propagateCartAdapter() {
        cartAdapter = new CartAdapter(itemList, itemQuantities);
        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                            viewHolder.noItemsTextView.setVisibility(View.VISIBLE);
                            viewHolder.cartItemsRecyclerView.setVisibility(View.GONE);
                            viewHolder.cartTotalContainer.setVisibility(View.GONE);
                            viewHolder.recommendedItemsHeader.setVisibility(View.GONE);
                            viewHolder.recommendedItemsRecyclerView.setVisibility(View.GONE);
                            viewHolder.checkoutButton.setVisibility(View.GONE);
                        }
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
        viewHolder.cartItemsRecyclerView.setAdapter(cartAdapter);
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
        IItem clickedItem = recommendedItemList.get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        startActivity(intent);
    }
}