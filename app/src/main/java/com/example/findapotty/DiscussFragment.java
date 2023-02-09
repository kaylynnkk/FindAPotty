package com.example.findapotty;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.FragmentDiscussBinding;

import java.util.ArrayList;

public class DiscussFragment extends Fragment {

    private FragmentDiscussBinding binding;
    private RecyclerView recyclerView;
    private DiscussionPostRecyclerViewAdaptor adaptor;
    private ArrayList<DiscussionPost> discussionPosts = new ArrayList<>();

    private static final String TAG = "DiscussFragment";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discuss, container, false);
        binding.setLifecycleOwner(this);

        recyclerView = binding.fdDicusssionSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), discussionPosts);
        recyclerView.setAdapter(adaptor);

//        initDiscussionBoard();
        addPostListener();



        return binding.getRoot();
    }

    private void initDiscussionBoard(){
        discussionPosts.add(new DiscussionPost(
                "https://www.redditstatic.com/avatars/defaults/v2/avatar_default_1.png",
                "yenalie",
                "The Suite Life of Aether & Lumine - General Question and Discussion Thread",
                "Man, I just had to unsub from DehyaMains. I won’t have primos to pull for her anyway and the cycling through the same topics(doomposting, complaining about doomposting, making up future mechanics or artifact sets, back to doomposting, etc) is getting pretty rough to watch. Plus I don’t think that’ll change in the next few weeks…"
                ));
        discussionPosts.add(new DiscussionPost(
                "https://styles.redditmedia.com/t5_5gferu/styles/profileIcon_snoo-nftv2_bmZ0X2VpcDE1NToxMzdfNmFjYjhmYjgyODgwZDM5YzJiODQ0NmY4Nzc4YTE0ZDM0ZWU2Y2ZiN180MDIwNDM_rare_4210bb9e-16e5-4abf-b81d-8856eaa6f28d-headshot.png",
                "cosmos0001",
                "The Suite Life of Aether & Lumine - General Question and Discussion Thread",
                "Some content"
                ));
        discussionPosts.add(new DiscussionPost(
                "https://styles.redditmedia.com/t5_2j9wpv/styles/profileIcon_snoo9375d976-d9a3-4437-b00d-b282b6c32022-headshot.png",
                "seokhwakangs",
                "3.6 beta: info on Kaveh + Baizhu skills, cons, and Dehya's team",
                "i think it's all but explicitly debunked, now that soraya was strongly implied to be the scholar he converses with in lantern rite epilogue (ngl p (jovially) disappointed in myself how did i miss her)"
                ));

    }


    private void addPostListener() {
        binding.fdAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discussionPosts.add(new DiscussionPost(
                        "https://styles.redditmedia.com/t5_2j9wpv/styles/profileIcon_snoo9375d976-d9a3-4437-b00d-b282b6c32022-headshot.png",
                        "seokhwakangs",
                        "3.6 beta: info on Kaveh + Baizhu skills, cons, and Dehya's team",
                        "i think it's all but explicitly debunked, now that soraya was strongly implied to be the scholar he converses with in lantern rite epilogue (ngl p (jovially) disappointed in myself how did i miss her)"
                ));

                adaptor.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
