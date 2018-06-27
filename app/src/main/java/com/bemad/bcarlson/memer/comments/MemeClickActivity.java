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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bemad.bcarlson.memer.Helper;
import com.bemad.bcarlson.memer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MemeClickActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager;
    private ArrayList<CommentObject> comments = new ArrayList<>();

    private LinearLayout commentLayout;
    private EditText commentField;
    private Button sendButton;
    private ImageView memeImage;
    private TextView memeDescription, memeCreatedBy, memeCreatedOn;

    private String userID, memeID, country;

    private DatabaseReference commentDB, userDB, chatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_click);

        memeID = getIntent().getStringExtra("memeID");
        userID = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();
        userDB = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userID);

        memeImage = findViewById(R.id.memeClickMeme);
        memeDescription = findViewById(R.id.memeClickDescriptionText);
        memeCreatedBy = findViewById(R.id.memeClickCreatedBy);
        memeCreatedOn = findViewById(R.id.memeClickCreatedOn);

        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.child("country").getValue() != null
                            && dataSnapshot.child("profile_image").getValue() != null) {
                        final String profileImage = dataSnapshot.child("profile_image")
                                .getValue().toString();
                        DatabaseReference memeDB = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("country")
                                .child(dataSnapshot.child("country").getValue().toString())
                                .child("memes")
                                .child(memeID);
                        memeDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                new Helper.DownloadImageTask(memeImage).execute(dataSnapshot
                                        .child("download").getValue().toString());
                                memeDescription.setText(dataSnapshot
                                        .child("description").getValue().toString());
                                memeCreatedBy.setText(dataSnapshot
                                        .child("username").getValue().toString());
                                memeCreatedOn.setText(dataSnapshot
                                        .child("created").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        commentDB = memeDB.child("comments");
                        commentDB.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                    HashMap<String, Object> info = (HashMap) dataSnapshot.getValue();
                                    String commentUserID = info.get("user").toString();
                                    String comment = info.get("comment").toString();
                                    long numLikes = (long) info.get("num_likes");
                                    long numDislikes = (long) info.get("num_dislikes");
                                    CommentObject newComment = new CommentObject(commentUserID, comment,
                                            dataSnapshot.getKey(), profileImage, numLikes, numDislikes, commentDB);
                                    comments.add(newComment);
                                    commentAdapter.notifyDataSetChanged();
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        commentLayoutManager = new LinearLayoutManager(MemeClickActivity.this);
        recyclerView.setLayoutManager(commentLayoutManager);

        commentAdapter = new CommentAdapter(getDataSetChat(), MemeClickActivity.this, userDB);
        recyclerView.setAdapter(commentAdapter);

        TextView commentText = findViewById(R.id.memeClickAddComment);
        commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentLayout = findViewById(R.id.memeClickSendLayout);
                commentLayout.setVisibility(View.VISIBLE);
                commentField = findViewById(R.id.memeClickComment);
                ImageView commentButton = findViewById(R.id.memeClickCommentButton);
                commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitComment();
                    }
                });
            }
        });
    }

    private ArrayList<CommentObject> getDataSetChat() {
        return comments;
    }

    private void submitComment() {
        String comment = commentField.getText().toString();
        if (!comment.isEmpty()) {
            DateFormat dateTimeFormat = SimpleDateFormat.getDateTimeInstance();
            Calendar cal = Calendar.getInstance();
            final String createdOn = dateTimeFormat.format(cal.getTime());
            String commentID = Helper.hash(comment + createdOn + userID + memeID);

            DatabaseReference commentInfo = commentDB.child(commentID);
            HashMap<String, Object> info = new HashMap<>();
            info.put("user", userID);
            info.put("comment", comment);
            info.put("num_likes", 0);
            info.put("num_dislikes", 0);
            commentInfo.setValue(info);

            commentField.setText(null);
            commentLayout.setVisibility(View.GONE);
        }
    }
}
