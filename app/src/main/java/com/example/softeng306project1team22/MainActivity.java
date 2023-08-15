package com.example.softeng306project1team22;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.CategoryAdapter;
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.Item;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Category> categoryList = new ArrayList<>();
    List<Item> recentlyViewed = new ArrayList<>();
    CategoryAdapter adapter;
    CompactItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create recyclerView instances for layout
        RecyclerView recyclerView = findViewById(R.id.category);
        RecyclerView historyView = findViewById(R.id.carousel_recycler_view);

        //Fetch All data required
        fetchCategoryData();
        fetchRecentlyViewed();

        //Create Adapters for different views
        itemAdapter = new CompactItemAdapter(recentlyViewed, getApplicationContext());
        adapter = new CategoryAdapter(categoryList, getApplicationContext(), new CategoryAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                System.out.println("Made it to onItemClick : " + position);
                viewCategory(position);
            }
        });

        //Set adapters that recyclerViews will use
        historyView.setAdapter(itemAdapter);
        recyclerView.setAdapter(adapter);


        //Set layout managers!
        historyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void fetchCategoryData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("category");

        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Data retrieved successfully");
            categoryList.addAll(queryDocumentSnapshots.toObjects(Category.class));
            adapter.notifyDataSetChanged();

        }).addOnFailureListener(e -> {
            System.out.println("Category Data Retrieval Failure");
        });
    }

    public void fetchRecentlyViewed() {

        //DISCLAIMER! TEST DATA
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef = dbs.collection("cleanser");

        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Data retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                if (documentSnapshot.getString("categoryName").equals("sunscreen")) {
                    Sunscreen sunscreen = documentSnapshot.toObject(Sunscreen.class);
                    recentlyViewed.add(sunscreen);
                } else if (documentSnapshot.getString("categoryName").equals("cleanser")) {
                    Cleanser cleanser = documentSnapshot.toObject(Cleanser.class);
                    //System.out.println("this is a real class: " + cleanser.getName());
                    recentlyViewed.add(cleanser);
                } else if (documentSnapshot.getString("categoryName").equals("moisturiser")) {
                    Moisturiser moisturiser = documentSnapshot.toObject(Moisturiser.class);
                    recentlyViewed.add(moisturiser);
                }
            }
            itemAdapter.notifyDataSetChanged();
        });
    }

    public void viewCategory(int position) {
        Category clickedCategory = categoryList.get(position);
        System.out.println("WE MADE IT HERE BABY : " + position);
        // Create intent and pass data here
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("categoryName", clickedCategory.getName());
        // Add any other relevant data to the intent
        this.startActivity(intent);
    }


}