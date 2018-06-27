package com.bemad.bcarlson.memer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Activity where a user can add their memes
 * UNIMPLEMENTED - Allow users to create memes in app
 * UNIMPLEMENTED - Checks if similar memes have been made before
 *  SUB-UNIMPLEMENTED - Checks if the exact same meme has been made before in that country
 *   Checks if memeID already exists, hash of combination of userCountry and meme byteMap
 */
public class SubmitMemeActivity extends AppCompatActivity {
    
    private ImageView memeImage;
    private EditText memeDescription;
    private Uri memeImageUri;

    private String userID, username, userCountry, userProfileImageUrl;
    private long num_memes, num_seen;
    private DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meme);

        memeImage = findViewById(R.id.addMemeImage);
        memeDescription = findViewById(R.id.addMemeDescription);
        Button addButton = findViewById(R.id.addMemeButton);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(userID);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Stores relevant user info into local variables to save with meme
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> userInfo = (HashMap) dataSnapshot.getValue();
                    if (userInfo.get("username") != null) {
                        username = userInfo.get("username").toString();
                    }
                    if (userInfo.get("country") != null) {
                        userCountry = userInfo.get("country").toString();
                    }
                    if (userInfo.get("num_memes") != null) {
                        num_memes = (long) userInfo.get("num_memes");
                    }
                    if (userInfo.get("num_seen") != null) {
                        num_seen = (long) userInfo.get("num_seen");
                    }
                    if (userInfo.get("profile_image") != null) {
                        userProfileImageUrl = userInfo.get("profile_image").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Does nothing
            }
        });
        
        memeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeme();
            }
        });
    }

    private void saveMeme() {
        if (memeImageUri == null) {
            Toast.makeText(this, "Please select a Meme Image", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(getApplication().getContentResolver(), memeImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        DateFormat dateTimeFormat = SimpleDateFormat.getDateTimeInstance();
        Calendar cal = Calendar.getInstance();
        final String createdOn = dateTimeFormat.format(cal.getTime());
        final String description = memeDescription.getText().toString();
        final String memeID = Helper.hash(userCountry + Arrays.toString(data) + createdOn);
        final StorageReference memeStoragePath = FirebaseStorage
                .getInstance()
                .getReference()
                .child("country")
                .child(userCountry)
                .child("memes")
                .child(memeID);
        UploadTask uploadTask = memeStoragePath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SubmitMemeActivity.this,
                        "Meme upload failed, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                memeStoragePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> memeInfo = new HashMap<>();
                        memeInfo.put("download", uri.toString());
                        memeInfo.put("user", userID);
                        memeInfo.put("username", username);
                        memeInfo.put("description", description);
                        memeInfo.put("created", createdOn);
                        memeInfo.put("num_likes", 0);
                        memeInfo.put("num_dislikes", 0);
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("country")
                                .child(userCountry)
                                .child("memes")
                                .child(memeID)
                                .updateChildren(memeInfo);
                        userDB.child("memes")
                                .child("" + num_memes)
                                .setValue(memeID);
                        userDB.child("num_memes")
                                .setValue(num_memes + 1);
                        userDB.child("seen")
                                .child("" + num_seen)
                                .setValue(memeID);
                        userDB.child("num_memes")
                                .setValue(num_seen + 1);
                        finish();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            memeImageUri = data.getData();
            memeImage.setImageURI(memeImageUri);
        }
    }
}
