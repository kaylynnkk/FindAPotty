package com.example.findapotty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.Photo;

import java.util.ArrayList;

public class Restroom {
    private LatLng latLng;
    private String placeID;
    private Photo[] photos;
    private OpeningHours openingHours;
    private boolean isOpen;

    public Restroom(LatLng latLng, String placeID, Photo[] photos, boolean isOpen) {
        this.latLng = latLng;
        this.placeID = placeID;
        this.photos = photos;
//        this.openingHours = openingHours;
        this.isOpen = isOpen;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getPlaceID() {
        return placeID;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public String getOpeningStatus() {
        if (isOpen){
            return "Opening";
        } else{
            return "Closed";
        }
//        if (openingHours != null){
//            if (openingHours.openNow){
//                return "Opening";
//            } else {
//                return "Closed";
//            }
//        } else {
//            return "Unknown";
//        }
    }

    public boolean isOpen_now() {
        if (openingHours != null){
            if (openingHours.openNow){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return latLng.toString() + "\n"
                + placeID + "\n"
                + getOpeningStatus() + "\n";
    }
}
