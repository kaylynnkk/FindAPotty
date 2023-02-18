package com.example.findapotty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.findapotty.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
//        binding.setLifecycleOwner(this);

        mAuth = FirebaseAuth.getInstance();

        binding.flLoginButton.setOnClickListener(view -> {
            checkCredentials(view);
        });
        binding.flSignupButton.setOnClickListener(view -> {
            actionSignUpPage(view);
        });

        return binding.getRoot();
    }

    private void actionSignUpPage(View view) {
        NavController controller = Navigation.findNavController(view);
        controller.navigate(R.id.action_navg_login_fragment_to_nav_signup_fragment);
    }

//    private void loginListener() {
//        binding.flLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
//                User user = new User("ano", "100");
//                mdb.child("users").child(mdb.push().getKey()).setValue(user);
//
//                NavController controller = Navigation.findNavController(view);
//                controller.navigate(R.id.action_loginFragment2_to_nav_search);
//            }
//        });
//    }

    private void checkCredentials(View view) {
        String username = binding.loginInputUsername.getText().toString();
        String password = binding.loginInputPassword.getText().toString();


        if (username.isEmpty() || !(username.length() > 7)) {
            showError(binding.loginInputUsername, "Your username is not valid!");

        } else if (password.isEmpty() || password.length() < 1) {
            showError(binding.loginInputPassword, "Password must be at least 7 characters!");

        } else {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(binding.getRoot().getContext(), "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            NavController controller = Navigation.findNavController(view);
                            controller.navigate(R.id.action_loginFragment2_to_nav_search);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(view.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
