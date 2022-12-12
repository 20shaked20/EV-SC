package com.example.ev_sc.Reviews;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class reviewsDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    final private String TAG = "ReviewsDB";

    /**
     * this method adds a new Review to the database.
     *
     */
    public void AddReviewToDatabase(reviewsObj curr_review, String StationID) {
        Log.d(TAG,"Station ID: => "+ StationID + " Curr review => "+ curr_review.toString());
        CollectionReference f = fStore.collection("stations").document(StationID)
                .collection("reviews");
        Map<String, Object> review = new HashMap<>();
        review.put("UID_user", curr_review.getUID_user());
        review.put("Stars", curr_review.getStars());
        review.put("Review", curr_review.getReview());

        //Check//
        f.add(review).addOnSuccessListener(unused -> Log.d(TAG, "reviews Profile is created for "));

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
        String UID_user = doc.getString("UID_user");
        String review_ = doc.getString("Review");



        return new reviewsObj(UID_user, stars,review_);

    }
}
