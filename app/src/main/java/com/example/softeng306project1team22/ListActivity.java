package com.example.softeng306project1team22;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
        itemList = new ArrayList<>();
        String categoryName = "sunscreen";
        fetchCategoryData(categoryName);
        fetchItemListData(categoryName);
    }

    private void fetchCategoryData(String categoryName) {
        db = FirebaseFirestore.getInstance();
        db.collection("category").document(categoryName.substring(0, 3))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    category = new Category(documentSnapshot.get("name", String.class), documentSnapshot.getId(), documentSnapshot.get("imageName", String.class));
                    populateCategoryDetails(category);
                });
    }

    private void fetchItemListData(String categoryName) {
        db = FirebaseFirestore.getInstance();
        db.collection(categoryName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    for (DocumentSnapshot document : documentSnapshot.getDocuments()) {
                        Map<String, Object> itemMap = document.getData();
                        switch ((String) itemMap.get("categoryName")) {
                            case "Sunscreen":
                                itemList.add(new Sunscreen(document.getId(), (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (Double) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("sunscreenType"), (String) itemMap.get("spf"), (String) itemMap.get("howToUse")));
                                break;
                            case "Cleanser":
                                itemList.add(new Cleanser(document.getId(), (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (Double) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("ph"), (String) itemMap.get("cleanserType"), (String) itemMap.get("howToUse")));
                                break;
                            case "Moisturiser":
                                itemList.add(new Moisturiser(document.getId(), (String) itemMap.get("name"), (String) itemMap.get("brand"), (ArrayList<String>) itemMap.get("imageNames"), (Double) itemMap.get("price"), (String) itemMap.get("categoryName"), (ArrayList<String>) itemMap.get("skinType"), (String) itemMap.get("moisturiserType"), (String) itemMap.get("howToUse"), (String) itemMap.get("timeToUse")));
                                break;
                        }
                    }
                    listAdapter = new ItemListAdapter(itemList);
                    vh.itemRecyclerView.setAdapter(listAdapter);
                    vh.itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                });
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

        public ViewHolder() {
            categoryNameHeader = findViewById(R.id.category_name);
            categoryIcon = findViewById(R.id.category_icon);
            itemRecyclerView = findViewById(R.id.rvItems);
        }
    }


}