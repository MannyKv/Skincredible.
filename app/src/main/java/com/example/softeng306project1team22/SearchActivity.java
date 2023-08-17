package com.example.softeng306project1team22;

import static com.example.softeng306project1team22.TestRepo.getAllItems;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.IItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ItemListAdapter itemAdapter;
    List<IItem> allItems;
    List<IItem> filtered;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search Items");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setFiltered(newText);
                return false;
            }
        });
        recyclerView = findViewById(R.id.search_recycled);
        retrieveAllItems();
        itemAdapter = new ItemListAdapter(filtered);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter.notifyDataSetChanged();

    }

    protected void retrieveAllItems() {
        allItems = getAllItems();
        filtered = new ArrayList<>(allItems);

    }

    private void retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                allItems.add(item);
            }
            itemAdapter.notifyDataSetChanged();
        });
    }

    protected void setFiltered(String filterBy) {
        filtered.clear();

        for (int x = 0; x < allItems.size(); x++) {
            if (allItems.get(x).getName().toLowerCase().contains(filterBy.toLowerCase())) {
                filtered.add(allItems.get(x));
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

}