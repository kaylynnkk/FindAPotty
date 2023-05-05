package com.example.findapotty.Trends;

import android.content.Intent;
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

import java.util.ArrayList;


public class Trends extends Fragment {

    private BarChart barChart;
    private Button next;
    private Button previous;
    private View rootView;

    //Here is where we create our bar graph for tracking the average number
    //of times a user pees or poops each day of that month
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trends, container, false);
        next = rootView.findViewById(R.id.nextBtn);
        previous = rootView.findViewById(R.id.backBtn);


        barChart = (BarChart) rootView.findViewById(R.id.barchart);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //This is the average of the daily poop counts for each month
        barEntries.add(new BarEntry(1, 1f));
        barEntries.add(new BarEntry(2, 2f));
        barEntries.add(new BarEntry(3, 0f));
        barEntries.add(new BarEntry(4, 3f));
        barEntries.add(new BarEntry(5, 0f));
        barEntries.add(new BarEntry(6, 1f));
        barEntries.add(new BarEntry(7, 0f));
        barEntries.add(new BarEntry(8, 1f));
        barEntries.add(new BarEntry(9, 1f));
        barEntries.add(new BarEntry(10, 2f));
        barEntries.add(new BarEntry(11, 2f));
        barEntries.add(new BarEntry(12, 3f));

        ArrayList<BarEntry> barEntries1 = new ArrayList<>();

        //This is the average of the daily pee count for each month
        barEntries1.add(new BarEntry(1, 3f));
        barEntries1.add(new BarEntry(2, 2f));
        barEntries1.add(new BarEntry(3, 5f));
        barEntries1.add(new BarEntry(4, 3f));
        barEntries1.add(new BarEntry(5, 3f));
        barEntries1.add(new BarEntry(6, 3f));
        barEntries1.add(new BarEntry(7, 3f));
        barEntries1.add(new BarEntry(8, 6f));
        barEntries1.add(new BarEntry(9, 3f));
        barEntries1.add(new BarEntry(10, 3f));
        barEntries1.add(new BarEntry(11, 2f));
        barEntries1.add(new BarEntry(12, 3f));
        barEntries1.add(new BarEntry(13,1f));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of Bowel Movements");
        barDataSet.setColors(new int[] {Color.parseColor("#5B3E31")});

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Number of times I peed");
        barDataSet1.setColors(new int[] {Color.parseColor("#FFFF00")});

        BarData data = new BarData(barDataSet, barDataSet1);

        float groupSpace = 0.1f;
        float barSpace = 0.02f;
        float barWidth = 0.43f;

        barChart.setData(data);

        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);



        //these are the axes labels
        String[] months = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
                "Nov", "Dec", "", "", ""};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(1);

        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisRight().setAxisMinValue(0);

        //for the next and previous buttons
//        previous.setOnClickListener((v) -> {
//            Intent intent = new Intent(Trends.this, TrendsWeekly.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        });
//
        next.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(rootView);
            navController.navigate(R.id.action_Trends_to_trendsWeeklyIntensity);
        });
        return rootView;
    }

    //for formatting the x axis
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