package com.example.ev_sc.Backend.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

// https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/GeoPoint //
public class StationObj implements StationInterface, Parcelable {

    private static final String TAG = "StationObj"; // tag for logging

    private double sumOf_reviews;
    private double avg_grade;
    private String station_address;
    private GeoPoint location;
    private int charging_stations;
    private String station_name;
    private String SID;

    public StationObj(double grade, String station_address, int charging_stations, String station_name, GeoPoint location, String s_id, Double sumOf_reviews) {
        this.avg_grade = grade;
        this.station_address = station_address;
        this.charging_stations = charging_stations;
        this.station_name = station_name;
        this.SID = s_id;
        this.location = location;
        this.sumOf_reviews = sumOf_reviews;
    }

    public StationObj(StationObj station) {
        this.avg_grade = station.getAverageGrade();
        this.station_address = station.getStation_address();
        this.location = station.getLocation();
        this.charging_stations = station.getCharging_stations();
        this.station_name = station.getStation_name();
        this.SID = station.getID();
        this.sumOf_reviews = station.getSumOf_reviews();
    }

    @Override
    public String getID() {
        return SID;
    }

    @Override
    public double getSumOf_reviews() {
        return this.sumOf_reviews;
    }

    @Override
    public double getAverageGrade() {
        return avg_grade;
    }

    @Override
    public String getStation_address() {
        return station_address;
    }

    @Override
    public int getCharging_stations() {
        return charging_stations;
    }

    @Override
    public String getStation_name() {
        return station_name;
    }

    @Override
    public GeoPoint getLocation() {
        return location;
    }

    @Override
    public void setStation_address(String station_address) {
        this.station_address = station_address;
    }

    @Override
    public void setCharging_stations(int charging_stations) {
        this.charging_stations = charging_stations;
    }

    @Override
    public LatLng getLatLng() {
        return new LatLng(this.location.getLatitude(), this.location.getLongitude());
    }

    @Override
    public void setSumOf_reviews(double num_of_reviews) {
        this.sumOf_reviews = num_of_reviews;
    }

    @Override
    public void setLocation(GeoPoint new_location) {
        this.location = new_location;
    }

    @Override
    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    @Override
    public void setAvgGrade(double avgGrade) {
        this.avg_grade = avgGrade;
    }


    @NonNull
    @Override
    public String toString() { // override toString method to better represent station data (logging etc.)
        return "Station Name: " + this.getStation_name() + "\n" +
                "Station Average Grade: " + this.getAverageGrade() + "\n" +
                "Station Location: " + this.getLocation().toString() + "\n" +
                "Charging Stations: " + this.getCharging_stations() + "\n" +
                "Station Address: " + this.getStation_address() + "\n" +
                "Station ID: " + this.getID() + "\n";
    }

    // ===============================================
    // All code below is the implementation of the Parcelable interface
    // ===============================================

    public StationObj(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StationObj createFromParcel(Parcel in) {
            return new StationObj(in);
        }

        public StationObj[] newArray(int size) {
            return new StationObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(avg_grade);
        dest.writeString(station_address);
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
        dest.writeInt(charging_stations);
        dest.writeString(station_name);
        dest.writeString(SID);

    }

    private void readFromParcel(Parcel in) {

        avg_grade = in.readDouble();
        station_address = in.readString();
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        location = new GeoPoint(lat, lon);
        charging_stations = in.readInt();
        station_name = in.readString();
        SID = in.readString();
    }

    // ===============================================
    // End of Parcelable implementation
    // ===============================================

}
