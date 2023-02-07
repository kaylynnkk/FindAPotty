package com.example.findapotty;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.ArrayList;

public class RestroomPageViewModel extends AndroidViewModel {

    private SavedStateHandle handle;

    private static String SAVE_SHP_DATA_NAME = "restroom_page";
    private static String RESTROOM_NAME = "restroom_name";
    private static String RESTROOMS = "restrooms";

    public RestroomPageViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);

//        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
//        handle.set(RESTROOM_NAME, shp.getString(RESTROOM_NAME, "restroom name"));
//        handle.set(RESTROOMS, new ArrayList<Restroom>());

        this.handle = handle;
    }

    public MutableLiveData<String> getRestroomName() {
        return handle.getLiveData(RESTROOM_NAME);
    }

    public MutableLiveData<String> setRestroomName() {
        return handle.getLiveData(RESTROOM_NAME);
    }
}
