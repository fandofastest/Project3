package com.example.project3.helper;

import android.content.Context;
import android.content.Intent;

import com.example.project3.PlayerActivity;

public class PlayerHelper {

    public static void playmusic(Context context, int position) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("fromclickplay",true);
       context.startActivity(intent);
    }


}
