package com.example.softeng306project1team22.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.softeng306project1team22.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    // Declaring ViewHolder class to store the views from the XML
    private class ViewHolder {
        Button backButton;
        TextView categoryTextView, brandTextView, productNameTextView;
        ImageView categoryImageView;
        ImageView productImageView;
        Button previousImageButton, nextImageButton;
        Button detailsButton, howToUseButton;
        CardView productDetailsCardView, howToUseCardView;
        TextView howToUseText;
        Button decreaseQuantityButton, increaseQuantityButton;
        TextView quantityValue;
        TextView priceTextView;
        TextView firstDetailTitle, firstDetailValue, secondDetailTitle, secondDetailValue, thirdDetailValue;
        Button cartButton;

        public ViewHolder() {
            backButton = findViewById(R.id.backButton);
            categoryImageView = findViewById(R.id.categoryImageView);
            categoryTextView = findViewById(R.id.categoryTextView);
            brandTextView = findViewById(R.id.brandTextView);
            productNameTextView = findViewById(R.id.productNameTextView);
            productImageView = findViewById(R.id.productImageView);
            previousImageButton = findViewById(R.id.previousImageButton);
            nextImageButton = findViewById(R.id.nextImageButton);
            detailsButton = findViewById(R.id.detailsButton);
            howToUseButton = findViewById(R.id.howToUseButton);
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
        }
    }

    private ViewHolder viewHolder;
    private ArrayList<String> imageNames;

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

        // Adding the item to the recently-viewed collection in Firestore
        addItemToRecentlyViewed(productId, productCategory.toLowerCase());

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

        // Setting the on click functionality for the details tab button
        viewHolder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the details view and hide the how to use view
                viewHolder.howToUseCardView.setVisibility(View.GONE);
                viewHolder.productDetailsCardView.setVisibility(View.VISIBLE);

                // Set the details button colour to white and the how to use button colour to green
                viewHolder.detailsButton.setBackgroundColor(0xFFFFFFFF);
                viewHolder.howToUseButton.setBackgroundColor(0xFF88CEC6);
            }
        });

        // Setting the on click functionality for the how to use tab button
        viewHolder.howToUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the how to use view and hide the details view
                viewHolder.howToUseCardView.setVisibility(View.VISIBLE);
                viewHolder.productDetailsCardView.setVisibility(View.GONE);

                // Set the how to use button colour to white and the details button colour to green
                viewHolder.detailsButton.setBackgroundColor(0xFF88CEC6);
                viewHolder.howToUseButton.setBackgroundColor(0xFFFFFFFF);
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

                quantityValue++;
                viewHolder.quantityValue.setText(String.valueOf(quantityValue));
            }
        });

        // Setting the on click functionality for the add to cart button
        viewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(productId, productCategory.toLowerCase());
            }
        });
    }

    // This function fetches the relevant item data from Firestore and sets the view elements in the layout with these values
    private void setData(String productCategory, String productId, String firstDetailName, String firstDetail, String secondDetailName, String secondDetail) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(productCategory.toLowerCase()).document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Resources resources = getResources();
                    viewHolder.categoryImageView.setImageResource(resources.getIdentifier(productId.substring(0, 3), "drawable", getPackageName()));
                    viewHolder.categoryTextView.setText(documentSnapshot.get("categoryName").toString().toUpperCase());
                    viewHolder.brandTextView.setText(documentSnapshot.get("brand").toString());
                    viewHolder.productNameTextView.setText(documentSnapshot.get("name").toString().toUpperCase());

                    ArrayList<String> databaseImageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                    imageNames.addAll(databaseImageNames);
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));

                    String priceText = "$" + documentSnapshot.get("price").toString();
                    viewHolder.priceTextView.setText(priceText);

                    viewHolder.firstDetailTitle.setText(firstDetailName);
                    viewHolder.firstDetailValue.setText(documentSnapshot.get(firstDetail).toString().toLowerCase());

                    viewHolder.secondDetailTitle.setText(secondDetailName);
                    viewHolder.secondDetailValue.setText(documentSnapshot.get(secondDetail).toString().toLowerCase());

                    viewHolder.thirdDetailValue.setText(String.join(", ", (ArrayList<String>) documentSnapshot.get("skinType")).toLowerCase());

                    viewHolder.howToUseText.setText(documentSnapshot.get("howToUse").toString());
                });
    }

    // This function writes to the item ID and quantity to the "cart" category in Firestore
    private void addItemToCart(String productId, String productCategory) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> itemInfo = new HashMap<>();

        itemInfo.put("itemId", productId);
        itemInfo.put("quantity", viewHolder.quantityValue.getText().toString());
        itemInfo.put("categoryName", productCategory);
        itemInfo.put("singleItemPrice", viewHolder.priceTextView.getText().toString().substring(1));

        database.collection("cart").document(productId).set(itemInfo);
    }

    // This function adds the current item to the recently viewed collection in Firestore
    private void addItemToRecentlyViewed(String productId, String productCategory) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Determine the size of the recently-viewed collection
        database.collection("recently-viewed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> documents = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        documents.add(document.getId());
                    }
                    // Format the data to be added to Firestore as a HashMap with key, value pairs
                    Map<String, Object> itemInfo = new HashMap<>();
                    itemInfo.put("itemId", productId);
                    itemInfo.put("timeAdded", FieldValue.serverTimestamp());
                    itemInfo.put("categoryName", productCategory);

                    // If there are more than 5 documents in the recently-viewed collection and the document being added is not already in it, delete the oldest one and then add the newest one
                    if (documents.size() > 5 && !documents.contains(productId)) {
                        // Query the collection and return the oldest document
                        Query sortedRecentlyViewed = database.collection("recently-viewed")
                                .orderBy("timeAdded", Query.Direction.ASCENDING)
                                .limit(1);

                        // Delete the oldest document
                        sortedRecentlyViewed.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    database.collection("recently-viewed").document(document.getId()).delete();
                                }
                            }
                        });

                        // Add the new document after the oldest one has been deleted
                        database.collection("recently-viewed").document(productId).set(itemInfo);
                    } else {
                        // If there are 5 or fewer documents in the collection, just add the new one
                        database.collection("recently-viewed").document(productId).set(itemInfo);
                    }
                }
            }
        });
    }

    private void setCartInfo(String productId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> itemsInCart = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    itemsInCart.add(document.getId());
                    if (document.getId().equals(productId)) {
                        viewHolder.quantityValue.setText(document.get("quantity").toString());
                    }
                }
                if (itemsInCart.contains(productId)) {
                    viewHolder.cartButton.setText("UPDATE CART");
                } else {
                    viewHolder.cartButton.setText("ADD TO CART");
                }
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
