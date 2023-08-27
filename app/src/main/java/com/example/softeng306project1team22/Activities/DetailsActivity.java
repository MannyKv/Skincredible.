package com.example.softeng306project1team22.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.softeng306project1team22.Data.DataRepository;
import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    // Declaring ViewHolder class to store the views from the XML
    private class ViewHolder {
        Button backButton;
        TextView categoryTextView, brandTextView, productNameTextView;
        ImageView categoryImageView;
        ImageView productImageView;
        ImageButton previousImageButton, nextImageButton;
        FloatingActionButton decreaseQuantityButton, increaseQuantityButton;
        BottomNavigationView navigationView;
        CardView productDetailsCardView, howToUseCardView;
        TextView howToUseText;
        TextView quantityValue;
        TextView priceTextView;
        TextView firstDetailTitle, firstDetailValue, secondDetailTitle, secondDetailValue, thirdDetailValue;
        Button cartButton;

        public ViewHolder() {
            backButton = findViewById(R.id.back_button);
            categoryImageView = findViewById(R.id.category_icon);
            categoryTextView = findViewById(R.id.category_name);
            brandTextView = findViewById(R.id.brandTextView);
            productNameTextView = findViewById(R.id.productNameTextView);
            productImageView = findViewById(R.id.productImageView);
            previousImageButton = findViewById(R.id.previousImageButton);
            nextImageButton = findViewById(R.id.nextImageButton);
            productDetailsCardView = findViewById(R.id.productDetailsCardView);
            howToUseCardView = findViewById(R.id.howToUseCardView);
            howToUseText = findViewById(R.id.howToUseText);
            decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
            quantityValue = findViewById(R.id.quantityValue);
            priceTextView = findViewById(R.id.priceTextView);
            firstDetailTitle = findViewById(R.id.firstDetailTitle);
            firstDetailValue = findViewById(R.id.firstDetailValue);
            secondDetailTitle = findViewById(R.id.secondDetailTitle);
            secondDetailValue = findViewById(R.id.secondDetailValue);
            thirdDetailValue = findViewById(R.id.thirdDetailValue);
            cartButton = findViewById(R.id.cartButton);
            navigationView = findViewById(R.id.nav_buttons);
            decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        }
    }

    private ViewHolder viewHolder;
    private ArrayList<String> imageNames;
    private DataRepository dataRepository = new DataRepository();
    private IItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Getting the data passed in via Intents from other classes
        Intent intent = getIntent();
        String productCategory = intent.getStringExtra("productCategory");
        String productId = intent.getStringExtra("productId");

        imageNames = new ArrayList<>();

        // Instantiating the ViewHolder so views can be referenced in methods
        viewHolder = new ViewHolder();
        viewHolder.productImageView.setTag("0");

        // Fetching and setting the item data based on the category and ID of the item passed in
        if (productCategory.equals("Cleanser")) {
            setData(productCategory, productId, "cleanser type", "cleanserType", "ph", "ph");
        } else if (productCategory.equals("Moisturiser")) {
            setData(productCategory, productId, "moisturiser type", "moisturiserType", "time to use", "timeToUse");
        } else {
            setData(productCategory, productId, "sunscreen type", "sunscreenType", "spf", "spf");
        }


        // Updating the text of the cart button depending on if the current item is in the cart
        setCartInfo(productId);

        // Setting the functionality for the back button to end the current activity and go back to the previous activity when clicked
        viewHolder.backButton.setOnClickListener(v -> finish());

        // Setting the on click functionality of the previous image button on the image slider
        viewHolder.previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using tags to determine the current image being displayed and then displaying the appropriate previous image based on this
                if (String.valueOf(viewHolder.productImageView.getTag()).equals("0")) {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(2), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("2");
                } else if (String.valueOf(viewHolder.productImageView.getTag()).equals("1")) {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("0");
                } else {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(1), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("1");
                }
            }
        });

        // Setting the on click functionality of the next image button on the image slider
        viewHolder.nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using tags to determine the current image being displayed and then displaying the appropriate next image based on this
                if (String.valueOf(viewHolder.productImageView.getTag()).equals("0")) {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(1), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("1");
                } else if (String.valueOf(viewHolder.productImageView.getTag()).equals("1")) {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(2), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("2");
                } else {
                    Resources resources = getResources();
                    imageViewTransition(getBaseContext(), viewHolder.productImageView, resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("0");
                }
            }
        });

        // Setting the on click functionality for the decrease quantity button
        viewHolder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(viewHolder.quantityValue.getText().toString());

                // Only decrease the quantity if it is not already 1, as users cannot add 0 items to the cart
                if (quantityValue > 1) {
                    quantityValue--;
                    viewHolder.quantityValue.setText(String.valueOf(quantityValue));
                }
            }
        });

        // Setting the on click functionality for the increase quantity button
        viewHolder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityValue = Integer.parseInt(viewHolder.quantityValue.getText().toString());

                if (quantityValue < 99) {
                    quantityValue++;
                    viewHolder.quantityValue.setText(String.valueOf(quantityValue));
                }
            }
        });

        // Setting the on click functionality for the add to cart button
        viewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemToCart();


                // Display popup dialog confirming purchase
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(DetailsActivity.this, R.style.alert_dialog);
                dialogBuilder
                        .setTitle("Success!")
                        .setMessage("Cart updated!")
                        .setPositiveButton("ok", null)
                        .setIcon(R.drawable.alert_success_icon)
                        .setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_search_rounded, null))
                        .show();
                viewHolder.cartButton.setText("UPDATE CART");

            }
        });

        setNavigationViewLinks();
    }

    // This function sets the navigation links for the navigation bar
    private void setNavigationViewLinks() {
        viewHolder.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                finish();
            } else if (itemId == R.id.search) {
                startActivity(new Intent(DetailsActivity.this, SearchActivity.class));
                finish();
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(DetailsActivity.this, CartActivity.class));
                finish();
            }
            return true;
        });
    }

    // This function fetches the relevant item data from Firestore and sets the view elements in the layout with these values
    private void setData(String productCategory, String productId, String firstDetailName, String firstDetail, String secondDetailName, String secondDetail) {


        dataRepository.fetchItemById(productCategory, productId).thenAccept(item -> {
            // Adding the item to the recently-viewed collection in Firestore
            currentItem = item;
            addItemToRecentlyViewed();
            System.out.println(item.getName());
            Resources resources = getResources();
            viewHolder.categoryImageView.setImageResource(resources.getIdentifier(productId.substring(0, 3), "drawable", getPackageName()));
            viewHolder.categoryTextView.setText(currentItem.getCategoryName());
            viewHolder.brandTextView.setText(currentItem.getBrand());
            viewHolder.productNameTextView.setText(currentItem.getName());

            ArrayList<String> databaseImageNames = currentItem.getImageNames();
            imageNames.addAll(databaseImageNames);
            viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));

            String priceText = "$" + currentItem.getPrice();
            viewHolder.priceTextView.setText(priceText);

            viewHolder.firstDetailTitle.setText(firstDetailName);
            viewHolder.secondDetailTitle.setText(secondDetailName);

            switch (currentItem.getCategoryName()) {
                case "Sunscreen":
                    viewHolder.firstDetailValue.setText(currentItem.getSunscreenType().toLowerCase());
                    viewHolder.secondDetailValue.setText(currentItem.getSpf().toLowerCase());
                    break;
                case "Cleanser":
                    viewHolder.firstDetailValue.setText(currentItem.getCleanserType().toLowerCase());
                    viewHolder.secondDetailValue.setText(currentItem.getPh().toLowerCase());

                    break;
                case "Moisturiser":
                    viewHolder.firstDetailValue.setText(currentItem.getMoisturiserType().toLowerCase());
                    viewHolder.secondDetailValue.setText(currentItem.getTimeToUse().toLowerCase());

                    break;
            }
            viewHolder.thirdDetailValue.setText(String.join(", ", currentItem.getSkinType()).toLowerCase());
            viewHolder.howToUseText.setText(item.getHowToUse().toLowerCase());
        });
    }

    // This function writes to the item ID and quantity to the "cart" category in Firestore
    private void addItemToCart() {
        String quantity = viewHolder.quantityValue.getText().toString();
        dataRepository.addItemToCart(currentItem.getId(), currentItem.getCategoryName(), currentItem.getPrice(), quantity);
    }

    // This function adds the current item to the recently viewed collection in Firestore
    private void addItemToRecentlyViewed() {
        dataRepository.addItemToRecentlyViewed(currentItem);
    }

    private void setCartInfo(String productId) {

        dataRepository.getCartDocuments().thenAccept(itemsMap -> {

            ArrayList<String> itemsInCart = new ArrayList<>();
            for (IItem i : itemsMap.keySet()) {
                itemsInCart.add(i.getId());
                if (i.getId().equals(productId)) {
                    viewHolder.quantityValue.setText(itemsMap.get(i));
                }
            }
            if (itemsInCart.contains(productId)) {
                viewHolder.cartButton.setText("UPDATE CART");
            } else {
                viewHolder.cartButton.setText("ADD TO CART");
            }

        });

    }

    // This function provides fade in and fade out animation transitions for the ImageView images
    private void imageViewTransition(Context context, ImageView imageView, int imageResId) {
        // Load the animations using the default Android fade in and fade out animations
        Animation animationOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animationIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        // Set the fade out animation
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // Set the fade in animation
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource(imageResId);
                animationIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                imageView.startAnimation(animationIn);
            }
        });
        imageView.startAnimation(animationOut);
    }

}