package com.example.ev_sc.Home.Station;

public class StationObj {

    public int grade;
    public String station_adress;
    public int charging_stations;
    public String station_name;
    public String[] reviews;

    public StationObj(int grade, String station_adress, int charging_stations, String station_name, String[] reviews) {
        this.grade = grade;
        this.station_adress = station_adress;
        this.charging_stations = charging_stations;
        this.station_name = station_name;
        //todo: for loop to fit the reviews in the object
    }

    public int getGrade() {
        return grade;
    }

    public String getStation_adress() {
        return station_adress;
    }

    public int getCharging_stations() {
        return charging_stations;
    }
    public String getStation_name() {
        return station_name;
    }

    public String[] getReviews() {
        return reviews;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setStation_adress(String station_adress) {
        this.station_adress = station_adress;
    }

    public void setCharging_stations(int charging_stations) {
        this.charging_stations = charging_stations;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }
}
