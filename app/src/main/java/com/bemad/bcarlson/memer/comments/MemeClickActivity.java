package com.bemad.bcarlson.memer.comments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bemad.bcarlson.memer.R;
import com.bemad.bcarlson.memer.chat.ChatActivity;
import com.bemad.bcarlson.memer.chat.ChatAdapter;
import com.bemad.bcarlson.memer.chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MemeClickActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager;
    private ArrayList<CommentObject> comments = new ArrayList<>();

    private EditText messageField;
    private Button sendButton;

    private String userID, chatID;

    private DatabaseReference commentDB, userDB, chatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userID = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.child("country").getValue() != null) {
                            commentDB = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("country")
                                    .child(dataSnapshot.child("country").getValue().toString())
                                    .child("memes")
                                    .child(memeID)
                                    .child("comments");
                            getComments();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        getChatID();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

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
                    getChatMessages();
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

    private void getComments() {
        commentDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String commentID = dataSnapshot.getKey();
                    DataSnapshot commentData = dataSnapshot.child(commentID);
                    if (commentData.exists() && commentData.getChildrenCount() > 0) {
                        HashMap<String, Object> info = (HashMap) commentData.getValue();
                        String commentUserID = info.get("user").toString();
                        String comment = info.get("comment").toString();
                        long numLikes = (long) info.get("num_likes");
                        long numDislikes = (long) info.get("num_dislikes");
                        CommentObject newComment = new CommentObject(
                                commentUserID, comment, numLikes, numDislikes);
                        comments.add(newComment);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
