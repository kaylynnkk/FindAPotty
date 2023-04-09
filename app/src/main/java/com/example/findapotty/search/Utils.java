package com.example.findapotty.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.findapotty.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.ui.IconGenerator;

public class Utils {

    static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        return null;
    }

    static BitmapDescriptor getMarkerIconWithLabel(Context context, String markerLabel) {
        IconGenerator iconGenerator = new IconGenerator(context);
        @SuppressLint("InflateParams") View markerView = LayoutInflater.from(context).inflate(R.layout.marker_restroom, null);
//        ImageView imgMarker = markerView.findViewById(R.id.mr_marker_icon);
        TextView tvLabel = markerView.findViewById(R.id.mr_marker_label);
//        imgMarker.setImageResource(R.drawable.ic_map_marker_restroom);
        tvLabel.setText(markerLabel);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(markerLabel));
    }

    static BitmapDescriptor getMarkerIconWithLabelLarge(Context context, String markerLabel) {
        IconGenerator iconGenerator = new IconGenerator(context);
        @SuppressLint("InflateParams") View markerView = LayoutInflater.from(context).inflate(R.layout.marker_restroom_large, null);
//        ImageView imgMarker = markerView.findViewById(R.id.mr_marker_icon);
        TextView tvLabel = markerView.findViewById(R.id.mrl_marker_label);
//        imgMarker.setImageResource(R.drawable.ic_map_marker_restroom_large);
        tvLabel.setText(markerLabel);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(markerLabel));
    }

    // com.google.android.gms.maps.model.LatLng ==> com.google.maps.model.LatLng
    static com.google.maps.model.LatLng convertLatLngType1_1(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }

    // com.google.maps.model.LatLng ==> com.google.android.gms.maps.model.LatLng
    static com.google.android.gms.maps.model.LatLng convertLatLngTypeV1_2(com.google.maps.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.lat, latLng.lng);
    }

    //
    static com.example.findapotty.model.LatLng convertLatLngTypeV2_1(com.google.maps.model.LatLng latLng) {
        return new com.example.findapotty.model.LatLng(latLng.lat, latLng.lng);
    }

    // com.example.findapotty.model.LatLng ==> com.google.android.gms.maps.model.LatLng
    static com.google.android.gms.maps.model.LatLng convertLatLngTypeV3_1(com.example.findapotty.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
    }

    // com.google.android.gms.maps.model.LatLng ==> com.example.findapotty.model.LatLng
    static com.example.findapotty.model.LatLng convertLatLngTypeV3_2(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.example.findapotty.model.LatLng(latLng.latitude, latLng.longitude);
    }
}
