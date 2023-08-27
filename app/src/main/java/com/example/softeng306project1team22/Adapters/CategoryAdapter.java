package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.Category;
import com.example.softeng306project1team22.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mCategory;
    Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener { //custom on click listener to pass through to the main activity
        public void onItemClick(View view, int position);
    }

    public CategoryAdapter(List<Category> mCategory, Context mContext, OnItemClickListener onItemClickListener) {
        this.mCategory = mCategory;
        this.mContext = mContext;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View categoryView = inflater.inflate(R.layout.category_card, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(categoryView);

        holder.cardView.setOnClickListener(new View.OnClickListener() { //assigns onclick functionality for category

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category thisCategory = mCategory.get(position);
        holder.textView.setText(thisCategory.getName());
        String filePath = thisCategory.getImageName();

        //sets image icon for the category
        int i = mContext.getResources().getIdentifier(filePath, "drawable", mContext.getPackageName());
        holder.imageView.setImageResource(i);
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
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
}
