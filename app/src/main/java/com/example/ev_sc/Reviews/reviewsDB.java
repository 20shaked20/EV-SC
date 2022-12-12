package com.example.ev_sc.Reviews;

import android.util.Log;

import com.example.ev_sc.Home.Station.StationObj;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class reviewsDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    final private String TAG = "ReviewsDB";

    /**
     * this method adds a new Review to the database.
     *
     */
    public void AddReviewToDatabase(reviewsObj curr_review) {
        Task<QuerySnapshot> documentReference = fStore.collection("stations/reviews").get();
        Map<String, Object> review = new HashMap<>();
        review.put("UID", curr_review.getUID());
        review.put("Stars", curr_review.getStars());
        review.put("Review", curr_review.getReview());
//
//        //Check//
//        documentReference.sub(review).addOnSuccessListener(unused -> Log.d(TAG, "reviews Profile is created for "));

    }

    /**
     * this method parses a firebase Review document into a review object.
     * @param doc Firebase review Document
     * @return Object Of review Type.
     */
    public reviewsObj GetReviewFromDatabase(QueryDocumentSnapshot doc)
    {
        //parser from firebase to object
        Double stars= doc.getDouble("Stars");
        String UID = doc.getString("UID");
        String Review = doc.getString("Review");

//
//        assert c_stations != null;
//        assert avg_rating!= null;

        return new reviewsObj(UID, stars,Review);

    }
}
