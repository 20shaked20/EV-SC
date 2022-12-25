package com.example.ev_sc.Profile.Favorites;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class FavoritesDB {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    final private String TAG = "FavoritesDB";

    /**
     * this method adds a new favorite station to the database.
     */
    public void AddFavoriteStationToDB(FavoriteObj curr_fav, String UID) {
        Log.d(TAG, "Added new favorite Station: => " + curr_fav.toString() + " For user: => " + UID);
        CollectionReference f = fStore.collection("users").document(UID)
                .collection("favorites");

        Map<String, Object> fav_station = new HashMap<>();
        fav_station.put("Station", curr_fav.getStation_name());
        fav_station.put("SID", curr_fav.getStation_id());
        fav_station.put("Location", curr_fav.getStation_GeoPoint());

        //Check//
        f.add(fav_station).addOnSuccessListener(unused -> Log.d(TAG, "reviews Profile is created for "));

    }

    /**
     * this method parses a firebase Favorite document into a Favorite object.
     *
     * @param doc Firebase Favorite Document
     * @return Object Of Favorite Type.
     */
    public FavoriteObj GetFavoriteStationFromDB(QueryDocumentSnapshot doc) {
        //parser from firebase to object
        String s_name = doc.getString("Station");
        String SID = doc.getString("SID");
        GeoPoint s_loc = doc.getGeoPoint("Location");

        return new FavoriteObj(SID, s_name, s_loc);

    }
}
