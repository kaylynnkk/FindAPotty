package com.example.findapotty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView btn;
    EditText inputUsername, inputPassword;
    Button fl_login_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        fl_login_button = findViewById(R.id.fl_signup_button);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        fl_login_button = findViewById(R.id.fl_login_button);
        fl_login_button.setOnClickListener((v)->{checkCredentials();});
        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener((v)->{
            startActivity(new Intent(Login.this, Register.class));
        });

    }

    private void checkCredentials()
    {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        if(username.isEmpty() || !(username.length() > 7))
        {
            showError(inputUsername, "Your username is not valid!");
        }
        else if(password.isEmpty() || password.length()<7)
        {
            showError(inputPassword, "Password must be at least 7 characters!");
        }
        else
        {
            //Toast.makeText(this, "Call Login Method", Toast.LENGTH_SHORT).show();
            //should change username to email throughout file probably
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //made second parameter MapFragment to navigate to that page
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(Login.this, MapFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {

                    }


                }
            });
        }
    }

    private void showError(EditText input, String s)
    {
        input.setError(s);
        input.requestFocus();
    }

}
