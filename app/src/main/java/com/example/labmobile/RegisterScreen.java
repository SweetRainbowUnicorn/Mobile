package com.example.labmobile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterScreen extends AppCompatActivity {
    private static final String TAG = "RegisterScreen";

    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private EditText phoneField;
    private Button signUpButton;

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
        signUpButton = findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(v -> {
            if (!checkFields()) {
                return;
            }
            registerUser(emailField.getEditableText().toString().trim(),
                    passwordField.getEditableText().toString().trim());
        });

        goToSignInScreen();
    }

    private void goToSignInScreen() {
        TextView goToRegisterButton = findViewById(R.id.member);
        goToRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(RegisterScreen.this, MainActivity.class)));
    }

    public void goToWelcome() {
        Intent intent = new Intent(RegisterScreen.this, WelcomeScreen.class);
        startActivity(intent);
    }

    public void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        setUserName(nameField.getEditableText().toString().trim(), currentUser);
                        Toast.makeText(RegisterScreen.this,
                                "Registration is successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterScreen.this,
                                "Registration failed. Try again", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setUserName(String name, FirebaseUser user) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        if (user != null) {
            user.updateProfile(userProfileChangeRequest)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            goToWelcome();
                        }
                    });
        } else {
            Toast.makeText(RegisterScreen.this, "Name wasn't added",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public boolean checkFields() {
        final String name = nameField.getText().toString().trim();
        final String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        final String phone = phoneField.getText().toString().trim();

        if (name.isEmpty()) {
            nameField.setError(getString(R.string.input_error_name));
            nameField.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            emailField.setError(getString(R.string.input_error_email));
            emailField.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError(getString(R.string.input_error_email_invalid));
            emailField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordField.setError(getString(R.string.input_error_password));
            passwordField.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            passwordField.setError(getString(R.string.input_error_password_length));
            passwordField.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            phoneField.setError(getString(R.string.input_error_phone));
            phoneField.requestFocus();
            return false;
        }

        if (phone.length() != 12) {
            phoneField.setError(getString(R.string.input_error_phone_invalid));
            phoneField.requestFocus();
            return false;
        }
        return true;
    }

}
