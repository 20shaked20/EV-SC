package com.example.ev_sc.Home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.ev_sc.R;

public class BufferScreen extends Activity {
    @Override
    public void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.splash_screen);

        // Add a 5 second delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);  // 5 seconds delay
    }
}