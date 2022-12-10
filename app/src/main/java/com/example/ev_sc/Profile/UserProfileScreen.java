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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Home.Station.StationDB;
import com.example.ev_sc.Person.DataBases.UserDB;
import com.example.ev_sc.Person.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileScreen extends AppCompatActivity {

    //widgets//
    private TextView profile_username;
    private ImageView profile_picture;
    private ImageView edit_profile;

    //vars//
    final private String TAG = "UserProfile";
    private String username;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    StorageReference fStorage = FirebaseStorage.getInstance().getReference();


    @Override
    public void onCreate(Bundle Instance) {

        Log.d(TAG, "Initializing Profile Screen");

        super.onCreate(Instance);
        setContentView(R.layout.user_profile);

        getExtras();
        init_widgets();
        set_user_data_in_layout();

    }

    /**
     * this method gets the user data from the previous instance using intent.getExtras.
     */
    private void getExtras() {
        Log.d(TAG,"getExtras => getting the data from the previous intent to load user.");
        Bundle user_data = getIntent().getExtras();

        if (user_data != null) {
            this.username = user_data.getString("Username");
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

        this.profile_username.setText(this.username);
        //below should be the entire code for the user profile..//
    }
}
