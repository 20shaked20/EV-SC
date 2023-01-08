package com.example.ev_sc.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.R;

public class ForgotPasswordScreen extends AppCompatActivity {

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
    }

    /**
     * Simple init widgets button.
     */
    private void init_widgets() {
        submit = (Button) findViewById(R.id.submit_forgot_password);
        back_to_login = (TextView) findViewById(R.id.back_to_login_forgot_password);
        email_address_text = (EditText) findViewById(R.id.email_forgot_password);
    }

    /**
     * Adding the listener click to move from Forgot Password screen to Login screen via
     * Back To Login text view.
     */
    private void OnClickBackToLogin() {
        back_to_login.setOnClickListener(view -> {
            Intent forgot_password_to_login = new Intent(view.getContext(), LoginScreen.class);
            startActivityForResult(forgot_password_to_login, 0);
        });
    }

    // TODO: add onclick handle for Submit!
}
