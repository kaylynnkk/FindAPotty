package com.example.findapotty.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {

    private static final String TAG = "DiscussFragment";
    private FragmentFavoriteBinding binding;
    private RecyclerView recyclerView;
    private FavortieRestroomRecyclerViewAdaptor adaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);

        recyclerView = binding.ffFavoriteRestrooms;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), discussionPosts);
        adaptor = new FavortieRestroomRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);

//        initDiscussionBoard();



        return binding.getRoot();
    }
}
