package com.example.findapotty.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.findapotty.BuildConfig;
import com.example.findapotty.search.MapFragmentDirections;
import com.example.findapotty.R;
import com.example.findapotty.Restroom;
import com.example.findapotty.RestroomManager;
import com.example.findapotty.databinding.FragmentMapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //    View rootView;
    MapView mMapView;
    private GoogleMap googleMap;
    private EditText mSearchText;

    private FusedLocationProviderClient fusedLocationClient;
    private boolean grantedDeviceLocation = false;
    private Location currentLocation = null;
    private boolean onSerchingComplete = false;
    FragmentMapBinding binding;
    private static final String TAG = "MapFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        binding.setLifecycleOwner(this);

        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mSearchText = binding.inputSearch;

        setUpIfNeeded();

        return binding.getRoot();
    }

    private void setUpIfNeeded() {
        if (googleMap == null) {
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        permissionCheck();

        init();

        // add marker on long press on map
        addMarker();

        // restroom info page
        displayRestroomPage();

        searchListener();


        getNearbyRestrooms();
        addRestroomIconOnMap();


    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            grantedDeviceLocation = true;
            Log.d(TAG, "permissionCheck: permission grated");
        }
    }

    @SuppressLint("MissingPermission")
    private void init() {

        MapStateManager mgr = new MapStateManager(getActivity());
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null) {
            Toast.makeText(getActivity(), "entering Resume State", Toast.LENGTH_SHORT).show();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            googleMap.setMapType(mgr.getSavedMapType());
        }

        // UI Section
        // zoom in/out button
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // device location
        if (grantedDeviceLocation) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        googleMap.setPadding(0, 0, 0, 150);
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

    @SuppressLint("MissingInflatedId")
//    private Bitmap getMarkerIconWithLabel(Context context, ImageView markerImage, String markerLabel, float angle) {
    private Bitmap getMarkerIconWithLabel(Context context, String markerLabel) {
        IconGenerator iconGenerator = new IconGenerator(context);
        View markerView = LayoutInflater.from(context).inflate(R.layout.marker_restroom, null);
        ImageView imgMarker = markerView.findViewById(R.id.mr_marker_icon);
        TextView tvLabel = markerView.findViewById(R.id.mr_marker_label);
        imgMarker.setImageResource(R.drawable.map_marker_restroom);
        tvLabel.setText(markerLabel);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return iconGenerator.makeIcon(markerLabel);
    }

    //    static List<com.google.maps.model.LatLng> convertCoordType(List<com.google.android.gms.maps.model.LatLng> list) {
//        List<com.google.maps.model.LatLng> resultList = new ArrayList<>();
//        for (com.google.android.gms.maps.model.LatLng item : list) {
//            resultList.add(new com.google.maps.model.LatLng(item.latitude, item.longitude));
//        }
//        return resultList;
//    }
    static com.google.maps.model.LatLng convertLatLngType(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }

    static com.google.android.gms.maps.model.LatLng convertLatLngTypeV2(com.google.maps.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.lat, latLng.lng);
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

    @SuppressLint("PotentialBehaviorOverride")
    private void displayRestroomPage() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

//                BottomSheetDialogFragment dialogFragment = new RestroomPageBottomSheet();
//                dialogFragment.show(getParentFragmentManager(), dialogFragment.getTag());

                try {

                    NavController controller = Navigation.findNavController(mMapView);

                    NavDirections action =
                            MapFragmentDirections.actionMapFragment2ToRestroomPageBottomSheet2(marker.getId());
                    controller.navigate(action);

//                    RestroomPageBottomSheet restroomPageBottomSheet = new RestroomPageBottomSheet();
//                    restroomPageBottomSheet.initRestroomPage(marker.getId());
                    Log.d(TAG, "onMarkerClick: " + marker.getId());
                } catch (IllegalArgumentException e) {
                    Log.d(TAG, "onMarkerClick: failed to find action");
                }
                return true;

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MapStateManager mgr = new MapStateManager(getActivity());
        mgr.saveMapState(googleMap);
        Toast.makeText(getActivity(), "Map State has been save", Toast.LENGTH_SHORT).show();
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        setupMapIfNeeded();
//    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        if (grantedDeviceLocation) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        currentLocation = new Location(location);
//                        setCurrentLocation(location);
//                        currentLocation = location;
                        Log.d(TAG, "onSuccess: current location fetched" + currentLocation.getLatitude()
                                + ", " + currentLocation.getLongitude());
//                                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 12);
                        // Logic to handle location object
                    } else {
                        Log.d(TAG, "onSuccess: failed to fetch current location");
                    }
                }
            });
        }
    }

    /*
     * 1. get device location
     * 2. http request using okhttp object
     * 3. parse json response to get restroom lat and lng
     * 4. add marker on map using lat and lng
     */
    private void getNearbyRestrooms() {
        getDeviceLocation();
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12);
                if (currentLocation != null) {

                    GeoApiContext geoApiContext = new GeoApiContext.Builder()
                            .apiKey(BuildConfig.MAPS_API_KEY)
                            .build();
                    try {
                        // get nearby restrooms
                        PlacesSearchResponse response = PlacesApi.nearbySearchQuery(
                                        geoApiContext,
                                        convertLatLngType(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                )
                                .radius(5000)
                                .keyword("restroom")
                                .await();
                        Log.d(TAG, "onMyLocationButtonClick: restroom found " + response.results.length);
                        for (PlacesSearchResult restroom : response.results) {

                            // Specify the fields to return.
                            final List<Place.Field> placeFields = Arrays.asList(
                                    Place.Field.ADDRESS,
                                    Place.Field.LAT_LNG,
                                    Place.Field.PHOTO_METADATAS,
                                    Place.Field.ID,
                                    Place.Field.NAME,
                                    Place.Field.OPENING_HOURS, Place.Field.UTC_OFFSET, Place.Field.BUSINESS_STATUS
                            );

                            // Construct a request object, passing the place ID and fields array.
                            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(restroom.placeId, placeFields);

                            Places.initialize(getActivity(), BuildConfig.MAPS_API_KEY);
                            PlacesClient placesClient = Places.createClient(getActivity());
                            placesClient.fetchPlace(request).addOnSuccessListener((res) -> {
                                Place place = res.getPlace();
                                Log.i(TAG, "onMyLocationButtonClick: " + "Place found: " + place.getName() + " " +
                                        place.getLatLng().toString());
                                Log.d(TAG, "onMyLocationButtonClick: open_now: " + place.isOpen());


                                // Get the photo metadata.
                                final List<PhotoMetadata> metadatas = place.getPhotoMetadatas();
                                if (metadatas == null || metadatas.isEmpty()) {
                                    Log.w(TAG, "No photo metadata.");
                                    return;
                                }
                                final PhotoMetadata photoMetadata = metadatas.get(0);

                                // Get the attribution text.
                                final String attributions = photoMetadata.getAttributions();

                                // Create a FetchPhotoRequest.
                                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                        .setMaxWidth(500) // Optional.
                                        .setMaxHeight(300) // Optional.
                                        .build();
                                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
//                                    imageView.setImageBitmap(bitmap);

                                    RestroomManager restroomManager = RestroomManager.getInstance();
                                    restroomManager.addRestroom(new Restroom(
                                            place.getLatLng(),
                                            place.getId(),
                                            bitmap,
                                            Boolean.TRUE.equals(place.isOpen()),
                                            place.getName(),
                                            place.getAddress()
                                    ));

                                }).addOnFailureListener((exception) -> {
                                    if (exception instanceof ApiException) {
                                        final ApiException apiException = (ApiException) exception;
                                        Log.e(TAG, "Place not found: " + exception.getMessage());
                                    }
                                });


                            }).addOnFailureListener((exception) -> {
                                if (exception instanceof ApiException) {
                                    final ApiException apiException = (ApiException) exception;
                                    Log.e(TAG, "Place not found: " + exception.getMessage());
                                }
                            });

                        }

                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Log.d(TAG, "getNearbyRestrooms: failed to fetch current location ");
                }
                return true;
            }
        });
    }

    private void addRestroomIconOnMap() {
//        getNearbyRestrooms();
        binding.mapShowRrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestroomManager restroomManager = RestroomManager.getInstance();
                Log.d(TAG, "addRestroomIconOnMap: " + restroomManager.getRestrooms().size());
                for (int i = 0; i < restroomManager.getRestrooms().size(); i++) {
                    Log.d(TAG, "addRestroomIconOnMap: added" + i);

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(restroomManager.getRestroomByIndex(i).getLatLng())
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    getMarkerIconWithLabel(
                                            getActivity(),
                                            restroomManager.getRestroomByIndex(i).getOpeningStatus()))));

                    restroomManager.getRestrooms().get(i).setMarkerId(marker.getId());
                }
            }
        });

    }
}