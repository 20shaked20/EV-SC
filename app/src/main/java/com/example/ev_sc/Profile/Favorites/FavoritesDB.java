package com.example.ev_sc.Profile.Favorites;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<FavoriteObj> station_parser(JsonArray parser) {
        List<FavoriteObj> parsed_stations_list = new ArrayList<>();
        for (JsonElement element : parser) {
            JsonObject station = element.getAsJsonObject();
            Log.d(TAG, "JSON Object: " + station);

            JsonObject locationObject = station.get("Location").getAsJsonObject();
            double latitude = locationObject.get("latitude").getAsDouble();
            double longitude = locationObject.get("longitude").getAsDouble();
            GeoPoint location = new GeoPoint(latitude, longitude);

            String sId = station.get("SID").getAsString();
            String name = station.get("Station").getAsString();

            FavoriteObj favoriteObj = new FavoriteObj(location, sId, name);
            // Add the new Favorite station to the all_stations list
            parsed_stations_list.add(favoriteObj);
        }
        return parsed_stations_list;
    }
}
