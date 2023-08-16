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
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public CategoryAdapter(List<Category> category, Context context, OnItemClickListener onItemClickListener) {
        this.category = category;
        this.context = context;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View categoryView = inflater.inflate(R.layout.category_card, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(categoryView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });


        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category thisCategory = category.get(position);
        holder.textView.setText(thisCategory.getName());
        System.out.println(thisCategory.getImageName());
        String filePath = thisCategory.getImageName();


        int i = context.getResources().getIdentifier(filePath, "drawable", context.getPackageName());
        holder.imageView.setImageResource(i);
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}
