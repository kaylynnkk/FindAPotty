package com.example.findapotty.discuss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentDiscussBinding;

import java.util.Random;

public class DiscussFragment extends Fragment {

    private FragmentDiscussBinding binding;
    private RecyclerView recyclerView;
    private DiscussionPostRecyclerViewAdaptor adaptor;
//    private ArrayList<DiscussionPost> discussionPosts = new ArrayList<>();
    private DiscussionPostManager discussionPostManager  = DiscussionPostManager.getInstance();

    private static final String TAG = "DiscussFragment";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discuss, container, false);
        binding.setLifecycleOwner(this);

        recyclerView = binding.fdDicusssionSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), discussionPosts);
        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);


//        initDiscussionBoard();

        // listener
        binding.fdAddPostButton.setOnClickListener(view -> toWriteNewPost(view));

        // receive arguments
        addToList();



        return binding.getRoot();
    }

    private DiscussionPost initDiscussionBoard(){
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

    private void toWriteNewPost(View view) {
//        discussionPostManager.addDiscussionPost(initDiscussionBoard(), adaptor);
//
//        adaptor.notifyItemInserted(0);
//        recyclerView.smoothScrollToPosition(0);
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_nav_discuss_to_addDiscussionPostFragment);
    }

    private void addToList() {
        if (getArguments() != null) {
//            DiscussionPost newDiscussionPost = DiscussFragmentArgs.fromBundle(getArguments()).getNewDiscussionPost();
//            discussionPostManager.addDiscussionPost(newDiscussionPost, adaptor);

            adaptor.notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
        }
    }

}
