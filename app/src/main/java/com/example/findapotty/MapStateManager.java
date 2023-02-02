package com.example.findapotty;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapStateManager {
//    private SavedStateHandle handle;
//
//    private static String CAMERA_LONGITUDE = "camera_longitude";
//    private static String CAMERA_LATITUDE = "camera_latitude";
//    private static String CAMERA_ZOOM = "camera_zoom";
//    private static final String MAP_BEARING = "bearing";
//    private static final String MAP_TILT = "tilt";
//    private static final String MAP_TYPE = "MAPTYPE";
//    private static String SAVE_SHP_DATA_NAME = "map_shp";
//
//    public MapViewModel(@NonNull Application application, SavedStateHandle handle) {
//        super(application);
//
//        if (! handle.contains(CAMERA_ZOOM)){
//            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
//            handle.set(CAMERA_LONGITUDE, 151.0d);
//            handle.set(CAMERA_LATITUDE, -34.0d);
//            handle.set(CAMERA_ZOOM, 12.0f);
//        }
//        this.handle = handle;
//    }
//
//    public MutableLiveData<Float> getCameraLongitude() {
//        return handle.getLiveData(CAMERA_LONGITUDE);
//    }
//
//    public MutableLiveData<Float> getCameraLatitude() {
//        return handle.getLiveData(CAMERA_LATITUDE);
//    }
//
//    public MutableLiveData<Float> getCameraZoom() {
//        return handle.getLiveData(CAMERA_ZOOM);
//    }
//
//    public void saveView(GoogleMap googleMap){
//        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = shp.edit();
//        putDouble(editor, CAMERA_LONGITUDE, longitude);
//        putDouble(editor, CAMERA_LATITUDE, latitude);
//        editor.putFloat(CAMERA_ZOOM, zoom);
//        editor.putFloat(TILT, position.tilt);
//        editor.putFloat(BEARING, position.bearing);
//        editor.putInt(MAPTYPE, mapMie.getMapType());
//        editor.apply();
//
//    }

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAPTYPE = "MAPTYPE";

    private static final String PREFS_NAME ="mapCameraState";

    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveMapState(GoogleMap mapMie) {
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        CameraPosition position = mapMie.getCameraPosition();

        putDouble(editor, LATITUDE, position.target.latitude);
        putDouble(editor, LONGITUDE, position.target.longitude);
        editor.putFloat(ZOOM, position.zoom);
        editor.putFloat(TILT, position.tilt);
        editor.putFloat(BEARING, position.bearing);
        editor.putInt(MAPTYPE, mapMie.getMapType());
        editor.apply();
    }

    public CameraPosition getSavedCameraPosition() {
        double latitude = getDouble(mapStatePrefs, LATITUDE, 0);
        if (latitude == 0) {
            return null;
        }
        double longitude = getDouble(mapStatePrefs, LONGITUDE, 0);
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mapStatePrefs.getFloat(ZOOM, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        float tilt = mapStatePrefs.getFloat(TILT, 0);

        return new CameraPosition(target, zoom, tilt, bearing);

    }

    public int getSavedMapType() {
        return mapStatePrefs.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL);
    }

    private void putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
