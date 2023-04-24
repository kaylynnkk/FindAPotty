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
    private float rating;


    public Restroom(
            LatLng latLng, String placeID, Bitmap photoBitmap, String photoPath,
            String name, String address, float rating) {
        this.latLng = latLng;
        this.placeID = placeID;
        this.photoBitmap = photoBitmap;
        this.photoPath = photoPath;
        this.name = name;
        this.address = address;
        this.rating = rating;
    }

    public Restroom(Restroom that) {
        this(
                that.getLatLng(), that.getPlaceID(), that.getPhotoBitmap(),
                that.getPhotoPath(), that.getName(), that.getAddress(), that.getRating()
        );
    }

    public Restroom() {
    }

    protected Restroom(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        placeID = in.readString();
        photoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        photoPath = in.readString();
        name = in.readString();
        address = in.readString();
        rating = in.readFloat();
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

    public float getRating() {
        return rating;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
        parcel.writeString(photoPath);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeFloat(rating);
    }
}
