package com.example.findapotty.search;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MapMarkersManager {

    private static MapMarkersManager instance;
    private ArrayList<Marker> markers = new ArrayList<>();


    public static MapMarkersManager getInstance() {
        if (instance == null) {
            instance = new MapMarkersManager();
        }
        return instance;
    }

    private MapMarkersManager() {
    }

    public void addMarker(Marker newMarker) {
        markers.add(newMarker);
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public Marker getMarkerById(String id) {
        for (Marker marker : markers) {
            if (marker.getId().equals(id)) {
                return marker;
            }
        }
        return null;
    }
}
