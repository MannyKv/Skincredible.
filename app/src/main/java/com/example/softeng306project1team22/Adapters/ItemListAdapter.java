package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Activities.DetailsActivity;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;

import java.util.List;
import java.util.Map;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    // declaring collection object holding the data to populate the RecyclerView
    private List<IItem> mItems;
    private Context mContext;
    private String category;
    private Map<String, String> itemQuantities;

    public ItemListAdapter(List<IItem> items, String categoryId) {
        mItems = items;
        category = categoryId;
    }

    // constructor when displaying items from various categories (no id specified)
    public ItemListAdapter(List<IItem> items, Map<String, String> itemQuantities) {
        mItems = items;
        category = "";
        this.itemQuantities = itemQuantities;
    }

    // inflating layout from XML and returning holder
    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView;
        ViewHolder holder;

        switch (category) {
            case "cle":
                itemView = inflater.inflate(R.layout.cleanser_item_card, parent, false);
                holder = new ViewHolder(itemView, false);
                break;
            case "mos":
                itemView = inflater.inflate(R.layout.moisturiser_item_card, parent, false);
                holder = new ViewHolder(itemView, false);
                break;
            case "sun":
                itemView = inflater.inflate(R.layout.sunscreen_item_card, parent, false);
                holder = new ViewHolder(itemView, false);
                break;
            default:
                itemView = inflater.inflate(R.layout.cart_item_card, parent, false);
                holder = new ViewHolder(itemView, true);
                break;
        }
        // Return a new holder instance
        return holder;
    }

    // this method populates the data from mItems to the view items
    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        // get the data object for the item view in this position
        IItem currItem = mItems.get(position);

        holder.productNameTextView.setText(currItem.getName());
        holder.brandTextView.setText(currItem.getBrand());
        holder.priceTextView.setText((String.valueOf(currItem.getPrice())));
        int i = mContext.getResources().getIdentifier(
                currItem.getImageNames().get(0), "drawable",
                mContext.getPackageName());
        holder.productImageView.setImageResource(i);

        if (!this.category.equals("")) {
            // check type of item
            switch (currItem.getCategoryName()) {
                case "Cleanser":
                    holder.tagOneTextView.setText(String.format("pH %s", currItem.getPh()));
                    holder.tagTwoTextView.setText(currItem.getCleanserType());
                    break;
                case "Sunscreen":
                    holder.tagOneTextView.setText(currItem.getSpf());
                    holder.tagTwoTextView.setText(currItem.getSunscreenType());
                    break;
                case "Moisturiser":
                    holder.tagOneTextView.setText(currItem.getTimeToUse());
                    holder.tagTwoTextView.setText(currItem.getMoisturiserType());
                    break;
            }
        } else {
            holder.cartQuantityTextView.setText(itemQuantities.get(currItem.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // to make view item clickable, the view holder class implements View.OnClickListener and has
    // onClick(View v) method.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // all views to be manipulated in sunscreen_item_card.xml and cart_item_card.xml
        public TextView brandTextView;
        public TextView productNameTextView;
        public TextView priceTextView;
        public TextView tagOneTextView;
        public TextView tagTwoTextView;
        public ImageView productImageView;

        public TextView cartQuantityTextView;

        public ViewHolder(View view, boolean cart) {
            super(view);
            view.setOnClickListener(this);

            if (cart) {
                brandTextView = view.findViewById(R.id.brand_name);
                productNameTextView = view.findViewById(R.id.product_name);
                priceTextView = view.findViewById(R.id.price);
                productImageView = view.findViewById(R.id.item_icon);
                cartQuantityTextView = view.findViewById(R.id.cart_quantity_text_view);
            } else {
                // initialising view objects
                brandTextView = view.findViewById(R.id.brand_name);
                productNameTextView = view.findViewById(R.id.product_name);
                priceTextView = view.findViewById(R.id.price);
                tagOneTextView = view.findViewById(R.id.tag1);
                tagTwoTextView = view.findViewById(R.id.tag2);
                productImageView = view.findViewById(R.id.item_icon);
            }
        }

        @Override
        public void onClick(View v) {
            // what to do when the view item is clicked
            IItem clickedItem = mItems.get(getAdapterPosition());

            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("productCategory", clickedItem.getCategoryName());
            intent.putExtra("productId", clickedItem.getId());
            mContext.startActivity(intent);
        }
    }


}