package com.example.findapotty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.findapotty.databinding.FragmentAppStartBinding;
import com.example.findapotty.user.AccountViewModel;

public class AppStartFragment extends Fragment {

    private static final String TAG = "AppStartFragment";
    private FragmentAppStartBinding binding;
    private AccountViewModel accountViewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         binding = FragmentAppStartBinding.inflate(inflater, container, false);
         accountViewModel = new AccountViewModel(binding.getRoot().getContext());



         return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(binding.getRoot());
        directLogin();
    }

    private void directLogin() {
        if (accountViewModel.getLoginState()) {
            Log.d(TAG, "login: direct login");
            // String[] credential = accountViewModel.getCredential();
            // checkCredentials(credential[0], credential[1]);
            navController.navigate(R.id.action_appStartFragment_to_nav_search);
        } else {
            Log.d(TAG, "login: manual login");
            navController.navigate(R.id.action_appStartFragment_to_navg_login_fragment);
        }
    }
}
