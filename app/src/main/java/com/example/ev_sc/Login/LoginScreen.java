package com.example.ev_sc.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Home.Station.AddStation;
import com.example.ev_sc.R;

import com.example.ev_sc.Register.RegisterScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends Activity {

    Button login_button;
    Button register_button_login;
    Button add_station_login_button; // temp

    EditText username_enter_login;
    EditText password_enter_login;

    TextView title_login;
    TextView username_view_login;
    TextView password_view_login;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.login);

        fAuth = FirebaseAuth.getInstance();

        // init widgets //

        login_button = (Button) findViewById(R.id.login_button);
        register_button_login = (Button) findViewById(R.id.register_button_login);
        add_station_login_button = (Button) findViewById(R.id.add_station_login_button);

        username_enter_login = (EditText) (findViewById(R.id.username_enter_login));
        password_enter_login = (EditText) (findViewById(R.id.password_enter_login));

        title_login = (TextView) (findViewById(R.id.title_login));
        username_view_login = (TextView) (findViewById(R.id.username_view_login));
        password_view_login = (TextView) (findViewById(R.id.password_view_login));

        // init listeners //
        OnClickRegisterButton();

        OnClickLoginButton();

        OnClickAddStationButton();

    }

    /**
     * Adding the listener click to move from Login screen to Register screen Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login_to_register = new Intent(view.getContext(), RegisterScreen.class);
                startActivityForResult(login_to_register, 0);
            }
        });
    }

    /**
     * Adding the listener click to move from Login screen to Home screen Via LOGIN BUTTON
     */
    private void OnClickLoginButton() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //successfully login.
                            Toast.makeText(LoginScreen.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        } else {
                            // email or password incorrect or user does not exist
                            Toast.makeText(LoginScreen.this, "Email Or Password Incorrect " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void OnClickAddStationButton() {
        add_station_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent login_to_add_station = new Intent(view.getContext(), AddStation.class);
                startActivityForResult(login_to_add_station, 0);
            }
        });
    }


}
