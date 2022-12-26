package com.example.ev_sc.Profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.HomeScreen;

import com.example.ev_sc.Login.LoginScreen;
import com.example.ev_sc.Person.UserObj;
import com.example.ev_sc.Profile.Favorites.FavoriteObj;
import com.example.ev_sc.Profile.Favorites.FavoritesDB;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserProfileScreen extends AppCompatActivity {

    //widgets//
    private TextView profile_username;
    private ImageView profile_picture;
    private ImageView edit_profile;
    private ListView favorite_stations;
    private UserObj curr_user;

    //vars//
    final private String TAG = "UserProfile";
    private String username;
    HashMap<String, LatLng> favorite_station_map;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    StorageReference fStorage = FirebaseStorage.getInstance().getReference();


    @Override
    public void onCreate(Bundle Instance) {

        Log.d(TAG, "Initializing Profile Screen");

        super.onCreate(Instance);
        setContentView(R.layout.user_profile);
        favorite_station_map = new HashMap<>();

        curr_user = getExtras();
//        init_widgets();
//        set_user_data_in_layout();
        fetch_data();

    }


    private UserObj getExtras() {
        Log.d(TAG, "getExtras => getting the data from the previous intent to load user.");
        UserObj user_data = getIntent().getParcelableExtra("User");
        assert user_data != null;
        Log.d(TAG, "getExtras => grabbed user data \n" + user_data.toString());
        return new UserObj(user_data);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.map_menu: {
                Log.d(TAG, "Selected: Move from Profile to map");

                startActivity(new Intent(UserProfileScreen.this, HomeScreen.class));
                finish();
                return true;
            }

            case R.id.logut_menu: {
                Log.d(TAG, "Selected: Logout");

                fAuth.signOut();
                startActivity(new Intent(UserProfileScreen.this, LoginScreen.class));
                finish();
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

        profile_username = (TextView) findViewById(R.id.profile_username_user);
        profile_picture = (ImageView) findViewById(R.id.profile_pic_user);
        edit_profile = (ImageView) findViewById(R.id.edit_profile_user);
        favorite_stations = (ListView) findViewById(R.id.favorite_list_view);

        // click on profile picture to change it //
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) { // checks if the result code is really the open gallery intent//
            if (resultCode == Activity.RESULT_OK) { // means we have some data inside//
                Uri imageUri = data.getData();

//                profile_picture.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = this.fStorage.child("users/" + fAuth.getCurrentUser().getUid() + "profile_pic.png");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_picture);
                    }
                });
            }
        });

    }


    /**
     * This method is responsible for updating the user profile via the current user login details.
     */
    private void set_user_data_in_layout() {
        Log.d(TAG, "set_user_data_in_profile: Updating User Profile");

        StorageReference profileRef = this.fStorage.child("users/" + fAuth.getCurrentUser().getUid() + "profile_pic.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_picture);
            }
        });

        this.profile_username.setText(this.curr_user.getUsername());
        //below should be the entire code for the user profile..//
        String[] stations = favorite_station_map.keySet().toArray(new String[0]);
        Log.d(TAG, "FAVORITE STATIONS: => " + stations.toString());

        ArrayAdapter<String> station_adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, stations);
        this.favorite_stations.setAdapter(station_adapter);
        this.favorite_stations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String chosen_station = (String)favorite_stations.getItemAtPosition(position);
                LatLng loc = favorite_station_map.get(chosen_station);

                Log.d(TAG,"LOCATION IS : => " + loc);

                Intent profile_screen_to_home = new Intent(UserProfileScreen.this, HomeScreen.class);
                profile_screen_to_home.putExtra("Lat", loc.latitude);
                profile_screen_to_home.putExtra("Lng", loc.longitude);
                startActivity(profile_screen_to_home);
                finish();

            }
        });
    }

    private void fetch_data() {
        fStore.collection("users").document(this.curr_user.getID())
                .collection("favorites").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            FavoritesDB db = new FavoritesDB();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // load all stations data from database to the hashmap with unique key of doc id//
                                FavoriteObj tmp_favorite = db.GetFavoriteStationFromDB(document);
                                favorite_station_map.put(tmp_favorite.getStation_name(),
                                        tmp_favorite.getStation_LatLng());
                            }
                        } else {
                            Log.e(TAG, "fetch_data: Error getting documents: ", task.getException());
                        }
                        //after we got the data from db, set it in the layout//
                        init_widgets();
                        set_user_data_in_layout();
                    }
                });


    }
}


