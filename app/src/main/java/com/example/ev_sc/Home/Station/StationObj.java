package com.example.ev_sc.Home.Station;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.ev_sc.Person.UserObj;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.UUID;

public class StationObj implements StationInterface, Parcelable {

    private double grade;
    private double avg_grade;
    private String station_address;
    private GeoPoint location; // https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/GeoPoint
    private int charging_stations;
    private String station_name;
    //private String[] reviews; // todo: this could probably better be represented as JSON or other data structure
    private String SID;

    public StationObj(double grade, String station_address, int charging_stations, String station_name, GeoPoint location) {
        this.grade = grade;
        this.station_address = station_address;
        this.charging_stations = charging_stations;
        this.station_name = station_name;
        this.SID = UUID.randomUUID().toString();
        this.location = location;
        //todo: for loop to fit the reviews in the object
    }

    public StationObj(StationObj station) {
        this.grade = station.getGrade();
        this.avg_grade = station.getAverageGrade();
        this.station_address = station.getStation_address();
        this.location = station.getLocation();
        this.charging_stations = station.getCharging_stations();
        this.station_name = station.getStation_name();
        this.SID = station.getID();
    }

    public String getID() {
        return SID;
    }

    public double getGrade() {
        return grade;
    }

    public double getAverageGrade() {
        return avg_grade;
    }

    public String getStation_address() {
        return station_address;
    }

    public int getCharging_stations() {
        return charging_stations;
    }

    public String getStation_name() {
        return station_name;
    }


    public GeoPoint getLocation() {
        return location;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setStation_address(String station_address) {
        this.station_address = station_address;
    }

    public void setCharging_stations(int charging_stations) {
        this.charging_stations = charging_stations;
    }

    /**
     * this method returns a latlng object from the location of the station.
     * @return LatLng object.
     */
    public LatLng getLatLng() {
        return new LatLng(this.location.getLatitude(), this.location.getLongitude());
    }

    public void setLocation(GeoPoint new_location) {
        this.location = new_location;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public void setAvgGrade(double avgGrade) {
        this.avg_grade = avgGrade;
    }


    @NonNull
    public String toString() { // override toString method to better represent station data (logging etc.)
        return "Station Name: " + this.getStation_name() + "\n" +
                "Station Grade: " + this.getGrade() + "\n" +
                "Station Location: " + this.getLocation().toString() + "\n" +
                "Charging Stations: " + this.getCharging_stations() + "\n" +
                "Station Address: " + this.getStation_address() + "\n" +
                "Station ID: " + this.getID() + "\n";
    }

    // todo: function to calculate average grade based on reviews

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
        dest.writeDouble(grade);
        dest.writeDouble(avg_grade);
        dest.writeString(station_address);
        dest.writeDouble(location.getLatitude());
        dest.writeDouble(location.getLongitude());
        dest.writeInt(charging_stations);
        dest.writeString(station_name);
        dest.writeString(SID);

    }

    private void readFromParcel(Parcel in) {

        grade = in.readInt();
        avg_grade = in.readDouble();
        station_address = in.readString();
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        location = new GeoPoint(lat, lon);
        charging_stations = in.readInt();
        station_name = in.readString();
        SID = in.readString();
    }

    // ===============================================
    // End of Parcelable implementation
    // ===============================================

}
