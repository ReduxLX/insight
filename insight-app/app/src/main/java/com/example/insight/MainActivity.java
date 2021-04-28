package com.example.insight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();
    }

    private void setupNavigation(){
        // Find Nav Controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Create a list of all top-level fragments (no back button)
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.homeFragment);
        topLevelDestinations.add(R.id.studentDiscoverFragment);
        topLevelDestinations.add(R.id.tutorDiscoverFragment);
        topLevelDestinations.add(R.id.studentBidsFragment);
        topLevelDestinations.add(R.id.tutorBidsFragment);
        topLevelDestinations.add(R.id.profileFragment);
        // Use the list to create app bar configurations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        // Set up AppBar using our created configurations
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Setup Bottom Navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    // Make the back button work
    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}
