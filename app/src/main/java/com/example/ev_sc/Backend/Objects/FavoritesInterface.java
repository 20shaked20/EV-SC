package com.example.ev_sc.Backend.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public interface FavoritesInterface {

    /**
     * Get name method
     *
     * @return the station name
     */
    public String getStation_name();

    /**
     * Get location method
     *
     * @return the location representing the station (GeoPoint)
     */
    public GeoPoint getStation_GeoPoint();

    /**
     * Get location method
     *
     * @return the location representing the station (Lat,Lng)
     */
    public LatLng getStation_LatLng();

    /**
     * Get id method
     *
     * @return the station id
     */
    public String getStation_id();
}
