package com.example.findapotty.discuss.post;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PostStateAdaptor extends FragmentStateAdapter {

    private ArrayList<Fragment> fragments;

    public PostStateAdaptor(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void setData(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }
}
