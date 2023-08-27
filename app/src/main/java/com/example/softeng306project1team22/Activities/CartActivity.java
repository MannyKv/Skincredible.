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
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Data.IDataRepository;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    /**
     * This class stores the views that are present on the XML page
     */
    private class ViewHolder {
        TextView noItemsTextView, recommendedItemsHeader;
        RecyclerView cartItemsRecyclerView;
        CardView cartTotalContainer;
        RecyclerView recommendedItemsRecyclerView;
        TextView totalPriceTextView;
        Button checkoutButton;
        BottomNavigationView navigationView;

        /**
         * The constructor finds each view by its ID in the corresponding XML file
         */
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

    private Map<String, String> itemQuantities;

    private ArrayList<String> productSkinTypes;

    private ArrayList<String> productIdsInCart;
    private IDataRepository dataRepository = new DataRepository();
    private boolean onResumeCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialise the lists and variables storing cart information
        onResumeCalled = false;
        viewHolder = new ViewHolder();
        itemList = new ArrayList<>();
        recommendedItemList = new ArrayList<>();
        itemQuantities = new HashMap<>();
        productSkinTypes = new ArrayList<>();
        productIdsInCart = new ArrayList<>();


        // Setting onclick functionality for the checkout button
        viewHolder.checkoutButton.setOnClickListener(v -> {
            if (itemList.size() > 0) {
                // Clear all the cart information and set the total price to $0.00
                itemList.clear();
                productSkinTypes.clear();
                productIdsInCart.clear();
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
            productIdsInCart.clear();
            loadData();
        } else {
            onResumeCalled = true;
        }
    }

    /**
     * This function sets the navigation links for the navigation bar
     */
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
            }
            return true;
        });
    }

    /**
     * This function retrieves and sets the data from the database
     */
    private void loadData() {
        // Retrieve the cart information
        dataRepository.getCartDocuments().thenAccept(itemsMap -> {
            // Display the "cart empty" message if the cart is empty
            if (itemsMap.isEmpty()) {
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

            // For each item in the cart, fetch its data and load it
            for (IItem i : itemsMap.keySet()) {
                totalPrice += (Double.parseDouble(i.getPrice())) * Double.parseDouble(itemsMap.get(i));
                productSkinTypes.addAll(i.getSkinType());
                productIdsInCart.add(i.getId());
                itemQuantities.put(i.getId(), itemsMap.get(i));
                itemList.add(i);
                if (itemList.size() == itemsMap.size()) {
                    getRecommendedItems(productSkinTypes, productIdsInCart);
                    propagateCartAdapter();
                }
            }
            String totalPriceString = "$" + String.format("%.2f", totalPrice);
            viewHolder.totalPriceTextView.setText(totalPriceString);
        });
    }

    /**
     * This function calculates the recommended items to display to the user based on the items in their cart
     *
     * @param productSkinTypes An ArrayList containing the skin types products are made for
     * @param productIdsInCart An ArrayList of the IDs of products in the cart
     */
    private void getRecommendedItems(ArrayList<String> productSkinTypes, ArrayList<String> productIdsInCart) {
        String mostCommonSkinType = findMostCommonElement(productSkinTypes);

        // Find two sunscreens based on the most commonly occurring skin type in the cart
        dataRepository.getReccomended("sunscreen", Sunscreen.class, mostCommonSkinType).thenAccept(item -> {
            int sunscreensFound = 0;
            for (IItem singleItem : item) {
                if (sunscreensFound > 1) {
                    break;
                }
                if (!productIdsInCart.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    sunscreensFound++;
                }
            }
            propagateItemAdapter();
        });

        // Find two cleansers based on the most commonly occurring skin type in the cart
        dataRepository.getReccomended("cleanser", Cleanser.class, mostCommonSkinType).thenAccept(item -> {
            int cleanserFound = 0;
            for (IItem singleItem : item) {
                if (cleanserFound > 1) {
                    break;
                }
                if (!productIdsInCart.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    cleanserFound++;
                }
            }
            propagateItemAdapter();
        });

        // Find two moisturisers based on the most commonly occurring skin type in the cart
        dataRepository.getReccomended("moisturiser", Moisturiser.class, mostCommonSkinType).thenAccept(item -> {
            int moisturiserFound = 0;
            for (IItem singleItem : item) {
                if (moisturiserFound > 1) {
                    break;
                }
                if (!productIdsInCart.contains(singleItem.getId())) {
                    recommendedItemList.add(singleItem);
                    moisturiserFound++;
                }
            }
            propagateItemAdapter();
        });
    }

    /**
     * This helper function finds the most commonly occurring element in a given ArrayList
     *
     * @param elementsArrayList An ArrayList containing elements to find the most common from
     * @return A string, the most commonly occurring element in the input ArrayList
     */
    private String findMostCommonElement(ArrayList<String> elementsArrayList) {
        Map<String, Integer> elementMap = new HashMap<>();

        // Find occurrences of each element in the ArrayList
        for (String element : elementsArrayList) {
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

    /**
     * This function clears the cart of all items
     */
    private void clearCart() {
        dataRepository.clearCart();
    }

    /**
     * This function propagates the cart item list based on the data retrieved from the cart collection
     */
    private void propagateCartAdapter() {
        CartAdapter cartAdapter = new CartAdapter(itemList, itemQuantities);
        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            // Update the total price displayed whenever an item is added or removed from the cart
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {

                dataRepository.getCartDocuments().thenAccept(itemsMap -> {

                    if (itemsMap.isEmpty()) {
                        viewHolder.noItemsTextView.setVisibility(View.VISIBLE);
                        viewHolder.cartItemsRecyclerView.setVisibility(View.GONE);
                        viewHolder.cartTotalContainer.setVisibility(View.GONE);
                        viewHolder.recommendedItemsHeader.setVisibility(View.GONE);
                        viewHolder.recommendedItemsRecyclerView.setVisibility(View.GONE);
                        viewHolder.checkoutButton.setVisibility(View.GONE);
                    }
                    double totalPrice = 0;
                    for (IItem i : itemsMap.keySet()) {
                        totalPrice += (Double.parseDouble(i.getPrice())) * Double.parseDouble(itemsMap.get(i));
                    }
                    String totalPriceString = "$" + String.format("%.2f", totalPrice);
                    viewHolder.totalPriceTextView.setText(totalPriceString);

                });
            }

        });
        viewHolder.cartItemsRecyclerView.setAdapter(cartAdapter);
        viewHolder.cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This function propagates the recommended item list based on the calculated recommended items to display
     */
    private void propagateItemAdapter() {
        CompactItemAdapter itemAdapter = new CompactItemAdapter(recommendedItemList, getApplicationContext(), (view, position) -> viewItem(position));
        viewHolder.recommendedItemsRecyclerView.setAdapter(itemAdapter);
        viewHolder.recommendedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * This function defines the onclick activity to open DetailsActivity for clicking an item in the recommended list
     *
     * @param position the position of the item clicked in the RecyclerView
     */
    public void viewItem(int position) {
        IItem clickedItem = recommendedItemList.get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        startActivity(intent);
    }
}