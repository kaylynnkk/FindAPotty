package com.example.findapotty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.findapotty.databinding.FragmentLoginBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.setLifecycleOwner(this);

        // login
        binding.flLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
                User user = new User("ano", "100");
                mdb.child("users").child(mdb.push().getKey()).setValue(user);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment2_to_nav_search);
            }
        });

        // sign up
        binding.flSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_navg_login_fragment_to_nav_signup_fragment);
            }
        });

        return binding.getRoot();
    }
}
