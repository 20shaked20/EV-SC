package com.example.ev_sc.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.R;
import com.google.firebase.auth.FirebaseAuth;

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
        OnClickSubmit();
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

    private void OnClickSubmit() {
        submit.setOnClickListener(view -> {
            
            //validation//
            String email = email_address_text.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                email_address_text.setError("Email Is Required");
                return;
            }

            //reset//
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                    Toast.makeText(ForgotPasswordScreen.this, "Password reset sent successfully, check your email", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // email or password incorrect or user does not exist
                    email_address_text.setError("Email Does Not Exist");
                }
            });

        });

    }
}
