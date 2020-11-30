package com.example.project3.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.bullhead.equalizer.EqualizerModel;
import com.bullhead.equalizer.Settings;
import com.example.project3.R;
import com.example.project3.model.SongModel;
import com.example.project3.notifservice.CreateNotification;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MusicService extends Service {
    public static String lirikurl;

    public  static String PLAYERSTATUS="STOP",REPEAT="OFF",SHUFFLE="OFF",CURRENTTYPE="OFF";
    public static int totalduration,currentduraiton,currentpos;
    String from;
    public static SongModel currentsongModel;
    Realm realm;
    public  static int sessionId;
    public static List<SongModel> currentlist = new ArrayList<>();
    public static Equalizer mEqualizer;
    public static BassBoost bassBoost;
    public static PresetReverb presetReverb;
    EqualizerModel equalizerModel;
    public static String ctitle;
    //player
    private MediaPlayer mp = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("pause")){
                    CreateNotification.createNotification(getApplicationContext(), currentlist.get(currentpos),
                            R.drawable.ic_play_notif, currentpos, currentlist.size()-1);
                    mp.pause();
                    PLAYERSTATUS="PAUSE";
                }
                else if (status.equals("resume")){
                    CreateNotification.createNotification(getApplicationContext(), currentlist.get(currentpos),
                            R.drawable.ic_pause_notif, currentpos, currentlist.size()-1);
                    PLAYERSTATUS="PLAYING";
                    mp.start();
                }
                else  if (status.equals("seek")){
                    int seek = intent.getIntExtra("seektime",0);
                    mp.pause();
                    mp.seekTo(seek);
                    mp.start();
                }
                else if (status.equals("stopmusic")){
                    PLAYERSTATUS="STOPING";
                    mp.release();
                }
                else if (status.equals("getduration")){
                    totalduration=mp.getDuration();
                    currentduraiton=mp.getCurrentPosition();
                }
                else if (status.equals("next")){
                    playsong(currentpos+1);
                }

                else if (status.equals("prev")){
                    playsong(currentpos-1);
                }
                else if (status.equals("settimer")){
                    Long end= intent.getLongExtra("end",0);
                    new CountDownTimer(end, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            PLAYERSTATUS="STOPING";
                            mp.release();
                        }
                    }.start();
                }
            }
        }, new IntentFilter("musicplayer"));

        registerReceiver(broadcastReceiver, new IntentFilter("app3"));



    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    playsong(currentpos-1);
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (PLAYERSTATUS.equals("PLAYING")){
                        Intent newintent = new Intent("musicplayer");
                        newintent.putExtra("status", "pause");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newintent);
                        CreateNotification.createNotification(getApplicationContext(), currentlist.get(currentpos),
                                R.drawable.ic_play_notif, currentpos, currentlist.size()-1);
                        mp.pause();
                        PLAYERSTATUS="PAUSE";
                    } else {
                        Intent newintent = new Intent("musicplayer");
                        newintent.putExtra("status", "playing");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newintent);
                        CreateNotification.createNotification(getApplicationContext(), currentlist.get(currentpos),
                                R.drawable.ic_pause_notif, currentpos, currentlist.size()-1);
                        PLAYERSTATUS="PLAYING";
                        mp.start();
                    }
                    break;
                case CreateNotification.ACTION_CLOSE:
                    mp.stop();
                    mp.release();

                    break;
                case CreateNotification.ACTION_NEXT:
                    playsong(currentpos+1);
                    break;
            }
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initrealm();
        playsong(intent.getIntExtra("pos",0));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void playsong(int pos){

        if (pos==currentlist.size()-1){
            pos=0;
        }
        else if (pos==-1){
            pos=currentlist.size()-1;
        }
        currentpos=pos;
        CreateNotification.createNotification(getApplicationContext(), currentlist.get(pos),
                R.drawable.ic_pause_notif, currentpos, currentlist.size()-1);

        try {
            currentsongModel =currentlist.get(pos);
            ctitle=currentsongModel.getTitle();
            lirikurl=currentsongModel.getLyric();

            Intent intent = new Intent("musicplayer");
            intent.putExtra("status", "prepare");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            mp.stop();
            mp.reset();
            mp.release();
            Uri myUri = Uri.parse(currentsongModel.getSongurl());
            mp = new MediaPlayer();
            mp.setDataSource(this, myUri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp1) {
                    if (REPEAT.equals("ON")){
                        playsong(currentpos);
                    }
                    else if (SHUFFLE.equals("ON")){
                        int pos= (int) (Math.random() * (currentlist.size()));
                        playsong(pos);
                    }
                    else {

                        playsong(currentpos+1);
                    }
                }
            });
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onPrepared(MediaPlayer mplayer) {
                    RealmHelper realmHelper = new RealmHelper(getApplication());
                    realmHelper.saverecent(currentsongModel);
                    sessionId=mp.getAudioSessionId();
                    if (mplayer.isPlaying()) {
                        mp.pause();
                    } else {
                        mp.start();
                        PLAYERSTATUS="PLAYING";
                        Intent intent = new Intent("musicplayer");
                        intent.putExtra("status", "playing");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        setEqualizer(sessionId);
                    }
                }
            });
            mp.prepareAsync();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void  initrealm(){
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        realm = Realm.getInstance(configuration);
    }

    public void setEqualizer(int audioSesionId){
        try {
            
            mEqualizer = new Equalizer(0, audioSesionId);
            bassBoost = new BassBoost(0, audioSesionId);
            bassBoost.setEnabled(true);
            BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
            BassBoost.Settings bassBoostSetting     = new BassBoost.Settings(bassBoostSettingTemp.toString());
            bassBoostSetting.strength = Settings.equalizerModel.getBassStrength();
            bassBoost.setProperties(bassBoostSetting);
            presetReverb = new PresetReverb(0, audioSesionId);
            presetReverb.setPreset(Settings.equalizerModel.getReverbPreset());
            presetReverb.setEnabled(true);

            mEqualizer.setEnabled(true);
            if (Settings.presetPos == 0){
                for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
                    mEqualizer.setBandLevel(bandIdx, (short) Settings.seekbarpos[bandIdx]);
                }
            }
            else {
                mEqualizer.usePreset((short) Settings.presetPos);
            }
        }

        catch (Exception e){
            Log.e("TAG", "setEqualizer: "+e.getMessage() );
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
