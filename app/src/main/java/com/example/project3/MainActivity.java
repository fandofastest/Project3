package com.example.project3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project3.fragment.DiscoverFragment;
import com.example.project3.fragment.HomeFragment;
import com.example.project3.fragment.LibraryFragment;
import com.example.project3.fragment.libraryfragment.MyPlaylistFragment;
import com.example.project3.helper.Dialog;
import com.example.project3.utils.RealmHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import static com.example.project3.utils.Static.listfav;
import static com.example.project3.utils.Static.listmyplaylist;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar;
    ActionBar actionBar;
    MenuItem setting;
    Menu mymenu;
    MenuInflater inflater;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initbottomnavigation();
        loadFragment(HomeFragment.newInstance("", ""), getString(R.string.app_name));

        getdblist();

    }

    void getdblist(){
        RealmHelper realmHelper = new RealmHelper(MainActivity.this);
        listfav=realmHelper.getSongsbyPlaylistid("1");

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem newitem) {

        if (newitem.getTitle().equals("Setting")){
            Context ctx=MainActivity.this;
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
                    Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
                else if (position1==1){
                    Dialog.privacydisclaimer("title","isi",MainActivity.this);
                }
                else if (position1==2){
                    Dialog.privacydisclaimer("title","isi",MainActivity.this);
                }
                else if (position1==3){
                    Dialog.sharedialog(MainActivity.this);
                }
               else if (position1==4){
                    Dialog.ratedialog(MainActivity.this,MainActivity.this);
                }


            });
            powerMenu.showAsAnchorRightBottom(toolbar);

        }


        return super.onOptionsItemSelected(newitem);
    }




    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        titleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleToolbar.setText(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }




    public void backfragment(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (titleToolbar.getText().equals(getString(R.string.app_name))){
           Dialog.showExitDialog(MainActivity.this,MainActivity.this);
        }
        else {
            super.onBackPressed();
        }
    }


    public void setTitleToolbar(String mytitle){
        titleToolbar.setText(mytitle);
        if (mytitle.equals(getString(R.string.app_name))){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        else {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

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

    public boolean loadFragment(Fragment fragment,String toolbartitle) {
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
    void initbottomnavigation(){
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    loadFragment(HomeFragment.newInstance("",""),getString(R.string.app_name));
//                    titleToolbar.setText(getString(R.string.app_name));
//                    actionBar.setDisplayHomeAsUpEnabled(false);
                    setting.setVisible(false);
                    return true;
                case R.id.discover:
                    loadFragment(DiscoverFragment.newInstance("",""),"Search");
//                    actionBar.setDisplayHomeAsUpEnabled(true);
                    setting.setVisible(true);
                    return true;
                case R.id.library:
                    loadFragment(LibraryFragment.newInstance("",""),"Library");
//                    actionBar.setDisplayHomeAsUpEnabled(true);
                    setting.setVisible(true);

                    return true;
            }
            return false;
        });
    }

}