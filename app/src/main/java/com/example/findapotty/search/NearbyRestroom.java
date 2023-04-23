package com.example.findapotty.search;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.findapotty.model.Restroom;

public class NearbyRestroom extends Restroom {

    private String markerId;
    private boolean isOpen;
    private long currentDistance;
    private String currentDistanceText;

    //added this private variable
    private float rating;

    public NearbyRestroom(Restroom that, boolean isOpen, long currentDistance, String currentDistanceText,float rating) {
        super(that);
        this.isOpen = isOpen;
        this.currentDistance = currentDistance;
        this.currentDistanceText = currentDistanceText;
        //add rating to the constructor
        this.rating = rating;
    }

    public NearbyRestroom() {}

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public boolean isOpenNow() {
        return isOpen;
    }

    public long getCurrentDistance() {
        return currentDistance;
    }

    //added this function
    public float getRating(){return rating;}

    public String getCurrentDistanceText() {
        return currentDistanceText;
    }

    public String getOpeningStatus() {
        if (isOpen) {
            return "Opening";
        } else {
            return "Closed";
        }
    }

    public void setOpenNow(boolean open) {
        isOpen = open;
    }

    public void setCurrentDistance(long currentDistance) {
        this.currentDistance = currentDistance;
    }

    public void setCurrentDistanceText(String currentDistanceText) {
        this.currentDistanceText = currentDistanceText;
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
        currentDistance = in.readLong();
        currentDistanceText = in.readString();
        //added this line too
        rating = in.readFloat();
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
        parcel.writeLong(currentDistance);
        parcel.writeString(currentDistanceText);
        //added this line too
        parcel.writeFloat(rating);
    }
}
