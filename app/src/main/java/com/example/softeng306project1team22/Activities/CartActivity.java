package com.example.softeng306project1team22.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private class ViewHolder {
        RecyclerView recommendedItemsRecyclerView;

        public ViewHolder() {
            recommendedItemsRecyclerView = findViewById(R.id.rvRecommendedItems);
        }
    }

    private ViewHolder viewHolder;

    private ArrayList<IItem> itemList;

    private ItemListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        viewHolder = new ViewHolder();

        fetchCartData();
    }

    private void fetchCartData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> itemMap = document.getData();
                        String id = document.getId();
                        String cartItemCategory = (String) itemMap.get("categoryName");

                        database.collection(cartItemCategory).document(id).get().addOnSuccessListener(documentSnapshot -> {
                            String name = (String) documentSnapshot.get("name");
                            String brand = (String) documentSnapshot.get("brand");
                            ArrayList<String> imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                            String price = (String) documentSnapshot.get("price");
                            String categoryName = (String) documentSnapshot.get("categoryName");
                            ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
                            String howToUse = (String) documentSnapshot.get("howToUse");

                            switch (cartItemCategory) {
                                case "Sunscreen":
                                    String sunscreenType = (String) documentSnapshot.get("sunscreenType");
                                    String spf = (String) documentSnapshot.get("spf");
                                    itemList.add(new Sunscreen(id, name, brand, imageNames, price, categoryName, skinType, sunscreenType, spf, howToUse));
                                    break;
                                case "Cleanser":
                                    String cleanserType = (String) documentSnapshot.get("cleanserType");
                                    String ph = (String) documentSnapshot.get("ph");
                                    itemList.add(new Cleanser(id, name, brand, imageNames, price, categoryName, skinType, ph, cleanserType, howToUse));
                                    break;
                                case "Moisturiser":
                                    String moisturiserType = (String) documentSnapshot.get("moisturiserType");
                                    String timeToUse = (String) documentSnapshot.get("timeToUse");
                                    itemList.add(new Moisturiser(id, name, brand, imageNames, price, categoryName, skinType, moisturiserType, howToUse, timeToUse));
                                    break;
                            }
                        });
                    }
                    propagateListAdapter();
                }
            }
        });
    }

    private void propagateListAdapter() {
        listAdapter = new ItemListAdapter(itemList);
        viewHolder.recommendedItemsRecyclerView.setAdapter(listAdapter);
        viewHolder.recommendedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
