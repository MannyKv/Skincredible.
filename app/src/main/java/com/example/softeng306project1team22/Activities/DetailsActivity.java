package com.example.softeng306project1team22.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.softeng306project1team22.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private class ViewHolder {
        ImageView productImageView;
        Button previousImageButton;
        Button nextImageButton;

        public ViewHolder() {
            productImageView = findViewById(R.id.productImageView);
            previousImageButton = findViewById(R.id.previousImageButton);
            nextImageButton = findViewById(R.id.nextImageButton);
        }
    }

    ViewHolder viewHolder;
    ArrayList<String> imageNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewHolder = new ViewHolder();

        viewHolder.productImageView.setTag("0");

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("cleanser").document("cle1")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    ArrayList<String> databaseImageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
                    imageNames.addAll(databaseImageNames);
                });

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
    }
}
