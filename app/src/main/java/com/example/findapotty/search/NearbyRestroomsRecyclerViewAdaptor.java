package com.example.findapotty.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.databinding.NearbySingleRestroomPreviewBinding;
import com.example.findapotty.user.FavoriteRestroomsManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NearbyRestroomsRecyclerViewAdaptor extends RecyclerView.Adapter<NearbyRestroomsRecyclerViewAdaptor.ViewHolder> {
    private NearbySingleRestroomPreviewBinding binding;
    private static final String TAG = "NearbyRestroomsRecyclerViewAdaptor";
    Context context;


    public NearbyRestroomsRecyclerViewAdaptor(Context context) {
//        this.discussionPosts = discussionPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public NearbyRestroomsRecyclerViewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = NearbySingleRestroomPreviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new NearbyRestroomsRecyclerViewAdaptor.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NearbyRestroom restroom = NearbyRestroomsManager.getInstance().getRestroomByIndex(position);
        Log.d(TAG, "onBindViewHolder: " + restroom.getName());
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
            NavDirections action =
                    MapFragmentDirections.actionMapFragment2ToRestroomPageBottomSheet2(restroom);
            controller.navigate(action);
        });

    }

    @Override
    public int getItemCount() {
        return NearbyRestroomsManager.getInstance().getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restroomPhoto;
        TextView restroomName;
        TextView restroomAddress;
        RelativeLayout parentLayout;

        public ViewHolder(NearbySingleRestroomPreviewBinding binding) {
            super(binding.getRoot());
            restroomPhoto = binding.nsrpRestroomPhoto;
            restroomName = binding.nsrpRestroomName;
            restroomAddress = binding.nsrpRestroomAddress;
            parentLayout = binding.nearbyRestroomItem;
        }
    }
}
