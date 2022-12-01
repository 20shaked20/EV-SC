package com.example.ev_sc.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ev_sc.Home.Station.StationDB;
import com.example.ev_sc.Home.Station.StationObj;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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
    private TextView name_of_station;
    private TextView rate_of_station;
    private TextView the_num_of_chargers;
    private TextView address_of_station;

    private ImageView return_map_station_widget;

    private StationObj current_station; // this is a ref to the station we are currently looking at //

    /*vars*/
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        search_bar = (EditText) findViewById(R.id.search_bar);

        getLocationPermission();

        //TODO: LoadStations(); ( Better for searching )
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
                startActivity(new Intent(HomeScreen.this, UserProfileScreen.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * this method allows for overriding buttons in the keyboard ( enter .. )
     * and calls for geoLocate to locate the required place
     */
    private void init() {
        Log.d(TAG, "init: initializing");

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

        fStore.collection("stations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // find the relevant document regarding the search string //
                                if (document.get("Name").equals(searchString)) {

                                    //parsing the station document from the database//
                                    StationDB s_DB = new StationDB();
                                    current_station = s_DB.GetStationFromDatabase(document);
                                    Log.d(TAG, "geoLocate: Station is => " + current_station.toString());

                                    // get the latlng//
                                    GeoPoint geoPoint = current_station.getLocation();
                                    double lat = geoPoint.getLatitude();
                                    double lng = geoPoint.getLongitude();
                                    LatLng latLng = new LatLng(lat, lng);

                                    moveCamera(latLng, true);

                                    return;
                                }
                            }
                            search_bar.setError("Station does not exist.");

                        } else {
                            Log.e(TAG, "geoLocate: Error getting documents: ", task.getException());

                        }
                    }
                });
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
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), false);

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
     * @param marker true = create a marker, false = dont create marker.
     */
    private void moveCamera(LatLng latLng, boolean marker) {
        Log.d(TAG, "moveCamera: Moving the camera to: (lat: " + latLng.latitude + ", lng: " + latLng.longitude + " )");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeScreen.DEFAULT_ZOOM));

        //create a marker on map//
        if (marker) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("Test");
            mMap.addMarker(options);

            // TODO: make this display the relevant data//
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    createNewStationPopup();
                    return true;
                }
            });
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

    /**
     * This is an outsource method, it creates the poppable station widget
     * it invokes the moment we click on a marker omn the map and shows the details of the station
     */
    @SuppressLint({"SetTextI18n", "CutPasteId"})
    // ignores cases where numbers are turned to strings.
    public void createNewStationPopup() {

        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupStation = getLayoutInflater().inflate(R.layout.station, null);

        /*widgets*/
        name_of_station = (TextView) PopupStation.findViewById(R.id.name_of_station);
        rate_of_station = (TextView) PopupStation.findViewById(R.id.rate_of_station);
        the_num_of_chargers = (TextView) PopupStation.findViewById(R.id.the_num_of_chargers);
        address_of_station = (TextView) PopupStation.findViewById(R.id.address_of_station);
        return_map_station_widget = (ImageView) PopupStation.findViewById(R.id.return_map_station_widget);


        name_of_station.setText(current_station.getStation_name());
        address_of_station.setText(current_station.getStation_address());
        the_num_of_chargers.setText(Integer.toString(current_station.getCharging_stations()));
        rate_of_station.setText(Double.toString(current_station.getAverageGrade()));

        dialogBuilder.setView(PopupStation);
        dialog = dialogBuilder.create();
        dialog.show();


        // listener to return to the map //
        return_map_station_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}
