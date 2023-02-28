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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.findapotty.R;
import com.example.findapotty.User;
import com.example.findapotty.databinding.FragmentAddDiscussionPostBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDiscussionPostFragment extends Fragment {

    private FragmentAddDiscussionPostBinding binding;
    private static final String TAG = "AddDiscussionPostFragment";
    private User user = User.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_discussion_post, container, false);

        titleCounter();
        contentCounter();
        binding.fadpSubmit.setOnClickListener(view -> {
            submit(view);
        });




        return binding.getRoot();
    }

    private void titleCounter() {
        binding.fadpTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            // meaning: chars of length <count> replaces chars of length <before> at <start>
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                Log.d(TAG, "onTextChanged: " + start + " " + before + " " + count);
//                Log.d(TAG, "onTextChanged: " + charSequence.length());
                binding.faspTitleLengthCount.setText(charSequence.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
    private void contentCounter() {

    }

    private void submit(View view) {
        Log.d(TAG, "submit: "+ user.getAvatarUrl().toString());
        DiscussionPost newPost = new DiscussionPost(user.getAvatarUrl().toString(), user.getUserName(),
                binding.fadpTitle.getText().toString(), binding.fadpContent.getText().toString());

        NavController controller = Navigation.findNavController(view);
        NavDirections action =
                AddDiscussionPostFragmentDirections.actionAddDiscussionPostFragmentToSinglePostDiscussionBoard(newPost);
        controller.navigate(action);

        // send post to database
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("discussion_posts");
        String postId = postRef.push().getKey();
        postRef.child(postId).setValue(newPost);
    }
}
