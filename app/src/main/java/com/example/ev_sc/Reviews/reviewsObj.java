package com.example.ev_sc.Reviews;

public class reviewsObj {

    private String UID_user;
    private Double stars;
    private String review;


    public reviewsObj(String UID_user, Double x, String review) {

        this.UID_user = UID_user;
        this.stars = x;
        this.review = review;
    }

    public reviewsObj(reviewsObj review) {

        this.UID_user = review.UID_user;
        this.stars = review.stars;
        this.review = review.review;
    }


    public String getUID_user() {
        return this.UID_user;
    }

    public Double getStars() {
        return this.stars;
    }

    public String getReview() {
        return this.review;
    }


    public void setUID_user(String uid) {
        this.UID_user = uid;
    }

    public void setStars(Double x) {
        this.stars = x;
    }

    public void setReview(String s) {
        this.review = s;
    }
}
