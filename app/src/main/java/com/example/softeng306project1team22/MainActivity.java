package com.example.softeng306project1team22;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> imageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("EXIT", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

//        TextView test = findViewById(R.id.test);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("cleanser").document("cle1")
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    imageNames = (ArrayList<String>) documentSnapshot.get("imageNames");
//                    test.setText(imageNames.get(0));
//                });
    }
}