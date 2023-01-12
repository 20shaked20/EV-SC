package com.example.ev_sc.Backend.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public interface FavoritesInterface {

    /**
     * Get name method
     *
     * @return the station name
     */
    String getStation_name();

    /**
     * Get location method
     *
     * @return the location representing the station (GeoPoint)
     */
    GeoPoint getStation_GeoPoint();

    /**
     * Get location method
     *
     * @return the location representing the station (Lat,Lng)
     */
    LatLng getStation_LatLng();

    /**
     * Get id method
     *
     * @return the station id
     */
    String getStation_id();
}
