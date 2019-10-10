package com.example.labmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button signInButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.login_screen_enter_email);
        passwordField = findViewById(R.id.login_screen_enter_password);
        signInButton = findViewById(R.id.go_to_welcome_button);

        signInButton.setOnClickListener(v -> {
            if (!validateEmailField() | !validatePasswordField()) {
                return;
            }
            signIn(emailField.getEditableText().toString().trim(), passwordField.getEditableText()
                    .toString().trim());
        });

        goToRegisterScreen();
    }

    private void goToRegisterScreen() {
        TextView goToRegisterButton = findViewById(R.id.register);
        goToRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterScreen.class)));
    }

    private void signIn(final String email, final String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                onSignInSuccess();
            } else {
                onSignInFailure();
            }
        });
    }

    private void onSignInSuccess() {
        final Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }

    private void onSignInFailure() {
        Toast.makeText(MainActivity.this, "Authentication failed",
                Toast.LENGTH_LONG).show();
    }

    public boolean validateEmailField() {
        String email = emailField.getEditableText().toString().trim();
        if (email.isEmpty()) {
            emailField.setError("This field is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Invalid email");
            return false;
        } else {
            emailField.setError(null);
            return true;
        }
    }

    public boolean validatePasswordField() {
        String password = passwordField.getEditableText().toString().trim();
        if (password.isEmpty()) {
            passwordField.setError("This field is required");
            return false;
        } else {
            passwordField.setError(null);
            return true;
        }
    }
}
