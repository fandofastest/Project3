package com.example.project3.playerfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project3.PlayerActivity;
import com.example.project3.R;
import com.example.project3.helper.Dialog;
import com.example.project3.utils.Ads;
import com.example.project3.utils.MusicService;
import com.example.project3.utils.MusicUtills;
import com.example.project3.utils.RealmHelper;
import com.example.project3.utils.Tools;
import com.google.android.material.snackbar.Snackbar;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import static com.example.project3.utils.MusicService.ctitle;
import static com.example.project3.utils.MusicService.currentsongModel;
import static com.example.project3.utils.Static.LOCALINTENTFILTER;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final Handler mHandler = new Handler();
    ImageView cover;
    TextView songtitle,artist,curdura,totaldura;
    ImageButton play,next,prev,shuffle,repeat,eq,addpl,sleep,buttonlirik;
    Context context;
    ProgressBar progressBar;
    IndicatorSeekBar seekBar;
    View parent_view;
    MusicUtills utills= new MusicUtills();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DialogFragment newFragment;

    public HomePlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePlayerFragment newInstance(String param1, String param2) {
        HomePlayerFragment fragment = new HomePlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ((PlayerActivity) getActivity()).setTitleToolbar(" ");
        context=getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);

        initactionbutton();
        getlocalbroadcaster();
        if (MusicService.PLAYERSTATUS.equals("PLAYING")){
            artist.setText(currentsongModel.getArtist());
            songtitle.setText(currentsongModel.getTitle());
            Tools.displayImageOriginal(context,cover, currentsongModel.getImageurl());
            play.setVisibility(View.VISIBLE);
            play.setImageResource(R.drawable.ic_pause);
            progressBar.setVisibility(View.GONE);
            mHandler.post(mUpdateTimeTask);



        }

        else if (MusicService.PLAYERSTATUS.equals("PAUSE")){
            play.setVisibility(View.VISIBLE);
            play.setImageResource(R.drawable.ic_playicon);
            progressBar.setVisibility(View.GONE);
        }


        seekBar.setProgress(0);
        seekBar.setMax(MusicUtills.MAX_PROGRESS);
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if(seekParams.fromUser){
                    seekBar.setProgress(seekParams.progress);
                    double currentseek = ((double) seekParams.progress/(double)MusicUtills.MAX_PROGRESS);
                    int totaldura= (int) MusicService.totalduration;
                    int seek= (int) (totaldura*currentseek);
                    Intent intent = new Intent(LOCALINTENTFILTER);
                    intent.putExtra("status", "seek");
                    intent.putExtra("seektime",seek);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        LinearLayout bannerlayout=view.findViewById(R.id.banner_container);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Ads ads = new Ads(context,false);
        ads.ShowBannerAds(bannerlayout,display);

    }

    void initComponent(View view){
        parent_view=view.findViewById(R.id.frameLayout3);
        songtitle=view.findViewById(R.id.textView18);
        artist=view.findViewById(R.id.artistnameplayer);
        curdura=view.findViewById(R.id.currentduration);
        totaldura=view.findViewById(R.id.totalduration);
        play=view.findViewById(R.id.playbutton);
        prev=view.findViewById(R.id.prev);
        next=view.findViewById(R.id.next);
        shuffle=view.findViewById(R.id.shuffle);
        repeat=view.findViewById(R.id.repeat);
        eq=view.findViewById(R.id.imageButton3);
        addpl=view.findViewById(R.id.imageButton8);
        sleep=view.findViewById(R.id.imageButton9);
        progressBar=view.findViewById(R.id.progressBar);
        seekBar=view.findViewById(R.id.seekbar);
        cover=view.findViewById(R.id.imageView9);
        buttonlirik=view.findViewById(R.id.imageButton15);

    }


    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            // Running this thread after 10 milliseconds
            if (MusicService.PLAYERSTATUS.equals("PLAYING")) {
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void updateTimerAndSeekbar() {
        Intent intent = new Intent(LOCALINTENTFILTER);
        intent.putExtra("status", "getduration");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        curdura.setText(utills.milliSecondsToTimer(MusicService.currentduraiton));
        totaldura.setText(utills.milliSecondsToTimer(MusicService.totalduration));
        // Updating progress bar
        int progress = (int) (utills.getProgressSeekBar(MusicService.currentduraiton,MusicService.totalduration));
        seekBar.setProgress(progress);
    }



    public void pause (){
        play.setImageResource(R.drawable.ic_playicon);
        Intent intent = new Intent(LOCALINTENTFILTER);
        intent.putExtra("status", "pause");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void resume (){

        play.setImageResource(R.drawable.ic_pause);
        Intent intent = new Intent(LOCALINTENTFILTER);
        intent.putExtra("status", "resume");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);
    }
    void initactionbutton(){

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.settimerdialog(context,getFragmentManager());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MusicService.PLAYERSTATUS.equals("PLAYING")){
                    pause();
                }
                else {
                    resume();
                }
            }
        });
        eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.showEqialog(getFragmentManager(),context);
//                Ads ads = new Ads(MusicPlayerActivity.this,true);
//                ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
//                    @Override
//                    public void onAdsfinish() {
//

//                showEqialog();
//
//                    }
//                });

            }
        });
        buttonlirik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = LirikFragment.newInstance("Lirik","coba");
                newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialoglirikFragmentTheme);
                newFragment.show(getFragmentManager(),"lirik");
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.SHUFFLE.equals("OFF")){
                    MusicService.SHUFFLE="ON";
//                    shuffle.setVisibility(View.VISIBLE);
                }
                else {
                    MusicService.SHUFFLE="OFF";
//                    shuffle.setVisibility(View.GONE);
                }
                Snackbar.make(parent_view, "Shuffle" +MusicService.SHUFFLE, Snackbar.LENGTH_SHORT).show();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.REPEAT.equals("OFF")){
                    MusicService.REPEAT="ON";
//                    repeat.setVisibility(View.VISIBLE);
                }
                else {
                    MusicService.REPEAT="OFF";
//                    repeat.setVisibility(View.GONE);
                }
                Snackbar.make(parent_view, "Repeat" +MusicService.REPEAT, Snackbar.LENGTH_SHORT).show();
            }
        });
        addpl.setOnClickListener(view -> {
            Dialog.addToPlaylist(context,getActivity(),currentsongModel);
//            ((PlayerActivity) getActivity()).showPlaylist(String.valueOf(currentsongModel.getId()));

        });

    }


    public void next (){
        curdura.setText("");
        totaldura.setText("");
        songtitle.setText("Please Wait");
        artist.setText("");
        play.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = new Intent(LOCALINTENTFILTER);
        intent.putExtra("status", "next");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }
    public void prev (){
        curdura.setText("");
        totaldura.setText("");
        songtitle.setText("Please Wait");
        artist.setText("");
        play.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(LOCALINTENTFILTER);
        intent.putExtra("status", "prev");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }

    public void getlocalbroadcaster(){
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("playing")){
                    progressBar.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    play.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);

                }
                else if (status.equals("prepare")){
                    play.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
////                    albumnanme.setText(MusicService.currentalbumname);
                    artist.setText(currentsongModel.getArtist());
                    songtitle.setText(currentsongModel.getTitle());
                    Tools.displayImageOriginal(context,cover, currentsongModel.getImageurl());
                }
                else if (status.equals("pause")){
                    play.setVisibility(View.VISIBLE);
                    play.setImageResource(R.drawable.ic_playicon);
                }

            }
        }, new IntentFilter(LOCALINTENTFILTER));

    }
}