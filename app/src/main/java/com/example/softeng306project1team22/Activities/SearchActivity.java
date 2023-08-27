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
import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Data.IDataRepository;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    /**
     * The ViewHolder class allows for activity views to be held in a single place for access
     * within the SearchActivity class.
     */
    private class ViewHolder {
        BottomNavigationView navigationView;
        RecyclerView recyclerView;
        TextView notFoundMsg;
        SearchView searchView;

        public ViewHolder() {
            notFoundMsg = findViewById(R.id.not_found);
            navigationView = findViewById(R.id.nav_buttons);
            recyclerView = findViewById(R.id.search_recycled);
            searchView = findViewById(R.id.searchView);
        }
    }

    ItemListAdapter itemAdapter;
    List<IItem> allItems;
    List<IItem> filtered;
    ViewHolder viewHolder;
    private IDataRepository dataRepository = new DataRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewHolder = new ViewHolder();
        viewHolder.searchView.setIconifiedByDefault(false);
        viewHolder.searchView.setQueryHint("search items . . .");
        viewHolder.searchView.clearFocus();
        viewHolder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        dataRepository.getAllItems().thenAccept(items -> {
            allItems = new ArrayList<>(items);
            filtered = new ArrayList<>(items);
            itemAdapter = new ItemListAdapter(filtered);
            viewHolder.recyclerView.setAdapter(itemAdapter);
        });

        viewHolder.searchView.setIconified(false);
        viewHolder.searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewHolder.searchView, InputMethodManager.SHOW_IMPLICIT);
        //Bind the recycler view layout manager
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setNavigationViewLinks();

    }

    /**
     * Navigates to other activities
     */
    private void setNavigationViewLinks() {
        viewHolder.navigationView.setSelectedItemId(R.id.search);
        viewHolder.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
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
     * @param filterBy the string to filter the list by
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
            viewHolder.notFoundMsg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notFoundMsg.setVisibility(View.GONE);
        }
        itemAdapter.notifyDataSetChanged(); //notify the adapter the data set has just changed
    }

}