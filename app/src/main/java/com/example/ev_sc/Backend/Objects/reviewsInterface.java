package com.example.ev_sc.Backend.Objects;

public interface reviewsInterface {
    /**
     * @return String representing the UID of the user
     */
    String getUID_user();

    /**
     * @return Double representing the Stars of the Review
     */
    double getStars();

    /**
     * @return String representing the Review
     */
    String getReview();

    /**
     * Changes the user id for the current review
     *
     * @param uid new UID
     */
    void setUID_user(String uid);

    /**
     * Sets a new amount of stars for the current review
     *
     * @param x amount of stars
     */
    void setStars(double x);

    /**
     * Sets a new review for the current review
     *
     * @param s string contains a review.
     */
    void setReview(String s);
}
