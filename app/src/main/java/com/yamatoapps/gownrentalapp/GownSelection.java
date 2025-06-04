package com.yamatoapps.gownrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GownSelection extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gown_selection);
        GridView gridView = findViewById(R.id.gvItems);
        ArrayList<Gown> items= new ArrayList<Gown>();
        GownAdapter adapter = new GownAdapter(this, items);
        db.collection("gowns").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    adapter.add(new Gown(documentSnapshot.getString("name"),
                            documentSnapshot.getString("description"),
                            documentSnapshot.getString("size"),
                            documentSnapshot.getDouble("price"),
                            documentSnapshot.getString("image_url"),
                            documentSnapshot.getString("name")

                    ));
                }
        gridView.setAdapter(adapter);
            }
        });
    }
}