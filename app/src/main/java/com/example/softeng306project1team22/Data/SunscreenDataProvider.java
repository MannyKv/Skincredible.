package com.example.softeng306project1team22.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.softeng306project1team22.Models.IItem;
import com.example.softeng306project1team22.Models.Sunscreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SunscreenDataProvider {

    public static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static Map<String, ArrayList<Object>> getSunscreenInfo() {
        Map<String, ArrayList<Object>> sunscreenInfo = new LinkedHashMap<String, ArrayList<Object>>();

        sunscreenInfo.put("sun1", new ArrayList<Object>(
                Arrays.asList(
                        "Aqua Sun Gel",
                        "Missha",
                        new ArrayList<String>(Arrays.asList("sun1_img1", "sun1_img2", "sun1_img3")),
                        16.99,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Normal", "Oily")),
                        "Chemical",
                        "SPF50+",
                        "Apply to face after moisturiser."
                )
        ));
        sunscreenInfo.put("sun2", new ArrayList<Object>(
                Arrays.asList(
                        "Birch Juice Moisturising Sunscreen",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("sun2_img1", "sun2_img2", "sun2_img3")),
                        35.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Oily", "Combination")),
                        "Chemical",
                        "SPF50+",
                        "Apply evenly on areas prone to UV exposure."
                )
        ));
        sunscreenInfo.put("sun3", new ArrayList<Object>(
                Arrays.asList(
                        "Aloe Soothing Sun Scream",
                        "COSRX",
                        new ArrayList<String>(Arrays.asList("sun3_img1", "sun3_img2", "sun3_img3")),
                        21.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Normal", "Sensitive", "Combination")),
                        "Chemical",
                        "SPF50",
                        "Blend in at final stage of basic care and before base makeup. Spread to whole face before ultraviolet ray exposure, with an overcoat in the intervals of 2 hours if staying outdoors for a long time."
                )
        ));
        sunscreenInfo.put("sun4", new ArrayList<Object>(
                Arrays.asList(
                        "Every Sun Day Moisturising Sun SPF50",
                        "Dr.Jart+",
                        new ArrayList<String>(Arrays.asList("sun4_img1", "sun4_img2", "sun4_img3")),
                        28.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Dry", "Oily", "Combination")),
                        "Chemical",
                        "SPF50",
                        "Apply a sufficient amount 20-30 minutes before exposure to ultraviolet rays."
                )
        ));
        sunscreenInfo.put("sun5", new ArrayList<Object>(
                Arrays.asList(
                        "All Around Safe Block Essence Sun SPF45",
                        "Missha",
                        new ArrayList<String>(Arrays.asList("sun5_img1", "sun5_img2", "sun5_img3")),
                        22.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Normal", "Combination")),
                        "Chemical",
                        "SPF45",
                        "Apply appropriate amount onto face at the last step of basic skincare, patting lightly until fully absorbed. Apply evenly onto neck, arms and legs and reapply as needed."
                )
        ));
        sunscreenInfo.put("sun6", new ArrayList<Object>(
                Arrays.asList(
                        "1025 Dokdo Sun Cream SPF50+",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("sun6_img1", "sun6_img2", "sun6_img3")),
                        35.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Dry", "Oily", "Combination")),
                        "Physical",
                        "SPF50+",
                        "Apply evenly to areas exposed to ultraviolet rays at the last stage of skincare."
                )
        ));
        sunscreenInfo.put("sun7", new ArrayList<Object>(
                Arrays.asList(
                        "Birch Juice Moisturising Mild-Up Sunscreen SPF50+",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("sun7_img1", "sun7_img2", "sun7_img3")),
                        35.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Dry", "Combination")),
                        "Physical",
                        "SPF50+",
                        "Apply evenly on areas prone to UV exposure."
                )
        ));
        sunscreenInfo.put("sun8", new ArrayList<Object>(
                Arrays.asList(
                        "365 Derma Relief Sun Cream SPF50+",
                        "Round Lab",
                        new ArrayList<String>(Arrays.asList("sun8_img1", "sun8_img2", "sun8_img3")),
                        35.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Normal", "Sensitive", "Combination")),
                        "Physical",
                        "SPF50+",
                        "Apply evenly to areas exposed to ultraviolet rays at the last stage of skincare."
                )
        ));
        sunscreenInfo.put("sun9", new ArrayList<Object>(
                Arrays.asList(
                        "Daily Go-To Sunscreen SPF50+",
                        "Purito",
                        new ArrayList<String>(Arrays.asList("sun9_img1", "sun9_img2", "sun9_img3")),
                        38.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Sensitive", "Combination")),
                        "Physical",
                        "SPF50+",
                        "Use daily as the last step of skincare routine. Apply evenly on the face 15 minutes before sun exposure and reapply every 2-3 hours during the day and immediately after towel drying, swimming or perspiring."
                )
        ));
        sunscreenInfo.put("sun10", new ArrayList<Object>(
                Arrays.asList(
                        "Every Sun Day Mind Sun SPF43",
                        "Dr.Jart+",
                        new ArrayList<String>(Arrays.asList("sun10_img1", "sun10_img2", "sun10_img3")),
                        28.0,
                        "Sunscreen",
                        new ArrayList<String>(Arrays.asList("Normal", "Oily", "Combination")),
                        "Physical",
                        "SPF43",
                        "Apply sufficient amount 20-30 minutes before exposure to ultraviolet rays."
                )
        ));

        return sunscreenInfo;
    }

    public static void addSunscreenToFirestore() {

        List<IItem> sunscreenList = getSunscreen();

        for (IItem sunscreen : sunscreenList) {
            database.collection("sunscreen").document(sunscreen.getId()).set(sunscreen).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("Sunscreen Collection", sunscreen.getId() + " added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Sunscreen Collection", sunscreen.getId() + " could not be added");
                }
            });
        }
    }

    public static List<IItem> getSunscreen() {
        List<IItem> sunscreenList = new ArrayList<IItem>();
        Map<String, ArrayList<Object>> sunscreenInfo = getSunscreenInfo();

        for (String key : sunscreenInfo.keySet()) {
            String name = (String) sunscreenInfo.get(key).get(0);
            String brand = (String) sunscreenInfo.get(key).get(1);
            ArrayList<String> imageNames = (ArrayList<String>) sunscreenInfo.get(key).get(2);
            String price = (String) sunscreenInfo.get(key).get(3);
            String categoryName = (String) sunscreenInfo.get(key).get(4);
            ArrayList<String> skinType = (ArrayList<String>) sunscreenInfo.get(key).get(5);
            String sunscreenType = (String) sunscreenInfo.get(key).get(6);
            String spf = (String) sunscreenInfo.get(key).get(7);
            String howToUse = (String) sunscreenInfo.get(key).get(8);

            Sunscreen sunscreen = new Sunscreen(key, name, brand, imageNames, price, categoryName, skinType, sunscreenType, spf, howToUse);
            sunscreenList.add(sunscreen);
        }

        return sunscreenList;
    }
}

