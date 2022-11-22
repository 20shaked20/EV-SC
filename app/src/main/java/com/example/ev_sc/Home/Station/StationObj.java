package com.example.ev_sc.Home.Station;

import com.google.firebase.firestore.GeoPoint;

public class StationObj implements StationInterface {

    private int grade;
    private double avg_grade;
    private String station_address;
    private GeoPoint location; // https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/GeoPoint
    private int charging_stations;
    private String station_name;
    private String[] reviews; // todo: this could probably better be represented as JSON or other data structure
    private String SID;

    public StationObj(int grade, String station_address, int charging_stations, String station_name, String[] reviews, String SID) {
        this.grade = grade;
        this.station_address = station_address;
        this.charging_stations = charging_stations;
        this.station_name = station_name;
        this.SID = SID;
        //todo: for loop to fit the reviews in the object
    }

    public String getID(){return SID;}
    public int getGrade() {
        return grade;
    }

    public double getAverageGrade() {return avg_grade;}

    public String getStation_address() {
        return station_address;
    }

    public int getCharging_stations() {
        return charging_stations;
    }

    public String getStation_name() {
        return station_name;
    }

    public String[] getReviews() {
        return reviews;
    }

    public GeoPoint getLocation(){return location;}

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setStation_address(String station_address) {
        this.station_address = station_address;
    }

    public void setCharging_stations(int charging_stations) {
        this.charging_stations = charging_stations;
    }

    public void setLocation(GeoPoint new_location){
        this.location = new_location;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public void setAvgGrade(double avgGrade){
        this.avg_grade = avgGrade;
    }

    // todo: function to calculate average grade based on reviews
}
