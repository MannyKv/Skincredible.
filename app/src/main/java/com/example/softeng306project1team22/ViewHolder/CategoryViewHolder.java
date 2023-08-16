package com.example.softeng306project1team22.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public ImageView imageView;

    public CardView cardView;


    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.category_card);
        textView = itemView.findViewById(R.id.category_name);
        imageView = itemView.findViewById(R.id.category_image);

    }


}







