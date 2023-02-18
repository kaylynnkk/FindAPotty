package com.example.findapotty.search.restroompage;

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

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentAddRestroomReviewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AddRestroomReviewFragment extends Fragment {

    private static final String TAG = "AddRestroomReviewFragment";
    private FragmentAddRestroomReviewBinding binding;

    private DatabaseReference dbr;
//    private Button backBT, submitBT, imgBT;
//    private ImageView imgIV;
//    private RatingBar ratingRB;
//    private EditText reviewET;
//    private String userId, userName, imgURL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_restroom_review, container, false);
        dbr = FirebaseDatabase.getInstance().getReference("Potty/Ratings");

        binding.farrSubmit.setOnClickListener(view -> {
            onSubmit(view);
        });
        binding.farrBack.setOnClickListener(view -> {
//            onBackPressed();
        });

        return binding.getRoot();
    }

    private void onSubmit(View view) {
//        String userId = dbr.push().getKey();
//        String userName = "joeKing";
//
//        Review rev = new Review(ratingRB.getRating(),
//                reviewET.getText().toString().trim(),
//                String.valueOf(System.currentTimeMillis()), 0,imgURL,userName);

        // go to userId branch of databse and insert review object
//        dbr.child(userId).setValue(rev);

        // Pop up to alert user that review has been submitted
        Toast.makeText(view.getContext(), "Review Submitted Successfully", Toast.LENGTH_SHORT).show();

        // leave write review activity and start display review activity
//        Intent intent = new Intent(WriteReviewActivity.this, RestroomPageBottomSheet.class);
//        startActivity(intent);

    }

//    private void onBackPressed() {
//    }

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
