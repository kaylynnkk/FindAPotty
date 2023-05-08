package com.example.findapotty.search.restroompage;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.findapotty.R;
import com.example.findapotty.databinding.BottomSheetRestroomPageBinding;
import com.example.findapotty.model.Restroom;
import com.example.findapotty.search.NearbyRestroom;
import com.example.findapotty.user.FavoriteRestroom;
import com.example.findapotty.user.FavoriteRestroomsManager;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RestroomPageBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "RestroomPageBottomSheet";
    ArrayList<RestroomReview> restroomReviews = new ArrayList<>();
    ArrayList<RestroomReview> spareReviewList = new ArrayList<>();
    private ImageView rr_photo;

    private BottomSheetRestroomPageBinding binding;
    private View rootView;
    private RecyclerView recyclerView;
    private RestroomReviewRecyclerViewAdaptor adaptor;
    Query dbr;
    FirebaseRecyclerOptions<RestroomReview> fbo;

    private boolean isFavorite = false;

    private Restroom restroom;
    private NearbyRestroom nearbyRestroom;
    private FavoriteRestroom favoriteRestroom;

    private Query mdb;
    private DatabaseReference refFavoriteRestrooms;

    String[] ratingsList = { "5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star"};
    String[] sorterOptionsList = { "Recommended", "Most Recent to Oldest", "Oldest to Most Recent"};
    private int ratingOptionPicked = 6;
    String sorterOptionPicked = "";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialog);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_restroom_page, null);
//        dialog.setContentView(rootView);
        binding = DataBindingUtil.bind(rootView);
//                .inflate(
//                getLayoutInflater(),
//                R.layout.bottom_sheet_restroom_page,
//                null, false);
//        binding.setLifecycleOwner(this);
        dialog.setContentView(binding.getRoot());

        assert getArguments() != null;
        restroom = RestroomPageBottomSheetArgs.fromBundle(getArguments()).getRestroom();
        nearbyRestroom = RestroomPageBottomSheetArgs.fromBundle(getArguments()).getNearByRestroom();
        favoriteRestroom = RestroomPageBottomSheetArgs.fromBundle(getArguments()).getFavoriteRestroom();
        mdb = FirebaseDatabase.getInstance().getReference();
     //   refFavoriteRestrooms = mdb.child("users")
       //         .child(User.getInstance().getUserId()).child("favoriteRestrooms");
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference("Reviews")
                .child(restroom.getPlaceID())
                .orderByChild("helpfulness");
        getReviewInfo();
        //populateReviewSection(dbr);
        initRestroomPage();
        addReviewListener();
        sortReviewListener();
        editPageListener();
        binding.bsrpBtnFavorite.setOnClickListener(view -> {
            setFavoriteState();
        });
        recyclerView = binding.rrPgReviewSection;

        return dialog;
    }

    private void initRestroomPage() {
//        if (favoriteRestroom!= null) {
//            Log.d(TAG, "initRestroomPage: favoriteRestroom");
//        }
//        if (nearbyRestroom != null) {
//            Log.d(TAG, "initRestroomPage: nearbyRestroom");
//        }
        if (restroom != null) {
            // set name
            binding.rrPgRrname.setText(restroom.getName());
            // set address
            binding.rrPgRraddress.setText(restroom.getAddress());

            binding.rrPgReviewCount.setText(restroomReviews.size()+" num Reviews");
            // set photos
            if (restroom.getPhotoBitmap() != null) {
                binding.rrPgRrPhotos.setImageBitmap(restroom.getPhotoBitmap());
            } else {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference ref = storageRef.child(restroom.getPhotoPath());
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(binding.getRoot().getContext())
                            .asBitmap()
                            .dontAnimate()
                            .load(uri)
                            .into(binding.rrPgRrPhotos);
                });
            }
            // set favorite state
            if (FavoriteRestroomsManager.getInstance().getRestrooms().containsKey(restroom.getPlaceID())) {
                // restroom found in favorite list
                binding.bsrpBtnFavorite.setImageResource(R.drawable.ic_selected_favorite);
                isFavorite = true;
            }

        }
    }

    private void sortReviewListener() {
        Button btn = rootView.findViewById(R.id.rr_pg_sort_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Sort Button Selected", Toast.LENGTH_SHORT).show();
                sortFilterBottomSheet();
            }

        });
    }

    public void sortFilterBottomSheet(){
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_reviewfilter);
            // get view of spinners and textview button
            SmartMaterialSpinner<String> filterRating = bottomSheetDialog.findViewById(R.id.rating_filter_options);
            SmartMaterialSpinner<String> sorterOption = bottomSheetDialog.findViewById(R.id.sorter_options);
            TextView applyfilters = bottomSheetDialog.findViewById(R.id.apply_filters);
            TextView resetfilters = bottomSheetDialog.findViewById(R.id.reset_filters);
            //create dropdown men when finite options and save user input into variable when selected
            ArrayAdapter ad1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ratingsList);
            ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);filterRating.setAdapter(ad1);
            filterRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    // get rating number to be filtered
                    ratingOptionPicked = 5 - position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    ratingOptionPicked = 6;
                }
            });
            ArrayAdapter ad2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sorterOptionsList);
            ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);sorterOption.setAdapter(ad2);
            sorterOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    sorterOptionPicked = sorterOptionsList[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    sorterOptionPicked = "";
                }
            });
            bottomSheetDialog.show();
            resetfilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), restroomReviews);
                    recyclerView.setAdapter(adaptor);
                }
            });
            // restart recyclerview after applying filter information
            applyfilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference("Reviews")
                            .child(restroom.getPlaceID())
                            .orderByChild("helpfulness");
                    populateReviewSection(dbr,ratingOptionPicked,sorterOptionPicked);
                    /*
                    if(ratingOptionPicked == -1 & sorterOptionPicked == ""){
                    }
                    else if(ratingOptionPicked == -1 & sorterOptionPicked != ""){
                        if (sorterOptionPicked == "Recommended"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getHelpfulness));
                        }
                        else if (sorterOptionPicked == "Most Recent to Oldest"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                        }
                        else if (sorterOptionPicked == "Oldest to Most Recent"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                            Collections.sort(spareReviewList,
                                    Comparator.comparing(RestroomReview::getTimestamp).reversed());
                        }
                    }
                    else if(ratingOptionPicked != -1 & sorterOptionPicked == "") {
                        for (RestroomReview rr : spareReviewList) {
                            if (!rr.getRating().equals(ratingOptionPicked)) {
                                spareReviewList.remove(rr);
                            }
                        }
                    }

                    else if(ratingOptionPicked != -1 & sorterOptionPicked != ""){
                        for(RestroomReview rr: restroomReviews){
                            if (!rr.getRating().equals(ratingOptionPicked)) {
                                spareReviewList.remove(rr);
                            }
                        }
                        if (sorterOptionPicked == "Recommended"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getHelpfulness));
                        }
                        else if (sorterOptionPicked == "Most Recent to Oldest"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                        }
                        else if (sorterOptionPicked == "Oldest to Most Recent"){
                            spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                            Collections.sort(spareReviewList,
                                    Comparator.comparing(RestroomReview::getTimestamp).reversed());
                        }

                    }
                    if (dbr != null) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), spareReviewList);
                        recyclerView.setAdapter(adaptor);
                    }

                     */
                    bottomSheetDialog.hide();


                }
            });
        }


    private void addReviewListener() {
        FloatingActionButton btn = rootView.findViewById(R.id.rr_page_add_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass resttroom object to add review fragment
                NavController controller = NavHostFragment.findNavController(RestroomPageBottomSheet.this);
                Bundle bundle = new Bundle();
                bundle.putParcelable("restroom_data", restroom);
                controller.navigate(R.id.action_navg_rr_pg_fragment_to_addRestroomReviewFragment, bundle);
//                restroomReviews.add(0, new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "1243r4tgg g5"));
//                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
//                mdb.child("restroom_pages").child(mdb.push().getKey()).setValue(restroomReviews.get(0));
//                adaptor.notifyItemInserted(0);
//                recyclerView.smoothScrollToPosition(0);

            }
        });
    }

    public void resizeImageOnScroll() {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                rr_photo = view.findViewById(R.id.rr_pg_rr_photos);
                float ratio = rr_photo.getHeight() / rr_photo.getWidth();
                float scale = getContext().getResources().getDisplayMetrics().density;

            }
        });
    }

    public void editPageListener() {
        rootView.findViewById(R.id.rr_pg_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavController controller = Navigation.findNavController(view);
                NavController controller = NavHostFragment.findNavController(RestroomPageBottomSheet.this);
                controller.navigate(R.id.action_navg_rr_pg_fragment_to_navg_rr_pg_edit_fragment);
            }
        });
    }

    private void setFavoriteState() {
        if (!isFavorite) {
            isFavorite = true;
            binding.bsrpBtnFavorite.setImageResource(R.drawable.ic_selected_favorite);
            Toast.makeText(binding.getRoot().getContext(),
                    "Added to faviorte list", Toast.LENGTH_SHORT).show();
            FavoriteRestroomsManager.getInstance().addRestroom(new FavoriteRestroom(restroom));

        } else {
            isFavorite = false;
            binding.bsrpBtnFavorite.setImageResource(R.drawable.ic_unselected_favorite);
            Toast.makeText(binding.getRoot().getContext(),
                    "Removed from faviorte list", Toast.LENGTH_SHORT).show();
            FavoriteRestroomsManager.getInstance().removeRestroom(restroom.getPlaceID());
        }
        refFavoriteRestrooms.setValue(FavoriteRestroomsManager.getInstance().getRestrooms());
    }

    private void populateReviewSection(Query dbr, Integer ratingOptionPicked, String sorterOptionPicked) {


        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                spareReviewList.clear();
                restroomReviews.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    RestroomReview rrObj = postSnapshot.getValue(RestroomReview.class);
                    spareReviewList.add(rrObj);
                    restroomReviews.add(rrObj);

                }
                if(ratingOptionPicked == 6 & sorterOptionPicked == ""){
                }
                else if(ratingOptionPicked == 6 & sorterOptionPicked != ""){
                    if (sorterOptionPicked == "Recommended"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getHelpfulness));
                    }
                    else if (sorterOptionPicked == "Most Recent to Oldest"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                        Collections.sort(spareReviewList,
                                Comparator.comparing(RestroomReview::getTimestamp).reversed());
                    }
                    else if (sorterOptionPicked == "Oldest to Most Recent"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                    }
                }
                else if(ratingOptionPicked != 6 & sorterOptionPicked == "") {
                    for (RestroomReview rr : restroomReviews) {
                        if (!rr.getRating().toString().contains(""+ratingOptionPicked)) {
                            spareReviewList.remove(rr);
                        }
                    }
                }

                else if(ratingOptionPicked != 6 & sorterOptionPicked != ""){
                    for (RestroomReview rr : restroomReviews) {
                        if (!rr.getRating().toString().contains(""+ratingOptionPicked)) {
                            spareReviewList.remove(rr);
                        }
                    }
                    if (sorterOptionPicked == "Recommended"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getHelpfulness));
                    }
                    else if (sorterOptionPicked == "Most Recent to Oldest"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                        Collections.sort(spareReviewList,
                                Comparator.comparing(RestroomReview::getTimestamp).reversed());
                    }
                    else if (sorterOptionPicked == "Oldest to Most Recent"){
                        spareReviewList.sort(Comparator.comparing(RestroomReview::getTimestamp));
                    }

                }
                if (dbr != null) {
                    // use firebas eui to populate recycler straigther form databse
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), spareReviewList);
                    recyclerView.setAdapter(adaptor);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getReviewInfo() {
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference("Reviews")
                .child(restroom.getPlaceID())
                .orderByChild("helpfulness");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                restroomReviews.clear();
                float totalRating =0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    RestroomReview rrObj = postSnapshot.getValue(RestroomReview.class);
                    restroomReviews.add(rrObj);

                }
                Log.i("ReviewList", "" + restroomReviews);
                for(RestroomReview rr: restroomReviews){
                    totalRating += rr.getRating();
                }
                binding.rrPgAvgRating.setRating(totalRating/restroomReviews.size());
                binding.rrPgReviewCount.setText(restroomReviews.size() + " Reviews");
                if (dbr != null) {
                    // use firebas eui to populate recycler straigther form databse
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), restroomReviews);
                    recyclerView.setAdapter(adaptor);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
