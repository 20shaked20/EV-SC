package com.example.ev_sc.Reviews;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class reviewsDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    final private String TAG = "ReviewsDB";

    /**
     * this method adds a new Review to the database.
     *
     * @return a mapped review object
     */
    public HashMap<String, Object> MapReview(reviewsObj curr_review) {
        Log.d(TAG, "Mapping review => " + curr_review.toString());

        HashMap<String, Object> review = new HashMap<>();
        review.put("Review", curr_review.getReview());
        review.put("Stars", curr_review.getStars());
        review.put("UID_user", curr_review.getUID_user());

        //Check//
        return review;

    }
}
