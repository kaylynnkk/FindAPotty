package com.example.findapotty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.findapotty.databinding.FragmentAppStartBinding;
import com.example.findapotty.login.LoginFragment;
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
        if (!accountViewModel.getUseState()) {
            accountViewModel.setUseState(true);
            if (accountViewModel.getLoginState()) {
                Log.d(TAG, "login: direct login");
                String[] credential = accountViewModel.getCredential();
                checkCredentials(credential[0], credential[1]);
            } else {
                Log.d(TAG, "login: manual login");
                navController.navigate(R.id.action_appStartFragment_to_navg_login_fragment);
            }

        } else {
            requireActivity().finish();
        }
    }

    private void checkCredentials(String username, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                        navController.navigate(R.id.action_appStartFragment_to_nav_search);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(binding.getRoot().getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
