package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CompactItemAdapter extends RecyclerView.Adapter<CompactItemAdapter.CompactViewHolder> {
    private List<IItem> mItems;
    Context mContext;

    private CategoryAdapter.OnItemClickListener mOnItemClickListener;

    public CompactItemAdapter(List<IItem> mItems, Context mContext, CategoryAdapter.OnItemClickListener onItemClickListener) {
        this.mItems = mItems;
        this.mContext = mContext;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_image_card, parent, false);
        CompactViewHolder holder = new CompactViewHolder(itemView); //create view holder
        holder.cardView.setOnClickListener(new View.OnClickListener() { //assign on click for the item

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompactViewHolder holder, int position) {
        IItem thisItem = mItems.get(position);
        
        String filePath = thisItem.getImageNames().get(0); //gets the first image to use as display
        System.out.println("This is the path for loading: " + filePath);
        int i = mContext.getResources().getIdentifier(filePath, "drawable", mContext.getPackageName());
        System.out.println("This is the image ID for loading: " + i);

        holder.imageView.setImageResource(i);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up); //loads a slide up animation for the item when loaded
        holder.cardView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {

        return mItems.size();

    }

    class CompactViewHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView imageView;
        public MaterialCardView cardView;


        public CompactViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.image_card);
            this.imageView = itemView.findViewById(R.id.itemcycle);
        }
    }
}
