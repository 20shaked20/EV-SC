package com.example.ev_sc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterScreen extends Activity {

    TextView title_register;

    TextView First_name_register;
    TextView line_first_name_register;

    TextView last_name_register;
    TextView line_last_name_register;

    TextView email_register;
    TextView line_email_register;

    TextView username_register;
    TextView line_uasername_register;

    TextView enter_password_register;
    TextView line_enter_password_register;

    TextView confirm_password_register_;
    TextView line_confirm_password_register;

    Button register_button;

    @Override
    protected void onCreate(Bundle Instance)
    {
        super.onCreate(Instance);
        setContentView(R.layout.register);


        title_register = (TextView) (findViewById(R.id.title_register));
        First_name_register = (TextView) (findViewById(R.id.First_name_register));
        line_first_name_register =(EditText)(findViewById(R.id.line_first_name_register));
        last_name_register = (TextView) (findViewById(R.id.last_name_register));
        line_last_name_register =(EditText)(findViewById(R.id.line_last_name_register));
        email_register = (TextView) (findViewById(R.id.email_register));
        line_email_register =(EditText)(findViewById(R.id.line_email_register));
        username_register = (TextView) (findViewById(R.id.username_register));
        line_uasername_register =(EditText)(findViewById(R.id.line_uasername_register));
        enter_password_register = (TextView) (findViewById(R.id.enter_password_register));
        line_enter_password_register =(EditText)(findViewById(R.id.line_enter_password_register));
        confirm_password_register_ = (TextView) (findViewById(R.id.confirm_password_register_));
        line_confirm_password_register =(EditText)(findViewById(R.id.line_confirm_password_register));

        register_button = (Button)findViewById(R.id.register_button);
    }
}
