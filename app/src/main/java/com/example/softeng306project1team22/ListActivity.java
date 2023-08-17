package com.example.softeng306project1team22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

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
        
        itemList = new ArrayList<>();
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        fetchCategoryData(categoryId);
    }

    private void fetchCategoryData(String categoryId) {
        db = FirebaseFirestore.getInstance();
        db.collection("category").document(categoryId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    category = new Category(documentSnapshot.get("name", String.class), categoryId, documentSnapshot.get("imageName", String.class));
                    populateCategoryDetails(category);
                    fetchItemListData(category.getName());
                });
    }

    private void fetchItemListData(String categoryName) {
        db = FirebaseFirestore.getInstance();
        db.collection(categoryName.toLowerCase())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    for (DocumentSnapshot document : documentSnapshot.getDocuments()) {
                        Map<String, Object> itemMap = document.getData();
                        String id = document.getId();
                        switch ((String) itemMap.get("categoryName")) {
                            case "Sunscreen":
                                itemList.add(new Sunscreen(id, (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (String) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("sunscreenType"), (String) itemMap.get("spf"), (String) itemMap.get("howToUse")));
                                break;
                            case "Cleanser":
                                itemList.add(new Cleanser(id, (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (String) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("ph"), (String) itemMap.get("cleanserType"), (String) itemMap.get("howToUse")));
                                break;
                            case "Moisturiser":
                                itemList.add(new Moisturiser(id, (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (String) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("moisturiserType"), (String) itemMap.get("howToUse"), (String) itemMap.get("timeToUse")));
                                break;
                        }
                    }
                    propagateListAdapter(category.getId());
                });
    }

    private void propagateListAdapter(String categoryId) {
        listAdapter = new ItemListAdapter(itemList);
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
        vh.categoryNameHeader.setText(category.getName().toUpperCase());

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

        public ViewHolder() {
            categoryNameHeader = findViewById(R.id.category_name);
            categoryIcon = findViewById(R.id.category_icon);
            itemRecyclerView = findViewById(R.id.rvItems);
            backButton = findViewById(R.id.back_button);
        }
    }


}