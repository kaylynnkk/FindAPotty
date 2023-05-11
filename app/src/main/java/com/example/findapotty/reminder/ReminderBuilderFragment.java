package com.example.findapotty.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findapotty.MainActivity;
import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentReminderbuilderBinding;
import com.example.findapotty.tunes.TunesPlayerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderBuilderFragment extends Fragment {
    FragmentReminderbuilderBinding binding;
    Button dateBT, timeBT, submitBT;
    EditText labelET;
    String alertTime;
    Calendar c = Calendar.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReminderbuilderBinding.inflate(inflater, container, false);
        labelET = binding.label;
        dateBT = binding.date;
        timeBT = binding.time;
        submitBT = binding.submit;
        // when the time button is clicked the setTime method is called
        timeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"Time button clicked",Toast.LENGTH_SHORT);
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
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT);}
                // once all fields have an inputted add data to firebase
                else {
                    DatabaseReference ref= FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("reminders");
                    String key = ref.push().getKey();
                    ReminderMessage rem = new ReminderMessage(key, label, date, time);
                    ref.child(key).setValue(rem);

                    // set alarm
                   // setAlarm(label, date, time);
                    resetData();/*
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.reminder2, new ReminderFragment())
                            .addToBackStack(null)
                            .commit();
                            */
                }

            }
        });

        return binding.getRoot();
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
                dateBT.setText((month + 1) + "-" + day + "-" + year);                             //sets the selected date as test for button
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

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(getContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String dateandtime = date + " " + alertTime;
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
