package com.example.ev_sc.Frontend;

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
        new Handler().postDelayed(() -> finish(), 3000);  // 3 seconds delay
    }
}