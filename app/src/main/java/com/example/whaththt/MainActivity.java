package com.example.whaththt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.whaththt.company_classes.CompanyHomeFragment;
import com.example.whaththt.company_classes.CompanyProfileFragment;
import com.example.whaththt.normal_classes.NormalHomeFragment;
import com.example.whaththt.normal_classes.NormalProfileFragment;
import com.example.whaththt.settings.SettingsFragment;
import com.example.whaththt.settings.account.SettingsAccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    String normalUser;
    Intent intent;
    String receivedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent = getIntent();
        bottomNavView = findViewById(R.id.bottomNavigationView);
        receivedString = intent.getStringExtra("normalUser");

        if (receivedString.equals("1")){
            replaceFragment(new NormalHomeFragment());
        }
        else if (receivedString.equals("2")) {
            replaceFragment(new CompanyHomeFragment());
        }




        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home)
                {
                    if (receivedString.equals("1")){
                        replaceFragment(new NormalHomeFragment());
                        return true;
                    }
                    else if (receivedString.equals("2")) {
                        replaceFragment(new CompanyHomeFragment());
                        return true;
                    }

                }
                else if (item.getItemId() == R.id.nav_settings) {
                    replaceFragment(new SettingsFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.nav_profile) {
                    if (receivedString.equals("1")){
                        replaceFragment(new NormalProfileFragment());
                        return true;
                    }
                    else if (receivedString.equals("2")) {
                        replaceFragment(new CompanyProfileFragment());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_frame_layout, fragment)
                .commit();
    }
}