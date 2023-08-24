package com.example.softeng306project1team22.ViewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;
import com.google.android.material.imageview.ShapeableImageView;

public class CompactViewHolder extends RecyclerView.ViewHolder {
    public ShapeableImageView imageView;
    public CardView cardView;


    public CompactViewHolder(@NonNull View itemView) {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.image_card);
        this.imageView = itemView.findViewById(R.id.itemcycle);
    }
}
