package com.example.findapotty;

import android.os.Bundle;
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

import com.example.findapotty.databinding.FragmentSignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FragmentSignupBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);
        binding.setLifecycleOwner(this);

        mAuth = FirebaseAuth.getInstance();

        binding.flRegisterButton.setOnClickListener(view -> {
            checkCredentials(view);
        });

        return binding.getRoot();
    }


    private void checkCredentials(View view) {
        String username = binding.signupInputUsername.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String password = binding.signupInputPassword.getText().toString();
        String confirmPass = binding.confirmPassword.getText().toString();

        if (username.isEmpty() || username.length() < 7) {
            showError(binding.signupInputUsername, "Your username is not valid!");
        } else if (email.isEmpty() || !email.contains("@")) {
            showError(binding.inputEmail, "Email is not valid!");
        } else if (password.isEmpty() || password.length() < 7) {
            showError(binding.signupInputPassword, "Password must be at least 7 characters long!");
        } else if (confirmPass.isEmpty() || !confirmPass.equals(password)) {
            showError(binding.confirmPassword, "Password does not match!");
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Successful Registration", Toast.LENGTH_SHORT).show();
                    // back to login
                    NavController controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_nav_signup_fragment_to_navg_login_fragment);

                } else {
//                    Toast.makeText(view.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(view.getContext(), "Specified email already exists, please use another one",
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
