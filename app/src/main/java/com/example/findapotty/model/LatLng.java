package com.example.findapotty.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/*
* https://stackoverflow.com/questions/41192002/firebase-latlng-is-missing-a-constructor-with-no-arguments
* trying to retrieve LatLng object from firebase database, but com.google.android.gms.maps.model.LatLng
* does not have a public constructor in the implementation, so an error occurs:
* "com.google.android.gms.maps.model.LatLng does not define a no-argument constructor."
* The solution is to make your own LatLng class.
 */
public class LatLng implements Parcelable {

    private Double latitude;
    private Double longitude;

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng() {
    }

    protected LatLng(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
    }
}
