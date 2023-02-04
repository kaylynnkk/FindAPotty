package com.example.findapotty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.findapotty.databinding.FragmentEditRestroomPageBinding;

public class RestroomPageEditFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final RestroomPageViewModel viewModel = new ViewModelProvider(this).get(RestroomPageViewModel.class);
        final FragmentEditRestroomPageBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_edit_restroom_page, container, false);
//        binding.setData(viewModel);
        binding.setLifecycleOwner(this);
        binding.rrPgESubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Restroom Page Updated", Toast.LENGTH_SHORT).show();
            }
        });

//        return inflater.inflate(R.layout.fragment_edit_restroom_page, container, false);
        return binding.getRoot();
    }
}
