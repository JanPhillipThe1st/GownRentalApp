package com.yamatoapps.gownrentalapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    TextView tvUsername,tvPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
            Intent gownSelectionIntent = new Intent(this, GownSelection.class);
        btnLogin.setOnClickListener(view -> {
            ProgressDialog progressDialog= new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            db.collection("gown_rental_users").where(Filter.and(
                            Filter.equalTo("username",tvUsername.getText().toString()),
                            Filter.equalTo("password",tvPassword.getText().toString()),Filter.equalTo("type","admin"))).
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if ( queryDocumentSnapshots.size() > 0){
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, AddGown.class));
                                Toast.makeText(MainActivity.this,"Logged in as Admin",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                db.collection("gown_rental_users").where(Filter.and(
                                        Filter.equalTo("username",tvUsername.getText().toString()),
                                        Filter.equalTo("password",tvPassword.getText().toString()),Filter.equalTo("type","customer")
                                )).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshotsCustomer) {
                                        if ( queryDocumentSnapshotsCustomer.getDocuments().size()>0){
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this,"Logged in as Customer",Toast.LENGTH_SHORT).show();
                                            startActivity(gownSelectionIntent);
                                        }
                                        else {
                                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                                            progressDialog.dismiss();
                                            ab.setTitle("Invalid Login");
                                            ab.setMessage("No user could be found");
                                            ab.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {

                                            });
                                            AlertDialog alertDialog = ab.create();
                                            alertDialog.show();
                                        }
                                    }

                                });
                            }
                        }
                    });
        });

    }
}