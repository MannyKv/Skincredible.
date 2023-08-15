package com.example.softeng306project1team22.Data;

import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.Item;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ItemDeserializer {

    public static Item deserialize(DocumentSnapshot documentSnapshot) {
        String name = documentSnapshot.getString("name");
        String id = documentSnapshot.getString("id");
        String brand = documentSnapshot.getString("brand");
        ArrayList<String> imageUri = (ArrayList<String>) documentSnapshot.get("imageUri");
        double price = documentSnapshot.getDouble("price");
        ArrayList<String> skinType = (ArrayList<String>) documentSnapshot.get("skinType");
        String howToUse = documentSnapshot.getString("howToUse");
        String ph = documentSnapshot.getString("ph");
        String cleanserType = documentSnapshot.getString("cleanserType");
        DocumentReference categoryRef = (DocumentReference) documentSnapshot.getDocumentReference("category");

        Cleanser cleanser = new Cleanser(categoryRef, id, name, brand, imageUri, price, skinType, ph, cleanserType, howToUse);

        return cleanser;
    }

}
