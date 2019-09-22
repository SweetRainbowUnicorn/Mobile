package com.example.labmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private EditText phoneField;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.register_screen_enter_email);
        passwordField = findViewById(R.id.register_screen_enter_password);
        nameField = findViewById(R.id.register_screen_enter_name);
        phoneField = findViewById(R.id.register_screen_enter_phone);
        findViewById(R.id.sign_up_button).setOnClickListener(this);

        goToSignInScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(RegisterScreen.this, "You're already logged in", Toast.LENGTH_LONG).show();
        }
    }

    private void goToSignInScreen() {
        Button goToRegisterButton = findViewById(R.id.go_to_sign_in_button);
        goToRegisterButton.setOnClickListener(v -> startActivity(new Intent(RegisterScreen.this, MainActivity.class)));
    }

    private void registerUser() {
        final String name = nameField.getText().toString().trim();
        final String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        final String phone = phoneField.getText().toString().trim();

        if (name.isEmpty()) {
            nameField.setError(getString(R.string.input_error_name));
            nameField.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailField.setError(getString(R.string.input_error_email));
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError(getString(R.string.input_error_email_invalid));
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError(getString(R.string.input_error_password));
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 8) {
            passwordField.setError(getString(R.string.input_error_password_length));
            passwordField.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            phoneField.setError(getString(R.string.input_error_phone));
            phoneField.requestFocus();
            return;
        }

        if (phone.length() != 12) {
            phoneField.setError(getString(R.string.input_error_phone_invalid));
            phoneField.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(name, email, phone);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterScreen.this, "Registration is successful", Toast.LENGTH_LONG).show();
                                        final Intent intent = new Intent(RegisterScreen.this, WelcomeScreen.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterScreen.this, "Registration failed. Try again", Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Toast.makeText(RegisterScreen.this, "Registration failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                registerUser();
                break;
        }
    }
}
