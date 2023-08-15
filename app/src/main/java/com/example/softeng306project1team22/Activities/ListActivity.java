package com.example.softeng306project1team22.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListActivity extends AppCompatActivity {
    ViewHolder vh;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        vh = new ViewHolder();
        String categoryId = "cle";
        fetchCategoryData(categoryId);
    }

    private void fetchCategoryData(String categoryId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("category").document(categoryId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    category = new Category(documentSnapshot.get("name", String.class), documentSnapshot.getId(), documentSnapshot.get("imageName", String.class));
                    populateCategoryDetails(category);
                });
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

        public ViewHolder() {
            categoryNameHeader = findViewById(R.id.category_name);
            categoryIcon = findViewById(R.id.category_icon);
        }
    }


}