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
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AddRestroomReviewFragment extends Fragment {

    private static final String TAG = "AddRestroomReviewFragment";
    private FragmentAddRestroomReviewBinding binding;

    private DatabaseReference dbr;
    private Button backBT, submitBT, imgBT;
    private ImageView rrPhotoIV;
    private TextView nameTV;
    private RatingBar ratingRB;
    private EditText reviewET;
    private String userId, userName, imgURL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRestroomReviewBinding.inflate(inflater, container, false);
        ratingRB = binding.rating;
        reviewET = binding.comment;
        rrPhotoIV = binding.rrImg;
        nameTV = binding.rrName;
        //get restroom object so I can add placeid to object
        // initlaize the rest of adate thats will be use to creat object
        Restroom rr = getArguments().getParcelable("restroom_data");
        nameTV.setText(rr.getName());
        if (rr.getPhotoBitmap() != null) {
            rrPhotoIV.setImageBitmap(rr.getPhotoBitmap());}
        binding.submit.setOnClickListener(view -> {
            //get restroom object so I can add placeid to object
            String restroomId = rr.getPlaceID();
            dbr =  FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                    .getReference("Reviews").child(restroomId);
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Date date = new Date();
            String reviewId = dbr.push().getKey();
            String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/findapotty.appspot.com/o/" +
                    "avatars%2Fdefault_avatar.jpg?alt=media&token=bfa281bd-bfc5-4f47-b62a-258f6698b6d6";
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Create review object
            RestroomReview rev = new RestroomReview(restroomId, reviewId, avatarUrl, username, userid,
                    ratingRB.getRating(),
                    reviewET.getText().toString().trim(),
                    formatter.format(date),0);
            // go to userId branch of databse and insert review object
            dbr.child(reviewId).setValue(rev);
            // Pop up to alert user that review has been submitted
            Toast.makeText(getContext(), "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
            // leave write review activity and start display review activity
            /*NavController controller = NavHostFragment.findNavController(AddRestroomReviewFragment.this);
            controller.navigate(R.id.action_addRestroomReviewFragment_to_navg_rr_pg_fragment);
             */
        });

        return binding.getRoot();
    }
}
