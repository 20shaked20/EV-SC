package com.example.ev_sc.Backend.Objects;

public class reviewsObj implements reviewsInterface {

    private String UID_user;
    private double stars;
    private String review;


    public reviewsObj(String UID_user, double x, String review) {
        this.UID_user = UID_user;
        this.stars = x;
        this.review = review;
    }

    public reviewsObj(reviewsObj review) {
        this.UID_user = review.UID_user;
        this.stars = review.stars;
        this.review = review.review;
    }

    @Override
    public String getUID_user() {
        return this.UID_user;
    }

    @Override
    public double getStars() {
        return this.stars;
    }

    @Override
    public String getReview() {
        return this.review;
    }

    @Override
    public void setUID_user(String uid) { this.UID_user = uid; }

    @Override
    public void setStars(double x) {
        this.stars = x;
    }

    @Override
    public void setReview(String s) {
        this.review = s;
    }
}
