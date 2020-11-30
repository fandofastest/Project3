package com.example.project3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project3.fragment.HomeFragment;
import com.example.project3.helper.Dialog;
import com.example.project3.playerfragment.HomePlayerFragment;
import com.example.project3.utils.MusicService;

import static com.example.project3.utils.Static.HOME;

public class PlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar;
    ActionBar actionBar;
    MenuItem setting;
    Menu mymenu;
    public static int playerpos;
    MenuInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initToolbar();

        if (getIntent().hasExtra("fromclickplay")){
            playerpos=getIntent().getIntExtra("pos",0);
            Intent playerservice= new Intent(PlayerActivity.this, MusicService.class);
            playerservice.putExtra("pos",playerpos);
            startService(playerservice);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loadFragment(HomePlayerFragment.newInstance("",""),"");

    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        titleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleToolbar.setText("App name");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
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


    @Override
    public void onBackPressed() {
        if (titleToolbar.getText().equals(" ")){
            super.onBackPressed();
            super.onBackPressed();
        }
        else {
            super.onBackPressed();

        }

    }

    public void setTitleToolbar(String mytitle){
        titleToolbar.setText(mytitle);

    }

    public boolean loadFragment(Fragment fragment, String toolbartitle) {
        if (fragment != null) {
            titleToolbar.setText(toolbartitle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}