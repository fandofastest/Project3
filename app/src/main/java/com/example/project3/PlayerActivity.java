package com.example.project3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project3.fragment.HomeFragment;
import com.example.project3.playerfragment.HomePlayerFragment;

public class PlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar;
    ActionBar actionBar;
    MenuItem setting;
    Menu mymenu;
    MenuInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initToolbar();
        loadFragment(HomePlayerFragment.newInstance("",""),"");

    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        titleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleToolbar.setText("App name");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mymenu = menu;
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, mymenu);
        actionBar.setDisplayHomeAsUpEnabled(false);

        setting = menu.findItem(R.id.action_setting);
        setting.setVisible(false);


        return true;
    }

    public boolean loadFragment(Fragment fragment, String toolbartitle) {
        if (fragment != null) {
            titleToolbar.setText(toolbartitle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.frame, fragment)
                    .addToBackStack("opsi")
                    .commit();
            return true;
        }
        return false;
    }
}