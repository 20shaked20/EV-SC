package com.example.ev_sc.Home;

import android.util.Log;

import com.example.ev_sc.Home.Station.StationObj;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationDB {

    static FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    final private String TAG = "StationDB";

    public HashMap<String, Object> MapStation(StationObj station) {
        HashMap<String, Object> mapped_station = new HashMap<>();

        mapped_station.put("Address", station.getStation_address());
        mapped_station.put("AverageRating", station.getAverageGrade());
        mapped_station.put("ChargingStations", station.getCharging_stations());
        mapped_station.put("Location", station.getLocation());
        mapped_station.put("Name", station.getStation_name());
        mapped_station.put("SID", station.getID());
        mapped_station.put("SumOf_reviews", station.getSumOf_reviews());

        Log.d(TAG, "Station mapped to: " + "\n" + mapped_station);
        return mapped_station;
    }

    public List<StationObj> station_parser(JsonArray parser) {
        List<StationObj> parsed_stations_list = new ArrayList<>();
        for (JsonElement element : parser) {
            JsonObject station = element.getAsJsonObject();
            Log.d(TAG, "JSON Object: " + station);

            int chargingStations = station.get("ChargingStations").getAsInt();
            String address = station.get("Address").getAsString();
            String name = station.get("Name").getAsString();
            double sumOfReviews = station.get("SumOf_reviews").getAsDouble();
            double avgRating = station.get("AverageRating").getAsDouble();
            String sId = station.get("SID").getAsString();

            JsonObject locationObject = station.get("Location").getAsJsonObject();

            double latitude = locationObject.get("latitude").getAsDouble();
            double longitude = locationObject.get("longitude").getAsDouble();

            GeoPoint location = new GeoPoint(latitude, longitude);

            StationObj station_obj = new StationObj(avgRating, address, chargingStations, name, location, sId, sumOfReviews);

            // Add the new StationObj object to the all_stations map
            parsed_stations_list.add(station_obj);
        }
        return parsed_stations_list;
    }

    public void deleteStationFromDatabase(StationObj Station) {
        DocumentReference documentReference = fStore.collection("stations").document(Station.getID());
        documentReference.delete().addOnSuccessListener(unused -> Log.d(TAG, "Station deleted"));
    }

}

