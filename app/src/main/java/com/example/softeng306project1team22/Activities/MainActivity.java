package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Category> categoryList;
    List<IItem> recentlyViewed = new ArrayList<>();
    CategoryAdapter adapter;
    CompactItemAdapter itemAdapter;
    BottomNavigationView navigationView;
    Boolean isActivityResumed = false;
    RecyclerView historyView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchRecentlyViewed();
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
        historyView = findViewById(R.id.carousel_recycler_view);

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

        //Set layout managers!
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

    private void fetchRecentlyViewed() {
        System.out.println("Made it into the fetchRecent");
        DataProvider.fetchRecentlyViewed("recently-viewed").thenAccept(items -> {
            System.out.println("data received");
            recentlyViewed.clear();
            recentlyViewed.addAll(items);
            System.out.println(recentlyViewed.get(0).getName());
            itemAdapter = new CompactItemAdapter(recentlyViewed, getApplicationContext(), new CategoryAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    viewItem(position);
                }
            });


            //Set adapters that recyclerViews will use
            historyView.setAdapter(itemAdapter);
            historyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
        IItem clickedItem = recentlyViewed.get(position);
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