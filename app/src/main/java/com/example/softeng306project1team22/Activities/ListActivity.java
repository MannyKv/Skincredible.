package com.example.softeng306project1team22.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ViewHolder vh;
    ArrayList<IItem> itemList;
    ItemListAdapter listAdapter;
    Category category;
    private DataRepository dataRepository = new DataRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // initialising views and buttons
        vh = new ViewHolder();
        vh.backButton.setOnClickListener(v -> finish());
        setNavigationViewLinks();
        itemList = new ArrayList<>();
        // obtaining category data so that the appropriate details can be displayed.
        Intent intent = getIntent();
        category = new Category(intent.getStringExtra("categoryName"), intent.getStringExtra("categoryId"), intent.getStringExtra("categoryIconName"));
        populateCategoryDetails(category);
        fetchItemListData(category);
    }

    /**
     * This method sets the navigation view links for the ListActivity.
     */
    private void setNavigationViewLinks() {
        vh.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
                finish();
            } else if (itemId == R.id.search) {
                startActivity(new Intent(ListActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(ListActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }


    /**
     * This method utilises the data repository in fetching required items from the database, and
     * adding them to a list to be displayed on the screen, by propogating the recycler view adapter
     * for the item list.
     *
     * @param category a specific category who's items are to be displayed.
     */
    private void fetchItemListData(Category category) {
        switch (category.getName()) {
            case "Sunscreen":
                dataRepository.fetchFromCollection("sunscreen", Sunscreen.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;

            case "Cleanser":
                dataRepository.fetchFromCollection("cleanser", Cleanser.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;

            case "Moisturiser":
                dataRepository.fetchFromCollection("moisturiser", Moisturiser.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;
        }
    }

    /**
     * This method propagates the adapter for the recycler view with a list of items to be displayed.
     * It additionally sets the layout manager of the item recycler view list to be different,
     * depending on what category the item is.
     *
     * @param categoryId the ID of the category who's data is to be displayed.
     */
    private void propagateListAdapter(String categoryId) {
        listAdapter = new ItemListAdapter(itemList, categoryId);
        vh.itemRecyclerView.setAdapter(listAdapter);
        switch (categoryId) {
            case "cle":
                vh.itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case "mos":
                vh.itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            default:
                vh.itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

    /**
     * This method updates the UI header with the appropriate category name and icon.
     *
     * @param category the category of the items displayed on this screen.
     */
    private void populateCategoryDetails(Category category) {
        vh.categoryNameHeader.setText(category.getName());

        int i = this.getResources().getIdentifier(
                category.getImageName(), "drawable",
                this.getPackageName());
        vh.categoryIcon.setImageResource(i);
    }

    /**
     * The ViewHolder class allows for activity views to be held in a single place for access
     * within the ListActivity class.
     */
    private class ViewHolder {
        TextView categoryNameHeader;
        ImageView categoryIcon;
        RecyclerView itemRecyclerView;
        Button backButton;
        BottomNavigationView navigationView;

        public ViewHolder() {
            categoryNameHeader = findViewById(R.id.category_name);
            categoryIcon = findViewById(R.id.category_icon);
            itemRecyclerView = findViewById(R.id.rvItems);
            backButton = findViewById(R.id.back_button);
            navigationView = findViewById(R.id.nav_buttons);
        }
    }
}