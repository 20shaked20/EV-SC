package com.example.ev_sc.Backend;

import androidx.annotation.NonNull;

public enum ServerStrings {
    AUTH("http://10.0.2.2:4242/api/user/auth/:"),
    ADD_USER("http://10.0.2.2:4242/api/user"),
    ALL_STATIONS("http://10.0.2.2:4242/api/stations"),
    UPDATE_STATION("http://10.0.2.2:4242/api/station/update/:"),
    ADD_REVIEW("http://10.0.2.2:4242/api/station"),
    ADD_FAVORITE("http://10.0.2.2:4242/api/user"),
    ALL_FAVORITES("http://10.0.2.2:4242/api/user"),
    USER_LOGOUT("http://10.0.2.2:4242/api/user/logout"),
    ADD_STATION("http://10.0.2.2:4242/api/station"),
    REMOVE_STATION("http://10.0.2.2:4242/api/station/delete/:"),
    FORGOT_PASS("http://10.0.2.2:4242/api/user/reset/:");

    private final String text;

    ServerStrings(final String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
