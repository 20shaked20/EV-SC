package com.example.ev_sc.Profile.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ev_sc.APIClient;
import com.example.ev_sc.Home.HomeScreen;
import com.example.ev_sc.Login.LoginScreen;
import com.example.ev_sc.ServerStrings;
import com.example.ev_sc.User.UserDB;
import com.example.ev_sc.User.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterScreen extends Activity {

    EditText line_first_name_register;
    EditText line_last_name_register;
    EditText line_email_register;
    EditText line_username_register;
    EditText line_enter_password_register;
    EditText line_confirm_password_register;

    Button register_button;

    final String TAG = "Register Screen";

    UserDB db = new UserDB();
    APIClient client = new APIClient();

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.register);

        // init widgets //
        init_widgets();
        // init listeners //
        OnClickRegisterButton();
    }

    /**
     * init widgets method
     */
    private void init_widgets() {
        line_first_name_register = (EditText) (findViewById(R.id.line_first_name_register));
        line_last_name_register = (EditText) (findViewById(R.id.line_last_name_register));
        line_email_register = (EditText) (findViewById(R.id.line_email_register));
        line_username_register = (EditText) (findViewById(R.id.line_uasername_register));
        line_enter_password_register = (EditText) (findViewById(R.id.line_enter_password_register));
        line_confirm_password_register = (EditText) (findViewById(R.id.line_confirm_password_register));
        register_button = (Button) findViewById(R.id.register_button);
    }

    /**
     * Adding the listener click to move from Register screen to login screen after registration Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = line_email_register.getText().toString().trim();
                String password = line_enter_password_register.getText().toString().trim();
                String confirm_password = line_confirm_password_register.getText().toString().trim();

                // data to be moved into the database user layer //
                String first_name = line_first_name_register.getText().toString().trim();
                String last_name = line_last_name_register.getText().toString().trim();
                String username = line_username_register.getText().toString().trim();

                // TODO: move this to a seperate validate_input() function
                if (TextUtils.isEmpty(email)) {
                    line_email_register.setError("Email Is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    line_enter_password_register.setError("Password Is Required");
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    line_confirm_password_register.setError("Confirm Password Is Required");
                    return;
                }
                if (!(password.equals(confirm_password))) {
                    line_confirm_password_register.setError("Passwords are not identical");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    line_username_register.setError("Username is required");
                }

                HashMap<String,String> newUser = db.MapUser(
                        email,
                        password,
                        first_name,
                        last_name,
                        username,
                        "0",
                        "0"
                );

                Log.d(TAG,"Sending register request to server");
                client.sendPostRequest(ServerStrings.ADD_USER.toString(), newUser, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,e.getMessage());
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
}
