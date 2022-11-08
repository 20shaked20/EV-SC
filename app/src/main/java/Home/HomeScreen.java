package Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import Login.LoginScreen;
import com.example.ev_sc.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends Activity {

    Button logout_button;

    @Override
    public void onCreate(Bundle Instance)
    {
        super.onCreate(Instance);
        setContentView(R.layout.home);

        logout_button = (Button) findViewById(R.id.logout_button_home_screen);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // logout user
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                finish();
            }
        });
    }
}
