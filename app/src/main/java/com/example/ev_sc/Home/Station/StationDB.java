package com.example.ev_sc.Home.Station;
import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.ev_sc.Home.Station.StationObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StationDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public void AddStationToDatabase(StationObj Station) {
        DocumentReference documentReference = fStore.collection("stations").document(Station.getID());
        Map<String, Object> station = new HashMap<>();

        station.put("Address",Station.getStation_address());
        station.put("Average Rating",Station.getAverageGrade());
        station.put("Charging Stations",Station.getCharging_stations());
        station.put("Location",Station.getLocation());
        station.put("Name",Station.getStation_name());

        documentReference.set(station).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user Profile is created for " + Station.getID());
            }
        });

    }

    public static void main(String[] args) {
        StationDB db = new StationDB();
        StationObj test_station = new StationObj(1,"test",1,"abu test", new String[]{"test1", "test2"},"test_station");
        db.AddStationToDatabase(test_station);
    }
}

