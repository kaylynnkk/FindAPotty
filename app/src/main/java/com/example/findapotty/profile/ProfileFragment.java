package com.example.findapotty.profile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.findapotty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



public class ProfileFragment extends Fragment {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB,
    textViewGender, textViewMobile;
    private String fullName, email, dob, gender, mobile;
    private ImageView imageView;
    private Button fl_done_button;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewWelcome = rootView.findViewById(R.id.textView_show_welcome);
        textViewFullName = rootView.findViewById(R.id.textView_show_full_name);
        textViewEmail = rootView.findViewById(R.id.textView_show_email);
        textViewDoB = rootView.findViewById(R.id.textView_show_dob);
        textViewGender = rootView.findViewById(R.id.textView_show_gender);
        textViewMobile = rootView.findViewById(R.id.textView_show_mobile);
        fl_done_button = rootView.findViewById(R.id.fl_done_button);
        fl_done_button.setOnClickListener((v)->{addUserProfile();});

        imageView = rootView.findViewById(R.id.imageView_profile_dp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent stuff goes here for switching to upload photo activity
                NavController controller = Navigation.findNavController(rootView);
                controller.navigate(R.id.action_nav_profile_to_uploadProfilePic);
                Log.d(TAG, "onComplete: 1111111111111111111");
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser != null)
        {
            showUserProfile();
        }
        else
        {
            //figure out how to make a toast
            Toast.makeText(rootView.getContext(), "Something went wrong! User's details" + "are not" +
                    "available at the moment.", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


    //if the user doesn't already exist in the database
    private void showUserProfile()
    {
        firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null)
                {
                    fullName = readUserDetails.fullName;
                    email = readUserDetails.email;
                    dob = readUserDetails.dob;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    textViewWelcome.setText("Welcome " + fullName + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(dob);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(imageView);
                }
                else
                {
                    addUserProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(rootView.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

            }
        });

    }
    private String TAG ="ProfileFragment";

    private void addUserProfile()
    {
        fullName = textViewFullName.getText().toString();
        email = textViewEmail.getText().toString();
        dob = textViewDoB.getText().toString();
        gender = textViewGender.getText().toString();
        mobile = textViewMobile.getText().toString();

        firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();

        Uri uri = firebaseUser.getPhotoUrl();
        Picasso.get().load(uri).into(imageView);

        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(fullName, email, dob, gender, mobile);
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(rootView.getContext(), "User Profile Information was Successfully added", Toast.LENGTH_LONG).show();
                    showUserProfile();
                    //put intent statements here to switch to new activity
                }
                else
                {
                    Toast.makeText(rootView.getContext(), "Failed to add User Profile information", Toast.LENGTH_LONG).show();
                }

            }
        });
       // register referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
    }
}