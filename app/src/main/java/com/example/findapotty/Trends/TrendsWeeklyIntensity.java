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

import java.util.ArrayList;


public class TrendsWeeklyIntensity extends Fragment {

    private BarChart barChart;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String month;
    private String painRating;
    private String pottyType;
    private int monthlyPooCounter = 0;
    private Button fl_previous_button;
    private Button fl_next_button;
    private View rootView;


    //The bar graph is created for tracking pain intensity per day of the
    //week.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trends_weekly_intensity, container, false);


        fl_previous_button = rootView.findViewById(R.id.backBtn);
        fl_next_button = rootView.findViewById(R.id.nextBtn);
        barChart = (BarChart) rootView.findViewById(R.id.barchart);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        //getDiaryEntries();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //average pain intensity of bowel movements for each day of the week.
        barEntries.add(new BarEntry(1, 2f));
        barEntries.add(new BarEntry(2, 2f));
        barEntries.add(new BarEntry(3, 5f));
        barEntries.add(new BarEntry(4, 6f));
        barEntries.add(new BarEntry(5, 7f));
        barEntries.add(new BarEntry(6, 1f));
        barEntries.add(new BarEntry(7, 3f));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Bowel Movement Pain Intensity");
        barDataSet.setColors(new int[] {Color.parseColor("#5B3E31")});


        BarData data = new BarData(barDataSet);

        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;

        barChart.setData(data);

        data.setBarWidth(barWidth);


        String[] months = new String[]{"","Sun","Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "","",""};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(1);

        barChart.getXAxis().setAxisMinimum(data.getXMin() - 0.25f);

        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisRight().setAxisMinValue(0);

        //controls previous and next buttons
//        fl_previous_button.setOnClickListener((v) -> {
//            Intent intent = new Intent(TrendsWeeklyIntensity.this, Trends.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        });

        fl_next_button.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(rootView);
            navController.navigate(R.id.action_trendsWeeklyIntensity_to_trendsYearlyIntensity);
        });
        return rootView;
    }

    //formats the x axis
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