package com.example.ev_sc.Frontend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.DataLayer.StationDB;
import com.example.ev_sc.Backend.HomeScreenLogics;
import com.example.ev_sc.Backend.Objects.StationObj;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.Backend.Objects.UserObj;
import com.example.ev_sc.Backend.Objects.FavoriteObj;
import com.example.ev_sc.Backend.DataLayer.FavoritesDB;
import com.example.ev_sc.R;
import com.example.ev_sc.Backend.DataLayer.reviewsDB;
import com.example.ev_sc.Backend.Objects.reviewsObj;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the Home screen for the user.
 * he can navigate to:
 * Station Widget
 * User profile Screen/ Admin profile Screen upon permission.
 * Search bar activity
 */
public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "Home";

    /*finals regarding the permissions of the user and map activity*/
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    /*vars*/
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;

    /*widgets*/
    private EditText search_bar;

    /*popup station*/
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog_popup_station;
    private AlertDialog dialog_search_station;

    /*mapped data*/
    private HashMap<String, View> StationPopUps = new HashMap<>(); //this map is used to map each station to its popup window//
    private HashMap<String, StationObj> all_stations = new HashMap<>(); // this is used to map all the stations upon login //

    /*database*/
    private final StationDB db = new StationDB();
    private final reviewsDB Rdb = new reviewsDB();
    private final FavoritesDB Fdb = new FavoritesDB();

    /*logics*/
    private final APIClient client = new APIClient();
    private final HomeScreenLogics HSL = new HomeScreenLogics();

    /*user profile handle*/
    private UserObj current_user;
    private Location currentLocation;
    private boolean favorite_flag = false;


    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        search_bar = findViewById(R.id.search_bar);

        getLocationPermission();

    }

    /**
     * this method responsible for creating the menu bar
     *
     * @param menu menu bar
     * @return true on success.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_appbar, menu);
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

            case R.id.profile_menu:
                if (current_user.getPermission() == 1) {
                    Intent home_to_admin_profile = new Intent(HomeScreen.this, AdminProfileScreen.class);
                    home_to_admin_profile.putExtra("User", current_user);

                    startActivity(home_to_admin_profile);
                    finish();
                    return true;
                } else {
                    Intent home_to_profile = new Intent(HomeScreen.this, UserProfileScreen.class);
                    home_to_profile.putExtra("User", current_user);

                    startActivity(home_to_profile);
                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method is responsible for loading all the data into the all_station object we use to map the information,
     * it loads it to a HashMap we then use.
     */
    private void load_stations_data() {
        final CountDownLatch latch = new CountDownLatch(1);

        Log.d(TAG, "loading_stations_data: loading Stations data from database to object");
        client.sendGetRequest(ServerStrings.ALL_STATIONS.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d(TAG, "Server response: " + responseBody);
                JsonArray parser = JsonParser.parseString(responseBody).getAsJsonArray();
                List<StationObj> parsed_stations = db.station_parser(parser);

                Log.d(TAG, "Station after parsing:" + "\n" + parsed_stations);
                for (StationObj station : parsed_stations) {
                    all_stations.put(station.getStation_name(), station);
                    Log.d(TAG, station.toString());
                }
                latch.countDown();
            }
        });

        //doing this so it will wait for the onResponse to complete//
        try {
            latch.await();
            if (!all_stations.isEmpty()) {
                create_map_markers();
                BuildAllPopUpStationWindows();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method loads the current user data,
     * using that data we transfer it via the intent to the user profile (onOptionItemSelected)
     */
    private void load_user_data() {
        Log.d(TAG, "load_user_data: loading User data from database");
        current_user = getIntent().getParcelableExtra("User");
        Log.d(TAG, "Client : " + current_user);
    }

    /**
     * method to set the map.
     *
     * @param googleMap google map object
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            // shows the blue dot on the map //
            mMap.setMyLocationEnabled(true);
            // pinpoint to the current location with a button, is set to false because we'll add on later //
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init_map_objects();
        }
    }

    /**
     * this method allows for overriding buttons in the keyboard ( enter .. )
     * and calls for geoLocate to locate the required place
     */
    private void init_map_objects() {
        //load the data only once the map is ready to serve//
        load_user_data();
        load_stations_data();

        Log.d(TAG, "init: initializing");
        search_bar.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                    keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                //execute method for searching the location//
                geoLocate();
            }
            return false;
        });
    }

    /**
     * Method to initialize the map on the screen
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing map...");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_home_screen);
        assert mapFragment != null;
        mapFragment.getMapAsync(HomeScreen.this);
    }

    /**
     * This method locates the address lat-lng from the database given the address name!
     */
    @SuppressLint("SetTextI18n")
    private void geoLocate() {
        Log.d(TAG, "geoLocate: Locating station location");

        String searchString = search_bar.getText().toString().trim();
        Log.d(TAG, "Search string is:" + searchString);
        StationObj current_station;

        List<StationObj> foundStations = new ArrayList<>();
        for (Map.Entry<String, StationObj> station : this.all_stations.entrySet()) {
            current_station = station.getValue();

            if (current_station.getStation_name().contains(searchString) ||
                    current_station.getStation_address().contains(searchString) ||
                    FuzzySearch.ratio(searchString, current_station.getStation_name()) > 70 ||
                    FuzzySearch.ratio(searchString, current_station.getStation_address()) > 50) {
                foundStations.add(current_station);
            }
        }

        if (foundStations.size() == 0) {
            search_bar.setError("No stations found matching your search");
            Log.d(TAG, "Search found no station results");
        } else {
            // sort the list by distance
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                foundStations.sort(Comparator.comparingInt(this::distUserToStation));
            }
            search_bar.setText("");
            Log.d(TAG, "Found stations: " + "\n" + foundStations);
            // TODO: implement this below
            showSearchResult(foundStations);
        }
    }

    /**
     * Calculate distance from user to given station using Haversine formula to calculate distance
     * between two points on a sphere, result is accurate up to two numbers after the decimal
     * point
     */
    private int distUserToStation(StationObj station) {
        LatLng stationCords = station.getLatLng();
        double distance = this.HSL.calcDist(currentLocation.getLatitude(), currentLocation.getLongitude(),
                stationCords.latitude, stationCords.longitude);
        Log.d(TAG, "distance to user from " + station.getStation_name() + "is " + distance);
        return (int) distance;
    }

    /**
     * this method simply gets the device location, in order to pinpoint it on the map for the user.
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location.");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            Log.d(TAG, "onComplete getDeviceLocation: found location!");
                            Log.d(TAG, "LOCATION OF THE USER:=>" + location);

                            //this if handles cases where the location is null in the phone, it can happen sometimes due to unprovided location/gps problems.
                            if (location != null)
                                currentLocation = location;
                            else {
                                currentLocation = new Location("");
                                currentLocation.setLatitude(32.046878537246435);
                                currentLocation.setLongitude(34.86588824882881);
                            }

                            //we're putting sleep because it takes time for the emulator to locate the location of the user//
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //move camera to the current location of the user//
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                        });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    /**
     * This method gets the permissions for the location of the user.
     */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location...");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * this method supposed to check permission for loading the map.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: Called.");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    // check case if all permissions are granted //
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: Permission failed..");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: Permission granted..");

                    mLocationPermissionGranted = true;
                    //init map//
                    initMap();
                }
            }
        }
    }

    /**
     * this method moves the current camera to a new location on the map
     *
     * @param latLng coordinates of the location (latitude,longitude)
     */
    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: Moving the camera to: (lat: " + latLng.latitude + ", lng: " + latLng.longitude + " )");

        Bundle extras = getIntent().getExtras();
        if (extras.get("Lat") != null && extras.get("Lng") != null) {
            if (!favorite_flag) {
                //this handles case where the user requested to move to a favorite location from the userProfile//
                LatLng fav_loc = new LatLng((Double) extras.get("Lat"), (Double) extras.get("Lng"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fav_loc, HomeScreen.DEFAULT_ZOOM));
                favorite_flag = true;
            } else
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeScreen.DEFAULT_ZOOM));
        } else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeScreen.DEFAULT_ZOOM));
    }

    /**
     * This method simply invokes createMarker method for every marker needed to be created.
     */
    private void create_map_markers() {
        Log.d(TAG, "Map Markers: " + all_stations);
        for (Map.Entry<String, StationObj> station : all_stations.entrySet()) {
            createMarker(station.getValue().getLatLng(), station.getValue().getStation_name());
        }
    }

    /**
     * this method is responsible for creating a station marker on the map.
     *
     * @param latLng a position we currently creating the marker for.
     */
    private void createMarker(LatLng latLng, String s_name) {
        if (mMap == null) {
            Log.e(TAG, "mMap object is null, can't create marker");
        } else {
            Log.d(TAG, "CreateMarker: Creating markers for station: =>" + s_name);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(s_name); //TODO: consider using a unique title for markerListener//
            mMap.addMarker(options);
            MarkerListener();
        }
    }

    /**
     * This method is responsible for setting a listener on the Marker in the map,
     * the listener opens a new poppable station window.
     */
    private void MarkerListener() {
        Log.d(TAG, "MarkerListener: Setting marker listener");
        mMap.setOnMarkerClickListener(marker -> {
            View v = StationPopUps.get(marker.getTitle());
            StartDialogStation(v);
            return true;
        });
    }

    /**
     * This private method is responsible for building all the station pop up windows.
     */
    private void BuildAllPopUpStationWindows() {
        Log.d(TAG, "BuildAllPopUpStationsWindows: building all the stations windows " + this.all_stations);

        for (Map.Entry<String, StationObj> station : this.all_stations.entrySet()) {
            Log.d(TAG, "Creating Station Window for  => " + station.getValue());
            createMarker(station.getValue().getLatLng(), station.getValue().getStation_name());
            createNewStationPopup(station.getValue());
        }
    }

    /**
     * This is an outsource method, it creates the poppable station widget
     * it invokes the moment we click on a marker omn the map and shows the details of the station
     */
    @SuppressLint({"SetTextI18n", "CutPasteId"})
    public void createNewStationPopup(StationObj station) {
        Log.d(TAG, "createNewStationPopup: creating new popup window for station => " + station.getStation_name());

        dialogBuilder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") final View PopupStation = getLayoutInflater().inflate(R.layout.station, null);

        /*widgets*/
        TextView name_of_station = PopupStation.findViewById(R.id.name_of_station);
        TextView rate_of_station = PopupStation.findViewById(R.id.rate_of_station);
        TextView the_num_of_chargers = PopupStation.findViewById(R.id.the_num_of_chargers);
        TextView address_of_station = PopupStation.findViewById(R.id.address_of_station);
        ImageView return_map_station_widget = PopupStation.findViewById(R.id.return_map_station_widget);
        FloatingActionButton rate_station = PopupStation.findViewById(R.id.rate_station_button);
        FloatingActionButton favorite_station = PopupStation.findViewById(R.id.favorite_station_button);
        FloatingActionButton edit_station_button = PopupStation.findViewById(R.id.admin_edit_station_button_in_popup);
        ImageButton waze_nav = PopupStation.findViewById(R.id.waze_nav);

        //init the widgets//
        name_of_station.setText(station.getStation_name());
        address_of_station.setText(station.getStation_address());
        the_num_of_chargers.setText(Integer.toString(station.getCharging_stations()));
        rate_of_station.setText(Double.toString(station.getAverageGrade()));

        // If the user is an admin, build & show the edit station button
        if (current_user.getPermission() == 1) {
            edit_station_button.setVisibility(View.VISIBLE);
            edit_station_button.setOnClickListener(view -> { // init a listener for the button
                Intent station_popup_to_edit_station = new Intent(view.getContext(), EditStationScreen.class);
                station_popup_to_edit_station.putExtra("Station", station);
                station_popup_to_edit_station.putExtra("User", current_user);
                startActivity(station_popup_to_edit_station);
                dialog_popup_station.dismiss();
                finish();

            });
        } else {
            edit_station_button.setVisibility(View.GONE);
        }

        // invokes the rating of the station popup window //
        RatingWidget(rate_station, rate_of_station, dialogBuilder, station);

        //button handler to add station to the favorites //
        AddFavoriteStation(favorite_station, station);

        // waze directions upon clicking on the button //
        WazeNavigator(waze_nav, station);

        // listener to return to the map //
        return_map_station_widget.setOnClickListener(v -> dialog_popup_station.dismiss());
        //adding the new popup to the hashmap//
        StationPopUps.put(station.getStation_name(), PopupStation);
    }

    /**
     * This method handles the request for adding a new favorite station for the user.
     *
     * @param favorite_station The button we set the listener on
     * @param station          the requested station to be favorite.
     */
    private void AddFavoriteStation(FloatingActionButton favorite_station, StationObj station) {
        favorite_station.setOnClickListener(v -> {
            FavoriteObj favorite = new FavoriteObj(station.getLocation(), station.getID(), station.getStation_name());
            HashMap<String, Object> mapped_favorite = Fdb.MapFavorite(favorite);
            Log.d(TAG, "Sending favorite post request to server");
            client.sendPostRequest(ServerStrings.ADD_FAVORITE + "/:" + current_user.getID() + "/favorite", mapped_favorite, new Callback() {
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
        });
    }

    /**
     * This method creates the Rating widget.
     *
     * @param rate_station    The button we set the listener on
     * @param rate_of_station the rating of the station as text view object
     * @param dialogBuilder   the dialog builder we invoke
     * @param station         the station we update and do our calculations on.
     */
    @SuppressLint("SetTextI18n")
    private void RatingWidget(FloatingActionButton rate_station, TextView rate_of_station, AlertDialog.Builder dialogBuilder, StationObj station) {
        rate_station.setOnClickListener(v -> {
            final View PopupRating = getLayoutInflater().inflate(R.layout.rating, null);
            dialogBuilder.setView(PopupRating);
            AlertDialog rating_dialog = dialogBuilder.create();
            rating_dialog.show();

            //widgets//
            Button button_submit_rating = PopupRating.findViewById(R.id.button_submit_rating);
            RatingBar rating_bar = PopupRating.findViewById(R.id.rating_bar);
            EditText review_line = PopupRating.findViewById(R.id.review_line);
            button_submit_rating.setOnClickListener(v1 -> {
                //vars//
                double user_rating = rating_bar.getRating();
                double curr_grade = station.getAverageGrade();
                double SumOf_reviews = station.getSumOf_reviews();
                String curr_review = review_line.getText().toString().trim();

                //review handle//
                reviewsObj review = new reviewsObj(current_user.getID(), user_rating, curr_review);
                HashMap<String, Object> mapped_review = Rdb.MapReview(review);
                Log.d(TAG, "Sending review post request to server");
                client.sendPostRequest(ServerStrings.ADD_REVIEW + "/:" + station.getID() + "/review", mapped_review, new Callback() {
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

                //rating handle//
                double updated_grade = HSL.AverageRating(SumOf_reviews, user_rating, curr_grade);
                //doing this in order for the changes be affected immediately//
                rate_of_station.setText(Double.toString(updated_grade));
                station.setAvgGrade(updated_grade);
                station.setSumOf_reviews(SumOf_reviews + 1);

                HashMap<String, Object> updateStation = db.MapStation(station);
                Log.d(TAG, "Sending Grade update request to server");
                client.sendPostRequest(ServerStrings.UPDATE_STATION + station.getID(), updateStation, new Callback() {
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
                rating_dialog.dismiss();
            });
        });
    }

    /**
     * This method sets the button to be a click listener to open the waze application
     * and start navigating to the desired station
     *
     * @param waze_nav ImageButton of waze
     * @param station  the required station to navigate to
     */
    private void WazeNavigator(ImageButton waze_nav, StationObj station) {
        waze_nav.setOnClickListener(v -> {
            String latitude = Double.toString(station.getLocation().getLatitude());
            String longitude = Double.toString(station.getLocation().getLongitude());
            try {
                // Launch Waze to look for desired station:
                String url = "https://waze.com/ul?q=66%20Acacia%20Avenue&ll=" + latitude + "," + longitude + "&navigate=yes";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // If Waze is not installed, open it in Google Play:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                startActivity(intent);
            }
        });
    }

    /**
     * This method gets a popup window and starts it on a dialog builder.
     *
     * @param PopupStation represents a View window of a given station.
     */
    public void StartDialogStation(View PopupStation) {
        dialogBuilder.setView(PopupStation);
        dialog_popup_station = dialogBuilder.create();

        if (PopupStation.getParent() != null)
            ((ViewGroup) PopupStation.getParent()).removeView(PopupStation); //is used to delete last view if existent to avoid breakage//

        dialog_popup_station.show();
    }

    /**
     * this method is responsible for inflating a search results recycler view.
     *
     * @param foundStations list of the stations found in the search
     */
    private void showSearchResult(List<StationObj> foundStations) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Results");

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchResultAdapter adapter = new SearchResultAdapter(foundStations);
        recyclerView.setAdapter(adapter);

        builder.setView(recyclerView);

        dialog_search_station = builder.create();
        recyclerView.setTag(dialog_search_station);
        Log.d(TAG, "View tag is " + recyclerView.getTag());
        dialog_search_station.show();
    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
        private List<StationObj> foundStations;

        public SearchResultAdapter(List<StationObj> foundStations) {
            this.foundStations = foundStations;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StationObj station = foundStations.get(position);
            // Calculate the distance to the station
            int distance = distUserToStation(station);
            // Set the station name and distance in the TextView
            if (distance > 1000) {
                holder.stationName.setText(station.getStation_name() + " (" + distance / 1000 + " km)");
            } else {
                holder.stationName.setText(station.getStation_name() + " (" + distance + " m)");
            }
            holder.itemView.setOnClickListener(view -> {
                moveCamera(station.getLatLng());
                if (dialog_search_station != null) {
                    dialog_search_station.dismiss();
                }
            });
            // Assign the Dialog object to the tag of the View
            holder.itemView.setTag(dialog_search_station);
        }

        @Override
        public int getItemCount() {
            return foundStations.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView stationName;

            public ViewHolder(View itemView) {
                super(itemView);
                stationName = itemView.findViewById(R.id.station_name);
            }
        }
    }

}