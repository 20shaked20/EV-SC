package com.example.ev_sc.Home.Station;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        station_name = (EditText) (findViewById(R.id.station_name));
        station_address = (EditText) (findViewById(R.id.station_address));
        station_charging = (EditText) (findViewById(R.id.station_charging));
        station_avg_grade = (EditText) (findViewById(R.id.station_avg_grade));
        station_latitude = (EditText) (findViewById(R.id.station_latitude));
        station_longtitude = (EditText) (findViewById(R.id.station_longtitude));
        add_station_button = (Button) (findViewById(R.id.add_station_button));


        OnClickAddButton();
    }

    private void OnClickAddButton() {
        add_station_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = station_name.getText().toString().trim();
                String address = station_address.getText().toString().trim();
                String charging_stations = station_charging.getText().toString().trim();
                String station_average_grade = station_avg_grade.getText().toString().trim();
                String latitude = station_latitude.getText().toString().trim();
                String longtitude = station_longtitude.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    station_name.setError("Name is empty");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    station_address.setError("Address is empty");
                    return;
                }
                if (TextUtils.isEmpty(charging_stations)) {
                    station_charging.setError("Must indicate how many charging stations");
                    return;
                }

                if (TextUtils.isEmpty(station_average_grade)) {
                    station_avg_grade.setError("Must indicate station grade");
                    return;
                }

                if (TextUtils.isEmpty(latitude)) {
                    station_latitude.setError("Confirm Password Is Required");
                    return;
                }

                if (TextUtils.isEmpty(longtitude)) {
                    station_longtitude.setError("Confirm Password Is Required");
                    return;
                }

                // preparing the geo location
                Double lat = Double.parseDouble(latitude);
                Double lon = Double.parseDouble(longtitude);
                GeoPoint station_coords = new GeoPoint(lat, lon);

                // preparing other station elements
                Integer charging = Integer.parseInt(charging_stations);
                Double station_grade = Double.parseDouble(station_average_grade);

                // creating Station object and adding it to database
                StationObj station_to_add = new StationObj(station_grade,address,charging,name,station_coords);
                StationDB db = new StationDB();
                db.AddStationToDatabase(station_to_add);
                Log.d("add station","Success");

            }
        });
    }
}