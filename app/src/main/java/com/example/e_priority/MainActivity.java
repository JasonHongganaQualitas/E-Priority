package com.example.e_priority;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        replaceFragment(new HomeFragment());

        navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home){
                replaceFragment(new HomeFragment());
            }
            else if (item.getItemId() == R.id.action_chat){
                replaceFragment(new ChatFragment());
            }
            else if (item.getItemId() == R.id.action_profile){
                replaceFragment(new ProfileFragment());
            }
            else {
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });
    }

    private void initialize() {
        navigation = findViewById(R.id.navigation);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }
}