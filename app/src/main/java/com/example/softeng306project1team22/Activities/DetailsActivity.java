package com.example.softeng306project1team22.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softeng306project1team22.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private class ViewHolder {
        TextView categoryTextView, brandTextView, productNameTextView;
        ImageView productImageView;
        Button previousImageButton;
        Button nextImageButton;
        Button decreaseQuantityButton;
        Button increaseQuantityButton;
        TextView priceTextView;
        TextView firstDetailTitle, firstDetailValue, secondDetailTitle, secondDetailValue, thirdDetailValue;

        public ViewHolder() {
            categoryTextView = findViewById(R.id.categoryTextView);
            brandTextView = findViewById(R.id.brandTextView);
            productNameTextView = findViewById(R.id.productNameTextView);
            productImageView = findViewById(R.id.productImageView);
            previousImageButton = findViewById(R.id.previousImageButton);
            nextImageButton = findViewById(R.id.nextImageButton);
            decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
            priceTextView = findViewById(R.id.priceTextView);
            firstDetailTitle = findViewById(R.id.firstDetailTitle);
            firstDetailValue = findViewById(R.id.firstDetailValue);
            secondDetailTitle = findViewById(R.id.secondDetailTitle);
            secondDetailValue = findViewById(R.id.secondDetailValue);
            thirdDetailValue = findViewById(R.id.thirdDetailValue);
        }
    }

    private ViewHolder viewHolder;
    private ArrayList<String> imageNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewHolder = new ViewHolder();

        viewHolder.productImageView.setTag("0");

        String productCategory = "moisturiser";

        String productId = "mos5";

        if (productCategory.equals("cleanser")) {
            setData(productCategory, productId, "cleanser type", "cleanserType", "ph", "ph");
        } else if (productCategory.equals("moisturiser")) {
            setData(productCategory, productId, "moisturiser type", "moisturiserType", "time to use", "timeToUse");
        } else {
            setData(productCategory, productId, "sunscreen type", "sunscreenType", "spf", "spf");
        }

        viewHolder.previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(viewHolder.productImageView.getTag()).equals("0")) {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(2), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("2");
                } else if (String.valueOf(viewHolder.productImageView.getTag()).equals("1")) {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("0");
                } else {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(1), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("1");
                }
            }
        });

        viewHolder.nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(viewHolder.productImageView.getTag()).equals("0")) {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(1), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("1");
                } else if (String.valueOf(viewHolder.productImageView.getTag()).equals("1")) {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(2), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("2");
                } else {
                    Resources resources = getResources();
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));
                    viewHolder.productImageView.setTag("0");
                }
            }
        });

        viewHolder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setData(String productCategory, String productId, String firstDetailName, String firstDetail, String secondDetailName, String secondDetail) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(productCategory).document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    viewHolder.categoryTextView.setText(documentSnapshot.get("categoryName").toString().toUpperCase());
                    viewHolder.brandTextView.setText(documentSnapshot.get("brand").toString());
                    viewHolder.productNameTextView.setText(documentSnapshot.get("name").toString().toUpperCase());

                    Resources resources = getResources();
                    ArrayList<String> databaseImageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                    imageNames.addAll(databaseImageNames);
                    viewHolder.productImageView.setImageResource(resources.getIdentifier(imageNames.get(0), "drawable", getPackageName()));

                    String priceText = "$" + documentSnapshot.get("price").toString();
                    viewHolder.priceTextView.setText(priceText);

                    viewHolder.firstDetailTitle.setText(firstDetailName);
                    viewHolder.firstDetailValue.setText(documentSnapshot.get(firstDetail).toString());

                    viewHolder.secondDetailTitle.setText(secondDetailName);
                    viewHolder.secondDetailValue.setText(documentSnapshot.get(secondDetail).toString());

                    viewHolder.thirdDetailValue.setText(String.join(", ", (ArrayList<String>) documentSnapshot.get("skinType")));
                });
    }
}
