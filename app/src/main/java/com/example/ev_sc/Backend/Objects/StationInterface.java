package com.example.ev_sc.Backend.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public interface StationInterface {

    /**
     * Gets the current station id
     *
     * @return string of station ID
     */
    String getID();

    /**
     * Gets the current amount of reviews given to the station
     *
     * @return amount of reviews.
     */
    double getSumOf_reviews();

    /**
     * @return Integer representing the Avg grade of the station.
     */
    double getAverageGrade();

    /**
     * @return String representing the Address of the station
     */
    String getStation_address();

    /**
     * @return Integer representing the amount of available charging station in the station.
     */
    int getCharging_stations();

    /**
     * @return String representing the Station name.
     */
    String getStation_name();

    /**
     * Changes the current grade of the station
     *
     * @param avgGrade new grade
     */
    void setAvgGrade(double avgGrade);

    /**
     * Gets the location of the station in GeoPoint object
     *
     * @return GeoPoint object of the location (lat,lng)
     */
    GeoPoint getLocation();

    /**
     * Changes the station address
     *
     * @param station_address a new station address
     */
    void setStation_address(String station_address);

    /**
     * Changes the amount of available chairing station points in the station
     *
     * @param charging_stations amount of charging.
     */
    void setCharging_stations(int charging_stations);

    /**
     * this method returns a latlng object from the location of the station.
     *
     * @return LatLng object.
     */
    LatLng getLatLng();

    /**
     * Changes the amount of reviews given by users.
     *
     * @param num_of_reviews new amount of reviews.
     */
    void setSumOf_reviews(double num_of_reviews);

    /**
     * Sets a new location for the station in manner of GeoPoint.
     *
     * @param new_location new location
     */
    void setLocation(GeoPoint new_location);

    /**
     * sets a new name for the station
     *
     * @param station_name new station name
     */
    void setStation_name(String station_name);


}
