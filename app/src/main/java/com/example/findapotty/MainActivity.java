package com.example.findapotty;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.findapotty.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);






    }

    @Override
    protected void onStart() {
        super.onStart();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // - Navigation View
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_history, R.id.nav_search, R.id.nav_feed,
                R.id.nav_discuss, R.id.nav_favorite, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        // - Bottom Navigation View
        BottomNavigationView bottomNavigationView = binding.appBarMain.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // default visibility
        bottomNavigationView.setVisibility(View.GONE);
        binding.appBarMain.toolbar.setVisibility(View.GONE);
        // change visibility
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.nav_search:
                case R.id.nav_feed:
                case R.id.nav_discuss:
                case R.id.nav_favorite:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.navg_login_fragment:
                case R.id.nav_signup_fragment:
                    binding.appBarMain.toolbar.setVisibility(View.GONE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.GONE);
                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
            }
        });

        grantPermissions();



    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public void grantPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED) {
            ;
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

}
