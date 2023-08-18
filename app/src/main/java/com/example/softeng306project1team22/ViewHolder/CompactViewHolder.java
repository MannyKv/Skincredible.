package com.example.softeng306project1team22.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.R;

public class CompactViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public LinearLayout linearLayout;


    public CompactViewHolder(@NonNull View itemView) {
        super(itemView);
        this.linearLayout = itemView.findViewById(R.id.image_card);
        this.imageView = itemView.findViewById(R.id.itemcycle);
    }
}
