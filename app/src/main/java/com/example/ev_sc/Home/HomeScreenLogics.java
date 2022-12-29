package com.example.ev_sc.Home;


public class HomeScreenLogics {

    private final int EARTH_RADIUS = 6371000; // for Haversine formula, value in meters

    // Haversine formula to calculate distance between two points on a sphere
    public double calcDist(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lonDistance = Math.toRadians(lon1 - lon2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }


}
