package com.example.findapotty.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentReminderBinding;
import com.example.findapotty.databinding.FragmentReminderbuilderBinding;
import com.example.findapotty.diary.ResultsFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    FloatingActionButton mCreateRem;
    private RecyclerView mRecyclerview;
    ReminderRecyclerViewAdaptor reminderRecyclerViewAdaptor;
    DatabaseReference dbr;
    ArrayList<ReminderMessage> remindersList = new ArrayList<>();

    Button dateBT, timeBT, submitBT;
    ImageView cancelBT;
    EditText labelET;
    String alertTime;
    Calendar c = Calendar.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReminderBinding.inflate(inflater, container, false);
       mRecyclerview = binding.recyclerView;
        mCreateRem = binding.createReminder;
        populateReminders();
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "fragment_reminderbuilder");
            }
        });

        return binding.getRoot();


    }
    private void populateReminders() {
        /*
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("reminders");

         */
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("reminders");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                remindersList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    remindersList.add(postSnapshot.getValue(ReminderMessage.class));

                }

                if (dbr != null) {
                    // use firebas eui to populate recycler straigther form databse
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        // pass string layoutname to inflater
        int layoutID = getResources().getIdentifier(layoutName,
                "layout", getActivity().getPackageName());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layoutID, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // lets taps outside the popup also dismiss it
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched

        labelET = popupView.findViewById(R.id.label);
        dateBT = popupView.findViewById(R.id.date);
        timeBT = popupView.findViewById(R.id.time);
        submitBT = popupView.findViewById(R.id.submit);
        cancelBT = popupView.findViewById(R.id.cancel);
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
                    /*
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("reminders");

                     */
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("reminders");
                    String key = ref.push().getKey();
                    ReminderMessage rem = new ReminderMessage(key, label, date, time);
                    ref.child(key).setValue(rem);

                    // set alarm
                    //setAlarm(label, date, time);
                    labelET.setText("");
                    dateBT.setText("Date");
                    timeBT.setText("Time");
                }

            }
        });
    }
    private void resetData(){
        labelET.setText("");
        dateBT.setText("Date");
        timeBT.setText("Time");
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
        Toast.makeText(getContext(),"In setTime()",Toast.LENGTH_SHORT);
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
        Toast.makeText(getContext(),"End of setTime()",Toast.LENGTH_SHORT);
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

    private void setAlarm(ReminderMessage rem) {
        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmBrodcast.class);
        intent.putExtra("event", rem.getLabel());
        intent.putExtra("time", rem.getTime());
        intent.putExtra("date", rem.getDate());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String dateandtime = rem.getDate() + " " + alertTime;
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getContext(), "Alarm", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}