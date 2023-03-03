package com.example.findapotty;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotLogin extends AppCompatActivity {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB,
    textViewGender, textViewMobile;
    private String fullName, email, dob, gender, mobile;
    private ImageView imageView;
    private Button fl_done_button;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_login);

        getSupportActionBar().setTitle("Home");

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        fl_done_button = findViewById(R.id.fl_done_button);
        fl_done_button.setOnClickListener((v)->{addUserProfile();});



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

    }

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
                    Toast.makeText(NotLogin.this, "User Profile Information was Successfully added", Toast.LENGTH_LONG).show();
                    //put intent statements here to switch to new activity
                }
                else
                {
                    Toast.makeText(NotLogin.this, "Failed to add User Profile information", Toast.LENGTH_LONG).show();
                }

            }
        });
       // register referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
    }
}