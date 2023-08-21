package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private class ViewHolder {
        Button backButton;
        RecyclerView cartItemsRecyclerView;
        RecyclerView recommendedItemsRecyclerView;
        TextView totalPriceTextView;
        Button checkoutButton;

        public ViewHolder() {
            backButton = findViewById(R.id.back_button);
            cartItemsRecyclerView = findViewById(R.id.rvCartItems);
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

        viewHolder.checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemList.size() > 0) {
                    itemList.clear();
                    productSkinTypes.clear();
                    productIds.clear();
                    clearCart();
                    viewHolder.totalPriceTextView.setText("$0.00");
                    Toast toast = Toast.makeText(CartActivity.this, "Thank you for your purchase!", Toast.LENGTH_LONG);
                    toast.show();
                    propagateListAdapter();
                }
            }
        });

        propagateListAdapter();

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onResumeCalled) {
            itemList.clear();
            recommendedItemList.clear();
            productSkinTypes.clear();
            productIds.clear();
            loadData();
        } else {
            onResumeCalled = true;
        }
    }

    private void loadData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                double totalPrice = 0;
                int documentPosition = 1;
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
                if (documentPosition == numberOfDocuments) {
                    getRecommendedItems(productSkinTypes, productIds);
                    propagateListAdapter();
                }
            }
        });
    }

    private void getRecommendedItems(ArrayList<String> productSkinTypes, ArrayList<String> productIds) {
        String mostCommonSkinType = findMostCommonElement(productSkinTypes);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

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

    private String findMostCommonElement(ArrayList<String> arrayList) {
        Map<String, Integer> elementMap = new HashMap<>();

        for (String element : arrayList) {
            elementMap.put(element, elementMap.get(element) == null ? 1 : elementMap.get(element) + 1);
        }

        String mostCommonElement = "";
        int maxValue = 0;

        for (String key : elementMap.keySet()) {
            int value = elementMap.get(key);
            if (value > maxValue) {
                maxValue = value;
                mostCommonElement = key;
            }
        }

        return mostCommonElement;
    }

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

    public void viewItem(int position) {
        Item clickedItem = recommendedItemList.get(position);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        startActivity(intent);
    }
}