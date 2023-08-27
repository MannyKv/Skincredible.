package com.example.softeng306project1team22.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306project1team22.Activities.DetailsActivity;
import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;

import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<IItem> mItems;
    private Context mContext;
    private Map<String, String> mItemQuantities;

    public CartAdapter(List<IItem> items, Map<String, String> itemQuantities) {
        mItems = items;
        mItemQuantities = itemQuantities;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView;
        ViewHolder holder;

        itemView = inflater.inflate(R.layout.cart_item_card, parent, false);
        holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IItem currentItem = mItems.get(position);

        holder.productNameTextView.setText(currentItem.getName());
        holder.brandTextView.setText(currentItem.getBrand());
        holder.priceTextView.setText((String.valueOf(currentItem.getPrice())));
        int i = mContext.getResources().getIdentifier(
                currentItem.getImageNames().get(0), "drawable",
                mContext.getPackageName());
        holder.productImageView.setImageResource(i);

        holder.cartQuantityTextView.setText(mItemQuantities.get(currentItem.getId()));
        holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(holder.cartQuantityTextView.getText().toString());

                // Only decrease the quantity if it is not already 1, as users cannot add 0 items to the cart
                if (quantityValue > 1) {
                    quantityValue--;
                    holder.cartQuantityTextView.setText(String.valueOf(quantityValue));
                    changeItemQuantity(currentItem.getId(), quantityValue);
                    notifyItemRangeChanged(holder.getAdapterPosition(), mItems.size() - holder.getAdapterPosition());
                }
            }
        });
        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(holder.cartQuantityTextView.getText().toString());

                quantityValue++;
                holder.cartQuantityTextView.setText(String.valueOf(quantityValue));
                changeItemQuantity(currentItem.getId(), quantityValue);
                notifyItemRangeChanged(holder.getAdapterPosition(), mItems.size() - holder.getAdapterPosition());
            }
        });
        holder.removeFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataRepository.deleteItemById(currentItem.getId());
                mItems.remove(currentItem);
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mItems.size() - holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void changeItemQuantity(String productId, int quantityValue) {
        DataRepository.modifyItemQuantity(productId, quantityValue);
        mItemQuantities.put(productId, String.valueOf(quantityValue));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView brandTextView;
        public TextView productNameTextView;
        public TextView priceTextView;
        public ImageView productImageView;
        public TextView cartQuantityTextView;
        public ImageButton decreaseQuantityButton;
        public ImageButton increaseQuantityButton;
        public ImageButton removeFromCartButton;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            brandTextView = view.findViewById(R.id.brand_name);
            productNameTextView = view.findViewById(R.id.product_name);
            priceTextView = view.findViewById(R.id.price);
            productImageView = view.findViewById(R.id.item_icon);
            cartQuantityTextView = view.findViewById(R.id.cart_quantity_text_view);
            decreaseQuantityButton = view.findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = view.findViewById(R.id.increaseQuantityButton);
            removeFromCartButton = view.findViewById(R.id.removeFromCartButton);
        }

        @Override
        public void onClick(View v) {
            IItem clickedItem = mItems.get(getAdapterPosition());

            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("productCategory", clickedItem.getCategoryName());
            intent.putExtra("productId", clickedItem.getId());
            mContext.startActivity(intent);
        }
    }
}
