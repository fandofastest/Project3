package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.example.project3.R;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.helper.PlayerHelper;
import com.example.project3.model.SongModel;
import com.example.project3.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.MusicService.currentlist;
import static com.example.project3.utils.Static.listnewmusic;
import static com.example.project3.utils.Static.listtrending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    StickyScrollView stickyScrollView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    RecyclerView rvnewest;
    ImageButton playrandom;
    SongAdapterList songAdapterList;
    public NewestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewestFragment newInstance(String param1, String param2) {
        NewestFragment fragment = new NewestFragment();
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
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playrandom=view.findViewById(R.id.imageButton7);
        rvnewest=view.findViewById(R.id.rv);
        rvnewest.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        rvnewest.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listnewmusic,R.layout.item_song_main,true,getActivity());
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PlayerHelper.playmusic(context,position);
                currentlist=listnewmusic;

            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        rvnewest.setAdapter(songAdapterList);
        stickyScrollView=view.findViewById(R.id.scrolll);
        stickyScrollView.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(int i, int i1, int i2, int i3) {
                Log.e("onScrollChanged", "onScrollChanged: "+i1 );
                if (i1<50){
                    rvnewest.setNestedScrollingEnabled(false);

                }
                else {
                    rvnewest.setNestedScrollingEnabled(true);

                }

            }

            @Override
            public void onScrollStopped(boolean b) {


            }
        });

        playrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerHelper.playmusic(context, Tools.getRandomNumber(0,listnewmusic.size()));
                currentlist=listnewmusic;

            }
        });


    }

}