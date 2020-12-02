package com.example.project3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project3.fragment.HomeFragment;
import com.example.project3.helper.Dialog;
import com.example.project3.notifservice.CreateNotification;
import com.example.project3.playerfragment.HomePlayerFragment;
import com.example.project3.utils.MusicService;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import static com.example.project3.utils.Static.HOME;

public class PlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar;
    ActionBar actionBar;
    MenuItem setting;
    Menu mymenu;
    NotificationManager notificationManager;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
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
        actionBar.setDisplayHomeAsUpEnabled(true);
        setting = menu.findItem(R.id.action_setting);
        setting.setVisible(true);
        return true;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Music App", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem newitem) {

        if (newitem.getTitle().equals("Setting")){
            Context ctx=PlayerActivity.this;
            PowerMenu powerMenu = new PowerMenu.Builder(ctx)
                    .addItem(new PowerMenuItem("About App", false)) // aad an item list.
                    .addItem(new PowerMenuItem("Privacy Policy", false)) // aad an item list.
                    .addItem(new PowerMenuItem("Disclaimer", false)) // aad an item list.
                    .addItem(new PowerMenuItem("Share This App", false)) // aad an item list.
                    .addItem(new PowerMenuItem("Rating App", false)) // aad an item list.
                    .addItem(new PowerMenuItem("Equalizer", false)) // aad an item list.
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(ContextCompat.getColor(ctx, R.color.white))
                    .setTextGravity(Gravity.LEFT)
                    .setTextTypeface(ResourcesCompat.getFont(ctx, R.font.nsregular))
                    .setSelectedTextColor(ContextCompat.getColor(ctx, R.color.merah))
                    .setMenuColor(ContextCompat.getColor(ctx, R.color.maincolour))
                    .build();

            powerMenu.setOnMenuItemClickListener((position1, item) -> {


                if (position1==0){
                    Intent intent = new Intent(ctx,AboutActivity.class);
                    startActivity(intent);
                }
                else if (position1==1){
                    Dialog.privacydisclaimer("title","isi",ctx);
                }
                else if (position1==2){
                    Dialog.privacydisclaimer("title","isi",ctx);
                }
                else if (position1==3){
                    Dialog.sharedialog(ctx);
                }
                else if (position1==4){
                    Dialog.ratedialog(ctx,PlayerActivity.this);
                }


            });
            powerMenu.showAsAnchorRightBottom(toolbar);

        }


        return super.onOptionsItemSelected(newitem);
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