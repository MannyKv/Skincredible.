package com.example.softeng306project1team22.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;

public class CompactViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public CardView cardView;

    public CompactViewHolder(@NonNull View itemView) {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.image_card);
        this.imageView = itemView.findViewById(R.id.itemcycle);
    }
}
