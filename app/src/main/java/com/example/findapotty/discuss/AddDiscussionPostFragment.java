package com.example.findapotty.discuss;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.findapotty.R;
import com.example.findapotty.user.User;
import com.example.findapotty.databinding.FragmentAddDiscussionPostBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDiscussionPostFragment extends Fragment {

    private FragmentAddDiscussionPostBinding binding;
    private static final String TAG = "AddDiscussionPostFragment";
    private final User user = User.getInstance();
    private MenuProvider menuProvider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_discussion_post, container, false);
        binding = FragmentAddDiscussionPostBinding.inflate(inflater, container, false);

        titleCounter();
        contentCounter();
        setToolbarMenu();


        return binding.getRoot();
    }

    private void setToolbarMenu() {
        requireActivity().addMenuProvider(
                menuProvider = new MenuProvider() {
                    @Override
                    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                        menuInflater.inflate(R.menu.add_post_menu, menu);
                    }

                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.add_post_menu_done){
                            submit();
                            return true;
                        }
                        return false;
                    }
                });
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

    private void submit() {
        View view = binding.getRoot();
        Log.d(TAG, "submit: "+ user.getAvatarUrl().toString());

        //yyyy-MM-dd HH:mm:ss
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());

        DiscussionPost newPost = new DiscussionPost(
                user.getAvatarUrl().toString(), user.getUserName(),
                binding.fadpTitle.getText().toString(), binding.fadpContent.getText().toString(),
                currentDateTime);

        NavController controller = Navigation.findNavController(view);
        NavDirections action =
                AddDiscussionPostFragmentDirections.actionAddDiscussionPostFragmentToSinglePostDiscussionBoard(newPost);
        controller.navigate(action);

        // send post to database
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("discussion_posts");
        String postId = postRef.push().getKey();
        postRef.child(postId).setValue(newPost);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().removeMenuProvider(menuProvider);
    }
}
