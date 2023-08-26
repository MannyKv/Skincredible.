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
import com.example.softeng306project1team22.DataProvider;
import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ViewHolder vh;
    Category category;
    FirebaseFirestore db;
    ArrayList<IItem> itemList;
    ItemListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        vh = new ViewHolder();
        vh.backButton.setOnClickListener(v -> finish());
        setNavigationViewLinks();
        itemList = new ArrayList<>();
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        fetchCategoryData(categoryId);
    }

    // This function sets the navigation links for the navigation bar
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

    private void fetchCategoryData(String categoryId) {
        category = DataProvider.getCategoryById(categoryId);
        populateCategoryDetails(category);
        fetchItemListData(category.getName());

    }

    private void fetchItemListData(String categoryName) {
        db = FirebaseFirestore.getInstance();
        switch (categoryName) {
            case "Sunscreen":

                DataProvider.retrieveFromCollection("sunscreen", Sunscreen.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;
            case "Cleanser":

                DataProvider.retrieveFromCollection("cleanser", Cleanser.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;
            case "Moisturiser":

                DataProvider.retrieveFromCollection("moisturiser", Moisturiser.class).thenAccept(item -> {
                    itemList.addAll(item);
                    propagateListAdapter(category.getId());
                });
                break;
        }
    }

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

    private void populateCategoryDetails(Category category) {
        vh.categoryNameHeader.setText(category.getName());

        int i = this.getResources().getIdentifier(
                category.getImageName(), "drawable",
                this.getPackageName());

        vh.categoryIcon.setImageResource(i);
    }

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