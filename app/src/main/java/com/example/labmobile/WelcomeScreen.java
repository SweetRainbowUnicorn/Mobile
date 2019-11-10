package com.example.labmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {

    private TextView welcomeMessage;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        welcomeMessage = findViewById(R.id.welcome);

        showUsername();
        signOut();
    }

    public void showUsername() {
        if (user != null) {
            String username = user.getDisplayName();
            if (username != null && !username.isEmpty()) {
                welcomeMessage.append("," + " " + username + "!");
            } else {
                welcomeMessage.append("!");
            }
        }
    }

    private void signOut() {
        Button signOut = findViewById(R.id.welcome_to_log_in);
        signOut.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
        });
    }
}
