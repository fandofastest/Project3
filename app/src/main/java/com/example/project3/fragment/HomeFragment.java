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
import android.widget.ImageButton;

import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.AlbumAdapter;
import com.example.project3.adapter.PlaylistAdapter;
import com.example.project3.adapter.SongAdapter;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.model.AlbumModel;
import com.example.project3.model.PLaylistModel;
import com.example.project3.model.SongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvrecent,rvnewest,rvplaylist,rvAlbum;
    SongAdapter recentAdapter;
    SongAdapterList newestAdapter;
    PlaylistAdapter playlistAdapter;
    AlbumAdapter albumAdapter;
    Context context;
    List<SongModel> listrecent = new ArrayList<>();
    List<SongModel> listnewest = new ArrayList<>();
    List<PLaylistModel> listplaylist = new ArrayList<>();
    List<AlbumModel> listalbum = new ArrayList<>();


    ImageButton buttonmoretrending,buttonmorenewest ;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton(view);
        rvrecent=view.findViewById(R.id.recyclerViewrecent);
        rvrecent.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        rvrecent.setHasFixedSize(true);
        //set data and list adapter
        recentAdapter = new SongAdapter(context, listrecent);
        recentAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        rvrecent.setAdapter(recentAdapter);
        getRecent();



        rvnewest=view.findViewById(R.id.rvnewest);
        rvnewest.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        rvnewest.setHasFixedSize(true);
        //set data and list adapter
        newestAdapter = new SongAdapterList(context, listnewest,R.layout.item_song_list_home,false);
        newestAdapter.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        rvnewest.setAdapter(newestAdapter);
        getNewest();


        rvplaylist=view.findViewById(R.id.rvplaylist);
        rvplaylist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        rvplaylist.setHasFixedSize(true);
        //set data and list adapter
        playlistAdapter = new PlaylistAdapter(context, listplaylist);
        playlistAdapter.setOnItemClickListener((view1, obj, position) -> {


        });
        rvplaylist.setAdapter(playlistAdapter);
        getPlaylist();


        rvAlbum=view.findViewById(R.id.rvalbum);
        rvAlbum.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        rvAlbum.setHasFixedSize(true);
        //set data and list adapter
        albumAdapter = new AlbumAdapter(context, listalbum);
        albumAdapter.setOnItemClickListener((view1, obj, position) -> {


        });
        rvAlbum.setAdapter(albumAdapter);
       getAlbum();

    }


    void initButton(View view){
        buttonmoretrending=view.findViewById(R.id.buttonmoretrending);
        buttonmorenewest=view.findViewById(R.id.newestmore);

        buttonmorenewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(NewestFragment.newInstance("",""),"");

            }
        });




        buttonmoretrending.setOnClickListener(v -> {
            ((MainActivity) getActivity()).loadFragment(TrendingFragment.newInstance("",""),"");

        });

    }
    void getRecent(){
        for (int i = 0; i <100 ; i++) {
            SongModel songModel = new SongModel();
            songModel.setTitle("xxxxx");
            songModel.setArtist("artisty xxxx");
            listrecent.add(songModel);

        }
        recentAdapter.notifyDataSetChanged();
    }

    void getNewest(){
        for (int i = 0; i <100 ; i++) {
            SongModel songModel = new SongModel();
            songModel.setTitle("xxxxx");
            songModel.setArtist("artisty xxxx");
            listnewest.add(songModel);

        }
        newestAdapter.notifyDataSetChanged();
    }
    void getPlaylist(){
        for (int i = 0; i <100 ; i++) {
            PLaylistModel pLaylistModel = new PLaylistModel();
            pLaylistModel.setName("xxxxx");
            listplaylist.add(pLaylistModel);
        }
        playlistAdapter.notifyDataSetChanged();
    }

    void getAlbum(){
        for (int i = 0; i <100 ; i++) {
            AlbumModel albumModel = new AlbumModel();
            albumModel.setAlbumName("xxxxx");
            listalbum.add(albumModel);
        }
        albumAdapter.notifyDataSetChanged();
    }
}