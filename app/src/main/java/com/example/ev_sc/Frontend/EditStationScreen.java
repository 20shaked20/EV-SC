package com.example.ev_sc.Frontend;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.Objects.StationObj;
import com.example.ev_sc.Backend.DataLayer.StationDB;
import com.example.ev_sc.Backend.Objects.UserObj;
import com.example.ev_sc.R;
import com.example.ev_sc.Backend.ServerStrings;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the Edit screen for Admins.
 * he can navigate to:
 * Home Screen
 * Logout -> Login Screen
 */
public class EditStationScreen extends AppCompatActivity {

    private static final String TAG = "Edit Station";

    APIClient client = new APIClient();
    StationDB db = new StationDB();

    /*objects*/
    private UserObj current_user;
    private StationObj current_station;

    /*widgets*/
    private EditText station_name;
    private EditText station_address;
    private EditText station_charging;
    private EditText station_lat;
    private EditText station_lon;
    private Button save_button;
    private Button remove_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_station);

        init_widgets();
        getExtras();
        set_station_data_in_layout(current_station);

        /*listeners*/
        onClickListenerSaveButton();
        onClickListenerRemoveButton();

    }

    /**
     * This listener is responsible to act once pressing the remove button,
     * it will request from the server to delete the chosen station.
     */
    private void onClickListenerRemoveButton() {
        remove_button.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStationScreen.this);
            builder.setTitle("Are you sure you want to delete this station? THIS CAN'T BE REVERSED");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                Log.d(TAG, "Sending remove station request to server");
                client.sendPostRequest(ServerStrings.REMOVE_STATION + current_station.getID(), new HashMap<>(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Server response: " + responseBody);
                        Log.d(TAG, "Station removed, new station details:" + "\n" + current_station);

                        Intent edit_station_to_home = new Intent(EditStationScreen.this, HomeScreen.class);
                        edit_station_to_home.putExtra("User", current_user);

                        startActivity(edit_station_to_home);
                        finish();
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            builder.show();
        });
    }

    /**
     * This listener is responsible to act once pressing the save button,
     * it will call the server with data given in the edit texts and will send a request to edit the chosen station.
     */
    private void onClickListenerSaveButton() {
        save_button.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStationScreen.this);
            builder.setTitle("Are you sure you want to update station details?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                current_station.setStation_name(station_name.getText().toString());
                current_station.setStation_address(station_address.getText().toString());
                current_station.setCharging_stations(Integer.parseInt(station_charging.getText().toString()));
                //parsing geo location//
                double lat = Double.parseDouble(station_lat.getText().toString().trim());
                double lon = Double.parseDouble(station_lon.getText().toString().trim());
                current_station.setLocation(new GeoPoint(lat, lon));
                // add station reviews

                HashMap<String, Object> updateStation = db.MapStation(current_station);

                Log.d(TAG, "Sending Grade update request to server");
                client.sendPostRequest(ServerStrings.UPDATE_STATION + current_station.getID(), updateStation, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Server response: " + responseBody);
                        Log.d(TAG, "Station edited, new station details:" + "\n" + current_station);

                        Intent edit_station_to_home = new Intent(EditStationScreen.this, HomeScreen.class);
                        edit_station_to_home.putExtra("User", current_user);

                        startActivity(edit_station_to_home);
                        finish();
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            builder.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_appbar, menu);
        return true;
    }

    /**
     * this method is responsible for handling the listeners on the action bar items
     *
     * @param item menu bar item
     * @return true on success
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.map_menu: {
                Log.d(TAG, "Selected: Move from Profile to map");

                Intent edit_station_to_home = new Intent(EditStationScreen.this, HomeScreen.class);
                edit_station_to_home.putExtra("User", current_user);

                startActivity(edit_station_to_home);
                finish();
                return true;
            }
            case R.id.logut_menu: {
                Log.d(TAG, "Selected: Logout");

                Log.d(TAG, "Sending signOut request to server");
                client.sendGetRequest(ServerStrings.USER_LOGOUT.toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Server response: " + responseBody);
                        startActivity(new Intent(EditStationScreen.this, LoginScreen.class));
                        finish();
                    }
                });
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
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

    /**
     * This method gets the data from the last intent,
     * here we extract:
     * The current user logged in.
     * The current station we wish to edit.
     */
    private void getExtras() {
        Log.d(TAG, "getExtras => getting the data from the previous intent");
        this.current_station = getIntent().getParcelableExtra("Station");
        assert current_station != null;
        Log.d(TAG, "getExtras => grabbed station data \n" + current_station);

        this.current_user = getIntent().getParcelableExtra("User");
        Log.d(TAG, "getExtras => grabbed user data \n" + current_user);
    }

    /**
     * This method sets the data given to us by the station in the screen.
     *
     * @param curr
     */
    @SuppressLint("SetTextI18n")
    private void set_station_data_in_layout(StationObj curr) {
        Log.d(TAG, "set_station_data_in_layout: Updating Station Profile");

        this.station_name.setText(curr.getStation_name());
        this.station_address.setText(curr.getStation_address());
        this.station_charging.setText(Integer.toString(curr.getCharging_stations()));
        this.station_lat.setText(Double.toString(curr.getLocation().getLatitude()));
        this.station_lon.setText(Double.toString(curr.getLocation().getLongitude()));
    }
}
