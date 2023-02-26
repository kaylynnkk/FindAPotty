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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.example.findapotty.User;
import com.example.findapotty.databinding.FragmentDiscussBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class DiscussFragment extends Fragment {

    private FragmentDiscussBinding binding;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<DiscussionPost, DiscussionPostRecyclerViewAdaptor.ViewHolder> adaptor;
//    private DiscussionPostManager discussionPostManager  = DiscussionPostManager.getInstance();
    private DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("discussion_posts/"+ User.getInstance().getUserId());

    private static final String TAG = "DiscussFragment";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discuss, container, false);

        // init variables
        recyclerView = binding.fdDicusssionSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<DiscussionPost> options =
                new FirebaseRecyclerOptions.Builder<DiscussionPost>()
                        .setQuery(postRef, DiscussionPost.class)
                        .build();
        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), options);
        recyclerView.setAdapter(adaptor);

        // listener
        binding.fdAddPostButton.setOnClickListener(view -> toWriteNewPost());
//        binding.fdRefreshBoard.setOnClickListener(view -> refreshBoard());


        return binding.getRoot();
    }

    private DiscussionPost genRandomPost(){
        Random random = new Random();
        switch (random.nextInt(3)){
            case 0: {
                return new DiscussionPost(
                        "https://www.redditstatic.com/avatars/defaults/v2/avatar_default_1.png",
                        "yenalie",
                        "The Suite Life of Aether & Lumine - General Question and Discussion Thread",
                        "Man, I just had to unsub from DehyaMains. I won’t have primos to pull for her anyway and the cycling through the same topics(doomposting, complaining about doomposting, making up future mechanics or artifact sets, back to doomposting, etc) is getting pretty rough to watch. Plus I don’t think that’ll change in the next few weeks…"
                );
            }
            case 1: {
                return new DiscussionPost(
                        "https://styles.redditmedia.com/t5_5gferu/styles/profileIcon_snoo-nftv2_bmZ0X2VpcDE1NToxMzdfNmFjYjhmYjgyODgwZDM5YzJiODQ0NmY4Nzc4YTE0ZDM0ZWU2Y2ZiN180MDIwNDM_rare_4210bb9e-16e5-4abf-b81d-8856eaa6f28d-headshot.png",
                        "cosmos0001",
                        "The Suite Life of Aether & Lumine - General Question and Discussion Thread",
                        "Some content"
                );
            }
            case 2: {
                return new DiscussionPost(
                        "https://styles.redditmedia.com/t5_2j9wpv/styles/profileIcon_snoo9375d976-d9a3-4437-b00d-b282b6c32022-headshot.png",
                        "seokhwakangs",
                        "3.6 beta: info on Kaveh + Baizhu skills, cons, and Dehya's team",
                        "i think it's all but explicitly debunked, now that soraya was strongly implied to be the scholar he converses with in lantern rite epilogue (ngl p (jovially) disappointed in myself how did i miss her)"
                );
            }
            default: return new DiscussionPost(
                    "https://styles.redditmedia.com/t5_2j9wpv/styles/profileIcon_snoo9375d976-d9a3-4437-b00d-b282b6c32022-headshot.png",
                    "seokhwakangs",
                    "3.6 beta: info on Kaveh + Baizhu skills, cons, and Dehya's team",
                    "i think it's all but explicitly debunked, now that soraya was strongly implied to be the scholar he converses with in lantern rite epilogue (ngl p (jovially) disappointed in myself how did i miss her)"
            );
        }
    }

    private void toWriteNewPost() {;
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_nav_discuss_to_addDiscussionPostFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        adaptor.startListening();
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
        adaptor.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

}
