package com.example.ev_sc.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.example.ev_sc.Home.Station.StationDB;
import com.example.ev_sc.Home.Station.StationObj;
import com.example.ev_sc.Person.DataBases.UserDB;
import com.example.ev_sc.Person.UserObj;
import com.example.ev_sc.Profile.AdminProfileScreen;
import com.example.ev_sc.Profile.UserProfileScreen;
import com.example.ev_sc.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback {

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

            init();
        }
    }

    private static final String TAG = "MapHome";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    /*widgets*/
    private EditText search_bar;

    /*popup station*/
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
//    private TextView name_of_station;
//    private TextView rate_of_station;
//    private TextView the_num_of_chargers;
//    private TextView address_of_station;
//    private FloatingActionButton rate_station;
//
//    private ImageView return_map_station_widget;

    private StationObj current_station; // this is a ref to the station we are currently looking at //

    /*vars*/
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private HashMap<String, StationObj> all_stations = new HashMap<String, StationObj>(); // this is used to map all the stations upon login //

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    /*user profile handle*/
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private UserObj current_user;

    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        search_bar = findViewById(R.id.search_bar);

        getLocationPermission();
        load_stations_data();
        load_user_data();

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.profile_menu:
                if (current_user.getPermissions() == 1) {
                    Intent home_to_admin_profile = new Intent(HomeScreen.this, AdminProfileScreen.class);
                    home_to_admin_profile.putExtra("User", current_user);

                    startActivity(home_to_admin_profile);
                    finish();
                    return true;
                } else {
                    Intent home_to_profile = new Intent(HomeScreen.this, UserProfileScreen.class);
                    //load profile upon clicking on it//
                    home_to_profile.putExtra("User", current_user);
                    startActivity(home_to_profile);

                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void load_stations_data() {
        Log.d(TAG, "loading_stations_data: loading Stations data from database to object");

        fStore.collection("stations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StationDB s_DB = new StationDB();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // load all stations data from database to the hashmap with unique key of doc id//
                                StationObj tmp_station = s_DB.GetStationFromDatabase(document);
                                all_stations.put(document.getId(), tmp_station);

                                //init map markers on the map//
                                createMarker(tmp_station.getLatLng(),tmp_station.getStation_name());

                            }
                        } else {
                            Log.e(TAG, "geoLocate: Error getting documents: ", task.getException());
                        }
                        BuildAllPopUpStationWindows();
                    }
                });
    }

    /**
     * this method loads the current user data,
     * using that data we transfer it via the intent to the user profile (onOptionItemSelected)
     */
    public void load_user_data() {
        Log.d(TAG, "load_user_data: loading User data from database");
        String Client_UID = fAuth.getCurrentUser().getUid();

        // this is parsing the data from firestore using the string UID of the current user logged in!//
        fStore.collection("users").
                document(Client_UID).get().addOnSuccessListener(documentSnapshot -> {
                    UserDB db = new UserDB();
                    current_user = db.GetUserFromDatabase(documentSnapshot);
                    Log.d(TAG, "CURRENT USER: => \n" + current_user.toString());
                });
    }

    /**
     * this method allows for overriding buttons in the keyboard ( enter .. )
     * and calls for geoLocate to locate the required place
     */
    private void init() {
        Log.d(TAG, "init: initializing");
        //TODO: avoid free text upon pressing ENTER
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //execute method for searching the location//
                    geoLocate();
                }
                return false;
            }
        });
    }

    /**
     * This method locates the address ltlng from the database given the address name!
     */
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocationg");

        String searchString = search_bar.getText().toString().trim();

        //TODO: consider moving some of the code to StationDB//

        for (Map.Entry<String, StationObj> station : this.all_stations.entrySet()) {
            if (station.getValue().getStation_name().equals(searchString)) {
                // get the latlng//
                current_station = station.getValue(); //updating the current station//
                Log.d(TAG, "STATION LOCATION =>" + current_station.getLatLng());

                moveCamera(current_station.getLatLng());

                return;
            }
        }
        search_bar.setError("Station does not exist.");
    }

    /**
     * this method simply gets the device location, in order to pinpoint it on the map for the user.
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location.");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {

                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete getDeviceLocation: found location!");
                            Location currentLocation = (Location) task.getResult();

                            //move camera to the current location of the user//
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                        } else {
                            Log.d(TAG, "onComplete getDeviceLocation: current location is null");
                            Toast.makeText(HomeScreen.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());
        }
    }

    /**
     * this method moves the current camera to a new location on the map
     *
     * @param latLng coordinates of the location (latitude,longitude)
     */
    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: Moving the camera to: (lat: " + latLng.latitude + ", lng: " + latLng.longitude + " )");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeScreen.DEFAULT_ZOOM));
    }

    /**
     * this method is responsible for creating a station marker on the map.
     *
     * @param latLng a position we currently creating the marker for.
     */
    private void createMarker(LatLng latLng, String s_name) {
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(s_name); //TODO: consider using a unique title for markerListener//
        mMap.addMarker(options);

        MarkerListener();
    }

    private void MarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                View v = StationPopUps.get(marker.getTitle());
                StartDialogStation(v);
                return true;
            }
        });
    }

    private void BuildAllPopUpStationWindows()
    {
        Log.d(TAG, "BuildAllPopUpStationsWindows...... " + this.all_stations.toString());

        for (Map.Entry<String, StationObj> station : this.all_stations.entrySet()) {
            Log.d(TAG, "Creating STation Window for STATION : => "+ station.getValue());
            createNewStationPopup(station.getValue());

        }
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

    HashMap<String,View> StationPopUps = new HashMap<String, View>();

    /**
     * This is an outsource method, it creates the poppable station widget
     * it invokes the moment we click on a marker omn the map and shows the details of the station
     */
    @SuppressLint({"SetTextI18n", "CutPasteId"})
    // ignores cases where numbers are turned to strings.
    public void createNewStationPopup(StationObj station) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupStation = getLayoutInflater().inflate(R.layout.station, null);

        /*widgets*/
        TextView name_of_station = (TextView) PopupStation.findViewById(R.id.name_of_station);
        TextView rate_of_station = (TextView) PopupStation.findViewById(R.id.rate_of_station);
        TextView the_num_of_chargers = (TextView) PopupStation.findViewById(R.id.the_num_of_chargers);
        TextView address_of_station = (TextView) PopupStation.findViewById(R.id.address_of_station);
        ImageView return_map_station_widget = (ImageView) PopupStation.findViewById(R.id.return_map_station_widget);
        FloatingActionButton rate_station = (FloatingActionButton) PopupStation.findViewById(R.id.rate_station_button);


        name_of_station.setText(station.getStation_name());
        address_of_station.setText(station.getStation_address());
        the_num_of_chargers.setText(Integer.toString(station.getCharging_stations()));
        rate_of_station.setText(Double.toString(station.getAverageGrade()));

        // invokes the rating of the station popup window //
        rate_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View PopupRating = getLayoutInflater().inflate(R.layout.rating, null);
                dialogBuilder.setView(PopupRating);
                dialog = dialogBuilder.create();
                dialog.show();

                //widgets//
                Button button_submit_rating = (Button) PopupRating.findViewById(R.id.button_submit_rating);
                RatingBar rating_bar = (RatingBar) PopupRating.findViewById(R.id.rating_bar);

                button_submit_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double curr_grade = station.getAverageGrade();
                        //TODO: create a good updating grade mechanisem relied upon database//
//                        Double update_grade = curr_grade
                        rate_of_station.setText(Double.toString(rating_bar.getRating()));
                        dialog.dismiss();
                    }
                });
            }
        });

        // waze directions upon clicking on the button //
        ImageButton waze_nav = PopupStation.findViewById(R.id.waze_nav);
        waze_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // listener to return to the map //
        return_map_station_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //adding the new popup to the hashmap//
        StationPopUps.put(station.getStation_name(), PopupStation);

    }

    public void StartDialogStation(View PopupStation) {
        dialogBuilder.setView(PopupStation);
        dialog = dialogBuilder.create();
        dialog.show();
    }


}
