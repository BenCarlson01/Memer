package com.bemad.bcarlson.memer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameField, emailField, passwordField;
    private Button registerButton;
    private RadioGroup genderGroup;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(RegistrationActivity.this, OldMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        nameField = findViewById(R.id.name);
        genderGroup = findViewById(R.id.gender);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectID = genderGroup.getCheckedRadioButtonId();
                final RadioButton genderButton = findViewById(selectID);
                if (genderButton.getText() == null) {
                    return;
                }

                final String name = nameField.getText().toString();
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userID = auth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDB =
                                                FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("Users")
                                                .child(userID);
                                        HashMap<String, Object> info = new HashMap<>();
                                        info.put("name", name);
                                        info.put("gender", genderButton.getText().toString());
                                        info.put("profileImgUrl", "default");
                                        currentUserDB.setValue(info);
                                    } else {
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        Toast.makeText(RegistrationActivity.this,
                                                "Registration Error: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void backPage(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
