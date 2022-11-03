package com.example.ev_sc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends Activity {

    Button login_button;
    Button register_button_login;

    EditText username_enter_login;
    EditText password_enter_login;

    TextView title_login;
    TextView username_view_login;
    TextView password_view_login;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.login);

        login_button = (Button) findViewById(R.id.login_button);
        register_button_login = (Button) findViewById(R.id.register_button_login);

        username_enter_login = (EditText) (findViewById(R.id.username_enter_login));
        password_enter_login = (EditText) (findViewById(R.id.password_enter_login));

        title_login = (TextView) (findViewById(R.id.title_login));
        username_view_login = (TextView) (findViewById(R.id.username_view_login));
        password_view_login = (TextView) (findViewById(R.id.password_view_login));


    }


}
