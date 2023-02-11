package com.example.findapotty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    TextView btn;

    private EditText inputUsername, inputPassword, inputEmail, confirmPassword;
    Button fl_register_button, fl_account_button;
    private FirebaseAuth mAuth;
    private ProgressBar mLoadingBar;
    View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_register, container, false);
    }
}
