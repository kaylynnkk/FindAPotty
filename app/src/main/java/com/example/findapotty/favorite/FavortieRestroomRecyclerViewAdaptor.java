package com.example.findapotty.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.example.findapotty.Restroom;
import com.example.findapotty.RestroomManager;
import com.example.findapotty.User;
import com.example.findapotty.databinding.FavoriteSingleRestroomPreviewBinding;
import com.example.findapotty.search.MapFragmentDirections;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FavortieRestroomRecyclerViewAdaptor extends RecyclerView.Adapter<FavortieRestroomRecyclerViewAdaptor.ViewHolder>{

    private FavoriteSingleRestroomPreviewBinding binding;
    Context context;


    public FavortieRestroomRecyclerViewAdaptor(Context context) {
//        this.discussionPosts = discussionPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.favorite_single_restroom_preview, parent, false);
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
        holder.restroomName.setText(restroom.getName());
        holder.restroomAddress.setText(restroom.getAddress());
        holder.parentLayout.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(view);
            NavDirections action = FavoriteFragmentDirections.actionNavFavoriteToNavgRrPgFragment(restroom);
            controller.navigate(action);
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

        public ViewHolder(FavoriteSingleRestroomPreviewBinding binding) {
            super(binding.getRoot());
            restroomPhoto = binding.fsrRestroomPhoto;
            restroomName = binding.fsrRestroomName;
            restroomAddress = binding.fsrRestroomAddress;
            parentLayout = binding.favoriteRestroomItem;
        }
    }
}
