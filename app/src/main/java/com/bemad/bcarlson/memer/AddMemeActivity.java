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
import java.util.HashMap;

/**
 * Activity where a user can add their memes
 * UNIMPLEMENTED - Allow users to create memes in app
 */
public class AddMemeActivity extends AppCompatActivity {
    
    private ImageView memeImage;
    private EditText memeDescription;
    private Uri memeImageUri;

    private String userID, username, userCountry, userProfileImageUri;
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
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    HashMap<String, Object> userInfo = (HashMap) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        name = nameField.getText().toString();
        phone = phoneField.getText().toString();
        HashMap<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("phone", phone);
        userDB.updateChildren(info);

        if (resultUri != null) {
            final StorageReference filePath = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("profileImages")
                    .child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            HashMap<String, Object> info = new HashMap<>();
                            info.put("profileImgUrl", downloadUrl.toString());
                            userDB.updateChildren(info);
                            finish();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Please select a Meme Image", Toast.LENGTH_SHORT).show();
        }
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
