package com.example.ev_sc;

public enum ServerStrings {
    AUTH("http://10.0.2.2:4242/api/user/auth/:"),
    ADD_USER("http://10.0.2.2:4242/api/user"),
    ALL_STATIONS("http://10.0.2.2:4242/api/stations");

    private final String text;

    ServerStrings(final String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
