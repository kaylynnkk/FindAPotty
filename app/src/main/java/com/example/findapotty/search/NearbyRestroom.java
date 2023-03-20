package com.example.findapotty.search;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.findapotty.model.Restroom;

public class NearbyRestroom extends Restroom {

    private String markerId;
    private boolean isOpen;
    private String currentDistance;

    public NearbyRestroom(Restroom that, boolean isOpen, String currentDistance) {
        super(that);
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public boolean isOpen_now() {
        return isOpen;
    }

    public String getCurrentDistance() {
        return currentDistance;
    }

    public String getOpeningStatus() {
        if (isOpen) {
            return "Opening";
        } else {
            return "Closed";
        }
    }

    public static final Creator<NearbyRestroom> CREATOR = new Creator<NearbyRestroom>() {
        @Override
        public NearbyRestroom createFromParcel(Parcel in) {
            return new NearbyRestroom(in);
        }

        @Override
        public NearbyRestroom[] newArray(int size) {
            return new NearbyRestroom[size];
        }
    };

    protected NearbyRestroom(Parcel in) {
        super(in);
        isOpen = in.readByte() != 0;
        markerId = in.readString();
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(markerId);
        parcel.writeByte((byte) (isOpen ? 1 : 0));
    }
}
