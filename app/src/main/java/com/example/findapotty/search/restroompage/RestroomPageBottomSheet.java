package com.example.findapotty.search.restroompage;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.example.findapotty.Restroom;
import com.example.findapotty.User;
import com.example.findapotty.databinding.BottomSheetRestroomPageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestroomPageBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "RestroomPageBottomSheet";
    private final ArrayList<RestroomReview> restroomReviews = new ArrayList<>();
    private ImageView rr_photo;

    private BottomSheetRestroomPageBinding binding;
    private View rootView;
    private RecyclerView recyclerView;
    private RestroomReviewRecyclerViewAdaptor adaptor;

    private boolean isFavorite = false;
    private Restroom restroom;

    private DatabaseReference mdb;
    private DatabaseReference refFavoriteRestrooms;

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
        binding.setLifecycleOwner(this);
        dialog.setContentView(binding.getRoot());

        recyclerView = binding.rrPgReviewSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), restroomReviews);
        recyclerView.setAdapter(adaptor);

        assert getArguments() != null;
        restroom = RestroomPageBottomSheetArgs.fromBundle(getArguments()).getRestroom();

        mdb = FirebaseDatabase.getInstance().getReference();
        refFavoriteRestrooms = mdb.child("users")
                .child(User.getInstance().getUserId()).child("favoriteRestrooms");

        initRestroomPage();
        initReviews();
        addReviewListener();
        editPageListener();
        binding.bsrpBtnFavorite.setOnClickListener(view -> {
            setFavoriteState();
        });


        return dialog;
    }

    private void initReviews() {
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim1"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim2"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim3"));
    }

    private void initRestroomPage() {
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
                    Glide.with(getContext())
                            .asBitmap()
                            .dontAnimate()
                            .load(uri)
                            .into(binding.rrPgRrPhotos);
                });
            }

            // set favorite state
            if (User.getInstance().getFavoriteRestrooms().containsKey(restroom.getPlaceID())) {
                // restroom found in favorite list
                binding.bsrpBtnFavorite.setImageResource(R.drawable.ic_selected_favorite);
                isFavorite = true;
            }

        }
    }

    private void addReviewListener() {
        FloatingActionButton btn = rootView.findViewById(R.id.rr_page_add_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add review");
//                restroomReviews.add(0, new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "1243r4tgg g5"));
//                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
//                mdb.child("restroom_pages").child(mdb.push().getKey()).setValue(restroomReviews.get(0));

                NavController controller = NavHostFragment.findNavController(RestroomPageBottomSheet.this);
                controller.navigate(R.id.action_navg_rr_pg_fragment_to_addRestroomReviewFragment);


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

            User.getInstance().addFavoriteRestroom(restroom.getPlaceID(), restroom);

        } else {
            isFavorite = false;
            binding.bsrpBtnFavorite.setImageResource(R.drawable.ic_unselected_favorite);
            Toast.makeText(binding.getRoot().getContext(),
                    "Removed from faviorte list", Toast.LENGTH_SHORT).show();

            User.getInstance().removeFavoriteRestroom(restroom.getPlaceID());
        }

        refFavoriteRestrooms.setValue(User.getInstance().getFavoriteRestrooms());
    }

}
