package com.example.ev_sc.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Backend.APIClient;

import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.Backend.Objects.UserObj;
import com.example.ev_sc.Backend.Objects.FavoriteObj;
import com.example.ev_sc.Backend.DataLayer.FavoritesDB;
import com.example.ev_sc.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the User profile screen.
 * he can navigate to:
 * Login Screen
 * Home
 * Edit Profile Picture
 * Favorite Stations
 */
public class UserProfileScreen extends AppCompatActivity {

    final private String TAG = "UserProfile";
    final private APIClient client = new APIClient();
    final private FavoritesDB Fdb = new FavoritesDB();

    private HashMap<String, LatLng> favorite_station_map = new HashMap<>();

    //widgets//
    private TextView profile_username;
    private ImageView profile_picture;
    private ImageView edit_profile;
    private ListView favorite_stations;
    private UserObj current_user;

    @Override
    public void onCreate(Bundle Instance) {
        Log.d(TAG, "Initializing Profile Screen");
        super.onCreate(Instance);
        setContentView(R.layout.user_profile);

        getExtras();
        load_user_profile();
    }

    /**
     * this method responsible for creating the menu bar
     *
     * @param menu menu bar
     * @return true on success.
     */
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
                Intent user_profile_to_home = new Intent(UserProfileScreen.this, HomeScreen.class);
                user_profile_to_home.putExtra("User", current_user);

                startActivity(user_profile_to_home);
                finish();
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
                        //TODO: fix this, right now response body is empty..  why?
                        Log.d(TAG, "Server response: " + responseBody);
                        startActivity(new Intent(UserProfileScreen.this, LoginScreen.class));
                        finish();
                    }
                });
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

        profile_username = findViewById(R.id.profile_username_user);
        profile_picture = findViewById(R.id.profile_pic_user);
        edit_profile = findViewById(R.id.edit_profile_user);
        favorite_stations = findViewById(R.id.favorite_list_view);

        // click on profile picture to change it //
        edit_profile.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });
    }

    /**
     * This method is responsible to load the user data from the previous intent.
     */
    private void getExtras() {
        Log.d(TAG, "getExtras => getting the data from the previous intent to load user.");
        current_user = getIntent().getParcelableExtra("User");
        assert current_user != null;
        Log.d(TAG, "getExtras => grabbed user data \n" + current_user.toString());

    }

    /**
     * This method is responsible to request from the server the user data in order to load it to the screen.
     */
    private void load_user_profile() {
        final CountDownLatch latch = new CountDownLatch(1);

        Log.d(TAG, "loading_stations_data: loading Stations data from database to object");
        client.sendGetRequest(ServerStrings.ALL_FAVORITES + "/:" + current_user.getID() + "/favorites", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d(TAG, "Server response: " + responseBody);
                JsonArray parser = JsonParser.parseString(responseBody).getAsJsonArray();
                List<FavoriteObj> parsed_stations = Fdb.station_parser(parser);

                Log.d(TAG, "Station after parsing:" + "\n" + parsed_stations);
                for (FavoriteObj station : parsed_stations) {
                    favorite_station_map.put(station.getStation_name(), station.getStation_LatLng());
                    Log.d(TAG, station.toString());
                }
                latch.countDown();
            }
        });
        //doing this so it will wait for the onResponse to complete//
        try {
            latch.await();
            if (!favorite_station_map.isEmpty()) {
                init_widgets();
                set_user_data_in_layout();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for updating the user profile via the current user login details.
     */
    private void set_user_data_in_layout() {
        Log.d(TAG, "set_user_data_in_profile: Updating User Profile");

//        StorageReference profileRef = this.fStorage.child("users/" + current_user.getID() + "profile_pic.png");
//        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profile_picture));

        this.profile_username.setText(this.current_user.getUserName());
        //below should be the entire code for the user profile..//
        String[] stations = favorite_station_map.keySet().toArray(new String[0]);
        Log.d(TAG, "Favorite Stations => " + Arrays.toString(stations));

        ArrayAdapter<String> station_adapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, stations);
        this.favorite_stations.setAdapter(station_adapter);
        this.favorite_stations.setOnItemClickListener((parent, view, position, id) -> {

            String chosen_station = (String) favorite_stations.getItemAtPosition(position);
            LatLng loc = favorite_station_map.get(chosen_station);

            Intent profile_screen_to_home = new Intent(UserProfileScreen.this, HomeScreen.class);
            profile_screen_to_home.putExtra("User", current_user);
            profile_screen_to_home.putExtra("Lat", loc.latitude);
            profile_screen_to_home.putExtra("Lng", loc.longitude);
            startActivity(profile_screen_to_home);
            finish();

        });
    }


//    // TODO: IMAGE HANDLE, this also needs to be inside the SERVER (LATER) //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) { // checks if the result code is really the open gallery intent//
//            if (resultCode == Activity.RESULT_OK) { // means we have some data inside//
//                Uri imageUri = data.getData();
//
////                profile_picture.setImageURI(imageUri);
//
//                uploadImageToFirebase(imageUri);
//            }
//        }
//    }
//
//    private void uploadImageToFirebase(Uri imageUri) {
//        StorageReference fileRef = this.fStorage.child("users/" + current_user.getID() + "profile_pic.png");
//        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().
//                addOnSuccessListener(uri -> Picasso.get().load(uri).into(profile_picture)));
//    }


}




