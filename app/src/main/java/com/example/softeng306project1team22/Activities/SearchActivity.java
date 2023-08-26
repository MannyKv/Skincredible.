package com.example.softeng306project1team22.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.DataProvider;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ItemListAdapter itemAdapter;
    List<IItem> allItems;
    List<IItem> filtered;
    BottomNavigationView navigationView;
    RecyclerView recyclerView;
    TextView notFoundMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        notFoundMsg = findViewById(R.id.not_found);
        SearchView searchView = findViewById(R.id.searchView);
        navigationView = findViewById(R.id.nav_buttons);
        recyclerView = findViewById(R.id.search_recycled);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Items");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //Query and pass in the string to the filter real time
                setFiltered(newText);
                return false;
            }
        });
        //Calls the get all items method in data provider and awaits response
        DataProvider.getAllItems().thenAccept(items -> {
            allItems = new ArrayList<>(items);
            filtered = new ArrayList<>(items);
            itemAdapter = new ItemListAdapter(filtered);
            recyclerView.setAdapter(itemAdapter);
        });

        searchView.setIconified(false);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        //Bind the recycler view layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setNavigationViewLinks();

    }

    // This function sets the navigation links for the navigation bar
    private void setNavigationViewLinks() {
        navigationView.setSelectedItemId(R.id.search);
        navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            } else if (itemId == R.id.search) {
                startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }


    /**
     * Sets the filter based on the string in the search query
     *
     * @param filterBy
     */
    protected void setFiltered(String filterBy) {
        filtered.clear(); //clear the current filtered items
        //filter the items in the allItems list
        for (int x = 0; x < allItems.size(); x++) {
            if (allItems.get(x).getName().toLowerCase().contains(filterBy.toLowerCase())) {
                filtered.add(allItems.get(x));

            }
        }
        if (filtered.size() == 0) { //if there is no filtered item display error
            notFoundMsg.setVisibility(View.VISIBLE);
        } else {
            notFoundMsg.setVisibility(View.GONE);
        }
        itemAdapter.notifyDataSetChanged(); //notify the adapter the data set has just changed
    }

}