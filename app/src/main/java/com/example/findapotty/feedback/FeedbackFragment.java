package com.example.findapotty.feedback;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findapotty.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Feedback fragment class allows users to submit a feedback */
public class FeedbackFragment extends Fragment {

    EditText namedata, emaildata, messagedata;
    Button submit_bt;
    DatabaseReference firebase;
    private View rootView;


    public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the feedback layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

        // connecting buttons to their designated ids
        namedata = rootView.findViewById(R.id.namedata);
        emaildata = rootView.findViewById(R.id.emaildata);
        messagedata = rootView.findViewById(R.id.messagedata);
        submit_bt = rootView.findViewById(R.id.submit_bt);

        // get activity associated with fragment
        Context context = getActivity().getApplicationContext();

        // obtain a unique id for every user to submit a feedback
        String UniqueID = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);

        // connecting to firebase
        // creates child reference to users and unique id
        // store data for each user separately under own unique id
        firebase = FirebaseDatabase.getInstance().getReference().child("Users").child(UniqueID);

        // retrieve text user input into name, email, and message
        submit_bt.setOnClickListener(v -> {
            final String name = namedata.getText().toString();
            final String email = emaildata.getText().toString();
            final String message = messagedata.getText().toString();

            // write name to name location in database
            DatabaseReference child_name = firebase.child("Name");
            child_name.setValue(name);
            if (name.isEmpty()){
                namedata.setError("This field is required.");
                submit_bt.setEnabled(false);
            } else {
                namedata.setError(null);
                submit_bt.setEnabled(true);
            }

            // write email to email location in database
            DatabaseReference child_email = firebase.child("Email");
            child_email.setValue(email);
            if (email.isEmpty()) {
                emaildata.setError("This field is required.");
                submit_bt.setEnabled(false);
            } else {
                emaildata.setError(null);
                submit_bt.setEnabled(true);
            }

            // write message to message location in database
            DatabaseReference child_message = firebase.child("Message");
            child_message.setValue(message);
            if (message.isEmpty()){
                messagedata.setError("This field is required.");
                submit_bt.setEnabled(false);
            } else {
                messagedata.setError(null);
                submit_bt.setEnabled(true);
            }
            // message to be displayed when user presses "submit"
            Toast.makeText(rootView.getContext(), "Successfully submitted!", Toast.LENGTH_SHORT).show();
        });
        return rootView;
    }
}
