package com.example.findapotty;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.findapotty.databinding.ActivityMainBinding;
import com.example.findapotty.user.AccountViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;



    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController = ((NavHostFragment) binding.navHostFragment.getFragment()).getNavController();

        // - init variables
        Toolbar toolbar = binding.appBarMain.mainToolbar;
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        // - tool bar
        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tunes,
                R.id.nav_reminder,
                R.id.nav_diary,
                R.id.nav_history,
                R.id.nav_search, R.id.nav_feed,
                R.id.nav_discuss, R.id.nav_favorite, R.id.nav_profile, R.id.nav_trainer)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // - Navigation View
        NavigationUI.setupWithNavController(navigationView, navController);

        // - Bottom Navigation View
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // - visibility
        // default visibility
        bottomNavigationView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        // change visibility
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            // bottom nav view
            switch (navDestination.getId()) {
                case R.id.nav_search:
                case R.id.nav_discuss:

                case R.id.nav_favorite:
                case R.id.nav_feed:

                case R.id.nav_profile:

                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
                default:
                    bottomNavigationView.setVisibility(View.GONE);
            }

            // toolbar
            switch (navDestination.getId()) {
                case R.id.navg_login_fragment:
                case R.id.nav_signup_fragment:
                    toolbar.setVisibility(View.GONE);
                    break;
//                case R.id.nav_search:
//                    toolbar.setVisibility(View.VISIBLE);
//                    toolbar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_common_toolbar, getTheme()));
//                    break;
//                case R.id.nav_feed:
//                    toolbar.setVisibility(View.VISIBLE);
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_feeds, this.getTheme()));
//                    break;
//                case R.id.nav_discuss:
//                case R.id.nav_favorite:
                default:
                    toolbar.setVisibility(View.VISIBLE);
//                    toolbar.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_common_toolbar, getTheme()));
            }
        });

        // manage navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navg_login_fragment) {
                    Log.d(TAG, "onNavigationItemSelected: 11111111111222");
                    AccountViewModel accountViewModel = new AccountViewModel(binding.getRoot().getContext());
                    accountViewModel.clearCredential();
                }
                navController.navigate(item.getItemId());
//                navController.popBackStack(R.id.nav_search, false);
                drawer.close();
                return true;
            }
        });

        grantPermissions();


    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    // private final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public void grantPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        };
        for (String permission : permissions) {
            if ( ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(
                        new String[] {permission},
                        1234);
            }
        }
    }

    /*@Override
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
    }*/

    // https://stackoverflow.com/a/41646358
    @Override
    public void setTitle(final CharSequence title) {
        binding.appBarMain.mainToolbar.postDelayed(
                () -> getSupportActionBar().setTitle(title), 10);
    }

    // https://stackoverflow.com/a/28939113
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
