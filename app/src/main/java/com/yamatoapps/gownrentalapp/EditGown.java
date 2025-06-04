package com.yamatoapps.gownrentalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditGown extends AppCompatActivity {
    Uri fileUri;
    Intent intent;
    TextView tvName,tvPrice,tvDetails,tvSize;
    ImageView iv;
    String document_id = "";
    String image_url = "";

    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gown);
        tvName = findViewById(R.id.textInputName);
        tvPrice = findViewById(R.id.textInputPrice);
        tvSize = findViewById(R.id.textInputSize);
        tvDetails = findViewById(R.id.textInputDescription);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Adding your selection...");
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> {finish();});
        Button btnUpdate = findViewById(R.id.btnUpdate) ;
        intent = getIntent();
        document_id = intent.getStringExtra("document_id");
        db.collection("gowns").document(document_id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                                tvName.setText(documentSnapshot.getString("name"));
                                tvPrice.setText(documentSnapshot.getDouble("price").toString());
                                tvSize.setText(documentSnapshot.getString("size"));
                                tvDetails.setText(documentSnapshot.getString("description"));
                            Picasso.get().load(documentSnapshot.getString("image_url")).into(iv);
                            image_url = documentSnapshot.getString("image_url");
                        });

        iv = findViewById(R.id.ivListingPhoto);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Intent imageIntent = new Intent();

        btnUploadImage.setOnClickListener(view ->{
            imageIntent.setType("image/*");
            imageIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imageIntent,"Pick image to upload"),22);
        });
        btnUpdate.setOnClickListener(view ->{
            Map<String, Object> listing = new HashMap<>();
            listing.put("name", tvName.getText().toString());
            listing.put("description",  tvDetails.getText().toString());
            listing.put("size",  tvSize.getText().toString());
            listing.put("price",  Double.parseDouble(tvPrice.getText().toString()));
            listing.put("image_url",  image_url);

            db.collection("gowns").document(document_id).update(listing)
                    .addOnCompleteListener(updateTask -> {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Congratulations! Your selection is successfully updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        iv = findViewById(R.id.ivListingPhoto);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data != null) {
            fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                iv.setImageBitmap(bitmap);
                uploadImage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public  void uploadImage(){
        if (fileUri != null){

            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(UUID.randomUUID().toString());
            UploadTask uploadTask = (UploadTask) storageReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {

            }).addOnFailureListener(listener->{
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail to Upload Image..", Toast.LENGTH_SHORT)
                        .show();
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        image_url = task.getResult().toString();

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
}