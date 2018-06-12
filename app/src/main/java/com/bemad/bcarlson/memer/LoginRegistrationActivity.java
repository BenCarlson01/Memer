package com.bemad.bcarlson.memer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegistrationActivity extends AppCompatActivity {

    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginRegistrationActivity.this, OldLoginActivity.class);
                startActivity(intent);
                //finish();

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginRegistrationActivity.this, RegistrationActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    /**
     * Changes activity to TestingActivity
     * Used by R.id.testingButton
     */
    public void toTestingActivity(View view) {
        Intent intent = new Intent(LoginRegistrationActivity.this, TestingActivity.class);
        startActivity(intent);
    }
}
