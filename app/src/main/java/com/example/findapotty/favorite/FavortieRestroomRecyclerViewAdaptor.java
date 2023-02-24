package com.example.findapotty.favorite;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.example.findapotty.Restroom;
import com.example.findapotty.User;
import com.example.findapotty.databinding.DiscussionBoardSinglePostPreviewBinding;
import com.example.findapotty.databinding.FavoriteSingleRestroomBinding;
import com.example.findapotty.discuss.DiscussionPostRecyclerViewAdaptor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavortieRestroomRecyclerViewAdaptor extends RecyclerView.Adapter<FavortieRestroomRecyclerViewAdaptor.ViewHolder>{

    private FavoriteSingleRestroomBinding binding;
    Context context;


    public FavortieRestroomRecyclerViewAdaptor(Context context) {
//        this.discussionPosts = discussionPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.favorite_single_restroom, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Restroom restroom = User.getInstance().getFavoriteRestroomByIndex(position);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageRef.child(restroom.getPhotoPath());
        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .asBitmap()
                    .dontAnimate()
                    .load(uri)
                    .into(holder.restroomPhoto);
        });



    }

    @Override
    public int getItemCount() {
        return User.getInstance().getFavoriteRestrooms().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restroomPhoto;
        TextView restroomName;
        TextView restroomAddress;
        RelativeLayout parentLayout;

        public ViewHolder(FavoriteSingleRestroomBinding binding) {
            super(binding.getRoot());
            restroomPhoto = binding.fsrRestroomPhoto;
            restroomName = binding.fsrRestroomName;
            restroomAddress = binding.fsrRestroomAddress;
            parentLayout = binding.favoriteRestroomItem;
        }
    }
}
