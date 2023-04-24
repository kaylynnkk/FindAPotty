package com.example.findapotty.search.restroompage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentAddRestroomReviewBinding;
import com.example.findapotty.databinding.FragmentDiaryBinding;
import com.example.findapotty.diary.ResultsFragment;
import com.example.findapotty.model.Restroom;
import com.example.findapotty.tunes.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class AddRestroomReviewFragment extends Fragment {

    private static final String TAG = "AddRestroomReviewFragment";
    private FragmentAddRestroomReviewBinding binding;

    private DatabaseReference dbr;
    private Button backBT, submitBT, imgBT;
    private ImageView imgIV;
    private RatingBar ratingRB;
    private EditText reviewET;
    private String userId, userName, imgURL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRestroomReviewBinding.inflate(inflater, container, false);
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference("Reviews");
        ratingRB = binding.rating;
        reviewET = binding.comment;
        binding.submit.setOnClickListener(view -> {
            Restroom rr = getArguments().getParcelable("restroom_data");
            String restroomId = rr.getPlaceID();
            String reviewId = dbr.push().getKey();
            String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/findapotty.appspot.com/o/" +
                    "avatars%2Fdefault_avatar.jpg?alt=media&token=bfa281bd-bfc5-4f47-b62a-258f6698b6d6";
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Create review object
            RestroomReview rev = new RestroomReview(restroomId, avatarUrl, username, userid,
                    ratingRB.getRating(),
                    reviewET.getText().toString().trim(),
                    String.valueOf(System.currentTimeMillis()));
            // go to userId branch of databse and insert review object
            dbr.child(reviewId).setValue(rev);
            // Pop up to alert user that review has been submitted
            Toast.makeText(getContext(), "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
            // leave write review activity and start display review activity
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.add_review, new RestroomPageBottomSheet())
                    .addToBackStack(null)
                    .commit();
            NavController controller = NavHostFragment.findNavController(AddRestroomReviewFragment.this);
            controller.navigate(R.id.action_navg_rr_pg_fragment_to_addRestroomReviewFragment);
        });
        binding.back.setOnClickListener(view -> {
            onBackPressed();
        });

        return binding.getRoot();
    }

    private void onSubmit(View view) {
        Bundle bundle = getArguments();
        Restroom rr = (Restroom) bundle.getSerializable("restroom_data");
        String restroomId = rr.getPlaceID();
        String reviewId = dbr.push().getKey();
        String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/findapotty.appspot.com/o/" +
                "avatars%2Fdefault_avatar.jpg?alt=media&token=bfa281bd-bfc5-4f47-b62a-258f6698b6d6";
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Create review object
        RestroomReview rev = new RestroomReview(restroomId, avatarUrl, username, userid,
                ratingRB.getRating(),
                reviewET.getText().toString().trim(),
                String.valueOf(System.currentTimeMillis()));
        // go to userId branch of databse and insert review object
        dbr.child(reviewId).setValue(rev);
        // Pop up to alert user that review has been submitted
        Toast.makeText(getContext(), "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
        // leave write review activity and start display review activity
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.add_review, new RestroomPageBottomSheet())
                .addToBackStack(null)
                .commit();

    }

    private void onBackPressed() {
        // leave write review activity and start display review activity
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.add_review, new RestroomPageBottomSheet())
                .addToBackStack(null)
                .commit();

    }

    /*private void loadPhotos() {
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }*/
}
