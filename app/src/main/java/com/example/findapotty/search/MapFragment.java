package com.example.findapotty.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.BuildConfig;
import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentMapBinding;
import com.example.findapotty.model.Restroom;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.Unit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private boolean grantedDeviceLocation = false;
    private Location currentLocation = null;
    private boolean onSerchingComplete = false;
    FragmentMapBinding binding;
    private static final String TAG = "MapFragment";
    private RecyclerView recyclerView;
    private NearbyRestroomsRecyclerViewAdaptor adaptor;
    private boolean onActiveScroll = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        binding.setLifecycleOwner(this);
        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        Log.d(TAG, "onCreateView: map create");
//        mSearchText = binding.inputSearch;
        recyclerView = binding.fmNearbyRestrooms;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new NearbyRestroomsRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);
        scrollToAttach();
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        // save map state
        MapStateManager mgr = new MapStateManager(requireActivity());
        mgr.saveMapState(googleMap);
//        Toast.makeText(requireActivity()(), "Map State has been save", Toast.LENGTH_SHORT).show();
//        MapStateManager.getInstance().saveGoogleMap(googleMap);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mMapView.getMapAsync(this);
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
//        searchListener();
        getNearbyRestrooms();
        addRestroomIconOnMap();


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
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // device location
        if (grantedDeviceLocation) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    /*
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
    */
/*
    private void searchAddress() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(requireActivity()());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);


            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(requireActivity()(), address.getAddressLine(0), Toast.LENGTH_SHORT).show();

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
            Toast.makeText(requireActivity()(), "Unable to fetch the entered address", Toast.LENGTH_SHORT).show();
        }
    }
*/
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(latLng).zoom(zoom).build()
                )
        );
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        return null;
    }

    @SuppressLint("MissingInflatedId")
//    private Bitmap getMarkerIconWithLabel(Context context, ImageView markerImage, String markerLabel, float angle) {
    private Bitmap getMarkerIconWithLabel(Context context, String markerLabel) {
        IconGenerator iconGenerator = new IconGenerator(context);
        @SuppressLint("InflateParams") View markerView = LayoutInflater.from(context).inflate(R.layout.marker_restroom, null);
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
    // com.google.android.gms.maps.model.LatLng ==> com.google.maps.model.LatLng
    static com.google.maps.model.LatLng convertLatLngType1_1(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }

    // com.google.maps.model.LatLng ==> com.google.android.gms.maps.model.LatLng
    static com.google.android.gms.maps.model.LatLng convertLatLngTypeV1_2(com.google.maps.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.lat, latLng.lng);
    }

    // com.example.findapotty.model.LatLng ==> com.google.android.gms.maps.model.LatLng
    com.google.android.gms.maps.model.LatLng convertLatLngTypeV3_1(com.example.findapotty.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
    }

    // com.google.android.gms.maps.model.LatLng ==> com.example.findapotty.model.LatLng
    com.example.findapotty.model.LatLng convertLatLngTypeV3_2(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.example.findapotty.model.LatLng(latLng.latitude, latLng.longitude);
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
                        .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.map_marker_long_press))
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
                        PlacesSearchResponse placesSearchResponse = PlacesApi.nearbySearchQuery(
                                        geoApiContext,
                                        convertLatLngType1_1(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                )
                                .radius(5000)
                                .keyword("restroom")
                                .await();
                        Log.d(TAG, "onMyLocationButtonClick: restroom found " + placesSearchResponse.results.length);
                        // loop through all nearby restrooms
                        for (PlacesSearchResult restroom : placesSearchResponse.results) {
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
                            Places.initialize(requireActivity(), BuildConfig.MAPS_API_KEY);
                            PlacesClient placesClient = Places.createClient(requireActivity());
                            placesClient.fetchPlace(request).addOnSuccessListener((res) -> {
                                Place place = res.getPlace();
                                // Get the photo metadata.
                                final List<PhotoMetadata> metadatas = place.getPhotoMetadatas();
                                if (metadatas == null || metadatas.isEmpty()) {
                                    Log.w(TAG, "No photo metadata.");
                                    return;
                                }
                                final PhotoMetadata photoMetadata = metadatas.get(0);
                                // Create a FetchPhotoRequest.
                                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                        .setMaxWidth(500) // Optional.
                                        .setMaxHeight(300) // Optional.
                                        .build();
                                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                                    // get distance
                                    try {
                                        DistanceMatrix distanceMatrix =
                                                DistanceMatrixApi.newRequest(geoApiContext)
                                                        .origins(new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                                        .destinations(convertLatLngType1_1(Objects.requireNonNull(place.getLatLng())))
//                                                    .mode(TravelMode.WALKING)
                                                        .units(Unit.METRIC)// m, km
                                                        .await();
                                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                                        // ***************** add restroom *****************
                                        NearbyRestroomsManager.getInstance()
                                                .addRestroom(new NearbyRestroom(
                                                                new Restroom(
                                                                        convertLatLngTypeV3_2(place.getLatLng()),
                                                                        place.getId(),
                                                                        bitmap,
                                                                        saveRestroomPhotosToStorage(place.getId(), bitmap),
                                                                        place.getName(),
                                                                        place.getAddress()
                                                                ),
                                                                Boolean.TRUE.equals(place.isOpen()),
                                                                distanceMatrix.rows[0].elements[0].status == DistanceMatrixElementStatus.OK ?
                                                                        distanceMatrix.rows[0].elements[0].distance.inMeters : -1,
                                                                distanceMatrix.rows[0].elements[0].status == DistanceMatrixElementStatus.OK ?
                                                                        distanceMatrix.rows[0].elements[0].distance.humanReadable : "Unknown"
                                                        )
                                                );
                                        adaptor.notifyItemInserted(0);

                                    } catch (ApiException | InterruptedException | IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                }).addOnFailureListener((exception) -> {
                                    if (exception instanceof ApiException) {
                                        final ApiException apiException = (ApiException) exception;
                                        Log.e(TAG, "Photo not found: " + exception.getMessage());
                                    }
                                });


                            }).addOnFailureListener((exception) -> {
                                if (exception instanceof ApiException) {
                                    final ApiException apiException = (ApiException) exception;
                                    Log.e(TAG, "Place not found: " + exception.getMessage());
                                }
                            });

                        }

                    } catch (ApiException | InterruptedException | IOException e) {
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
                NearbyRestroomsManager restroomsManager = NearbyRestroomsManager.getInstance();
                Log.d(TAG, "addRestroomIconOnMap: " + restroomsManager.getRestrooms().size());
                for (int i = 0; i < restroomsManager.getRestrooms().size(); i++) {
                    Log.d(TAG, "addRestroomIconOnMap: added" + i);
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(convertLatLngTypeV3_1(restroomsManager.getRestroomByIndex(i).getLatLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    getMarkerIconWithLabel(
                                            requireActivity(),
                                            restroomsManager.getRestroomByIndex(i).getOpeningStatus()))));
                    restroomsManager.getRestroomsList().get(i).setMarkerId(marker.getId());

                }
                NearbyRestroomsManager.getInstance().sortByDistance();
                adaptor.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }
        });
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

    private void setDownloadPhotoUrl(Restroom restroom, String photoPath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageRef.child(photoPath);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                restroom.setPhotoUrl(String.valueOf(uri));
            }
        });
    }

    // https://www.jb51.net/article/198584.htm
    // https://developer.android.com/reference/androidx/recyclerview/widget/LinearSnapHelper
    // stick item after the scroll is stopped
    private void scrollToAttach() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!onActiveScroll) {
                        recyclerView.postDelayed(() -> smoothScrollToCenter(), 50);
                    }
                }
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_SETTLING) {
                    onActiveScroll = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING && !onActiveScroll) {
                    // scroll is almost stop in this range
                    if (dx >= -5 && dx <= 5) {
                        recyclerView.stopScroll();
                    }
                }
            }
        });

    }

    private void smoothScrollToCenter() {
        onActiveScroll = true;
        View stickyInfoView = recyclerView.getChildAt(0); // get the first visible view
        int rcyWidth = recyclerView.getWidth();
        int itemWidth = stickyInfoView.getWidth();
        int itemRight = stickyInfoView.getRight();
        int threshold = (rcyWidth - itemWidth) / 2;
        // when item left edge is not attached to threshold
        if ((itemWidth + threshold) != itemRight) {
            // over half of the view is visible
            if (itemRight >= (itemWidth / 2)) {
                recyclerView.smoothScrollBy(-(itemWidth - itemRight + threshold), 0);// scroll back to the item
            }
            // under half of the view is visible
            else {
                recyclerView.smoothScrollBy(itemRight - threshold, 0);// scroll to next item
            }
        }
    }
}