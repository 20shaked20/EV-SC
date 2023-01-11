package com.example.ev_sc.Frontend;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.DataLayer.StationDB;
import com.example.ev_sc.Backend.Objects.StationObj;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.R;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddStationScreen extends Activity {

    EditText station_name;
    EditText station_address;
    EditText station_charging;
    EditText station_avg_grade;
    EditText station_latitude;
    EditText station_longitude;

    Button add_station_button;

    String name;
    String address;
    String charging_stations;
    String station_average_grade;
    String latitude;
    String longitude;

    //database//
    APIClient client = new APIClient();
    StationDB db = new StationDB();


    private static final String TAG = "Add Station"; // tag for logging

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.add_station);

        init_widgets();
        OnClickAddButton();
    }

    /**
     * This method adds the on click button listener for the add station button,
     * it will simply use the data entered and then send it to the server to be uploaded to the database
     */
    private void OnClickAddButton() {
        add_station_button.setOnClickListener(view -> {

            get_user_input();

            if (!validate_input(name, address, charging_stations, station_average_grade, latitude, longitude)) {
                Log.e(TAG, "Couldn't validate input for station");
                return;
            }
            // preparing the geo location
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            GeoPoint station_coords = new GeoPoint(lat, lon);

            // preparing other station elements
            int charging = Integer.parseInt(charging_stations);
            double station_grade = Double.parseDouble(station_average_grade);
            String s_id = UUID.randomUUID().toString();
            Double sumOf_reviews = (double) 0;

            HashMap<String, Object> station_to_add = db.MapStation(new StationObj(station_grade, address, charging, name, station_coords, s_id, sumOf_reviews));

            Log.d(TAG, "\n" + station_to_add); // logging station details for debugging

            // adding station to database
            Log.d(TAG, "Sending Add Station post request to server");
            client.sendPostRequest(ServerStrings.ADD_STATION.toString(), station_to_add, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d(TAG, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Server response: " + responseBody);
                }
            });
            clear_text();
            clear_input();
        });
    }

    private boolean validate_input(String s_name, String s_address, String s_charging, String s_grade, String s_lat, String s_lon) {

        if (TextUtils.isEmpty(s_name) || (TextUtils.isDigitsOnly(s_name))) {
            station_name.setError("Name is empty");
            return false;
        }
        if (TextUtils.isEmpty(s_address) || (TextUtils.isDigitsOnly(s_address))) {
            station_address.setError("Address is empty");
            return false;
        }

        if (TextUtils.isEmpty(s_charging) || !(TextUtils.isDigitsOnly(s_charging))) {
            station_charging.setError("Must indicate how many charging stations");
            return false;
        }

        if (TextUtils.isEmpty(s_grade) || !(TextUtils.isDigitsOnly(s_charging))) {
            station_avg_grade.setError("Must indicate station grade");
            return false;
        }

        if (TextUtils.isEmpty(s_lat) || !(TextUtils.isDigitsOnly(s_charging))) {
            station_latitude.setError("Latitude is required");
            return false;
        }

        if (TextUtils.isEmpty(s_lon) || !(TextUtils.isDigitsOnly(s_charging))) {
            station_longitude.setError("Longitude is required");
            return false;
        }
        return true;
    }

    private void clear_text() {
        station_name.getText().clear();
        station_address.getText().clear();
        station_charging.getText().clear();
        station_avg_grade.getText().clear();
        station_latitude.getText().clear();
        station_longitude.getText().clear();

        Log.d(TAG, "Cleared user input text");
    }

    private void init_widgets() {
        station_name = findViewById(R.id.station_name);
        station_address = findViewById(R.id.station_address);
        station_charging = findViewById(R.id.station_charging);
        station_avg_grade = findViewById(R.id.station_avg_grade);
        station_latitude = findViewById(R.id.station_latitude);
        station_longitude = findViewById(R.id.station_longtitude);
        add_station_button = findViewById(R.id.add_station_button);

        Log.d(TAG, "Initialized widgets");
    }

    private void get_user_input() {
        name = station_name.getText().toString().trim();
        address = station_address.getText().toString().trim();
        charging_stations = station_charging.getText().toString().trim();
        station_average_grade = station_avg_grade.getText().toString().trim();
        latitude = station_latitude.getText().toString().trim();
        longitude = station_longitude.getText().toString().trim();

        Log.d(TAG, "Data to add: " + name + ", " + address + ", " + "charging_stations: " + charging_stations
                + ", " + "Average Grade: " + station_average_grade + ", " + "Coordinates: " + latitude + "," + longitude);
    }

    private void clear_input() {
        name = "";
        address = "";
        charging_stations = "";
        station_average_grade = "";
        latitude = "";
        longitude = "";

        Log.d(TAG, "Cleared input");
    }
}