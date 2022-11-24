package com.example.ev_sc.Home.Station;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ev_sc.R;
import com.google.firebase.firestore.GeoPoint;

public class AddStation extends Activity {

    EditText station_name;
    EditText station_address;
    EditText station_charging;
    EditText station_avg_grade;
    EditText station_latitude;
    EditText station_longtitude;

    Button add_station_button;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.add_station);

        // init widgets //
        station_name = findViewById(R.id.station_name);
        station_address = findViewById(R.id.station_address);
        station_charging = findViewById(R.id.station_charging);
        station_avg_grade = findViewById(R.id.station_avg_grade);
        station_latitude = findViewById(R.id.station_latitude);
        station_longtitude = findViewById(R.id.station_longtitude);
        add_station_button = findViewById(R.id.add_station_button);


        OnClickAddButton();
    }

    private void OnClickAddButton() {
        add_station_button.setOnClickListener(view -> {
            String name = station_name.getText().toString().trim();
            String address = station_address.getText().toString().trim();
            String charging_stations = station_charging.getText().toString().trim();
            String station_average_grade = station_avg_grade.getText().toString().trim();
            String latitude = station_latitude.getText().toString().trim();
            String longtitude = station_longtitude.getText().toString().trim();

            if (!validate_input(name, address, charging_stations, station_average_grade, latitude, longtitude)) {
                Toast.makeText(AddStation.this, "Error! Incorrect Input!", Toast.LENGTH_SHORT).show();
                return;
            }
            // preparing the geo location
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longtitude);
            GeoPoint station_coords = new GeoPoint(lat, lon);

            // preparing other station elements
            int charging = Integer.parseInt(charging_stations);
            double station_grade = Double.parseDouble(station_average_grade);

            // creating Station object and adding it to database
            try {
                StationObj station_to_add = new StationObj(station_grade, address, charging, name, station_coords);
                StationDB db = new StationDB();
                db.AddStationToDatabase(station_to_add);
                Toast.makeText(AddStation.this, "Station Added!", Toast.LENGTH_SHORT).show();
                Log.d("add station", "Success");
            } catch (Exception e) {
                Toast.makeText(AddStation.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            clear_text();
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
            station_longtitude.setError("Longitude is required");
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
        station_longtitude.getText().clear();
    }
}