package com.example.findapotty.user;

public class FavoriteRestroomsManager extends RestroomsManager<FavoriteRestroom>{

    private static FavoriteRestroomsManager instance;
    private FavoriteRestroomsManager() {
    }

    public static FavoriteRestroomsManager getInstance() {
        if (instance == null) {
            instance = new FavoriteRestroomsManager();
        }
        return instance;
    }
}
