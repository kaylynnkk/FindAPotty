package com.example.findapotty;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController controller;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, controller);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);

        // default visibility
        bottomNavigationView.setVisibility(View.GONE);
        // change visibility
        controller.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.nav_search:
                case R.id.nav_feed:
                case R.id.nav_add:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.GONE);
            }
//            if (navDestination.getId() == R.id.navg_login_fragment || ){
//                bottomNavigationView.setVisibility(View.GONE);
//            }else{
//                bottomNavigationView.setVisibility(View.VISIBLE);
//            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}
