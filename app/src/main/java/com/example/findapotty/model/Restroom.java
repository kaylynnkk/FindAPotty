package com.example.findapotty.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Restroom implements Parcelable {
    private LatLng latLng;
    private String placeID;
    private Bitmap photoBitmap;
    private String photoPath;
    private String name;
    private String address;


    public Restroom(
            LatLng latLng, String placeID, Bitmap photoBitmap, String photoPath,
            String name, String address) {
        this.latLng = latLng;
        this.placeID = placeID;
        this.photoBitmap = photoBitmap;
        this.photoPath = photoPath;
        this.name = name;
        this.address = address;
    }

    public Restroom(Restroom that){
        this(
                that.getLatLng(), that.getPlaceID(), that.getPhotoBitmap(),
                that.getPhotoPath(), that.getName(), that.getAddress()
        );
    }

    public Restroom(){}

    protected Restroom(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        placeID = in.readString();
        photoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        address = in.readString();
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

    public String getPhotoPath() {
        return photoPath;
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
        parcel.writeString(name);
        parcel.writeString(address);
    }
}
