package com.example.ev_sc;

public enum ServerStrings {
    AUTH("http://10.0.2.2:4242/api/user/auth/:"),
    ADD_USER("http://10.0.2.2:4242/api/user"),
    ALL_STATIONS("http://10.0.2.2:4242/api/stations"),
    UPDATE_STATION("http://10.0.2.2:4242/api/station"),
    ADD_REVIEW("http://10.0.2.2:4242/api/station"),
    ADD_FAVORITE("http://10.0.2.2:4242/api/user");

    private final String text;

    ServerStrings(final String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
