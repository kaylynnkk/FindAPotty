package com.example.findapotty.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.findapotty.MainActivity;
import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentDiaryBinding;
import com.example.findapotty.tunes.TunesPlayerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // added references to all data in view
        durationET = binding.timeLength;
        painRatingSB = binding.seekBar;
        pottyTypeRG = binding.pottyOptions;
        stoolTypeSP = binding.stoolType;
        stoolColorSP = binding.colorPoop;
        urineColorSP = binding.colorPee;
        notesET = binding.notes;
        submitBT = binding.submit;
        stoolTypeIV = binding.stoolTypeChart;
        stoolColorIV = binding.stoolColorChart;
        urineColorIV = binding.urineColorChart;
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
        ArrayAdapter ad1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, stoolColorsList);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);stoolColorSP.setAdapter(ad1);
        stoolColorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                stoolColor = stoolColorsList[position];}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter ad2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,
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

        ArrayAdapter ad3 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,
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
        urineColorIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view, "diary_urine_color_popup_window");
            }
        });
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
                // prompt user to enter input for date and time
                if (duration.equals(0)) {
                    Toast.makeText(getContext(), "Potty time can't be 0 minutes!", Toast.LENGTH_SHORT).show();
                } else if (pottyTypeRG.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Must pick potty type!", Toast.LENGTH_SHORT).show();
                } else if ((pottyType.equals("Pee") || pottyType.equals("Both")) && urineColor.equals("")) {
                    Toast.makeText(getContext(), "Please enter urine color!", Toast.LENGTH_SHORT).show();
                } else if ((pottyType.equals("Poop") || pottyType.equals("Both")) && (stoolType.equals("0") || stoolColor.equals(""))){
                    Toast.makeText(getContext(), "Please answer stool questions!", Toast.LENGTH_SHORT).show();
                } else if((pottyType.equals("Pee")  && !(stoolType.equals("0") || stoolColor.equals("")))
                        || (pottyType.equals("Poop") & !urineColor.isEmpty())){
                    Toast.makeText(getContext(), "Incompatible potty type!", Toast.LENGTH_SHORT).show();
                    // if there is no problem with the user input to the  answers than data is saved
                    // in database
                } else {
                    // creates reference to firebase
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                            .getReference().child("users")
                            .child(uid).child("diary entries");
                    // create object with user inputs
                    DiaryEntry entry = new DiaryEntry(submissionDate, monthOfSubmission(),
                            pottyType, stoolColor, urineColor,notes,dayOfWeek, duration,pain, Integer.parseInt(stoolType));
                    // use userid as key
                    ref.child(ref.push().getKey()).setValue(entry);
                    // alert user that entry has been saves and go to results
                    Toast.makeText(getContext(), "Diary Entry Submitted",
                            Toast.LENGTH_LONG).show();
                    // move from diary entry page to diary results
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ResultsFragment fragment = new ResultsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("diary_data", entry);
                    fragment.setArguments(bundle);
                    ft.replace(R.id.diary1, fragment)
                            .addToBackStack(null)
                            .commit();
                }

            }

        });
        return root;
    }
    // convert month of submission from integer to abbreviations
    // for eaiser retreival in potty trends
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
        // dismiss the popup window when touched inside the popupp
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}