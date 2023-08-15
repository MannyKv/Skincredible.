package com.example.softeng306project1team22.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public Button button;
    public ImageView imageView;


    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button_category);
        imageView = itemView.findViewById(R.id.categoryImage);
        
    }


}







