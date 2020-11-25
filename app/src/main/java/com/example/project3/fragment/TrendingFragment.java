package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.project3.R;
import com.example.project3.adapter.SongAdapter;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.model.SongModel;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.Static.listtrending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    RecyclerView rvtrending;
    SongAdapterList songAdapterList;
    CarouselView carouselView;
    int[] imageslider = {R.drawable.imageslider, R.drawable.imageslider, R.drawable.imageslider, R.drawable.imageslider, R.drawable.imageslider};

    public TrendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
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
        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initcarousel(view);
        rvtrending=view.findViewById(R.id.rvfragmenttrending);
        rvtrending.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        rvtrending.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listtrending,R.layout.item_song_main,true);
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        rvtrending.setAdapter(songAdapterList);
    }


    void initcarousel(View view){
        carouselView = view.findViewById(R.id.carouselView);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
               imageView.setImageResource(imageslider[position]);
            }
        });
        carouselView.setPageCount(imageslider.length);
    }


}