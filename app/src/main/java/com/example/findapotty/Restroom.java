package com.example.findapotty;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Restroom {
    private LatLng latLng;
    private String placeID;
    private Bitmap photoBitmap;
    private boolean isOpen;
    private String name;
    private String address;
    private String markerId;

    public Restroom(
            LatLng latLng, String placeID, Bitmap photoBitmap,
            boolean isOpen, String name, String address) {
        this.latLng = latLng;
        this.placeID = placeID;
        this.photoBitmap = photoBitmap;
        this.isOpen = isOpen;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getPlaceID() {
        return placeID;
    }


    // Usage: imageView.setImageBitmap(bitmap);
    public Bitmap getPhoto() {
        return photoBitmap;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getOpeningStatus() {
        if (isOpen) {
            return "Opening";
        } else {
            return "Closed";
        }
    }

    public boolean isOpen_now() {
        return isOpen;
    }

    @NonNull
    @Override
    public String toString() {
        return latLng.toString() + "\n"
                + placeID + "\n"
                + getOpeningStatus() + "\n";
    }
}
