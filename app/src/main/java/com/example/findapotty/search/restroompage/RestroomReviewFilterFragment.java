package com.example.findapotty.search.restroompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.findapotty.R;
// ...

public class RestroomReviewFilterFragment extends DialogFragment {

    SmartMaterialSpinner<String> ratingOptions;
    SmartMaterialSpinner<String> sorterOptions;

    public RestroomReviewFilterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static RestroomReviewFilterFragment newInstance(String title) {
        RestroomReviewFilterFragment frag = new RestroomReviewFilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviewfilter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        ratingOptions = view.findViewById(R.id.rating_filter_options);
        sorterOptions = view.findViewById(R.id.sorter_options);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        ratingOptions.requestFocus();
        sorterOptions.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}