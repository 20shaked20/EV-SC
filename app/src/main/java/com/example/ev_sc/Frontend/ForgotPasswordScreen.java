package com.example.ev_sc.Frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the Forgot password screen for the user.
 * he can navigate to:
 * Login Screen
 */
public class ForgotPasswordScreen extends AppCompatActivity {

    final String TAG = "ForgotPass Screen";

    APIClient client = new APIClient();

    //widgets//
    Button submit;
    TextView back_to_login;
    EditText email_address_text;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.forgot_password);

        init_widgets();
        // init listeners //
        OnClickBackToLogin();
        OnClickSubmit();
    }

    /**
     * Simple init widgets button.
     */
    private void init_widgets() {
        submit = findViewById(R.id.submit_forgot_password);
        back_to_login = findViewById(R.id.back_to_login_forgot_password);
        email_address_text = findViewById(R.id.email_forgot_password);
    }

    /**
     * Adding the listener click to move from Forgot Password screen to Login screen via
     * Back To Login text view.
     */
    private void OnClickBackToLogin() {
        back_to_login.setOnClickListener(view -> {
            Intent forgot_password_to_login = new Intent(view.getContext(), LoginScreen.class);
            startActivity(forgot_password_to_login);
        });
    }

    /**
     * This sets a listener on the submit button,
     * it will simply send a request to the server to send to the email requested a password reset.
     * it will only work if the email is valid and has an registered account.
     */
    private void OnClickSubmit() {
        submit.setOnClickListener(view -> {

            //validation//
            String email = email_address_text.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                email_address_text.setError("Email Is Required");
                return;
            }

            //reset//
            Log.d(TAG, "Sending reset password request to server");
            client.sendGetRequest(ServerStrings.FORGOT_PASS + email, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d(TAG, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Server response: " + responseBody);
                    Intent forgot_pass_to_login = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(forgot_pass_to_login);
                    finish();
                }
            });

        });

    }
}
