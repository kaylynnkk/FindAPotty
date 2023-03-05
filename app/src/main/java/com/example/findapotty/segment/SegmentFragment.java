package com.example.findapotty.segment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.findapotty.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SegmentFragment} factory method to
 * create an instance of this fragment.
 */
public class SegmentFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         //Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_segment, container, false);

        return rootView;
    }
}