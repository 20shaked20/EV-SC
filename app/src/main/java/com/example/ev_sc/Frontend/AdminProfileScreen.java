package com.example.ev_sc.Frontend;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Backend.Objects.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class AdminProfileScreen extends AppCompatActivity {

    // Widgets
    private TextView admin_name;
    private ImageView admin_pic;
    private Button add_station_button;

    // Vars
    final private String TAG = "AdminProfile";

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    StorageReference fStorage = FirebaseStorage.getInstance().getReference();

    @Override
    public void onCreate(Bundle Instance) {

        Log.d(TAG, "Initializing Admin Profile Screen");

        super.onCreate(Instance);
        setContentView(R.layout.admin_profile);

        UserObj curr = getExtras();
        init_widgets();
        set_user_data_in_layout(curr);

        // Listeners
        OnClickAddStationButton();
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
                startActivity(new Intent(AdminProfileScreen.this, HomeScreen.class));
                finish();
                return true;
            }
            case R.id.logut_menu: {
                Log.d(TAG, "Selected: Logout");

                fAuth.signOut();
                startActivity(new Intent(AdminProfileScreen.this, LoginScreen.class));
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

        admin_name = findViewById(R.id.title_admin);
        admin_pic = findViewById(R.id.admin_profile_image);
        add_station_button = findViewById(R.id.admin_add_station_button);

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
                        Picasso.get().load(uri).into(admin_pic);
                    }
                });
            }
        });

    }

    private void OnClickAddStationButton() {
        add_station_button.setOnClickListener(view -> {
            Intent login_to_add_station = new Intent(view.getContext(), AddStationScreen.class);
            startActivityForResult(login_to_add_station, 0);
        });
    }


    /**
     * This method is responsible for updating the user profile via the current user login details.
     */
    private void set_user_data_in_layout(UserObj curr) {
        Log.d(TAG, "set_user_data_in_profile: Updating User Profile");

        StorageReference profileRef = this.fStorage.child("users/" + fAuth.getCurrentUser().getUid() + "profile_pic.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(admin_pic);
            }
        });

        this.admin_name.setText(curr.getUserName());
        //below should be the entire code for the user profile..//
    }


}
