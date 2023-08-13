package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.R;
import com.example.softeng306project1team22.ViewHolder.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> category;
    Context context;

    public CategoryAdapter(List<Category> category, Context context) {
        this.category = category;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View categoryView = inflater.inflate(R.layout.recycled_view, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(categoryView);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category thisCategory = category.get(position);
        holder.button.setText(thisCategory.getName());
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}
