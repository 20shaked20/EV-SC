package Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ev_sc.R;
import Register.RegisterScreen;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends Activity {

    Button login_button;
    Button register_button_login;

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

        login_button = (Button) findViewById(R.id.login_button);
        register_button_login = (Button) findViewById(R.id.register_button_login);

        username_enter_login = (EditText) (findViewById(R.id.username_enter_login));
        password_enter_login = (EditText) (findViewById(R.id.password_enter_login));

        title_login = (TextView) (findViewById(R.id.title_login));
        username_view_login = (TextView) (findViewById(R.id.username_view_login));
        password_view_login = (TextView) (findViewById(R.id.password_view_login));

        /** adding the listener click to move from Login screen to Register screen.**/
        register_button_login.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent login_to_register = new Intent(view.getContext(), RegisterScreen.class);
                startActivityForResult(login_to_register, 0);
            }
        });

//        // login button to home screen //
//        login_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent login_to_home = new Intent(v.getContext(), HomeScreen.class);
//                startActivityForResult(login_to_home, 0);
//
//                //TODO: add checks if user entered valid (existing) password and email, also check if not blank.
//            }
//        });
    }


}
