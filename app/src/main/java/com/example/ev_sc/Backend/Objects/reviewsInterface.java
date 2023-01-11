package com.example.ev_sc.Backend.Objects;

public interface reviewsInterface {
    /**
     * @return String representing the UID of the user
     */
    public String getUID_user();
    /**
     * @return Double representing the Stars of the Review
     */
    public Double getStars();
    /**
     * @return String representing the Review
     */
    public String getReview();


    public void setUID_user(String uid);

    public void setStars(Double x);

    public void setReview(String s);
}
