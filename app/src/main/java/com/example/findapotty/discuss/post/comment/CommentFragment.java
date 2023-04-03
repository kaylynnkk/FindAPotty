package com.example.findapotty.discuss.post.comment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.DiscussionBoardSinglePostCommentSectionBinding;
import com.example.findapotty.discuss.post.comment.reply.Reply;
import com.example.findapotty.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommentRecyclerViewAdaptor adaptor;
    private CommentsManager commentsManager = CommentsManager.getInstance();
    private static final String TAG = "CommentFragment";
    private final String postId;

    public CommentFragment(String postId) {
        this.postId = postId;
    }

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
        binding.dbspceSwipeRefresh.setOnRefreshListener(() -> {
            int size = commentsManager.getCount();
            commentsManager.clearItems();
            adaptor.notifyItemRangeRemoved(0, size);
            init();
            binding.dbspceSwipeRefresh.setRefreshing(false);
        });
        return binding.getRoot();
    }

    private void init() {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("discussion_posts")
                .child(postId).child("comments");
        commentsRef.orderByChild("uploadTime").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        HashMap<String, Comment> comments = (HashMap<String, Comment>) snapshot.getValue();
//                        CommentsManager.getInstance().setItems(comments);
                        for (DataSnapshot commentSnapShot : snapshot.getChildren()) {
                            Comment comment = commentSnapShot.getValue(Comment.class);
                            comment.setId(commentSnapShot.getKey());
                            // replies
                            comment.getRepliesManager().addItem(0, new Reply("this is a reply 1"));
                            comment.getRepliesManager().addItem(0, new Reply("this is a reply 2"));
                            comment.getRepliesManager().addItem(0, new Reply("this is a reply 3"));
                            Log.d(TAG, "onDataChange: id: " + commentSnapShot.getKey());
                            CommentsManager.getInstance().addItem(0, comment);
                            adaptor.notifyItemInserted(0);
                        }
                        commentsRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void addComment() {
        Comment newComment = new Comment(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()),
                User.getInstance().getUserName(),
                getResources().getString(R.string.post_test_content));

        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("discussion_posts")
                .child(postId).child("comments");
        String commentId = commentsRef.push().getKey();
        if (commentId != null) {
            commentsRef.child(commentId).setValue(newComment);
        } else {
            Toast.makeText(getContext(), "Failed to upload your comment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commentsManager.clearItems();
    }
}
