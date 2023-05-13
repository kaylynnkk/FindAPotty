package com.example.findapotty.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.example.findapotty.databinding.FragmentReminderBinding;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    FloatingActionButton addReminderBT;
    private RecyclerView mRecyclerview;
    ReminderRecyclerViewAdaptor reminderRecyclerViewAdaptor;
    Query dbr;
    ArrayList<ReminderMessage> remindersList = new ArrayList<ReminderMessage>();

    Button dateBT, timeBT, submitBT;
    RelativeLayout dimmer;
    ImageView cancelBT;
    EditText labelET;
    String alertTime;
    Calendar c = Calendar.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReminderBinding.inflate(inflater, container, false);
       mRecyclerview = binding.recyclerView;
        addReminderBT = binding.createReminder;
        // when button is press show popup window
        addReminderBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "reminder_builder_popup");

            }
        });
        // show list of reminders if users have them
        populateReminders();

        return binding.getRoot();


    }
    private void populateReminders() {
        // ge qurey to database
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("reminders");
        // listen for data at query
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // empty arraylist
                if(remindersList.size() > 0){
                    remindersList.clear();
                }
                // add objects to the arraylist
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReminderMessage rem = postSnapshot.getValue(ReminderMessage.class);
                    remindersList.add(rem);
                }
                // populate recyclerview from database using arraylist of remindermessage objects
                if (dbr != null) {
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    reminderRecyclerViewAdaptor = new ReminderRecyclerViewAdaptor(getContext(), remindersList);
                    mRecyclerview.setAdapter(reminderRecyclerViewAdaptor);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onButtonShowPopupWindowClick(View view, String layoutName) {
        // get target layout that will be the popup
        int popupLayout = getResources().getIdentifier(layoutName,
                "layout", getActivity().getPackageName());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(popupLayout, null);
        // size the popup
        int w = LinearLayout.LayoutParams.WRAP_CONTENT;
        int h= LinearLayout.LayoutParams.WRAP_CONTENT;
        // create popup object
        final PopupWindow popupWindow = new PopupWindow(popupView, w, h, true);
        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // get view in the popuplayout
        labelET = popupView.findViewById(R.id.label);
        dateBT = popupView.findViewById(R.id.date);
        timeBT = popupView.findViewById(R.id.time);
        submitBT = popupView.findViewById(R.id.submit);
        cancelBT = popupView.findViewById(R.id.cancel);
        // if cancel button is clicked popup is dismissed
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        // when the time button is clicked the setTime method is called
        timeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
        // when the time button is clicked the setDate method is called
        dateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        // when user clicks submit button
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieves user input from buttons and edittext
                String label = labelET.getText().toString().trim();
                String date = dateBT.getText().toString().trim();
                String time = timeBT.getText().toString().trim();
                // if date or time string has not changed from none then user hasnt entered an input
                // if label string is empty user hasnt entered na input
                // prompt user to enter tinput for date and time
                if (time.equals("Time") || date.equals("Date") || label.isEmpty()) {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT);
                }
                // once all fields have an inputted add data to firebase
                else {
                    // get reference to database
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("reminders");
                    // create key from reminder object
                    String key = ref.push().getKey();
                    // create reminder object
                    ReminderMessage rem = new ReminderMessage(key, label, date, time);
                    // add to database
                    ref.child(key).setValue(rem);
                    // reset values
                    labelET.setText("");
                    dateBT.setText("Date");
                    timeBT.setText("Time");
                    // dismiss popup
                    if(popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }

                }

            }
        });
    }

    private void setDate() {
        // initialize variable for day, month, and year
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        // intializing the date picker and passing context
        DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            // calendar pops up and user can set values for day, month, and year
            public void onDateSet(DatePicker dp, int year, int month, int day) {
                dateBT.setText((month + 1) + "-" + day + "-" + year);
            }
            // values that are being passed
        }, year, month, day);
        // shows data picker
        dpd.show();
    }
    private void setTime() {
        // initialize variable for hour and minute
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // intializing the date picker and passing context
        TimePickerDialog tmd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            // clock pops up and user can set values for day, month, and year
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                alertTime = hour + ":" + minute;
                // format hour and minute so its the not in military time
                timeBT.setText(FormatTime(hour, minute));
            }
            // values that are being passed
        }, hour, minute, false);
        // shows time picker
        tmd.show();
    }
    public String FormatTime(int hour, int minute) {
        // create variable to save new formated mintue
        String newMin;
        // if minute is a single digit add zero to the from
        if (minute / 10 == 0) {
            newMin = "0" + minute;
            // if minute if is already two digits turn into string
        } else {
            newMin = "" + minute;
        }
        // convert hour from military time to am-pm time
        // return formated
        if (hour == 0) {
            return "12" + ":" + newMin + " AM";
        } else if (hour < 12) {
            return hour + ":" + newMin + " AM";
        } else if (hour == 12) {
            return "12" + ":" + newMin + " PM";
        } else {
            return (hour - 12) + ":" + newMin + " PM";
        }

    }
}