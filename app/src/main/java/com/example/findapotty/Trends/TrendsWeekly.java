package com.example.findapotty.Trends;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findapotty.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class TrendsWeekly extends Fragment {

    private BarChart barChart;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String month;
    private String painRating;
    private String pottyType;
    private int monthlyPooCounter = 0;
    private Button fl_next_button;
    private View rootView;


    //setting up values from activity layout and creating bar graph
    //that tracks weekly bladder and bowel count
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trends_weekly, container, false);

        fl_next_button = rootView.findViewById(R.id.nextBtn);
        barChart = (BarChart) rootView.findViewById(R.id.barchart);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        //getDiaryEntries();
        //This data represents daily bowel movements
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //try w/o the f at the end
        barEntries.add(new BarEntry(1, 1f));
        barEntries.add(new BarEntry(2, 1f));
        barEntries.add(new BarEntry(3, 0f));
        barEntries.add(new BarEntry(4, 2f));
        barEntries.add(new BarEntry(5, 1f));
        barEntries.add(new BarEntry(6, 1f));
        barEntries.add(new BarEntry(7, 1f));

        //This data represents daily bladder movements
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();

        barEntries1.add(new BarEntry(1, 3f));
        barEntries1.add(new BarEntry(2, 5f));
        barEntries1.add(new BarEntry(3, 4f));
        barEntries1.add(new BarEntry(4, 4f));
        barEntries1.add(new BarEntry(5, 3f));
        barEntries1.add(new BarEntry(6, 2f));
        barEntries1.add(new BarEntry(7, 5f));
        barEntries1.add(new BarEntry(8,1f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of Bowel Movements");
        barDataSet.setColors(new int[] {Color.parseColor("#5B3E31")});

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Number of times I peed");
        barDataSet1.setColors(new int[] {Color.parseColor("#FFFF00")});



        BarData data = new BarData(barDataSet, barDataSet1);

        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;

        barChart.setData(data);

        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);


        //these are the labels for the x axis
        String[] months = new String[]{"","Sun","Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "","","",""};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1);

        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisRight().setAxisMinValue(0);


        fl_next_button.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(rootView);
            navController.navigate(R.id.action_nav_trends_to_Trends);
        });
        return rootView;
    }

    //this just formats the values on the x axis
    public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

        private String[] mValues;
        public MyXAxisValueFormatter(String[] values)
        {
            this.mValues = values;
        }

        public String getFormattedValue(float value)
        {
            return mValues[(int)value];
        }

    }


}