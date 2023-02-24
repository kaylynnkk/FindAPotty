package com.example.findapotty;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

public class Restroom implements Parcelable {
    private final LatLng latLng;
    private final String placeID;
    private final Bitmap photoBitmap;
    private String photoUrl;
    private final boolean isOpen;
    private final String name;
    private final String address;
    private String markerId;


    public Restroom(
            LatLng latLng, String placeID, Bitmap photoBitmap, String photoUrl,
            boolean isOpen, String name, String address) {
        this.latLng = latLng;
        this.placeID = placeID;
        this.photoBitmap = photoBitmap;
        this.photoUrl = photoUrl;
        this.isOpen = isOpen;
        this.name = name;
        this.address = address;
    }

    protected Restroom(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        placeID = in.readString();
        photoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        isOpen = in.readByte() != 0;
        name = in.readString();
        address = in.readString();
        markerId = in.readString();
    }

    public static final Creator<Restroom> CREATOR = new Creator<Restroom>() {
        @Override
        public Restroom createFromParcel(Parcel in) {
            return new Restroom(in);
        }

        @Override
        public Restroom[] newArray(int size) {
            return new Restroom[size];
        }
    };

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
    @Exclude
    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Exclude
    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    @Exclude
    public String getOpeningStatus() {
        if (isOpen) {
            return "Opening";
        } else {
            return "Closed";
        }
    }

    @Exclude
    public boolean isOpen_now() {
        return isOpen;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return latLng.toString() + "\n"
                + placeID + "\n"
                + getOpeningStatus() + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(latLng, i);
        parcel.writeString(placeID);
        parcel.writeParcelable(photoBitmap, i);
        parcel.writeByte((byte) (isOpen ? 1 : 0));
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(markerId);
    }
}
