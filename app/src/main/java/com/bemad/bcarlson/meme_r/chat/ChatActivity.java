package com.bemad.bcarlson.meme_r.chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bemad.bcarlson.meme_r.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager chatLayoutManager;
    private ArrayList<ChatObject> resultsChat = new ArrayList<>();

    private EditText messageField;
    private Button sendButton;

    private String currentID, matchID, chatID;

    private DatabaseReference userDB, chatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentID = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        matchID = getIntent()
                .getExtras()
                .getString("matchID");
        userDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(currentID)
                .child("connections")
                .child("matches")
                .child(matchID)
                .child("chatID");
        chatDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Chat");

        getChatID();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        chatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(chatLayoutManager);

        chatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        recyclerView.setAdapter(chatAdapter);

        messageField = findViewById(R.id.message);
        sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private ArrayList<ChatObject> getDataSetChat() {
        return resultsChat;
    }

    private void getChatID() {
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatID = dataSnapshot.getValue().toString();
                    chatDB = chatDB.child(chatID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText().toString();
        if (!message.isEmpty()) {
            DatabaseReference messageDB = chatDB.push();
            HashMap<String, String> messageMap = new HashMap<>();
            messageMap.put("createdByUser", currentID);
            messageMap.put("message", message);
            messageDB.setValue(messageMap);
            messageField.setText(null);
        }
    }
}
