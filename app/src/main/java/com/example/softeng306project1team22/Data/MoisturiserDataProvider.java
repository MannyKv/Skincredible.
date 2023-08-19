package com.example.softeng306project1team22.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Moisturiser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoisturiserDataProvider {

    public static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static Map<String, ArrayList<Object>> getMoisturisersInfo() {
        Map<String, ArrayList<Object>> moisturisersInfo = new LinkedHashMap<String, ArrayList<Object>>();

        moisturisersInfo.put("mos1", new ArrayList<Object>(
                Arrays.asList(
                        "1025 Dokdo Cream",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("mos1_img1", "mos1_img2", "mos1_img3")),
                        "47.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Dry", "Sensitive")),
                        "Cream",
                        "Apply to face after cleansing.",
                        "Daytime"
                )
        ));
        moisturisersInfo.put("mos2", new ArrayList<Object>(
                Arrays.asList(
                        "Advanced Snail 92 All in one Cream",
                        "COSRX",
                        new ArrayList<String>(Arrays.asList("mos2_img1", "mos2_img2", "mos2_img3")),
                        "30.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Normal", "Dry", "Combination")),
                        "Cream",
                        "Cleanse and dry area around acne and pimples. Apply a patch to the area.",
                        "Night time"
                )
        ));
        moisturisersInfo.put("mos3", new ArrayList<Object>(
                Arrays.asList(
                        "Aloe Real Cool Soothing Gel",
                        "Benton",
                        new ArrayList<String>(Arrays.asList("mos3_img1", "mos3_img2", "mos3_img3")),
                        "12.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Combination")),
                        "Gel",
                        "Apply generously to face, body or scalp then gently pat until absorbed.",
                        "Daytime"
                )
        ));
        moisturisersInfo.put("mos4", new ArrayList<Object>(
                Arrays.asList(
                        "AC Collection Lightweight Soothing Moisturiser",
                        "COSRX",
                        new ArrayList<String>(Arrays.asList("mos4_img1", "mos4_img2", "mos4_img3")),
                        "30.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Dry", "Oily", "Sensitive", "Combination")),
                        "Cream",
                        "Gently apply a proper amount of the cream onto face after cleansing and toning, avoiding eye area. Tap the area for better absorption.",
                        "Night time"
                )
        ));
        moisturisersInfo.put("mos5", new ArrayList<Object>(
                Arrays.asList(
                        "Fundamental Water Gel Cream",
                        "Klairs",
                        new ArrayList<String>(Arrays.asList("mos5_img1", "mos5_img2", "mos5_img3")),
                        "48.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Normal", "Combination")),
                        "Gel",
                        "Apply adequate amount at last stage of skin care routine, after cleansing",
                        "Night time"
                )
        ));
        moisturisersInfo.put("mos6", new ArrayList<Object>(
                Arrays.asList(
                        "Birch Juice Moisturising Cream",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("mos6_img1", "mos6_img2", "mos6_img3")),
                        "43.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Normal", "Dry", "Sensitive", "Combination")),
                        "Cream",
                        "At the last step of skincare, apply appropriate amount to skin to create moisture barrier. Allow for absorption.",
                        "Daytime"
                )
        ));
        moisturisersInfo.put("mos7", new ArrayList<Object>(
                Arrays.asList(
                        "Dermide Balancing Barrier Balm",
                        "Purito",
                        new ArrayList<String>(Arrays.asList("mos7_img1", "mos7_img2", "mos7_img3")),
                        "40.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Normal", "Dry")),
                        "Balm",
                        "Apply every morning and evening, gently pressing onto dry skin",
                        "Daytime and Night time"
                )
        ));
        moisturisersInfo.put("mos8", new ArrayList<Object>(
                Arrays.asList(
                        "Clean Lotus Water Cream",
                        "Rovectin",
                        new ArrayList<String>(Arrays.asList("mos8_img1", "mos8_img2", "mos8_img3")),
                        "27.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Oily", "Sensitive", "Dry", "Combination")),
                        "Cream",
                        "After cleansing, toning and treatment products, apply gently over the skin. If skin is very dry, apply a thicker layer for added moisture.",
                        "Daytime and Night time"
                )
        ));
        moisturisersInfo.put("mos9", new ArrayList<Object>(
                Arrays.asList(
                        "Cicaluronic Gel Treatment",
                        "Mizon",
                        new ArrayList<String>(Arrays.asList("mos9_img1", "mos9_img2", "mos9_img3")),
                        "36.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Sensitive")),
                        "Gel",
                        "Gently apply to face, avoiding eye and mouth area. Tap the areas where the cream was applied in order for it to be absorbed. Reapply the cream on extremely dry areas.",
                        "Daytime"
                )
        ));
        moisturisersInfo.put("mos10", new ArrayList<Object>(
                Arrays.asList(
                        "Ceramidin Oil Balm",
                        "Dr.Jart+",
                        new ArrayList<String>(Arrays.asList("mos10_img1", "mos10_img2", "mos10_img3")),
                        "43.00",
                        "Moisturiser",
                        new ArrayList<String>(Arrays.asList("Dry")),
                        "Oil Balm",
                        "Gently apply on dry skin.",
                        "Daytime and Night time"
                )
        ));

        return moisturisersInfo;
    }

    public static void addMoisturisersToFirestore() {

        List<IItem> moisturisersList = getMoisturisers();

        for (IItem moisturiser : moisturisersList) {
            database.collection("moisturiser").document(moisturiser.getId()).set(moisturiser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Moisturiser Collection", moisturiser.getId() + " added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Moisturiser Collection", moisturiser.getId() + " could not be added");
                }
            });
        }
    }

    public static List<IItem> getMoisturisers() {
        List<IItem> moisturisersList = new ArrayList<IItem>();
        Map<String, ArrayList<Object>> moisturisersInfo = getMoisturisersInfo();

        for (String key : moisturisersInfo.keySet()) {
            String name = (String) moisturisersInfo.get(key).get(0);
            String brand = (String) moisturisersInfo.get(key).get(1);
            ArrayList<String> imageNames = (ArrayList<String>) moisturisersInfo.get(key).get(2);
            String price = (String) moisturisersInfo.get(key).get(3);
            String categoryName = (String) moisturisersInfo.get(key).get(4);
            ArrayList<String> skinType = (ArrayList<String>) moisturisersInfo.get(key).get(5);
            String moisturiserType = (String) moisturisersInfo.get(key).get(6);
            String howToUse = (String) moisturisersInfo.get(key).get(7);
            String timeToUse = (String) moisturisersInfo.get(key).get(8);

            Moisturiser moisturiser = new Moisturiser(key, name, brand, imageNames, price, categoryName, skinType, moisturiserType, howToUse, timeToUse);
            moisturisersList.add(moisturiser);
        }

        return moisturisersList;
    }
}
