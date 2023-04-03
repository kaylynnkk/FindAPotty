package com.example.findapotty.diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.findapotty.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//this class is to add the take the reminders from the user and inserts into database
public class DiaryEntryActivity extends AppCompatActivity{
    RadioGroup pottyTypeRG;
    SeekBar painRatingSB;
    Button  submitBT;
    EditText durationET, notesET;
    SmartMaterialSpinner<String> stoolColorSP, urineColorSP, stoolTypeSP;
    String stoolType, stoolColor, urineColor, entryId, userId, pottyType;
    String[] stoolTypeList = { "1", "2", "3", "4", "5", "6", "7"};
    String[] stoolColorsList = { "Brown", "Pale", "Green", "Yellow", "Bright Red",
            "Dark Red", "Black-Red"};
    String[] urineColorsList = { "Clear", "Pale or Transparent","Dark Yellow", "Orange",
            "Dark Orange or Brown", "Dark Brown or Black", "Pink or Red", "Blue or Green",
            "Cloudy", "White or Milky"};
    ImageView stoolTypeIV, stoolColorIV, urineColorIV;
    Integer pain;
    Calendar c = Calendar.getInstance();
    Format simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_entry_activity);

        // added references to all data in view
        durationET = (EditText) findViewById(R.id.time_length);
        painRatingSB = (SeekBar) findViewById(R.id.seekBar);
        pottyTypeRG = (RadioGroup) findViewById(R.id.potty_options);
        stoolTypeSP = findViewById(R.id.stool_type);
        stoolColorSP = findViewById(R.id.color_poop);
        urineColorSP = findViewById(R.id.color_pee);
        notesET = (EditText) findViewById(R.id.notes);
        submitBT = (Button) findViewById(R.id.submit);
        stoolTypeIV = (ImageView) findViewById(R.id.stool_type_chart);
        stoolColorIV = (ImageView) findViewById(R.id.stool_color_chart);
        urineColorIV = (ImageView) findViewById(R.id.urine_color_chart);

        //  saves user input to variable when radio button is selected
        pottyTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected Radio Button
                RadioButton rb = group.findViewById(checkedId);
                pottyType = rb.getText().toString().trim();
            }
        });
        /*create dropdown men when finite options and save user input into variable when selected
        Dropdown options created for stoolcolor, stooltype, and urinecolor
         */
        ArrayAdapter ad1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                stoolColorsList);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stoolColorSP.setAdapter(ad1);
        stoolColorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                stoolColor = stoolColorsList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter ad2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                urineColorsList);
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urineColorSP.setAdapter(ad2);
        urineColorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                urineColor = urineColorsList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter ad3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                stoolTypeList);
        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stoolTypeSP.setAdapter(ad3);
        stoolTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                stoolType = stoolTypeList[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // when info icon is selected the method id called that makes the corresponding image to popup
        // Seperate clicklisterner to stooltype, stoolcolor, and urinecolor
        stoolTypeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "diary_stool_type_popup_window");
            }
        });
        stoolColorIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "diary_stool_color_popup_window");
            }
        });
        urineColorIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "diary_urine_color_popup_window");
            }
        });
        // when ser move seekbar to desired rating and the number is saved in vairable
        if (painRatingSB != null) {
            painRatingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    pain = (Integer) seekBar.getProgress();
                }
            });
        }

        // when user clicks submit button
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieves user input from buttons and edittext
                String submissionDate = simpleDateFormat.format(c.getTime());
                Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                Integer duration = Integer.valueOf(durationET.getText().toString().trim());
                String notes = notesET.getText().toString().trim();
                // if date or time string has not changed from none then user hasnt entered an input
                // if label string is empty user hasnt entered na input
                // prompt user to enter tinput for date and time
                if (duration.equals(0)) {
                    Toast.makeText(getApplicationContext(), "Potty time can't be 0 minutes!", Toast.LENGTH_SHORT).show();
                } else if (pottyTypeRG.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Must pick potty type!", Toast.LENGTH_SHORT).show();
                } else if ((pottyType.equals("Pee") || pottyType.equals("Both")) && urineColor.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter urine color!", Toast.LENGTH_SHORT).show();
                } else if ((pottyType.equals("Poop") || pottyType.equals("Both")) && (stoolType.equals("0") || stoolColor.equals(""))){
                    Toast.makeText(getApplicationContext(), "Please answer stool questions!", Toast.LENGTH_SHORT).show();
                } else if((pottyType.equals("Pee")  && !(stoolType.equals("0") || stoolColor.equals("")))
                || (pottyType.equals("Poop") & !urineColor.isEmpty())){
                    Toast.makeText(getApplicationContext(), "Incompatible potty type!", Toast.LENGTH_SHORT).show();
                } else {
                    // creates reference to firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference("diary_entries");

                    // entryId = FirebaseAuth.getInstance().getCurrentUser().getUid;
                    entryId = "mgtco5YF59Qrso120oH04onMH1z2";
                    // create object with user inputs
                    DiaryEntry entry = new DiaryEntry(submissionDate, monthOfSubmission(),
                            pottyType, stoolColor, urineColor,notes,dayOfWeek, duration,pain, Integer.parseInt(stoolType));
                    // use userid as key
                    ref.child(entryId).setValue(entry);
                    // alert user that entry has been saves and go to results
                    Toast.makeText(getApplicationContext(), "DIARY ENTRY SUBMITTED",
                            Toast.LENGTH_LONG).show();
                    nextActivity(entry);


                }

            }

        });

    }
    // convert month of submission from integer to abbreviations
    public String monthOfSubmission(){
        Integer month = c.get(Calendar.MONTH);
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Error";

        }
    }
    public void nextActivity(DiaryEntry entry){
        try{
            Intent i = new Intent(this, ResultsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("diary_data", entry);
            i.putExtras(bundle);
            startActivity(i);
        }
        catch (Exception e) {
            Log.e("nextActivity", e.getMessage());
        }
    }

    public void onButtonShowPopupWindowClick(View view, String layoutName) {
        // pass string layoutname to inflater
        int layoutID =getResources().getIdentifier(layoutName,
                "layout", getPackageName());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
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
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}