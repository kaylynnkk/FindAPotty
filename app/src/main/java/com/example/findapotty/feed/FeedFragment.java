package com.example.findapotty.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findapotty.R;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment{

    private View rootView;
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the devpage recycler layout for this fragment
        rootView = inflater.inflate(R.layout.devpage_recyclerview, container, false);

        // Create list of developers
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("Peter Johnson","BYD to take on Tesla’s Autopilot with new driver-assist software – report","The competition between the world’s largest EV makers is about to heat up. According to a new report, BYD is set to unveil a new advanced driver assistance system (ADAS) later this year that will rival Tesla’s Autopilot.\\n more…\\nThe post BYD to take on Tesla’s…",R.drawable.tesla));
        articleList.add(new Article("news@appleinsider.com (Amber Neely)","EU regulators ramp up probe into NFC tech at core of Apple Pay","European Union officials continue to scrutinize Apple for its restrictions on the NFC antenna used for Apple Pay, a practice the European Commission dubbed anti-competitive.Apple PayThe European Commission, which oversees antitrust laws in the EU, has accused…",R.drawable.phone));
        articleList.add(new Article("Barbie Latza Nadeau,Valentina Di Donato,Anna Cooban","Italian pasta prices are soaring. Rome is in crisis talks with producers - CNN","Italy's government convened crisis talks Thursday to investigate the reasons behind a surge in prices for pasta, one of the country's most beloved and culturally important foods.",R.drawable.pasta));
        articleList.add(new Article("Aria Alamalhodaei","Intuitive Machines prepares for first lunar mission, faces challenge to NASA contract win","Intuitive Machines is preparing for its first lunar mission to the moon’s south pole in the third quarter of this year.",R.drawable.space));

        // Create a new articleadapter with the list of developers
        ArticleAdapter articleAdapter = new ArticleAdapter(articleList);

        // Get a reference to the recycler view and set the adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(articleAdapter);

        // Set the layout manager for the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
