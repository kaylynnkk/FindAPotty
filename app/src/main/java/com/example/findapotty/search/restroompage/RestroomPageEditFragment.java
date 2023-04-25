package com.example.findapotty.search.restroompage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentEditRestroomPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/* This feature does not work properly and still needs work.
* */
public class RestroomPageEditFragment extends Fragment {

    private EditText editTextRestroomName, editTextRestroomDescription;

    private Button buttonSubmit;

    private String restroomName, restroomDescription;

    private FirebaseAuth authRestroomPage;

    private FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final RestroomPageViewModel viewModel = new ViewModelProvider(this).get(RestroomPageViewModel.class);
        final FragmentEditRestroomPageBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_edit_restroom_page, container, false);
//        binding.setData(viewModel);
        binding.setLifecycleOwner(this);

//         Initialize views
        editTextRestroomName = binding.rrPgERestroomName;
        editTextRestroomDescription = binding.rrPgERestroomDescription;
        buttonSubmit = binding.rrPgESubmitButton;

        // Get current values in the UI
        editTextRestroomName.setText(restroomName);
        editTextRestroomDescription.setText(restroomDescription);

        // Set up button onclicklistener
        binding.rrPgESubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get new values from UI
                String newRestroomName = editTextRestroomName.getText().toString();
                String newRestroomDescription = editTextRestroomDescription.getText().toString();

                // Create new intent with new values
                Intent resultIntent = new Intent();
                resultIntent.putExtra("restroom_name",newRestroomName);
                resultIntent.putExtra("restroom_description",newRestroomDescription);

                // Set the result and finish
                getActivity().setResult(Activity.RESULT_OK,resultIntent);
                getActivity().finish();

                // Show confirmation message
                Toast.makeText(getActivity(), "Restroom Page Updated", Toast.LENGTH_SHORT).show();
            }
        });

//        return inflater.inflate(R.layout.fragment_edit_restroom_page, container, false);
        return binding.getRoot();
    }
}
