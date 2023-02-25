package com.example.findapotty.discuss;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentAddDiscussionPostBinding;

public class AddDiscussionPostFragment extends Fragment {

    private FragmentAddDiscussionPostBinding binding;
    private static final String TAG = "AddDiscussionPostFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_discussion_post, container, false);

        binding.fadpTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // meaning: chars of length <count> replaces chars of length <before> at <start>
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + start + " " + before + " " + count);
                Log.d(TAG, "onTextChanged: " + charSequence.length());
                binding.faspTitleLengthCount.setText(charSequence.length() + "/30");


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return binding.getRoot();
    }
}
