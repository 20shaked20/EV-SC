package com.example.ev_sc.Profile.Favorites;

import android.util.Log;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;

public class FavoritesDB {


    final private String TAG = "FavoritesDB";

    /**
     * this method maps a favorite object
     */
    public HashMap<String, Object> MapFavorite(FavoriteObj curr_fav) {
        Log.d(TAG, "Added new favorite Station  => " + curr_fav.toString());

        HashMap<String, Object> fav_station = new HashMap<>();
        fav_station.put("Location", curr_fav.getStation_GeoPoint());
        fav_station.put("SID", curr_fav.getStation_id());
        fav_station.put("Station", curr_fav.getStation_name());

        return fav_station;
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
