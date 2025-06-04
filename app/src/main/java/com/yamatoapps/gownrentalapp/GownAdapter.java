package com.yamatoapps.gownrentalapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GownAdapter extends ArrayAdapter<Gown> {


    public GownAdapter(@NonNull Context context, ArrayList<Gown> gowns) {
        super(context, 0, gowns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gown gown = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gown_card_item, parent, false);
        }
        TextView tvDetails = (TextView) convertView.findViewById(R.id.tvDetails);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        ImageView ivGownImage = (ImageView) convertView.findViewById(R.id.ivGownImage);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rbRating);
        Button btnOrder = (Button) convertView.findViewById(R.id.btnOrder);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        Picasso.get().load(gown.image_url).into(ivGownImage);
        tvPrice.setText("₱ "+gown.price);
        tvDescription.setText(gown.description);
        btnOrder.setOnClickListener(view -> {

        });
        return convertView;
    }
}
