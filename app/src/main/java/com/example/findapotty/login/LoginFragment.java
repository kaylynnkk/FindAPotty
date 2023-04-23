package com.example.findapotty.login;

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

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentLoginBinding;
import com.example.findapotty.user.AccountViewModel;
import com.example.findapotty.user.FavoriteRestroomsManager;
import com.example.findapotty.user.User;
import com.example.findapotty.user.VisitedRestroomsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private FragmentLoginBinding binding;
    private AccountViewModel accountViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
//        binding.setLifecycleOwner(this);
        mAuth = FirebaseAuth.getInstance();
        accountViewModel = new AccountViewModel(binding.getRoot().getContext());
        directLogin();
        binding.flLoginButton.setOnClickListener(view -> {
            login();
        });
        binding.flSignupButton.setOnClickListener(view -> {
            actionSignUpPage(view);
        });
        binding.flLoginButton2.setOnClickListener(view -> {
            quickLogin();
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

    private void directLogin() {
        if (accountViewModel.getLoginState()) {
            Log.d(TAG, "login: direct login");
            String[] credential = accountViewModel.getCredential();
            checkCredentials(credential[0], credential[1]);
        }
    }

    private void login() {
        Log.d(TAG, "login: manually login");
        String username = binding.loginInputUsername.getText().toString();
        String password = binding.loginInputPassword.getText().toString();
        if (username.isEmpty() || !(username.length() > 7)) {
            showError(binding.loginInputUsername, "Your username is not valid!");

        } else if (password.isEmpty() || password.length() < 1) {
            showError(binding.loginInputPassword, "Password must be at least 7 characters!");

        } else {
            checkCredentials(username, password);
        }
    }

    private void checkCredentials(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
                        String userId = mAuth.getCurrentUser().getUid();
                        User user = User.getInstance();
                        // retrieve user info
                        mdb.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User retrievedUser = snapshot.getValue(User.class);
                                User currentUser = User.getInstance();
                                // user id
                                currentUser.setUserId(retrievedUser.getUserId());
                                // user name
                                currentUser.setUserName(retrievedUser.getUserName());
                                // user avatar
                                currentUser.setAvatarPath(retrievedUser.getAvatarPath());
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                StorageReference ref = storageRef.child(currentUser.getAvatarPath());
                                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                    currentUser.setAvatarUrl(uri);
                                });
                                // user favorite restrooms
                                FavoriteRestroomsManager.getInstance()
                                        .setRestrooms(retrievedUser.getFavoriteRestrooms());
                                // visited retrooms
                                VisitedRestroomsManager.getInstance()
                                        .setRestrooms(retrievedUser.getVisitedRestrooms());
                                Toast.makeText(binding.getRoot().getContext(), "Welcome " + currentUser.getUserName(),
                                        Toast.LENGTH_SHORT).show();
                                mdb.child("users").child(userId).removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        // save credential
                        accountViewModel.saveCredential(username, password);
                        accountViewModel.setLoginState(true);
                        // end retrieve user info
                        NavController controller = Navigation.findNavController(binding.getRoot());
                        controller.navigate(R.id.action_loginFragment2_to_nav_search);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(binding.getRoot().getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    private void quickLogin() {
        NavController controller = Navigation.findNavController(binding.getRoot());
        controller.navigate(R.id.action_loginFragment2_to_nav_search);
    }
}
