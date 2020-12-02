package com.example.project3.playerfragment;

import android.annotation.SuppressLint;
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
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.helper.LyricHelper;
import com.example.project3.utils.MusicService;
import com.lauzy.freedom.library.Lrc;
import com.lauzy.freedom.library.LrcView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.example.project3.utils.Static.LOCALINTENTFILTER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LirikFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LirikFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Lrc> lrcs;
    LrcView mLrcView;
    TextView title,artist;
    private final Handler mHandler = new Handler();
    InputStream input;
    boolean showing=false;


    public LirikFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LirikFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LirikFragment newInstance(String param1, String param2) {
        LirikFragment fragment = new LirikFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lirik, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title=view.findViewById(R.id.liriktitle);
        artist=view.findViewById(R.id.lirikartist);
        mLrcView=view.findViewById(R.id.likikview);
        showing=true;
        loadlirik();
    }
    public void loadlirik(){
        title.setText(MusicService.currentsongModel.getTitle());
        artist.setText(MusicService.currentsongModel.getArtist());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            input = new URL(MusicService.lirikurl).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lrcs = LyricHelper.parseInputStream(input);
        mLrcView.setLrcData(lrcs);

        mHandler.post(mUpdateTimeTask);
        mLrcView.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                int currentseek = (int) time;
                Intent intent = new Intent(LOCALINTENTFILTER);
                intent.putExtra("status", "seek");
                intent.putExtra("seektime",currentseek);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
        getlocalbroadcaster();

    }
    public void getlocalbroadcaster(){
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new BroadcastReceiver() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("playing")){
                    if (showing){
                        loadlirik();
                    }
                }
            }
        }, new IntentFilter(LOCALINTENTFILTER));

    }
    public void updatelirik(){
        mLrcView.updateTime(MusicService.currentduraiton);
    }

    /**
     * Background Runnable thread
     */
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
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        updatelirik();
    }
}