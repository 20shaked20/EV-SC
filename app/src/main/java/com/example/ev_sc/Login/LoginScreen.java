package com.example.ev_sc.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Profile.Register.RegisterScreen;
import com.example.ev_sc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.ev_sc.APIClient;

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
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                System.out.println("Email:" + email+ "Pass: "+ password);
                APIClient client = new APIClient();
                client.sendGetRequest("http://10.0.2.2:4242/user/auth/:" + email + "/:" + password, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.equals("User Authenticated")){
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                            finish();
                        }
                    }
                });
            }
        });
    }
}


