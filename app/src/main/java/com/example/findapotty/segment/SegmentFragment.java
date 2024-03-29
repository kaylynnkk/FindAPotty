package com.example.findapotty.segment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findapotty.R;

/* Page that displays the additional features options: diary, trends, trainer, reminders, tunes, contact us*/
public class SegmentFragment extends Fragment {

    private TextView diary_bt , trends_bt, trainer_bt, reminders_bt, tunes_bt, contactus_bt, aboutus_bt;
    private View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

         // Inflate the segment layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_segment, container, false);

        //  connecting buttons to their designated ids
        diary_bt = rootView.findViewById(R.id.diary_bt);
        trends_bt = rootView.findViewById(R.id.trends_bt);
        trainer_bt = rootView.findViewById(R.id.trainer_bt);
        reminders_bt = rootView.findViewById(R.id.reminders_bt);
        tunes_bt = rootView.findViewById(R.id.tunes_bt);
        contactus_bt = rootView.findViewById(R.id.contactus_bt);
        aboutus_bt = rootView.findViewById(R.id.aboutus_bt);

        // NAVIGATIONS
        // navigates from segment page to feedback page when user clicks on 'contact us'
        contactus_bt.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_segment_to_nav_feedback));
        // navigates from segment page to dev page when user clicks on 'about us'
        aboutus_bt.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_segment_to_nav_devpage));
        // naviagets from segment page to trainer page when user clicks on 'trainer'
        trainer_bt.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_segment_to_nav_trainer));

        return rootView;
    }
}