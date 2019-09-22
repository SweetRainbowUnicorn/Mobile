package com.example.labmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.login_screen_enter_email);
        passwordField = findViewById(R.id.login_screen_enter_password);

        findViewById(R.id.go_to_welcome_button).setOnClickListener(v -> {
            final String email = emailField.getText().toString();
            final String password = passwordField.getText().toString();
            signIn(email,password);
        });

        goToRegisterScreen();
    }

    private void goToRegisterScreen() {
        Button goToRegisterButton = findViewById(R.id.go_to_sign_up_button);
        goToRegisterButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterScreen.class)));
    }

    private void signIn(final String email, final String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                onSignInSuccess();
            } else {
                onSignInFailure();
            }
        });
    }

    private void onSignInSuccess(){
        final Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }

    private void onSignInFailure(){
        Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
    }
}
