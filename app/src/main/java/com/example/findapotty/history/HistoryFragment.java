package com.example.findapotty.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentHistroyBinding;
import com.example.findapotty.favorite.FavortieRestroomRecyclerViewAdaptor;

public class HistoryFragment extends Fragment {

    private FragmentHistroyBinding binding;
    private static final String TAG = "HistoryFragment";
    private RecyclerView recyclerView;
    private VisitedRestroomsRecyclerViewAdaptor adaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistroyBinding.inflate(inflater, container, false);
        recyclerView = binding.fhVisitedRestrooms;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), discussionPosts);
        adaptor = new VisitedRestroomsRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);



        return binding.getRoot();

    }
}
