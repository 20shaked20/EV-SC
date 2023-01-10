package com.example.ev_sc.Profile.Favorites;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class FavoriteObj implements FavoritesInterface {

    private String station_name;
    private String station_id;
    private GeoPoint station_location;

    public FavoriteObj(GeoPoint loc, String id, String name) {
        this.station_id = id;
        this.station_name = name;
        this.station_location = loc;
    }

    @Override
    public String getStation_name() {
        return this.station_name;
    }

    @Override
    public GeoPoint getStation_GeoPoint() {
        return this.station_location;
    }

    @Override
    public LatLng getStation_LatLng() {
        return new LatLng(this.station_location.getLatitude(), this.station_location.getLongitude());
    }

    @Override
    public String getStation_id() {
        return station_id;
    }

    @Override
    public String toString() {
        return "FavoriteObj{" +
                "station_name='" + station_name + '\'' +
                ", station_id='" + station_id + '\'' +
                ", station_location=" + station_location +
                '}';
    }
}
