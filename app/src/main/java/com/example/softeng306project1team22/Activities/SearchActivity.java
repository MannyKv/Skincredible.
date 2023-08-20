package com.example.softeng306project1team22.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Adapters.ItemListAdapter;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.example.softeng306project1team22.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ItemListAdapter itemAdapter;
    List<IItem> allItems = new ArrayList<>();
    List<IItem> filtered = new ArrayList<>();

    RecyclerView recyclerView;
    TextView notFoundMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        notFoundMsg = findViewById(R.id.not_found);
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
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
        searchView.setIconified(false);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
        recyclerView = findViewById(R.id.search_recycled);

        itemAdapter = new ItemListAdapter(filtered);
        recyclerView.setAdapter(itemAdapter);
        retrieveAllItems();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter.notifyDataSetChanged();

    }

    protected void retrieveAllItems() {
        // allItems = getAllItems();

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        CollectionReference colRef1 = dbs.collection("cleanser");
        CollectionReference colRef2 = dbs.collection("moisturiser");
        CollectionReference colRef3 = dbs.collection("sunscreen");

        retrieveFromCollection(colRef1, Cleanser.class);
        retrieveFromCollection(colRef2, Moisturiser.class);
        retrieveFromCollection(colRef3, Sunscreen.class);
        // filtered = new ArrayList<>(allItems);
    }

    private void retrieveFromCollection(CollectionReference colRef, Class<?> itemClass) {
        colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("Firestore", "Recently viewed retrieved successfully");
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IItem item = (IItem) documentSnapshot.toObject(itemClass);
                allItems.add(item);
                filtered.add(item);
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
        if (filtered.size() == 0) {
            notFoundMsg.setVisibility(View.VISIBLE);
        } else {
            notFoundMsg.setVisibility(View.GONE);
        }
        itemAdapter.notifyDataSetChanged();
    }

}