package com.example.ev_sc.Frontend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.Objects.UserObj;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the Admin profile screen
 * he can navigate to:
 * Home Screen
 * Logout Screen
 * Add station Screen
 */
public class AdminProfileScreen extends AppCompatActivity {

    final private String TAG = "AdminProfile";
    APIClient client = new APIClient();

    //Widgets//
    private TextView admin_name;
    private Button add_station_button;
    private UserObj current_user;


    @Override
    public void onCreate(Bundle Instance) {
        Log.d(TAG, "Initializing Admin Profile Screen");
        super.onCreate(Instance);
        setContentView(R.layout.admin_profile);

        getExtras();
        init_widgets();
        set_user_data_in_layout();
        // Listeners
        OnClickAddStationButton();
    }

    private void getExtras() {
        Log.d(TAG, "getExtras => getting the data from the previous intent to load user.");
        current_user = getIntent().getParcelableExtra("User");
        assert current_user != null;
        Log.d(TAG, "getExtras => grabbed user data \n" + current_user);
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
                Intent admin_profile_to_home = new Intent(AdminProfileScreen.this, HomeScreen.class);
                admin_profile_to_home.putExtra("User", current_user);

                startActivity(admin_profile_to_home);
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
                        startActivity(new Intent(AdminProfileScreen.this, LoginScreen.class));
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
        admin_name = findViewById(R.id.title_admin);
        add_station_button = findViewById(R.id.admin_add_station_button);
    }

    /**
     * Adds listener for the add station button to move to AddStationScreen
     */
    private void OnClickAddStationButton() {
        add_station_button.setOnClickListener(view -> {
            Intent login_to_add_station = new Intent(view.getContext(), AddStationScreen.class);
            login_to_add_station.putExtra("User", current_user);

            startActivity(login_to_add_station);
            finish();
        });
    }

    /**
     * This method is responsible for updating the user profile via the current user login details.
     */
    private void set_user_data_in_layout() {
        Log.d(TAG, "set_user_data_in_profile: Updating User Profile");

        this.admin_name.setText(current_user.getUserName());
        //below should be the entire code for the user profile..//
    }
}
