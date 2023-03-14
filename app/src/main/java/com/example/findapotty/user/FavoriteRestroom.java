package com.example.findapotty.user;

import com.example.findapotty.model.Restroom;

public class FavoriteRestroom extends Restroom {

    public FavoriteRestroom(Restroom restroom) {
        super(restroom);
    }

    public FavoriteRestroom() {}

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    public String getPhotoPath() {
        return super.getPhotoPath();
    }
}
