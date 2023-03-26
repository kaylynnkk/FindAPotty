package com.example.findapotty.discuss.post.comment;

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
import com.example.findapotty.databinding.DiscussionBoardSinglePostCommentSectionBinding;
import com.example.findapotty.discuss.DiscussionPostManager;
import com.example.findapotty.discuss.DiscussionPostRecyclerViewAdaptor;

public class CommentFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommentRecyclerViewAdaptor adaptor;
    private CommentsManager commentsManager  = CommentsManager.getInstance();
    private static final String TAG = "DiscussFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DiscussionBoardSinglePostCommentSectionBinding binding =
                DiscussionBoardSinglePostCommentSectionBinding.inflate(inflater, container, false);

        recyclerView = binding.dbspceCommentSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new CommentRecyclerViewAdaptor(getContext());
        recyclerView.setAdapter(adaptor);

        init();

        return binding.getRoot();
    }

    private void init() {
        CommentsManager.getInstance().addItem(
                new Comment("11-11-22", "test1",
                        getResources().getString(R.string.post_test_content))
        );
        CommentsManager.getInstance().addItem(
                new Comment("11-11-33", "test2", "gggggggggggggggggg")
        );
        CommentsManager.getInstance().addItem(
                new Comment("11-11-44", "test3", "hhhhhhhhhhhhhhhhhhh")
        );
    }
}
