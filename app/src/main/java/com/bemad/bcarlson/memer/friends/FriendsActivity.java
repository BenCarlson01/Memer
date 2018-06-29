package com.bemad.bcarlson.memer.friends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bemad.bcarlson.memer.R;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        TextView addFriendButton = findViewById(R.id.friendsAddFriends);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes to AddFriendActivity
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });
    }
}
