package com.bemad.bcarlson.memer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Activity that allows a user to create an account
 * Username, country, email, password required to create an account
 * UNIMPLEMENTED - Send new user verification email to verify account
 * UNIMPLEMENTED - Adds confirm password field so that user can verify password
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField, emailField,
            passwordField, comfirmPasswordField;
    private Spinner countryField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.registerUsernameField);
        countryField = findViewById(R.id.registerCountry);
        emailField = findViewById(R.id.registerEmailField);
        passwordField = findViewById(R.id.registerPasswordField);
        comfirmPasswordField = findViewById(R.id.registerConfirmPasswordField);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks to see that there is a response to all fields, then registers new user
             *  with Firebase authenticator
             * UNIMPLEMENTED - Optional fields
             */
            @Override
            public void onClick(View v) {
                final String username = usernameField.getText().toString();
                final String country = String.valueOf(countryField.getSelectedItem());
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Please choose a valid username.",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (country.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Please choose a valid country.",
                        Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Please choose a valid email.",
                        Toast.LENGTH_SHORT).show();
                    return;
                }

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            /**
                             * Attempts to create a new user
                             * If task is unsuccessful, notifies user
                             */
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userID = auth.getCurrentUser().getUid();
                                DatabaseReference currentUserDB = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child(country)
                                        .child("users")
                                        .child(userID);
                                HashMap<String, Object> userInfo = new HashMap<>();
                                userInfo.put("username", username);
                                //userInfo.put("email", email); etc.
                                userInfo.put("profile_image", "default");
                                currentUserDB.setValue(userInfo);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(RegisterActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}
