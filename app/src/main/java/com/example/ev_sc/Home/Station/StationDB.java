package com.example.ev_sc.Home.Station;
import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.ev_sc.Home.Station.StationObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class StationDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

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

        assert c_stations != null;
        assert avg_rating!= null;

        return new StationObj(avg_rating, s_address, c_stations.intValue(), s_name, s_loc);

    }

}

