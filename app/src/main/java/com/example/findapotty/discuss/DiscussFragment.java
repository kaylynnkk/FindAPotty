package com.example.findapotty.discuss;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentDiscussBinding;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Random;

public class DiscussFragment extends Fragment {

    private FragmentDiscussBinding binding;
    private RecyclerView recyclerView;
    private DiscussionPostRecyclerViewAdaptor adaptor;
    private DiscussionPostManager discussionPostManager  = DiscussionPostManager.getInstance();
    private DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("discussion_posts");

    private static final String TAG = "DiscussFragment";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discuss, container, false);

        // init variables
        recyclerView = binding.fdDicusssionSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);

        // init
        if (discussionPostManager.onInit){
            init();
            discussionPostManager.onInit = false;
        }

        // listener
        binding.fdAddPostButton.setOnClickListener(view -> toWriteNewPost());
//        binding.fdRefreshBoard.setOnClickListener(view -> refreshBoard());
        binding.fdSwipeRefresh.setOnRefreshListener(() -> {
            discussionPostManager.clearDiscussionPosts(adaptor);
            init();
            binding.fdSwipeRefresh.setRefreshing(false);
        });


        return binding.getRoot();
    }

    private void init() {
        Log.d(TAG, "init: ");
        discussionPostManager.clearDiscussionPosts(adaptor);

        postRef.orderByChild("uploadTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    DiscussionPost discussionPost = post.child("main").getValue(DiscussionPost.class);
                    if (discussionPost != null) {
                        discussionPost.setPostId(post.getKey());
                        discussionPostManager.addDiscussionPost(discussionPost, adaptor);
                    }
                }
                postRef.orderByChild("uploadTime").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toWriteNewPost() {;
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_nav_discuss_to_addDiscussionPostFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        discussionPostManager.onInit = true;
        discussionPostManager.clearDiscussionPosts(adaptor);
    }

}
