package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.Item;
import com.example.softeng306project1team22.R;
import com.example.softeng306project1team22.ViewHolder.CompactViewHolder;

import java.util.List;

public class CompactItemAdapter extends RecyclerView.Adapter<CompactViewHolder> {
    private List<Item> items;
    Context context;

    public CompactItemAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override

    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_scroll_main, parent, false);
        CompactViewHolder holder = new CompactViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompactViewHolder holder, int position) {
        Item thisItem = items.get(position);
        String filePath = thisItem.getImageNames().get(0);
        System.out.println("This is the path for loading: " + filePath);
        int i = context.getResources().getIdentifier(filePath, "drawable", context.getPackageName());
        System.out.println("This is the image ID for loading: " + i);

        holder.imageView.setImageResource(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
