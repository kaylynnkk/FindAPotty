package com.example.findapotty.tunes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.findapotty.databinding.FragmentDiaryBinding;
import com.example.findapotty.databinding.FragmentTunesBinding;

public class TunesFragment extends Fragment {

    private FragmentTunesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TunesViewModel tunesViewModel =
                new ViewModelProvider(this).get(TunesViewModel.class);

        binding = FragmentTunesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTunes;
        tunesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}