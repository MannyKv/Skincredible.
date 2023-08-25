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
import com.example.softeng306project1team22.ViewHolder.CompactViewHolder;

import java.util.List;

public class CompactItemAdapter extends RecyclerView.Adapter<CompactViewHolder> {
    private List<IItem> items;
    Context context;


    private CategoryAdapter.OnItemClickListener mOnItemClickListener;

    public CompactItemAdapter(List<IItem> items, Context context, CategoryAdapter.OnItemClickListener onItemClickListener) {
        this.items = items;
        this.context = context;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override

    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_image_card, parent, false);
        CompactViewHolder holder = new CompactViewHolder(itemView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompactViewHolder holder, int position) {
        IItem thisItem = items.get(position);
        String filePath = thisItem.getImageNames().get(0);
        System.out.println("This is the path for loading: " + filePath);
        int i = context.getResources().getIdentifier(filePath, "drawable", context.getPackageName());
        System.out.println("This is the image ID for loading: " + i);
        holder.imageView.setImageResource(i);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        holder.linearLayout.startAnimation(animation);


    }

    @Override
    public int getItemCount() {

        return items.size();

    }


}
