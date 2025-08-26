package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.prototype.tabs.EmptyTabFragment;
import com.example.prototype.tabs.MainTabFragment;
import com.example.prototype.tabs.TextCardTabFragment;
import com.example.prototype.tabs.StoriesTabFragment;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        
        // Set default fragment (main tab)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainTabFragment())
                .commit();
        }
    }
    
    private boolean onNavigationItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_empty1) {
            selectedFragment = new StoriesTabFragment();
        } else if (itemId == R.id.nav_empty2) {
            selectedFragment = EmptyTabFragment.newInstance("Empty Tab 2");
        } else if (itemId == R.id.nav_main) {
            selectedFragment = new MainTabFragment();
        } else if (itemId == R.id.nav_empty3) {
            selectedFragment = EmptyTabFragment.newInstance("Empty Tab 3");
        } else if (itemId == R.id.nav_text_card) {
            selectedFragment = new TextCardTabFragment();
        }
        
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
            return true;
        }
        
        return false;
    }
}