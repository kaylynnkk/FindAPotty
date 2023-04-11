package com.example.findapotty.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findapotty.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//this class is to add the take the reminders from the user and inserts into database
public class ReminderActivity extends AppCompatActivity {

    Button dateBT, timeBT, submitBT;
    EditText labelET;
    String alertTime;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_activity);
        
        // added references to all data in view
        labelET = (EditText) findViewById(R.id.label);
        dateBT = (Button) findViewById(R.id.date);                                             
        timeBT = (Button) findViewById(R.id.time);
        submitBT = (Button) findViewById(R.id.submit);
        
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
                    Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT);}
                // once all fields have an inputted add data to firebase
                else {
                    DatabaseReference ref= FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("reminders").push();
                    ref.child("label").setValue(label);
                    ref.child("date").setValue(date);
                    ref.child("time").setValue(time);
                    // set alarm
                    setAlarm(label, date, time);
                    resetData();
                }


            }
        });
    }
    private void resetData(){
        labelET.setText("");
        dateBT.setText("Date");
        dateBT.setText("Time");
    }

    private void setDate() {
        // initialize variable for day, month, and year
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        // intializing the date picker and passing context
        DatePickerDialog dpd = new DatePickerDialog(ReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        // initialize variable for hour and minute
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // intializing the date picker and passing context
        TimePickerDialog tmd = new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + alertTime;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);                //this intent will be called once the setting alaram is completes
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity ot mainactivity

    }

}