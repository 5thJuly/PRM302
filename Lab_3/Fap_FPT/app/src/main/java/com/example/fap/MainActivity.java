package com.example.fap;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private View scrollView;
    private View fragmentContainer;
    private BottomNavigationView bottomNav;
    private MaterialToolbar toolbar;
    private MaterialCardView weeklyTimetableCard, examScheduleCard;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNavigation();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        scrollView = findViewById(R.id.scrollView);
        fragmentContainer = findViewById(R.id.nav_host_fragment);
        bottomNav = findViewById(R.id.bottomNav);
        weeklyTimetableCard = findViewById(R.id.weeklyTimetableCard);
        examScheduleCard = findViewById(R.id.examScheduleCard);
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_settings)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);

        NavigationView navigationView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupClickListeners() {
        weeklyTimetableCard.setOnClickListener(v -> showFragment(WeeklyTimetableFragment.class));
        examScheduleCard.setOnClickListener(v -> showFragment(ExamScheduleFragment.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
        if (handled) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return handled;
    }

    private void showFragment(Class<? extends Fragment> fragmentClass) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (fragmentClass.equals(WeeklyTimetableFragment.class)) {
            navController.navigate(R.id.weeklyTimetableCard);
        } else if (fragmentClass.equals(ExamScheduleFragment.class)) {
            navController.navigate(R.id.examScheduleCard);
        } else {
            navController.navigate(R.id.nav_home);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (navController.getCurrentDestination().getId() == R.id.nav_home) {
            super.onBackPressed();
        } else {
            navController.navigateUp();
        }
    }
}
