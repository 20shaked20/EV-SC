package com.example.ev_sc.Home.Station;

public interface StationInterface {

    /**
     * @return Integer representing the Avg grade of the station.
     */
    public int getGrade();

    /**
     * @return String representing the Address of the station
     */
    public String getStation_address();

    /**
     * @return Integer representing the amount of available charging station in the station.
     */
    public int getCharging_stations();

    /**
     * @return String representing the Station name.
     */
    public String getStation_name();

    /**
     * @return List of String that contains all the reviews given by users for this station.
     */
    public String[] getReviews();

    public void setGrade(int grade);

    public void setStation_address(String station_address);

    public void setCharging_stations(int charging_stations);

    public void setStation_name(String station_name);

    public void setReviews(String[] reviews);

}
