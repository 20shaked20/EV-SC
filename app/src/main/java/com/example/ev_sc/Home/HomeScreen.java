package com.example.ev_sc.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.ev_sc.Login.LoginScreen;
import com.example.ev_sc.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    public void onCreate(Bundle Instance)
    {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_home_screen);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng Beersheva = new LatLng(31.284275, 34.873075);
        map.addMarker(new MarkerOptions().position(Beersheva).title("Beersheva"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Beersheva));
    }
}

//        logout_button = (Button) findViewById(R.id.logout_button_home_screen);
//
//        logout_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut(); // logout user
//                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
//                finish();
//            }
//        });