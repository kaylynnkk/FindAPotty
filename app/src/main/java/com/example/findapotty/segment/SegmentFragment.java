package com.example.findapotty.segment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.findapotty.R;

public class SegmentFragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //feed page to be displayed after user pressing the "Feed" button on the homepage
        View rootView = inflater.inflate(R.layout.fragment_segment, container, false);

        return rootView;
}
