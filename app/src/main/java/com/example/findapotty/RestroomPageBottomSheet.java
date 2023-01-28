package com.example.findapotty;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RestroomPageBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "RestroomPageBottomSheet";
    private ArrayList<RestroomReview> restroomReviews = new ArrayList<RestroomReview>();
    private ImageView rr_photo;
    private View view;
    private RecyclerView recyclerView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");

        super.onCreateDialog(savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialog);

        view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_restroom_page, null);
        dialog.setContentView(view);

        recyclerView = view.findViewById(R.id.rr_pg_review_section);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RestroomReviewRecyclerViewAdaptor(getContext(), restroomReviews));

        initReviews();
        addReviewListener();



        return dialog;
    }


    private void initReviews(){
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim"));
        restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim"));
    }
    
    // unusable
    private void addReviewListener(){
        FloatingActionButton btn = view.findViewById(R.id.rr_page_add_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add review");
                restroomReviews.add(new RestroomReview("https://i.redd.it/tpsnoz5bzo501.jpg", "1243r4tgg g5"));
            }
        });
    }

    public void resizeImageOnScroll(){
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                rr_photo = view.findViewById(R.id.rr_pg_rr_photos);
                float ratio = rr_photo.getHeight() / rr_photo.getWidth();
                float scale = getContext().getResources().getDisplayMetrics().density;

            }
        });
    }

}
