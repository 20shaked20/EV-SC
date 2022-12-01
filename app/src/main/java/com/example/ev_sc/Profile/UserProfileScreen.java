package com.example.ev_sc.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Home.Station.StationDB;
import com.example.ev_sc.Person.DataBases.UserDB;
import com.example.ev_sc.Person.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileScreen extends AppCompatActivity {

    //widgets//
    private TextView profile_username;
    private ImageView profile_picture;
    private ImageView edit_profile;

    //vars//
    private UserObj current_user;
    final private String TAG = "UserProfile";

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();


    @Override
    public void onCreate(Bundle Instance) {

        Log.d(TAG, "Initializing Profile Screen");


        super.onCreate(Instance);
        setContentView(R.layout.user_profile);

        init_widgets();
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
                startActivity(new Intent(UserProfileScreen.this, HomeScreen.class));
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
    }

    /**
     * this method loads the current user data into his profile using the database information!
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
                    set_user_data_in_layout();
                });
    }


    /**
     * This method is responsible for updating the user profile via the current user login details.
     */
    private void set_user_data_in_layout() {
        Log.d(TAG, "set_user_data_in_profile: Updating User Profile");

        this.profile_username.setText(this.current_user.getUsername());
        //below should be the entire code for the user profile..//
    }
}
