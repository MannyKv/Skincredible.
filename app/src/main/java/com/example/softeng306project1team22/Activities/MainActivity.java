package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.CategoryAdapter;
import com.example.softeng306project1team22.Adapters.CompactItemAdapter;
import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Data.IDataRepository;
import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The ViewHolder class allows for activity views to be held in a single place for access
     * within the MainActivity class.
     */
    private class ViewHolder {
        CardView searchBar;
        RecyclerView recyclerView;
        RecyclerView historyView;
        BottomNavigationView navigationView;

        public ViewHolder() {
            searchBar = findViewById(R.id.search);
            recyclerView = findViewById(R.id.mCategory);
            historyView = findViewById(R.id.carousel_recycler_view);
            navigationView = findViewById(R.id.nav_buttons);
        }
    }

    List<Category> categoryList;
    List<IItem> recentlyViewed = new ArrayList<>();
    CategoryAdapter adapter;
    CompactItemAdapter itemAdapter;
    ViewHolder viewHolder;
    Boolean isActivityResumed = false;
    private IDataRepository dataRepository = new DataRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchRecentlyViewed();
        isActivityResumed = true;
        setContentView(R.layout.activity_main);

        viewHolder = new ViewHolder();
        viewHolder.searchBar.setOnClickListener(v -> searchItem());
        
        //Fetch All data required
        dataRepository.getCategories().thenAccept(categories -> {
            categoryList = new ArrayList<>(categories);
            adapter = new CategoryAdapter(categoryList, getApplicationContext(), (view, position) -> viewCategory(position));
            viewHolder.recyclerView.setAdapter(adapter);
        });

        //Set layout managers!
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set nav view links
        setNavigationViewLinks();
    }

    /**
     * This function sets the navigation links for the navigation bar
     */

    private void setNavigationViewLinks() {
        viewHolder.navigationView.setSelectedItemId(R.id.home);
        viewHolder.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }

    /**
     * Called when the activity is resumed
     */
    protected void onResume() {
        super.onResume();

        if (!isActivityResumed) {
            fetchRecentlyViewed();
        }
        isActivityResumed = true;
    }

    /**
     * called when a new activity is started
     */
    @Override
    protected void onPause() {
        super.onPause();

        isActivityResumed = false; // Mark the activity as paused
    }

    /**
     * Called when the activity is started
     * makes a database call from the DataProvider class and awaits a return.
     * Creates the adapter and binds the on click and adapter to the recycler view
     */
    private void fetchRecentlyViewed() {
        dataRepository.fetchRecentlyViewed("recently-viewed").thenAccept(items -> {
            recentlyViewed.clear();
            recentlyViewed.addAll(items);
            itemAdapter = new CompactItemAdapter(recentlyViewed, getApplicationContext(), (view, position) -> viewItem(position));

            //Set adapters that recyclerViews will use
            viewHolder.historyView.setAdapter(itemAdapter);
            viewHolder.historyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        });
    }

    /**
     * Passes an intent to view a category based on the clicked category position
     *
     * @param position the position of the clicked category
     */
    public void viewCategory(int position) {
        Category clickedCategory = categoryList.get(position);
        // Create intent and pass data here
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("categoryId", clickedCategory.getId());
        intent.putExtra("categoryName", clickedCategory.getName());
        intent.putExtra("categoryIconName", clickedCategory.getImageName());
        // Add any other relevant data to the intent
        startActivity(intent);
    }

    /**
     * passes an intent to view an item based on the clicked item position
     *
     * @param position the position of the recently viwewed item list
     */
    public void viewItem(int position) {
        IItem clickedItem = recentlyViewed.get(position);
        // Create intent and pass data here
        Intent intent = new Intent(this, DetailsActivity.class);
        // Add any other relevant data to the intent
        intent.putExtra("productCategory", clickedItem.getCategoryName());
        intent.putExtra("productId", clickedItem.getId());
        this.startActivity(intent);
    }

    /**
     * opens the search item activity via intents
     */
    public void searchItem() {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
}