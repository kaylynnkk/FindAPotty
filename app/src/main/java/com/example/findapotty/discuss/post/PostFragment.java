package com.example.findapotty.discuss.post;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findapotty.R;
import com.example.findapotty.databinding.DiscussionBoardSinglePostBinding;
import com.example.findapotty.discuss.DiscussionPost;
import com.example.findapotty.discuss.post.comment.Comment;
import com.example.findapotty.discuss.post.comment.CommentFragment;
import com.example.findapotty.discuss.post.main.MainFragment;
import com.example.findapotty.user.User;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostFragment extends Fragment {

    private DiscussionBoardSinglePostBinding binding;
    private boolean onUpload = false;
    private DiscussionPost post;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DiscussionBoardSinglePostBinding.inflate(inflater, container, false);
        setViewPagerAdapter();
        commentCounter();
        binding.dbspUploadComment.setOnClickListener((view -> upload()));
        return binding.getRoot();
    }

    public void setViewPagerAdapter() {
        PostStateAdaptor singlePostStateAdaptor = new PostStateAdaptor(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        post = PostFragmentArgs.fromBundle(getArguments()).getPost();
        fragmentList.add(new MainFragment(post));
        fragmentList.add(new CommentFragment(post.getPostId()));
        singlePostStateAdaptor.setData(fragmentList);
        binding.dbspPages.setAdapter(singlePostStateAdaptor);
        new TabLayoutMediator(binding.dbspTabs, binding.dbspPages,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Post");
                            break;
                        case 1:
                            tab.setText("Comments");
                            break;
                    }
                }).attach();
    }

    private void commentCounter() {
        binding.dbspCommentBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    binding.dbspUploadComment.setImageResource(R.drawable.ic_upload);
                    onUpload = true;
                } else {
                    binding.dbspUploadComment.setImageResource(R.drawable.ic_upload_off);
                    onUpload = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void upload() {
        if (onUpload) {
            Comment newComment = new Comment(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()),
                    User.getInstance().getUserName(),
                    binding.dbspCommentBox.getText().toString());
            DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("discussion_posts")
                    .child(post.getPostId()).child("comments");
            String commentId = commentsRef.push().getKey();
            if (commentId != null) {
                commentsRef.child(commentId).setValue(newComment);
                binding.dbspCommentBox.getText().clear();
                binding.dbspCommentBox.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.getRoot().getApplicationWindowToken(), 0);
                Toast.makeText(getContext(), "Comment is successfully posted! :)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to post your comment :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
