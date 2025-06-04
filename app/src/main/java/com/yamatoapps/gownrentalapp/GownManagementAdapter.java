package com.yamatoapps.gownrentalapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GownManagementAdapter extends ArrayAdapter<Gown> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GownManagementAdapter(@NonNull Context context, ArrayList<Gown> gowns) {
        super(context, 0, gowns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gown gown = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gown_card, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvSize = (TextView) convertView.findViewById(R.id.tvSize);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        ImageView ivGownImage = (ImageView) convertView.findViewById(R.id.ivGownImage);
        Button btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);

        Picasso.get().load(gown.image_url).into(ivGownImage);
        tvPrice.setText("₱ "+gown.price.toString());
        tvName.setText(gown.name);
        tvSize.setText(gown.size);
        tvDescription.setText(gown.description);
        btnDelete.setOnClickListener(view -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
            alertDialogBuilder.setTitle("Delete Gown");
            alertDialogBuilder.setMessage("Are you sure you want to delete this gown?");
            alertDialogBuilder.setPositiveButton("NO", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alertDialogBuilder.setNegativeButton("YES", (dialogInterface, i) -> {

                MaterialAlertDialogBuilder deleteDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
                deleteDialogBuilder.setTitle("Delete success");
                deleteDialogBuilder.setMessage("Gown deleted successfully!");
                deleteDialogBuilder.setPositiveButton("OK", (deleteDialogBuilderDialogInterface,j)->{
                    deleteDialogBuilderDialogInterface.dismiss();
                    Activity context = (Activity) parent.getContext();
                });
                db.collection("gowns").document(gown.id).delete().addOnSuccessListener(unused -> {
                    deleteDialogBuilder.create().show();
                    dialogInterface.dismiss();
                });
            });
            alertDialogBuilder.create().show();
        });

        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(parent.getContext(), EditGown.class);
            intent.putExtra("document_id",gown.id);
            parent.getContext().startActivity(intent);
        });
        return convertView;
    }
}
