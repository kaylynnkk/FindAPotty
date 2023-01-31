package com.example.findapotty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private EditText mSearchText;
    private View bottomSheet;
    private static final String TAG = "MapFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSearchText = rootView.findViewById(R.id.input_search);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                init();

                // add marker on long press on map
                addMarker();

                // restroom info page
                displayRestroomPage();

                searchListener();
            }
        });

        return rootView;
    }

    private void init() {
        // For showing a move to my location button
//        googleMap.setMyLocationEnabled(true);

//         For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // UI Section
        // zoom in/out button
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void searchListener() {
        Log.d(TAG, "init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    Log.d(TAG, "onEditorAction: " + mSearchText.getText().toString());

                    //locate the address
                    searchAddress();
                }
                return false;
            }
        });
    }

    private void searchAddress() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);


            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(getActivity(), address.getAddressLine(0), Toast.LENGTH_SHORT).show();

            float zoomLevel = 10f;
            if (address.getFeatureName().equals(address.getCountryName())) { //probably a country
                Log.d(TAG, "country");
                zoomLevel = 3f;
            } else if (address.getThoroughfare() != null) { // probably a street
                Log.d(TAG, "street");
                zoomLevel = 15f;
            } else if (address.getFeatureName().equals(address.getAdminArea())) { // probably a state
                Log.d(TAG, "state");
                zoomLevel = 7f;
            }


            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), zoomLevel);

        } else {
            Toast.makeText(getActivity(), "Unable to fetch the entered address", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(latLng).zoom(zoom).build()
                )
        );
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showDialog(int id) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialog);
        BottomSheetBehavior behavior = dialog.getBehavior();
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ;

            }
        });
        dialog.setContentView(id);
        dialog.show();
    }

    private void addMarker() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marker")
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.map_marker_long_press))
                );
                moveCamera(latLng, googleMap.getCameraPosition().zoom);

                showDialog(R.layout.bottom_sheet_add_marker);
            }
        });
    }

    private void displayRestroomPage() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

//                BottomSheetDialogFragment dialogFragment = new RestroomPageBottomSheet();
//                dialogFragment.show(getParentFragmentManager(), dialogFragment.getTag());

                NavController controller = Navigation.findNavController(mMapView);
                controller.navigate(R.id.action_mapFragment2_to_restroomPageBottomSheet2);

                return true;
            }
        });
    }
}