package com.bemad.bcarlson.meme_r;

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

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameText, emailText, passwordText;
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
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        nameText = findViewById(R.id.name);
        genderGroup = findViewById(R.id.gender);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectID = genderGroup.getCheckedRadioButtonId();
                final RadioButton genderButton = findViewById(selectID);
                if (genderButton.getText() == null) {
                    return;
                }

                final String name = nameText.getText().toString();
                final String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();
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
                                                .child(genderButton.getText().toString())
                                                .child(userID);
                                        currentUserDB.setValue(name);
                                    } else {
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        Toast.makeText(RegistrationActivity.this,
                                                "Registration Error: " + e.getMessage(),
                                                Toast.LENGTH_SHORT);
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
}