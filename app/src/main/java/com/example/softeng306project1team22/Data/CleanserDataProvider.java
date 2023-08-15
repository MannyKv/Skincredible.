package com.example.softeng306project1team22.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.softeng306project1team22.Models.Cleanser;
import com.example.softeng306project1team22.Models.IItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CleanserDataProvider {

    public static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static Map<String, ArrayList<Object>> getCleansersInfo() {
        Map<String, ArrayList<Object>> cleanserInfo = new LinkedHashMap<String, ArrayList<Object>>();

        cleanserInfo.put("cle1", new ArrayList<Object>(
                Arrays.asList(
                        "Low pH Morning Cleanser",
                        "COSRX",
                        new ArrayList<String>(Arrays.asList("cle1_img1.png", "cle1_img2.png", "cle1_img3.png")),
                        16.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Normal", "Acne Prone")),
                        "For use in the morning and night after removing makeup. Squeeze a proper amount onto hand and foam up. Gently massage onto wet face, avoiding the eyes and mouth area. Rinse afterwards with warm water.",
                        "5.5",
                        "Gel"
                )
        ));
        cleanserInfo.put("cle2", new ArrayList<Object>(
                Arrays.asList(
                        "1025 Dokdo Cleansing Balm",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("cle2_img1.png", "cle2_img2.png", "cle2_img3.png")),
                        36.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Dry", "Combination")),
                        "Spread appropriate amount onto a dry face and massage gently while adding makeup and waste products. Rinse thoroughly with lukewarm water afterwards.",
                        "5",
                        "Balm"
                )
        ));
        cleanserInfo.put("cle3", new ArrayList<Object>(
                Arrays.asList(
                        "Honest Cleansing Foam",
                        "Benton",
                        new ArrayList<String>(Arrays.asList("cle3_img1.png", "cle3_img2.png", "cle3_img3.png")),
                        13.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Dry", "Combination")),
                        "Apply appropriate amount onto palm, mix with water and lather. Gently rub the resulting foam onto face and then wash away the foam with warm water.",
                        "6",
                        "Foam"
                )
        ));
        cleanserInfo.put("cle4", new ArrayList<Object>(
                Arrays.asList(
                        "Heartleaf Pore Control Cleansing Oil",
                        "Anua",
                        new ArrayList<String>(Arrays.asList("cle4_img1.png", "cle4_img2.png", "cle4_img3.png")),
                        27.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Acne Prone", "Sensitive")),
                        "Take 2-3 pumps and gently smooth and roll over face. Wet face to emulsify cleansing oil to remove blackheads. Rinse thoroughly with water afterwards.",
                        "5.5",
                        "Oil"
                )
        ));
        cleanserInfo.put("cle5", new ArrayList<Object>(
                Arrays.asList(
                        "pH Balancing Bubble Free Cleansing Gel",
                        "Acwell",
                        new ArrayList<String>(Arrays.asList("cle5_img1.png", "cle5_img2.png", "cle5_img3.png")),
                        25.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Oily", "Combination")),
                        "Take adequate amount with dry hand and gently massage onto the entire face. Add a small amount of water. Rinse with lukewarm water afterwards.",
                        "5",
                        "Gel"
                )
        ));
        cleanserInfo.put("cle6", new ArrayList<Object>(
                Arrays.asList(
                        "Green Tea Hydrating Amino Acid Cleansing Foam",
                        "Innisfree",
                        new ArrayList<String>(Arrays.asList("cle6_img1.png", "cle6_img2.png", "cle6_img3.png")),
                        23.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Combination")),
                        "Squeeze appropriate amount and lather. Smoothly massage over face and then thoroughly rinse with lukewarm water.",
                        "6",
                        "Foam"
                )
        ));
        cleanserInfo.put("cle7", new ArrayList<Object>(
                Arrays.asList(
                        "pH Balancing Watery Cleansing Oil",
                        "Acwell",
                        new ArrayList<String>(Arrays.asList("cle7_img1.png", "cle7_img2.png", "cle7_img3.png")),
                        34.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Normal", "Dry", "Combination")),
                        "Take adequate amount of cleansing oil into dry hands and massage gently onto dry face. Wet hands to emulsify the oil. Rinse well with warm water afterwards and pat dry.",
                        "6",
                        "Oil"
                )
        ));
        cleanserInfo.put("cle8", new ArrayList<Object>(
                Arrays.asList(
                        "Madagascar Centella Light Cleansing Oil",
                        "SKIN1004",
                        new ArrayList<String>(Arrays.asList("cle8_img1.png", "cle8_img2.png", "cle8_img3.png")),
                        50.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Normal", "Combination")),
                        "Use a few pumps of cleansing oil onto a dry face with dry hands. Massage gently using circular motions and emulsify with water to create a milky emotion. Rinse off completely with lukewarm water.",
                        "5.5",
                        "Oil"
                )
        ));
        cleanserInfo.put("cle9", new ArrayList<Object>(
                Arrays.asList(
                        "Cicaluronic Low pH Cleansing Foam",
                        "Mizon",
                        new ArrayList<String>(Arrays.asList("cle9_img1.png", "cle9_img2.png", "cle9_img3.png")),
                        26.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Sensitive")),
                        "Take an appropriate amount and create a foam. Gently massage foam over entire face and rinse off with lukewarm water afterwards.",
                        "5",
                        "Foam"
                )
        ));
        cleanserInfo.put("cle10", new ArrayList<Object>(
                Arrays.asList(
                        "Clean It Zero Cleansing Balm Fore Clarifying",
                        "Banila Co",
                        new ArrayList<String>(Arrays.asList("cle10_img1.png", "cle10_img2.png", "cle10_img3.png")),
                        32.0,
                        "Cleanser",
                        new ArrayList<String>(Arrays.asList("Oily", "Combination")),
                        "Take adequate amount with spatula onto dry hand. Melt it by skin heat and tab on forehead, cheek and chin. Gently massage in circular motions to melt makeup away. Add small amount of water to emulsify, then rinse with lukewarm water.",
                        "5",
                        "Balm"
                )
        ));

        return cleanserInfo;
    }

    public static void addCleansersToFirestore() {

        List<IItem> cleansersList = getCleansers();

        for (IItem cleanser : cleansersList) {
            database.collection("cleanser").document(cleanser.getId()).set(cleanser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Cleanser Collection", cleanser.getId() + " added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Cleanser Collection", cleanser.getId() + " could not be added");
                }
            });
        }
    }

    public static List<IItem> getCleansers() {
        List<IItem> cleansersList = new ArrayList<IItem>();
        Map<String, ArrayList<Object>> cleansersInfo = getCleansersInfo();

        for (String key : cleansersInfo.keySet()) {
            String name = (String) cleansersInfo.get(key).get(0);
            String brand = (String) cleansersInfo.get(key).get(1);
            ArrayList<String> imageUri = (ArrayList<String>) cleansersInfo.get(key).get(2);
            double price = (double) cleansersInfo.get(key).get(3);
            String categoryName = (String) cleansersInfo.get(key).get(4);
            ArrayList<String> skinType = (ArrayList<String>) cleansersInfo.get(key).get(5);
            String howToUse = (String) cleansersInfo.get(key).get(6);
            String ph = (String) cleansersInfo.get(key).get(7);
            String cleanserType = (String) cleansersInfo.get(key).get(8);

            Cleanser cleanser = new Cleanser(key, name, brand, imageUri, price, categoryName, skinType, ph, cleanserType, howToUse);
            cleansersList.add(cleanser);
        }

        return cleansersList;
    }
}
