package com.example.ev_sc.Home;

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

import com.example.ev_sc.Home.Station.StationObj;
import com.example.ev_sc.User.UserDB;
import com.example.ev_sc.User.UserObj;
import com.example.ev_sc.Profile.AdminProfileScreen;
import com.example.ev_sc.Profile.Favorites.FavoriteObj;
import com.example.ev_sc.Profile.Favorites.FavoritesDB;
import com.example.ev_sc.Profile.EditStation.EditStationScreen;
import com.example.ev_sc.Profile.UserProfileScreen;
import com.example.ev_sc.R;
import com.example.ev_sc.Reviews.reviewsDB;
import com.example.ev_sc.Reviews.reviewsObj;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xdrop.fuzzywuzzy.FuzzySearch;


public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MapHome";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    /*widgets*/
    private EditText search_bar;

    /*popup station*/
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog_popup_station;
    AlertDialog dialog_search_station;

    /*vars*/
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;

    /*maps*/
    HashMap<String, View> StationPopUps = new HashMap<String, View>(); //this map is used to map each station to its poppable window//
    private HashMap<String, StationObj> all_stations = new HashMap<String, StationObj>(); // this is used to map all the stations upon login //

    /*firebase*/
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    /*user profile handle*/
    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private UserObj current_user;
    private Location currentLocation;

    private HomeScreenLogics HSL;

    //tmp//
    LatLng fav_loc;

    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        search_bar = findViewById(R.id.search_bar);
        HSL = new HomeScreenLogics();

        getLocationPermission();
        load_user_data();
        load_stations_data();

        // TODO: tmp for favorite locating after moving from profile to home//
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.fav_loc = new LatLng((Double) extras.get("Lat"), (Double) extras.get("Lng"));
        }
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

    /**
     * This method is responsible for loading all the data from the firestore database,
     * it loads it to a HashMap we then use.
     */
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
                                StationObj tmp_station = s_DB.getStationFromDatabase(document);
                                all_stations.put(document.getId(), tmp_station);

                                //init map markers on the map//
                                createMarker(tmp_station.getLatLng(), tmp_station.getStation_name());

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
    private void load_user_data() {
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
     * Method to initialize the map on the screen
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing map...");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_home_screen);
        assert mapFragment != null;
        mapFragment.getMapAsync(HomeScreen.this);
    }

    /**
     * This method locates the address ltlng from the database given the address name!
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
                foundStations.sort((s1, s2) -> {
                    int distDiff = distUserToStation(s1) - distUserToStation(s2);
                    return distDiff;
                });
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
        LatLng stationCoords = station.getLatLng();
        double distance = this.HSL.calcDist(currentLocation.getLatitude(), currentLocation.getLongitude(),
                stationCoords.latitude, stationCoords.longitude);
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
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(@NonNull Location location) {
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

                            }
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

        if (fav_loc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fav_loc, HomeScreen.DEFAULT_ZOOM));
            fav_loc = null;
        } else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, HomeScreen.DEFAULT_ZOOM));
    }

    /**
     * this method is responsible for creating a station marker on the map.
     *
     * @param latLng a position we currently creating the marker for.
     */
    private void createMarker(LatLng latLng, String s_name) {
        Log.d(TAG, "CreateMarker: Creating markers for station: =>" + s_name);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(s_name); //TODO: consider using a unique title for markerListener//
        mMap.addMarker(options);

        MarkerListener();
    }

    /**
     * This method is responsible for setting a listener on the Marker in the map,
     * the listener opens a new poppable station window.
     */
    private void MarkerListener() {
        Log.d(TAG, "MarkerListener: Setting marker listener");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                View v = StationPopUps.get(marker.getTitle());
                StartDialogStation(v);
                return true;
            }
        });
    }

    /**
     * This private method is responsible for building all the station pop up windows.
     */
    private void BuildAllPopUpStationWindows() {
        Log.d(TAG, "BuildAllPopUpStationsWindows: building all the stations windows " + this.all_stations.toString());

        for (Map.Entry<String, StationObj> station : this.all_stations.entrySet()) {
            Log.d(TAG, "Creating STation Window for STATION : => " + station.getValue());
            createMarker(station.getValue().getLatLng(), station.getValue().getStation_name());
            createNewStationPopup(station.getValue());
        }
    }

    /**
     * This is an outsource method, it creates the poppable station widget
     * it invokes the moment we click on a marker omn the map and shows the details of the station
     */
    @SuppressLint({"SetTextI18n", "CutPasteId"})
//     ignores cases where numbers are turned to strings.
    public void createNewStationPopup(StationObj station) {
        Log.d(TAG, "createNewStationPopup: creating new popup window for station => " + station.getStation_name());

        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupStation = getLayoutInflater().inflate(R.layout.station, null);

        /*widgets*/
        TextView name_of_station = (TextView) PopupStation.findViewById(R.id.name_of_station);
        TextView rate_of_station = (TextView) PopupStation.findViewById(R.id.rate_of_station);
        TextView the_num_of_chargers = (TextView) PopupStation.findViewById(R.id.the_num_of_chargers);
        TextView address_of_station = (TextView) PopupStation.findViewById(R.id.address_of_station);
        ImageView return_map_station_widget = (ImageView) PopupStation.findViewById(R.id.return_map_station_widget);
        FloatingActionButton rate_station = (FloatingActionButton) PopupStation.findViewById(R.id.rate_station_button);
        FloatingActionButton favorite_station = (FloatingActionButton) PopupStation.findViewById(R.id.favorite_station_button);
        FloatingActionButton edit_station_button = (FloatingActionButton) PopupStation.findViewById(R.id.admin_edit_station_button_in_popup);


        // If the user is an admin, build & show the edit station button
        if (current_user.getPermissions() == 1) {
            edit_station_button.setVisibility(View.VISIBLE);
            edit_station_button.setOnClickListener(view -> { // init a listener for the button
                Intent station_popup_to_edit_station = new Intent(view.getContext(), EditStationScreen.class);
                station_popup_to_edit_station.putExtra("Station", station);
                startActivity(station_popup_to_edit_station);
            });
        } else {
            edit_station_button.setVisibility(View.GONE);
        }

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
                AlertDialog rating_dialog = dialogBuilder.create();
                rating_dialog.show();


                //widgets//
                Button button_submit_rating = (Button) PopupRating.findViewById(R.id.button_submit_rating);
                RatingBar rating_bar = (RatingBar) PopupRating.findViewById(R.id.rating_bar);
                EditText review_line = (EditText) PopupRating.findViewById(R.id.review_line);

                button_submit_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Double user_rating = Double.valueOf(rating_bar.getRating());
                        Double curr_grade = station.getAverageGrade();
                        Double SumOf_reviews = station.getSumOf_reviews();
                        String curr_review = review_line.getText().toString().trim();

                        reviewsObj review = new reviewsObj(current_user.getID(), user_rating, curr_review);
                        reviewsDB reviewsDB = new reviewsDB();
                        Log.d(TAG, "Review??????????????????? " + review.toString());

                        reviewsDB.AddReviewToDatabase(review, station.getID());
                        double grade = 0;
                        if (SumOf_reviews == 0) {
                            grade = user_rating;
                        } else {
                            grade = (SumOf_reviews * curr_grade + user_rating) / (SumOf_reviews + 1);
                        }

                        rate_of_station.setText(Double.toString(grade));
                        station.setAvgGrade(grade);
                        SumOf_reviews += 1;
                        station.setSumOf_reviews(SumOf_reviews);
                        StationObj.StationDB.updateStationToDatabase(station);

                        rating_dialog.dismiss();

                    }
                });
            }
        });
        //button handler to add station to the favorites //
        favorite_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoritesDB db = new FavoritesDB();
                FavoriteObj favoriteObj = new FavoriteObj(station.getID(), station.getStation_name(), station.getLocation());
                db.AddFavoriteStationToDB(favoriteObj, current_user.getID());
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
                dialog_popup_station.dismiss();
            }
        });

        //adding the new popup to the hashmap//
        StationPopUps.put(station.getStation_name(), PopupStation);

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
        // Create a DialogBuilder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the title of the dialog
        builder.setTitle("Search Results");

        // Create a RecyclerView object to display the list of found stations
        RecyclerView recyclerView = new RecyclerView(this);
        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create an adapter for the RecyclerView
        SearchResultAdapter adapter = new SearchResultAdapter(foundStations);
        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);

        // Add the RecyclerView to the DialogBuilder
        builder.setView(recyclerView);

        // Create and show the dialog
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
