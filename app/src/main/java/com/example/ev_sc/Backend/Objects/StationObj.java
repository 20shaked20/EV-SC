package com.example.ev_sc.Backend.Objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class StationObj implements StationInterface, Parcelable {

    private static final String TAG = "StationObj"; // tag for logging

    private double sumOf_reviews;
    private double avg_grade;
    private String station_address;
    private GeoPoint location; // https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/GeoPoint
    private int charging_stations;
    private String station_name;
    //private String[] reviews; // todo: this could probably better be represented as JSON or other data structure
    private String SID;

    public StationObj(double grade, String station_address, int charging_stations, String station_name, GeoPoint location, String s_id,Double sumOf_reviews) {
        this.avg_grade = grade;
        this.station_address = station_address;
        this.charging_stations = charging_stations;
        this.station_name = station_name;
        this.SID = s_id;
        this.location = location;
        this.sumOf_reviews=sumOf_reviews;
        //todo: for loop to fit the reviews in the object
    }

    public StationObj(StationObj station) {
        this.avg_grade = station.getAverageGrade();
        this.station_address = station.getStation_address();
        this.location = station.getLocation();
        this.charging_stations = station.getCharging_stations();
        this.station_name = station.getStation_name();
        this.SID = station.getID();
        this.sumOf_reviews= station.getSumOf_reviews();
    }

    public String getID() {
        return SID;
    }

    public double getSumOf_reviews() {
    return this.sumOf_reviews;
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
    public void setSumOf_reviews(double num_of_reviews){this.sumOf_reviews=num_of_reviews;}

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
                "Station Average Grade: " + this.getAverageGrade() + "\n" +
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

    public static class StationDB {

        static FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        final private String TAG = "StationDB";

        /**
         * this method adds a new station to the database.
         * @param Station Station Object
         */
        public void AddStationToDatabase(StationObj Station) {
            DocumentReference documentReference = fStore.collection("stations").document(Station.getID());
            Map<String, Object> station = new HashMap<>();

            station.put("Address",Station.getStation_address());
            station.put("Average Rating",Station.getAverageGrade());
            station.put("Charging Stations",Station.getCharging_stations());
            station.put("Location",Station.getLocation());
            station.put("Name",Station.getStation_name());
            station.put("SID", Station.getID());
            station.put("SumOf_reviews", Station.getSumOf_reviews());


            //Check//
            documentReference.set(station).addOnSuccessListener(unused -> Log.d(TAG, "Station Profile is created for " + Station.getID()));

        }

        /**
         * this method parses a firebase Station document into a station object.
         * @param doc Firebase Station Document
         * @return Object Of Station Type.
         */
        public StationObj GetStationFromDatabase(QueryDocumentSnapshot doc)
        {
            //parser from firebase to object
            Double avg_rating = doc.getDouble("Average Rating");
            Double c_stations = doc.getDouble("Charging Stations");
            String s_name = doc.getString("Name");
            String s_address = doc.getString("Address");
            GeoPoint s_loc = doc.getGeoPoint("Location");
            String s_id = doc.getString("SID");
            Double sumof_reviews = doc.getDouble("SumOf_reviews");
            assert c_stations != null;
            assert avg_rating!= null;

            return new StationObj(avg_rating, s_address, c_stations.intValue(), s_name, s_loc,s_id,sumof_reviews);

        }

        public static void updateStationToDatabase(StationObj Station) {
            DocumentReference documentReference = fStore.collection("stations").document(Station.getID());
            Map<String, Object> station = new HashMap<>();

            station.put("Address", Station.getStation_address());
            station.put("Average Rating", Station.getAverageGrade());
            station.put("Charging Stations", Station.getCharging_stations());
            station.put("Location", Station.getLocation());
            station.put("Name", Station.getStation_name());
            station.put("SID", Station.getID());
            station.put("SumOf_reviews", Station.getSumOf_reviews());


            //Check//
            documentReference.set(station);
        }

    }
}
