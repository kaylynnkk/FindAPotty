package com.example.findapotty;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.BottomSheetRestroomPageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RestroomPageBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "RestroomPageBottomSheet";
    private ArrayList<RestroomReview> restroomReviews = new ArrayList<>();
    private ImageView rr_photo;

    private BottomSheetRestroomPageBinding binding;
    private View rootView;
    private RecyclerView recyclerView;
    private RestroomReviewRecyclerViewAdaptor adaptor;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialog);

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_restroom_page, null);
//        dialog.setContentView(rootView);

        binding = DataBindingUtil.bind(rootView);
//                .inflate(
//                getLayoutInflater(),
//                R.layout.bottom_sheet_restroom_page,
//                null, false);
        binding.setLifecycleOwner(this);
        dialog.setContentView(binding.getRoot());

        recyclerView = binding.rrPgReviewSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new RestroomReviewRecyclerViewAdaptor(getContext(), restroomReviews);
        recyclerView.setAdapter(adaptor);


        initRestroomPage(RestroomPageBottomSheetArgs.fromBundle(getArguments()).getMarkerId());
        initReviews();
        addReviewListener();
        editPageListener();


        return dialog;
    }

    private void initReviews() {
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim1"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim2"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim3"));
    }

    private void initRestroomPage(String markId) {
        RestroomManager restroomManager = RestroomManager.getInstance();
        Restroom restroom = restroomManager.getRestroomByMarkerId(markId);
        if (restroom != null){
            binding.rrPgRrPhotos.setImageBitmap(
                    restroomManager.getRestroomByMarkerId(markId).getPhoto());
        }
    }

    private void addReviewListener() {
        FloatingActionButton btn = rootView.findViewById(R.id.rr_page_add_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add review");
                restroomReviews.add(0, new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "1243r4tgg g5"));
                adaptor.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public void resizeImageOnScroll() {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                rr_photo = view.findViewById(R.id.rr_pg_rr_photos);
                float ratio = rr_photo.getHeight() / rr_photo.getWidth();
                float scale = getContext().getResources().getDisplayMetrics().density;

            }
        });
    }

    public void editPageListener() {

        rootView.findViewById(R.id.rr_pg_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavController controller = Navigation.findNavController(view);
                NavController controller = NavHostFragment.findNavController(RestroomPageBottomSheet.this);
                controller.navigate(R.id.action_navg_rr_pg_fragment_to_navg_rr_pg_edit_fragment);
            }
        });
    }

}
