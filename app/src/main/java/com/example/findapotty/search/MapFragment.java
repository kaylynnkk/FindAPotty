package com.example.findapotty.search;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.BuildConfig;
import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentMapBinding;
import com.example.findapotty.user.User;
import com.example.findapotty.user.VisitedRestroom;
import com.example.findapotty.user.VisitedRestroomsManager;
import com.google.android.gms.common.util.Hex;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.PhotoRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.Unit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private boolean grantedDeviceLocation = false;
    private Location currentLocation = null;
    FragmentMapBinding binding;
    private static final String TAG = "MapFragment";
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NearbyRestroomsRecyclerViewAdaptor adaptor;
    private int prevSnapPosition = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
//        binding.setLifecycleOwner(this);
        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        Log.d(TAG, "onCreateView: map create");
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        // save map state
        MapStateManager mgr = new MapStateManager(requireActivity());
        mgr.saveMapState(googleMap);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        MapMarkersManager.getInstance().getMarkers().clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mMapView.getMapAsync(this);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        permissionCheck();
        init();
        initRecyclerView();
        // add marker on long press on map
//        addMarker();
        // restroom info page
//        displayRestroomPage();
        googleMap.setOnMarkerClickListener((Marker marker) -> {
            scrollRcyToPosition(marker);
            return true;
        });
        googleMap.setOnMyLocationButtonClickListener(() -> {
            getDeviceLocation();
            return true;
        });
        binding.mapShowRrButton.setOnClickListener((view) -> addRestroomIconOnMap());


    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            grantedDeviceLocation = true;
            Log.d(TAG, "permissionCheck: permission grated");
        }
    }

    @SuppressLint("MissingPermission")
    private void init() {
        Log.d(TAG, "init: ");
//        if (MapStateManager.getInstance().getGoogleMap() != null) {
//            GoogleMap map = MapStateManager.getInstance().getGoogleMap();
//            googleMap.moveCamera(
//                    CameraUpdateFactory.newCameraPosition(map.getCameraPosition()));
//            googleMap.setMapType(map.getMapType());
//        }
        // resume map state
        MapStateManager mgr = new MapStateManager(requireActivity());
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null) {
//            Toast.makeText(requireActivity()(), "entering Resume State", Toast.LENGTH_SHORT).show();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            googleMap.setMapType(mgr.getSavedMapType());
        }
        // UI Section
        // zoom in/out button
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // device location
        if (grantedDeviceLocation) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    private void initRecyclerView() {
        recyclerView = binding.fmNearbyRestrooms;
        recyclerView.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new NearbyRestroomsRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = linearSnapHelper.findTargetSnapPosition(layoutManager, 0, 0);
                Log.d(TAG, "onScrollStateChanged: cur pos " + position);
                if (prevSnapPosition != position) {
                    Log.d(TAG, "onScrollStateChanged: going another restroom");
                    Log.d(TAG, "onScrollStateChanged: prev" + prevSnapPosition + " " + "cur pos");
                    resetMarkerIcon(prevSnapPosition);
                    highlightMarkerIcon(position);
                    prevSnapPosition = position;
                }
            }
        });
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(latLng).zoom(zoom).build()
                )
        );
    }

    private void showDialog(int id) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.CustomBottomSheetDialog);
        BottomSheetBehavior<FrameLayout> behavior = dialog.getBehavior();
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Toast.makeText(requireActivity(), "", Toast.LENGTH_SHORT).show();
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
                        .icon(Utils.bitmapDescriptorFromVector(requireActivity(), R.drawable.map_marker_long_press))
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
                NearbyRestroom restroom = NearbyRestroomsManager.getInstance().getRestroomByMarkerId(marker.getId());
                if (restroom != null) {
                    // insert to database
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    VisitedRestroom visitedRestroom =
                            VisitedRestroomsManager.getInstance().getRestrooms().get(restroom.getPlaceID());
                    //yyyy-MM-dd HH:mm:ss
                    @SuppressLint("SimpleDateFormat") String currentDateTime = new SimpleDateFormat("MM-dd-yyyy HH:mm")
                            .format(Calendar.getInstance().getTime());
                    if (visitedRestroom != null) {
                        visitedRestroom.setFrequency(visitedRestroom.getFrequency() + 1);
                        visitedRestroom.setDateTime(currentDateTime);
                    } else {
                        VisitedRestroomsManager.getInstance().addRestroom(
                                new VisitedRestroom(restroom, currentDateTime, 1)
                        );
                    }
                    ref.child("users").child(User.getInstance().getUserId()).child("visitedRestrooms")
                            .setValue(VisitedRestroomsManager.getInstance().getRestrooms());
                    try {
                        NavController controller = Navigation.findNavController(mMapView);
                        NavDirections action =
                                MapFragmentDirections.actionMapFragment2ToRestroomPageBottomSheet2(restroom, restroom, null);
                        controller.navigate(action);
                        Log.d(TAG, "onMarkerClick: " + marker.getId());
                    } catch (IllegalArgumentException e) {
                        Log.d(TAG, "onMarkerClick: failed to find action");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        if (grantedDeviceLocation) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        currentLocation = location;
                        Log.d(TAG, "onSuccess: current location fetched" + currentLocation.getLatitude()
                                + ", " + currentLocation.getLongitude());
                        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 12);
                    } else {
                        Toast.makeText(requireActivity(), "Failed to fetch current location", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: failed to fetch current location");
                    }
                }
            });
        }
    }

    /*
     * 1. fetch nearby places
     * 2. fetch place metadata
     * 3. fetch photos
     * 4. fetch distance
     */
    private void getNearbyRestrooms() {
        if (currentLocation != null) {
            GeoApiContext geoApiContext = new GeoApiContext.Builder()
                    .apiKey(BuildConfig.MAPS_API_KEY)
                    .build();
            try {
                // 1. fetch nearby places
                PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(
                                geoApiContext,
                                Utils.convertLatLngType1_1(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        )
                        .radius(5000)
                        .keyword("restroom")
                        .await();
                // async run each fetching task, 4 tasks at a time
                ExecutorService executor = Executors.newFixedThreadPool(4);
                for (PlacesSearchResult restroom : placesSearchResponse.results) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                NearbyRestroom nearbyRestroom = new NearbyRestroom();
                                // Specify the fields to return.
                                final PlaceDetailsRequest.FieldMask[] placeFieldMasks = {
                                        PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS,
                                        PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION_LAT,
                                        PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION_LNG,
                                        PlaceDetailsRequest.FieldMask.PHOTOS,
                                        PlaceDetailsRequest.FieldMask.PLACE_ID,
                                        PlaceDetailsRequest.FieldMask.NAME,
                                        PlaceDetailsRequest.FieldMask.OPENING_HOURS,
                                        PlaceDetailsRequest.FieldMask.UTC_OFFSET,
                                        PlaceDetailsRequest.FieldMask.BUSINESS_STATUS,
                                        PlaceDetailsRequest.FieldMask.RATING
                                };
                                // 2. fetch place metadata
                                PlaceDetails placeDetails = new PlaceDetailsRequest(geoApiContext).
                                        placeId(restroom.placeId)
                                        .fields(placeFieldMasks)
                                        .await();
                                String rrName = placeDetails.name;
                                Log.d(TAG, rrName + " run: place detail fetched");
                                // set attributes for restrooms
                                nearbyRestroom.setLatLng(Utils.convertLatLngTypeV2_1(placeDetails.geometry.location));
                                nearbyRestroom.setPlaceID(placeDetails.placeId);
                                nearbyRestroom.setName(placeDetails.name);
                                nearbyRestroom.setAddress(placeDetails.formattedAddress);
                                nearbyRestroom.setOpenNow(placeDetails.openingHours != null ?
                                        placeDetails.openingHours.openNow : false);
                                nearbyRestroom.setRating(placeDetails.rating);
                                // 3. fetch photos
                                if (placeDetails.photos != null) {
                                    ImageResult imageResult = new PhotoRequest(geoApiContext)
                                            .photoReference(placeDetails.photos[0].photoReference)
                                            .maxWidth(500)
                                            .maxHeight(300)
                                            .await();
                                    Log.d(TAG, rrName + " run: photo fetched");
                                    // set attributes for restrooms
                                    Bitmap photoBitmap =
                                            BitmapFactory.decodeByteArray(imageResult.imageData, 0, imageResult.imageData.length);
                                    nearbyRestroom.setPhotoBitmap(photoBitmap);
                                    nearbyRestroom.setPhotoPath(saveRestroomPhotosToStorage(placeDetails.placeId, photoBitmap));
                                }
                                // 4. fetch distance
//                                DistanceMatrix distanceMatrix = new DistanceMatrixApiRequest(geoApiContext)
//                                        .origins(new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
//                                        .destinations(placeDetails.geometry.location)
////                            .mode(TravelMode.WALKING)
//                                        .units(Unit.METRIC)// m, km
//                                        .await();
//                                Log.d(TAG, rrName + " run: distance fetched");
//                                // set attributes for restrooms
//                                nearbyRestroom.setCurrentDistance(
//                                        distanceMatrix.rows[0].elements[0].status == DistanceMatrixElementStatus.OK ?
//                                                distanceMatrix.rows[0].elements[0].distance.inMeters : Long.MAX_VALUE
//                                );
//                                nearbyRestroom.setCurrentDistanceText(
//                                        distanceMatrix.rows[0].elements[0].status == DistanceMatrixElementStatus.OK ?
//                                                distanceMatrix.rows[0].elements[0].distance.humanReadable : "Unknown"
//                                );
                                NearbyRestroomsManager.getInstance().addRestroom(nearbyRestroom);


                            } catch (ApiException | InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                executor.shutdown(); // do not accept threads anymore
                executor.awaitTermination(10, TimeUnit.SECONDS); // block and wait for all threads to finish until timed out

            } catch (ApiException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Log.d(TAG, "getNearbyRestrooms: failed to fetch current location ");
        }
    }

    private void addRestroomIconOnMap() {
        recyclerView.setVisibility(View.VISIBLE);
        getNearbyRestrooms();
        // add marker on map
        NearbyRestroomsManager nearbyRestroomsManager = NearbyRestroomsManager.getInstance();
        for (int i = 0; i < nearbyRestroomsManager.getCount(); i++) {
            NearbyRestroom nearbyRestroom = nearbyRestroomsManager.getRestroomByIndex(i);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(Utils.convertLatLngTypeV3_1(nearbyRestroom.getLatLng()))
                    .icon(Utils.getMarkerIconWithLabel(
                            requireActivity(),
                            nearbyRestroom.getOpeningStatus()))
                    .zIndex(1)
            );
            if (marker != null) {
                MapMarkersManager.getInstance().addMarker(marker);
                nearbyRestroom.setMarkerId(marker.getId());
            }
        }
        // sort restrooms
        NearbyRestroomsManager.getInstance().sortByDistance();
        adaptor.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(0);
    }

    private String saveRestroomPhotosToStorage(String placeId, Bitmap bitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // hash photo with sha-1
        String sha1Photo;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            sha1Photo = Hex.bytesToStringLowercase(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String photoPath = "images/" + placeId + "/" + sha1Photo + ".jpg";
        StorageReference mountainImagesRef = storageRef.child(photoPath);
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: photo upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: photo uploaded");
            }
        });
        return photoPath;
    }

    private void highlightMarkerIcon(int position) {
        if (position >= 0) {
            moveCamera(
                    Utils.convertLatLngTypeV3_1(
                            NearbyRestroomsManager.getInstance().getRestroomByIndex(position).getLatLng()
                    ),
                    googleMap.getCameraPosition().zoom
            );
            // set icon for selected marker
            NearbyRestroom nearbyRestroom = NearbyRestroomsManager.getInstance().getRestroomByIndex(position);
            Marker marker = MapMarkersManager.getInstance().getMarkerById(nearbyRestroom.getMarkerId());
            if (marker != null) {
                marker.setIcon(Utils.getMarkerIconWithLabelLarge(
                                getContext(),
                                nearbyRestroom.getOpeningStatus()
                        )
                );
                marker.setZIndex(2);
            }
        }
    }

    private void resetMarkerIcon(int position) {
        //reset icon for previous selected marker
        NearbyRestroom nearbyRestroom = NearbyRestroomsManager.getInstance().getRestroomByIndex(position);
        Marker marker = MapMarkersManager.getInstance().getMarkerById(nearbyRestroom.getMarkerId());
        if (marker != null) {
            marker.setIcon(Utils.getMarkerIconWithLabel(
                            getContext(),
                            nearbyRestroom.getOpeningStatus()
                    )
            );
            marker.setZIndex(1);
        }
    }

    private void scrollRcyToPosition(Marker marker) {
        int index = NearbyRestroomsManager.getInstance().getRestroomIndexByMarkerId(marker.getId());
        if (index != -1) {
            recyclerView.smoothScrollToPosition(index);
        }
    }

}