package com.example.softeng306project1team22.ViewHolder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public Button button;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button_category);
    }

}







