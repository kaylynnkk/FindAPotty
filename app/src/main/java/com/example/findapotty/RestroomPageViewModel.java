package com.example.findapotty;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class RestroomPageViewModel extends AndroidViewModel {

    private SavedStateHandle handle;

    private static String SAVE_SHP_DATA_NAME = "restroom_page";
    private static String RESTROOM_NAME = "restroom_name";

    public RestroomPageViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);

        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
        handle.set(RESTROOM_NAME, shp.getString(RESTROOM_NAME, "restroom name"));

        this.handle = handle;
    }

    public MutableLiveData<String> getRestroomName() {
        return handle.getLiveData(RESTROOM_NAME);
    }
}
