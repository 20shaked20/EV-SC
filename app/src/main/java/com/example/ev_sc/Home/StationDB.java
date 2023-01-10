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

//    /**
//     * this method parses a firebase Station document into a station object.
//     * @param doc Firebase Station Document
//     * @return Object Of Station Type.
//     */
//    public StationObj getStationFromDatabase(QueryDocumentSnapshot doc)
//    {
//        //parser from firebase to object
//        Double avg_rating = doc.getDouble("Average Rating");
//        Double c_stations = doc.getDouble("Charging Stations");
//        String s_name = doc.getString("Name");
//        String s_address = doc.getString("Address");
//        GeoPoint s_loc = doc.getGeoPoint("Location");
//        String s_id = doc.getString("SID");
//        Double sumof_reviews = doc.getDouble("SumOf_reviews");
//        assert c_stations != null;
//        assert avg_rating!= null;
//
//        return new StationObj(avg_rating, s_address, c_stations.intValue(), s_name, s_loc,s_id,sumof_reviews);
//
//    }

//    /**
//     * this method retrieves a station from the database by its ID and returns it as a StationObj.
//     * @param stationID the ID of the station to retrieve
//     * @return the retrieved station as a StationObj
//     */
//    public StationObj getStationFromDatabase(String stationID) {
//        DocumentReference documentReference = fStore.collection("stations").document(stationID);
//        final StationObj[] station = {null};
//        documentReference.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult();
//                if (doc != null) {
//                    station[0] = getStationFromDatabase(doc);
//                }
//            } else {
//                Log.d(TAG, "Error getting document: ", task.getException());
//            }
//        });
//        return station[0];
//    }

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
        documentReference.set(station).addOnSuccessListener(unused -> Log.d("StationDB", "Station updated success!"));
    }

    public void deleteStationFromDatabase(StationObj Station){
        DocumentReference documentReference = fStore.collection("stations").document(Station.getID());
        documentReference.delete().addOnSuccessListener(unused -> Log.d(TAG, "Station deleted"));
    }

    public List<StationObj> station_parser(JsonArray parser){
        List<StationObj> parsed_stations_list = new ArrayList<>();
        for (JsonElement element: parser){
            JsonObject station = element.getAsJsonObject();
            Log.d(TAG, "JSON Object: " + station);
            int chargingStations = station.get("Charging Stations").getAsInt();
            String address = station.get("Address").getAsString();
            String name = station.get("Name").getAsString();
            double sumOfReviews = station.get("SumOf_reviews").getAsDouble();
            double avgRating = station.get("Average Rating").getAsDouble();
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

}

