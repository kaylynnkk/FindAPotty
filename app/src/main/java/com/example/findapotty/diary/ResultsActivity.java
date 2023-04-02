package com.example.findapotty.diary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;


public class ResultsActivity extends AppCompatActivity implements Serializable {

    TextView waterIntakeTV, durationTV, painRatingTV, pottyTypeTV, stoolTypeTV, stoolColorTV,
            urineColorTV, notesTV,prediction1TV,prediction2TV, prediction3TV;
    Button submitBT;

    View answersView;
    ImageButton dropdownBT;
    Boolean visibilityFlag = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_results_activity);
        // added references to all data in view
        waterIntakeTV = (TextView) findViewById(R.id.intake);
        durationTV = (TextView) findViewById(R.id.time_spent);
        painRatingTV= (TextView) findViewById(R.id.pain_level);
        pottyTypeTV = (TextView) findViewById(R.id.potty_type);
        stoolTypeTV = (TextView) findViewById(R.id.stool_type);
        stoolColorTV = (TextView) findViewById(R.id.stool_color);
        urineColorTV = (TextView) findViewById(R.id.urine_color);
        notesTV = (TextView) findViewById(R.id.notes);
        submitBT = (Button) findViewById(R.id.submit);
        dropdownBT = (ImageButton) findViewById(R.id.dropdown_button);
        answersView = (View) findViewById(R.id.displayed_answers);
        prediction1TV = (TextView) findViewById(R.id.predict_1);
        prediction2TV = (TextView) findViewById(R.id.predict_2);
        prediction3TV = (TextView) findViewById(R.id.predict_3);

        // retrieve object from previos activity
        DiaryEntry d = (DiaryEntry) getIntent().getExtras().getSerializable("diary_data");

        // call methods to populate information in the view
        waterIntakeClassifer(d);
        populateAnswers(d);
        predictConditions(d);

        // answers from preovios activity is hidden until expand more buttom is selected
        // and can be rehidden if expand less btton is selected
        dropdownBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibilityFlag){
                    answersView.setVisibility(View.INVISIBLE);
                    dropdownBT.setImageResource(R.drawable.baseline_expand_more_24);
                    visibilityFlag = false;
                } else {
                    answersView.setVisibility(View.VISIBLE);
                    dropdownBT.setImageResource(R.drawable.baseline_expand_less_24);
                    visibilityFlag =true;
                }
            }
        });
        // user can create start another diary entry by pressing submit button
        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultsActivity.this, DiaryEntryActivity.class);
                startActivity(i);

            }
        });

    }
    // retrieve data from object and set text to corresponding text view
    public void populateAnswers(DiaryEntry d){
        durationTV.setText(""+d.getDuration());
        painRatingTV.setText(""+d.getPainRating());
        pottyTypeTV.setText(""+d.getPottyType());
        stoolTypeTV.setText(""+d.getStoolType());
        stoolColorTV.setText(""+d.getStoolColor());
        urineColorTV.setText(""+d.getUrineColor());
        notesTV.setText(""+d.getAdditionalNotes());
    }
    // switch statement to determine water intake classification based on peecolor
    public void waterIntakeClassifer(DiaryEntry d) {

        String peeColor = d.getUrineColor();
        String waterIntake;
        switch (peeColor) {
            case "Clear":
                waterIntake = "EXCESSIVE";
                waterIntakeTV.setTextColor(Color.parseColor("#C70039"));
                break;
            case "Dark Yellow":
            case "Orange":
            case "Dark Orange or Brown":
            case "Blue or Green":
                waterIntake = "INSUFFICIENT";
                waterIntakeTV.setTextColor(Color.parseColor("#C70039"));
                break;
            default:
                waterIntake = "HEALTHY";
                waterIntakeTV.setTextColor(Color.parseColor("#008000"));
                break;
        }
        waterIntakeTV.setText(""+waterIntake);
    }
    // switch statment that dtermines conditions based on stooltype, stoolcolor, and rincolor
    public void predictConditions(DiaryEntry entry) {
        Integer stoolType = entry.getStoolType();
        String urineColor = entry.getUrineColor();
        String stoolColor = entry.getStoolColor();
        String st, uc, sc;
        switch (stoolType) {
            case 1:
            case 2:
                st = "Constipated";
                break;
            case 5:
                st = "Insufficient Fiber";
                break;
            case 6:
            case 7:
                st = "Diarrhea";
                break;
            default:
                st = "Normal";
                break;
        }
        switch (urineColor) {
            case "Cloudy":
                uc = "Urinary Tract Infection";
                break;
            case "White or Milky":
                uc = "Chyluria";
                break;
            case "Dark Brown or Black":
                uc = "Liver Disease";
            default:
                uc = "Normal";
                break;
        }
        switch (stoolColor) {
            case "Pale":
                sc = "Lack of Bile";
                break;
            case "Green":
                sc = "Bacterial Infection";
                break;
            case "Yellow":
                sc = "Small Intestine Infection";
            case "Bright Red":
            case "Dark Red":
            case "Black-Red":
                sc = "Bleeding in Digestive Tract";
            default:
                sc = "Normal";
                break;
        }
        if(st != "Normal"){
            prediction1TV.setVisibility(View.VISIBLE);
            prediction1TV.setText(""+st);
        }
        if(uc != "Normal"){
            prediction2TV.setVisibility(View.VISIBLE);
            prediction2TV.setText(""+uc);
        }
        if(sc != "Normal"){
            prediction3TV.setVisibility(View.VISIBLE);
            prediction3TV.setText(""+sc);
        }
        if(sc == "Normal" && uc == "Normal" && st == "Normal"){
            prediction1TV.setVisibility(View.VISIBLE);
            prediction1TV.setText("No Concerns Predicted");

        }
    }
}