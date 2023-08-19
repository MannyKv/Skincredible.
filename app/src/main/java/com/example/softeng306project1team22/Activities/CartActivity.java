package com.example.softeng306project1team22.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private class ViewHolder {
        Button backButton;
        RecyclerView recommendedItemsRecyclerView;
        TextView totalPriceTextView;


        public ViewHolder() {
            backButton = findViewById(R.id.back_button);
            recommendedItemsRecyclerView = findViewById(R.id.rvCartItems);
            totalPriceTextView = findViewById(R.id.totalPriceTextView);
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

        viewHolder.backButton.setOnClickListener(v -> finish());

        itemList = new ArrayList<>();

        loadData();

    }

    private void loadData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                double totalPrice = 0;
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String categoryName = (String) document.get("categoryName");
                    String productId = document.getId();
                    totalPrice += (Double.parseDouble(document.get("singleItemPrice").toString())) * Double.parseDouble(document.get("quantity").toString());

                    fetchItemData(categoryName, productId);
                }
                String totalPriceString = "$" + String.format("%.2f", totalPrice);
                viewHolder.totalPriceTextView.setText(totalPriceString);
            }
        });
    }

    private void fetchItemData(String cartItemCategoryName, String productId) {
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
                propagateListAdapter();
            }
        });
    }

    private void propagateListAdapter() {
        listAdapter = new ItemListAdapter(itemList);
        viewHolder.recommendedItemsRecyclerView.setAdapter(listAdapter);
        viewHolder.recommendedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}