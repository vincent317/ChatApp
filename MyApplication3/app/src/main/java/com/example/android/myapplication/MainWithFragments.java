package com.example.android.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainWithFragments extends AppCompatActivity {

    private final FragmentManager fm = getSupportFragmentManager();
    private final Fragment chatFragment = new chatFragment();
    private final Fragment friendFragment = new friendFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.container, chatFragment, "2").hide(chatFragment).commit();
        fm.beginTransaction().add(R.id.container,friendFragment, "1").commit();
    }

    //为导航栏注册
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()) {
                case R.id.nav_chat:
                    fm.beginTransaction().hide(friendFragment).show(chatFragment).commit();
                    return true;
                case R.id.nav_friends:
                    fm.beginTransaction().hide(chatFragment).show(friendFragment).commit();
                    return true;
            }
            return false;
        }
    };
}

