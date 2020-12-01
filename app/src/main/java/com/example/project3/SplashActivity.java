package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    ViewPager mViewPager;

    // images array
    int[] images = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences mSettings = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean newuser = mSettings.getBoolean("status", true);
        if (newuser){
            mViewPager = findViewById(R.id.viewPagerMain);
//        // Initializing the ViewPagerAdapter
            mViewPagerAdapter = new ViewPagerAdapter(SplashActivity.this, images);//
//        // Adding the Adapter to the ViewPager
            mViewPager.setAdapter(mViewPagerAdapter);
            SharedPreferences.Editor editor = mSettings.edit();
             newuser = false;
            editor.putBoolean("status", newuser);
            editor.apply();
        }
        else {
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    class ViewPagerAdapter extends PagerAdapter {

        // Context object
        Context context;

        // Array of images
        int[] images;
        int[] iconss = {R.drawable.navslider1, R.drawable.navslider2, R.drawable.navslider3};

        String [] title ={"Free","Full Feature","Best Audoio"};
        String [] desc ={"Play all music anytime, anywhere!","Enjoy music with all feature","More 1000 songs with high quality audio"};
        // Layout Inflater
        LayoutInflater mLayoutInflater;


        // Viewpager Constructor
        public ViewPagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // return the number of images
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((LinearLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // inflating the item.xml
            View itemView = mLayoutInflater.inflate(R.layout.item_slide, container, false);

            // referencing the image view from the item.xml file
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
            TextView titel =itemView.findViewById(R.id.judul);
            TextView des =itemView.findViewById(R.id.desc);
            ImageView icon =itemView.findViewById(R.id.ssicon);

            ImageButton next =itemView.findViewById(R.id.nextsplash);
            ImageButton skip =itemView.findViewById(R.id.skip);


            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position+1==images.length){
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    mViewPager.setCurrentItem(position+1);
                }
            });





            // setting the image in the imageView
            imageView.setImageResource(images[position]);
            icon.setImageResource(iconss[position]);
            titel.setText(title[position]);
            des.setText(desc[position]);

            // Adding the View
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((LinearLayout) object);
        }
    }

}