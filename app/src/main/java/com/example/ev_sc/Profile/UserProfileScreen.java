package com.example.ev_sc.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.R;

public class UserProfileScreen extends AppCompatActivity {

    //widgets//
    private TextView profile_username;
    private ImageView profile_picture;
    private ImageView edit_profile;

    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.user_profile);

        init_widgets();
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
     * @param item menu bar item
     * @return true on success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.map_menu:
                startActivity(new Intent(UserProfileScreen.this, HomeScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * this method simply initializes all the widgets we use at this screen
     */
    public void init_widgets() {
        profile_username = (TextView) findViewById(R.id.profile_username_user);
        profile_picture = (ImageView) findViewById(R.id.profile_pic_user);
        edit_profile = (ImageView) findViewById(R.id.edit_profile_user);
    }
}
