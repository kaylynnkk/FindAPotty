package com.example.findapotty.search.restroompage;


import android.app.Dialog;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.findapotty.MainActivity;
import com.example.findapotty.R;
import com.example.findapotty.databinding.BottomSheetRestroomPageBinding;
import com.example.findapotty.diary.ResultsFragment;
import com.example.findapotty.model.Restroom;
import com.example.findapotty.search.NearbyRestroom;
import com.example.findapotty.tunes.TunesPlayerFragment;
import com.example.findapotty.user.FavoriteRestroom;
import com.example.findapotty.user.FavoriteRestroomsManager;
import com.example.findapotty.user.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestroomPageBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "RestroomPageBottomSheet";
 //   private final ArrayList<RestroomReview> restroomReviews = new ArrayList<>();
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
    String ratingOptionPicked;
    String sorterOptionPicked;
    String category;

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
        initRestroomPage();
       // initReviews();
        addReviewListener();
        sortReviewListener();
        editPageListener();
        binding.bsrpBtnFavorite.setOnClickListener(view -> {
            setFavoriteState();
        });
        recyclerView = binding.rrPgReviewSection;
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference("Reviews").child(restroom.getPlaceID());
        if(dbr != null) {
            // use firebas eui to populate recycler straigther form databse
            fbo = new FirebaseRecyclerOptions.Builder<RestroomReview>()
                    .setQuery(dbr, RestroomReview.class)
                    .build();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adaptor = new RestroomReviewRecyclerViewAdaptor(fbo);
            recyclerView.setAdapter(adaptor);
            adaptor.startListening();
        }
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
                String[] details = showBottonSheet();

                dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                        .getReference("Reviews")
                        .child(restroom.getPlaceID())
                        .equalTo(details[0]);
                if(dbr != null) {
                    // use firebas eui to populate recycler straigther form databse
                    adaptor.startListening();
                    fbo = new FirebaseRecyclerOptions.Builder<RestroomReview>()
                            .setQuery(dbr, RestroomReview.class)
                            .build();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adaptor = new RestroomReviewRecyclerViewAdaptor(fbo);
                    adaptor.startListening();
                    recyclerView.setAdapter(adaptor);

                }
            }
        });
    }

    public String[] showBottonSheet(){
        String[] options = {};
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.fragment_reviewfilter);

        SmartMaterialSpinner<String> filterRating = bottomSheetDialog.findViewById(R.id.rating_filter_options);
        SmartMaterialSpinner<String> sorterOption = bottomSheetDialog.findViewById(R.id.sorter_options);


        ArrayAdapter ad1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ratingsList);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);filterRating.setAdapter(ad1);
        filterRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ratingOptionPicked = ratingsList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter ad2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, sorterOptionsList);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);sorterOption.setAdapter(ad2);
        sorterOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sorterOptionPicked = sorterOptionsList[position];
                if(sorterOptionPicked == "Recommended"){
                    category = "helpfulness";
                }
                else{
                    category = "timestamp";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        bottomSheetDialog.show();
        options[0] = ratingOptionPicked;
        options[1] = sorterOptionPicked;
        options[2] = category;
        return options;
        }
    private void addReviewListener() {
        FloatingActionButton btn = rootView.findViewById(R.id.rr_page_add_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    //called to continue data being retrievedfrom friebase
    @Override public void onStart() {
        super.onStart();
        adaptor.startListening();
    }

    //called to stop data  retrieval from friebase
    @Override public void onStop() {
        super.onStop();
        adaptor.stopListening();
    }

}
