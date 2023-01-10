package com.example.ev_sc.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.ev_sc.APIClient;
import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Profile.Register.RegisterScreen;
import com.example.ev_sc.R;
import com.example.ev_sc.ServerStrings;
import com.example.ev_sc.User.UserObj;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginScreen extends Activity {

    Button login_button;
    Button register_button_login;

    EditText username_enter_login;
    EditText password_enter_login;

    final String TAG = "Login Screen";
    APIClient client = new APIClient();



    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.login);

        init_widgets();
        // init listeners //
        OnClickRegisterButton();
        OnClickLoginButton();
    }

    /**
     * Init widgets method
     */
    private void init_widgets() {
        login_button = (Button) findViewById(R.id.login_button);
        register_button_login = (Button) findViewById(R.id.register_button_login);
        username_enter_login = (EditText) (findViewById(R.id.username_enter_login));
        password_enter_login = (EditText) (findViewById(R.id.password_enter_login));
    }

    /**
     * Adding the listener click to move from Login screen to Register screen Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button_login.setOnClickListener(view -> {
            Intent login_to_register = new Intent(view.getContext(), RegisterScreen.class);
            startActivityForResult(login_to_register, 0);
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
            Log.d(TAG,"Sending login request to server");
            client.sendGetRequest(ServerStrings.AUTH + email + "/:" + password, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d(TAG,e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Server response: " + responseBody);
                    Gson gson = new Gson();
                    UserObj user = gson.fromJson(responseBody, UserObj.class);
                    Intent login_to_map = new Intent(getApplicationContext(), HomeScreen.class);
                    login_to_map.putExtra("User",user);
                    startActivity(login_to_map);
                    finish();
                }
            });
        });
    }
}


