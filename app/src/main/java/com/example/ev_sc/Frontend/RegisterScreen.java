package com.example.ev_sc.Frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;

import com.example.ev_sc.Backend.APIClient;
import com.example.ev_sc.Backend.ServerStrings;
import com.example.ev_sc.Backend.DataLayer.UserDB;
import com.example.ev_sc.R;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class handles the register screen for the user.
 * he can navigate to:
 * Login Screen
 */
public class RegisterScreen extends Activity {

    final String TAG = "Register Screen";

    UserDB db = new UserDB();
    APIClient client = new APIClient();

    //widgets//
    Button register_button;
    EditText line_first_name_register;
    EditText line_last_name_register;
    EditText line_email_register;
    EditText line_username_register;
    EditText line_enter_password_register;
    EditText line_confirm_password_register;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.register);

        init_widgets();
        //init listeners//
        OnClickRegisterButton();
    }

    /**
     * init widgets method
     */
    private void init_widgets() {
        line_first_name_register = findViewById(R.id.line_first_name_register);
        line_last_name_register = findViewById(R.id.line_last_name_register);
        line_email_register = findViewById(R.id.line_email_register);
        line_username_register = findViewById(R.id.line_uasername_register);
        line_enter_password_register = findViewById(R.id.line_enter_password_register);
        line_confirm_password_register = findViewById(R.id.line_confirm_password_register);
        register_button = findViewById(R.id.register_button);
    }

    /**
     * Adding the listener click to move from Register screen to login screen after registration Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button.setOnClickListener(view -> {
            String email = line_email_register.getText().toString().trim();
            String password = line_enter_password_register.getText().toString().trim();
            String confirm_password = line_confirm_password_register.getText().toString().trim();

            // data to be moved into the database user layer //
            String first_name = line_first_name_register.getText().toString().trim();
            String last_name = line_last_name_register.getText().toString().trim();
            String username = line_username_register.getText().toString().trim();

            boolean validate = data_validation(email, password, confirm_password, username);
            if (validate) {

                HashMap<String, Object> newUser = db.MapUser(
                        email,
                        password,
                        first_name,
                        last_name,
                        username,
                        "0",
                        "0"
                );

                Log.d(TAG, "Sending register request to server");
                client.sendPostRequest(ServerStrings.ADD_USER.toString(), newUser, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Server response: " + responseBody);

                        Intent register_to_login = new Intent(view.getContext(), LoginScreen.class);
                        startActivityForResult(register_to_login, 0);
                        finish();
                    }
                });
            }
        });
    }

    /**
     * This method validates the input given inside the register screen
     *
     * @param email            email of the user
     * @param password         password the user entered
     * @param confirm_password password the user entered
     * @param username         the chosen username
     * @return true if everything is correct to the format, false otherwise.
     */
    private boolean data_validation(String email, String password, String confirm_password, String username) {
        if (TextUtils.isEmpty(email)) {
            line_email_register.setError("Email Is Required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            line_enter_password_register.setError("Password Is Required");
            return false;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            line_confirm_password_register.setError("Confirm Password Is Required");
            return false;
        }
        if (!(password.equals(confirm_password))) {
            line_confirm_password_register.setError("Passwords are not identical");
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            line_username_register.setError("Username is required");
            return false;
        }
        return true;
    }
}
