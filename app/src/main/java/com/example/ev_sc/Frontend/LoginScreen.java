package com.example.ev_sc.Frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.R;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.Backend.Objects.UserObj;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the login screen for the user.
 * he can navigate to:
 * Register Screen,
 * Forgot Password Screen
 * Home Screen
 */
public class LoginScreen extends Activity {

    final String TAG = "Login Screen";

    APIClient client = new APIClient();

    //widgets//
    Button login_button;
    Button register_button_login;
    TextView forgot_password;
    EditText username_enter_login;
    EditText password_enter_login;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.login);

        init_widgets();
        // init listeners //
        OnClickRegisterButton();
        OnClickLoginButton();
        OnClickForgotPassword();
    }

    /**
     * Init widgets method
     */
    private void init_widgets() {
        login_button = findViewById(R.id.login_button);
        register_button_login = findViewById(R.id.register_button_login);
        username_enter_login = findViewById(R.id.username_enter_login);
        password_enter_login = findViewById(R.id.password_enter_login);
        forgot_password = findViewById(R.id.forgot_password_login);
    }

    /**
     * Adding the listener click to move from Login screen to Register screen Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button_login.setOnClickListener(view -> {
            Intent login_to_register = new Intent(view.getContext(), RegisterScreen.class);
            startActivity(login_to_register);
            finish();
        });
    }

    /**
     * This method is responsible to set a listener on the forgot password text view.
     * it moves the user from login to forgot pass screen.
     */
    private void OnClickForgotPassword() {
        forgot_password.setOnClickListener(view -> {
            Intent login_to_forgot_pass = new Intent(view.getContext(), ForgotPasswordScreen.class);
            startActivity(login_to_forgot_pass);
            finish();
        });
    }

    /**
     * Adding the listener click to move from Login screen to Home screen Via LOGIN BUTTON
     */
    private void OnClickLoginButton() {
        login_button.setOnClickListener(v -> {
            Log.d(TAG, "PRESSED LOGIN");

            String email = username_enter_login.getText().toString().trim();
            String password = password_enter_login.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                username_enter_login.setError("Email Is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                password_enter_login.setError("Password Is Required");
            }
            /*TODO: add more exceptions*/

            //authenticate the user
            Log.d(TAG, "Sending login request to server");
            client.sendGetRequest(ServerStrings.AUTH + email + "/:" + password, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d(TAG, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Server response: " + responseBody);
                    Gson gson = new Gson();
                    UserObj user = gson.fromJson(responseBody, UserObj.class);
                    Intent login_to_map = new Intent(getApplicationContext(), HomeScreen.class);
                    login_to_map.putExtra("User", user);
                    startActivity(login_to_map);
                    finish();
                }
            });
        });
    }
}


