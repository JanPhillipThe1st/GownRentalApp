package com.yamatoapps.gownrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ManageGowns extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lvGowns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gowns);
        lvGowns = findViewById(R.id.lvGowns);
        ArrayList<Gown> gowns = new ArrayList<Gown>();
        GownManagementAdapter adapter = new GownManagementAdapter(ManageGowns.this, gowns);
        db.collection("gowns").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    adapter.add(new Gown(documentSnapshot.getString("name"),
                            documentSnapshot.getString("description"),
                            documentSnapshot.getString("size"),
                            documentSnapshot.getDouble("price"),
                            documentSnapshot.getString("image_url"),
                            documentSnapshot.getId()

                    ));
                }
                lvGowns.setAdapter(adapter);
            }
        });
    }
}