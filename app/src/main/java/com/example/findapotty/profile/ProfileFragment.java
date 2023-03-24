package com.example.findapotty.profile;

import android.os.Bundle;
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
import androidx.navigation.Navigation;

import com.example.findapotty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB,
    textViewGender, textViewMobile;
    private String fullName, email, dob, gender, mobile;
    private ImageView imageView;
    private Button fl_done_button;

    private ImageView icon_segment;
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
        icon_segment = rootView.findViewById(R.id.icon_segment);
        fl_done_button.setOnClickListener((v)->{addUserProfile();});

        //navigates from user profile to segment page when user clicks on segment icon
        icon_segment.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_segment));

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null)
        {
//            Toast.makeText(NotLogin.this, "Something went wrong! User's details" +
//                    "are not available at the moment.", Toast.LENGTH_LONG).show();

            addUserProfile();
        }
        else
        {
            showUserProfile();
        }

        return rootView;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView();
//
//        getSupportActionBar().setTitle("Home");
//
//
//
//    }

    //if the user doesn't already exist in the database
    private void showUserProfile()
    {

    }

    private void addUserProfile()
    {
        fullName = textViewFullName.getText().toString();
        email = textViewEmail.getText().toString();
        dob = textViewDoB.getText().toString();
        gender = textViewGender.getText().toString();
        mobile = textViewMobile.getText().toString();

        firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();

        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(fullName, email, dob, gender, mobile);
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(rootView.getContext(), "User Profile Information was Successfully added", Toast.LENGTH_LONG).show();
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