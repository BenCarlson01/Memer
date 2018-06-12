package com.bemad.bcarlson.memer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField, emailField,
            passwordField, comfirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.registerUsernameField);
        emailField = findViewById(R.id.registerEmailField);
        passwordField = findViewById(R.id.registerPasswordField);
        comfirmPasswordField = findViewById(R.id.registerConfirmPasswordField);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
