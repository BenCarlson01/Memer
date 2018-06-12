package com.bemad.bcarlson.memer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity to log a user in
 * New users can go to registration page from here
 * UNIMPLEMENTED - Log in with Facebook and other social media websites
 *  currently can only log in with email and password
 * UNIMPLEMENTED - Show password option
 * UNIMPLEMENTED - Keep user logged in
 *  (as option, currently does automatically and cannot choose otherwise)
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        emailField = findViewById(R.id.loginEmailField);
        passwordField = findViewById(R.id.loginPasswordField);
        Button loginButton = findViewById(R.id.loginButton);
        TextView forgotPasswordText = findViewById(R.id.loginForgotPasswordText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to sign into Firebase authenticator with give email and password
             * Displays a Toast if login fails
             */
            @Override
            public void onClick(View v) {
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(LoginActivity.this,
                                            "Login Error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            /**
             * Changes activity to ForgotPasswordActivity
             * UNIMPLEMENTED - May pass user's email address to ForgotPasswordActivity
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                //Pass email to ForgotPasswordActivity here
                startActivity(intent);
            }
        });
    }

    /**
     * Adds a state listener to Firebase authenticator on application start to check for:
     *  a successful login,
     *  a currently logged in user
     */
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    /**
     * Removes state listener from Firebase authenticator on activity finish
     */
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    /**
     * Changes activity to RegisterActivity
     */
    public void toRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
