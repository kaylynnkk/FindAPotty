package com.example.findapotty;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    TextView btn;
    EditText inputUsername, inputPassword;
    Button fl_login_button;
    private FirebaseAuth mAuth;
    //can remove line below
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fl_login_button = findViewById(R.id.fl_signup_button);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        fl_login_button = findViewById(R.id.fl_login_button);
        fl_login_button.setOnClickListener((v)->{checkCredentials();});
        mAuth = FirebaseAuth.getInstance();

//        btn.setOnClickListener((v)->{
//            startActivity(new Intent(loginActivity.this, loginActivity.class));
//        });

    }


    private void checkCredentials()
    {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
                @SuppressLint("ResourceType")
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //made second parameter MapFragment to navigate to that page
                    if(task.isSuccessful())
                    {

                        NavController navController = Navigation.findNavController(Login.this, R.id.nav_host_fragment);
                        navController.navigateUp();
                        navController.navigate(R.layout.fragment_map);
//                        startActivity(intent);
                        Toast.makeText(Login.this, "Successful Login", Toast.LENGTH_SHORT).show();
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
