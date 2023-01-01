package com.example.ev_sc.Profile.EditStation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.Station.StationObj;
import com.example.ev_sc.Home.StationDB;
import com.example.ev_sc.Person.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditStationScreen extends AppCompatActivity {

    private EditText station_name;
    private EditText station_address;
    private EditText station_charging;
    private EditText station_lat;
    private EditText station_lon;
    private Button save_button;
    private Button remove_button;
    private ScrollView station_reviews;

    private static final String TAG = "Edit Station";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_station);

        init_widgets();
        StationObj station = getExtras();
        set_station_data_in_layout(station);

        save_button.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStationScreen.this);
            builder.setTitle("Are you sure you want to update station details?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                station.setStation_name(station_name.getText().toString());
                station.setStation_address(station_address.getText().toString());
                station.setCharging_stations(Integer.parseInt(station_charging.getText().toString()));
               // GeoPoint updated_coords = new GeoPoint(Double.parseDouble(String.valueOf(station_lat)),Double.parseDouble(String.valueOf(station_lon)));
               // station.setLocation(updated_coords);
                // add station reviews

                StationDB.updateStationToDatabase(station);

                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            builder.show();
        });

        remove_button.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStationScreen.this);
            builder.setTitle("Are you sure you want to delete this station? THIS CAN'T BE REVERSED");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                StationDB db = new StationDB();
                db.deleteStationFromDatabase(station);

                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            builder.show();
        });
    }

    /**
     * this method simply initializes all the widgets we use at this screen
     */
    private void init_widgets() {
        Log.d(TAG, "init_widgets : initializing widgets");

        station_name = findViewById(R.id.edit_station_name);
        station_address = findViewById(R.id.edit_station_address);
        station_charging = findViewById(R.id.edit_station_charging);
        station_lat = findViewById(R.id.edit_station_latitude);
        station_lon = findViewById(R.id.edit_station_longtitude);
        save_button = findViewById(R.id.save_edited_station_button);
        remove_button = findViewById(R.id.remove_station_button);

    }

    private StationObj getExtras() {
        Log.d(TAG, "getExtras => getting the data from the previous intent to load station.");
        StationObj station_data = getIntent().getParcelableExtra("Station");
        assert station_data != null;
        Log.d(TAG, "getExtras => grabbed station data \n" + station_data);
        return new StationObj(station_data);
    }

    private void set_station_data_in_layout(StationObj curr) {
        Log.d(TAG, "set_station_data_in_layout: Updating Station Profile");

        this.station_name.setText(curr.getStation_name());
        this.station_address.setText(curr.getStation_address());
        this.station_charging.setText(curr.getCharging_stations());
        //this.station_lat.setText((int) curr.getLatLng().latitude);
       // this.station_lon.setText((int) curr.getLatLng().longitude);

    }
}
