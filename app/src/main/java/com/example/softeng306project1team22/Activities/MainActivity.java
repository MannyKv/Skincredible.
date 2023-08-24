package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.CategoryAdapter;
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.DataProvider;
import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.Item;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Category> categoryList;
    List<Item> recentlyViewed = new ArrayList<>();
    CategoryAdapter adapter;
    CompactItemAdapter itemAdapter;
    BottomNavigationView navigationView;
    Boolean isActivityResumed = false;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityResumed = true;
        setContentView(R.layout.activity_main);
        SearchView searchBar = findViewById(R.id.searchB);
        searchBar.setOnQueryTextListener(null);
        searchBar.setIconifiedByDefault(true);
        searchBar.setQueryHint("Search Items");
        navigationView = findViewById(R.id.nav_buttons);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        //Create recyclerView instances for layout
        RecyclerView recyclerView = findViewById(R.id.category);
        RecyclerView historyView = findViewById(R.id.carousel_recycler_view);

        //Fetch All data required
        DataProvider.fetchCategoryData().thenAccept(categories -> {
            categoryList = new ArrayList<>(DataProvider.getCategories());
            adapter = new CategoryAdapter(categoryList, getApplicationContext(), new CategoryAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    System.out.println("Made it to onItemClick : " + position);
                    viewCategory(position);
                }
            });
            recyclerView.setAdapter(adapter);
        });
        fetchRecentlyViewed();

        //Create Adapters for different views
        itemAdapter = new CompactItemAdapter(recentlyViewed, getApplicationContext(), new CategoryAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                viewItem(position);
            }
        });


        //Set adapters that recyclerViews will use
        historyView.setAdapter(itemAdapter);


        //Set layout managers!
        historyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set nav view links
        setNavigationViewLinks();
    }

    // This function sets the navigation links for the navigation bar
    private void setNavigationViewLinks() {
        navigationView.setSelectedItemId(R.id.home);
        navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            } else if (itemId == R.id.search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }

    protected void onResume() {
        super.onResume();

        if (!isActivityResumed) {
            fetchRecentlyViewed();
        }
        isActivityResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isActivityResumed = false; // Mark the activity as paused
    }

  /*  private void fetchCategoryData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("category");

        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Data retrieved successfully");
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                categoryList.add(new Category(document.get("name", String.class), document.getId(), document.get("imageName", String.class)));
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            System.out.println("Category Data Retrieval Failure");
        });
    }*/

    private void fetchRecentlyViewed() {
        recentlyViewed.clear();
        //DISCLAIMER! TEST DATA
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef = dbs.collection("recently-viewed");

        colRef.orderBy("timeAdded", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                String id = documentSnapshot.getString("itemId");
                CollectionReference itemRef;
                if (id.contains("sun")) {
                    itemRef = dbs.collection("sunscreen");
                } else if (id.contains("mos")) {
                    itemRef = dbs.collection("moisturiser");
                } else {
                    itemRef = dbs.collection("cleanser");
                }

                itemRef.document(id).get().addOnSuccessListener(referencedDocSnapshot -> {
                    if (referencedDocSnapshot.getString("categoryName").equals("Sunscreen")) {
                        Sunscreen sunscreen = referencedDocSnapshot.toObject(Sunscreen.class);
                        recentlyViewed.add(sunscreen);

                    } else if (referencedDocSnapshot.getString("categoryName").equals("Cleanser")) {
                        Cleanser cleanser = referencedDocSnapshot.toObject(Cleanser.class);
                        System.out.println("this is a real class: " + cleanser.getName());
                        recentlyViewed.add(cleanser);
                    } else if (referencedDocSnapshot.getString("categoryName").equals("Moisturiser")) {
                        Moisturiser moisturiser = referencedDocSnapshot.toObject(Moisturiser.class);
                        recentlyViewed.add(moisturiser);
                    }
                    itemAdapter.notifyDataSetChanged();
                });
            }

            itemAdapter.notifyDataSetChanged();
        });
    }

    public void viewCategory(int position) {
        Category clickedCategory = categoryList.get(position);
        // Create intent and pass data here
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("categoryId", clickedCategory.getId());
        // Add any other relevant data to the intent
        startActivity(intent);
    }

    public void viewItem(int position) {
        Item clickedItem = recentlyViewed.get(position);
        // Create intent and pass data here
        Intent intent = new Intent(this, DetailsActivity.class);
        // Add any other relevant data to the intent
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        this.startActivity(intent);
    }

    public void searchItem() {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
}