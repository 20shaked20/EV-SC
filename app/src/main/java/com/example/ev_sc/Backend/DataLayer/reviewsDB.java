package com.example.ev_sc.Backend.DataLayer;

import android.util.Log;
import com.example.ev_sc.Backend.Objects.reviewsObj;
import java.util.HashMap;

public class reviewsDB {

    final private String TAG = "ReviewsDB";

    /**
     * this method maps a review object
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
