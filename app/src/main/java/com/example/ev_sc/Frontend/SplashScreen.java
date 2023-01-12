package com.example.ev_sc.Frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.R;

/**
 * This class handles the splash screen logo showing our "EV-SC logo"
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 2000;
    private Handler handler;

    private final Runnable runnable = () -> {
        // Start the next activity
        Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(runnable, SPLASH_SCREEN_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(runnable);
    }
}
