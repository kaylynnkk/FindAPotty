package com.example.findapotty.diary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentDiaryresultsBinding;
public class ResultsFragment extends Fragment {
    private FragmentDiaryresultsBinding binding;
    TextView waterIntakeTV, durationTV, painRatingTV, pottyTypeTV, stoolTypeTV, stoolColorTV,
            urineColorTV, notesTV,prediction1TV,prediction2TV, prediction3TV;
    Button submitBT;

    View answersView;
    ImageButton dropdownBT;
    Boolean visibilityFlag = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryresultsBinding.inflate(inflater, container, false);

        // added references to all data in view
        waterIntakeTV = binding.intake;
        durationTV = binding.timeSpent;
        painRatingTV= binding.painLevel;
        pottyTypeTV = binding.pottyType;
        stoolTypeTV = binding.stoolType;
        stoolColorTV = binding.stoolColor;
        urineColorTV = binding.urineColor;
        notesTV = binding.notes;
        submitBT = binding.submit;
        dropdownBT = binding.dropdownButton;
        answersView = binding.displayedAnswers;
        prediction1TV = binding.predict1;
        prediction2TV = binding.predict2;
        prediction3TV = binding.predict3;



        // retrieve object from previous activity
        Bundle bundle = getArguments();
        DiaryEntry d = (DiaryEntry) bundle.getSerializable("diary_data");
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
                nextActivity();

            }
        });
        return binding.getRoot();
    }

    public void nextActivity(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.diary2, new DiaryFragment())
                .addToBackStack(null)
                .commit();
    }
    // retrieve data from object and set text to corresponding text view
    public void populateAnswers(DiaryEntry d){
        durationTV.setText(""+d.getDuration());
        if(d.getPainRating() == null){
            painRatingTV.setText("0");
        }
        else{
            painRatingTV.setText(""+d.getPainRating());
        }
        pottyTypeTV.setText(""+d.getPottyType());
        stoolTypeTV.setText(""+d.getStoolType());
        stoolColorTV.setText(""+d.getStoolColor());
        urineColorTV.setText(""+d.getUrineColor());
        if(d.getAdditionalNotes() == ""){
            notesTV.setText("N/A");
        }
        else{
            notesTV.setText(""+d.getAdditionalNotes());
        }
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
            prediction1TV.setText("No Concerns");

        }
    }
}
