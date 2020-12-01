package com.example.project3.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project3.MainActivity;
import com.example.project3.PlayerActivity;
import com.example.project3.R;
import com.example.project3.adapter.AlbumAdapter;
import com.example.project3.adapter.PlaylistAdapter;
import com.example.project3.adapter.SongAdapter;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.helper.PlayerHelper;
import com.example.project3.model.AlbumModel;
import com.example.project3.model.PLaylistModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.Config;
import com.example.project3.utils.Static;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.MusicService.currentlist;
import static com.example.project3.utils.Static.listalbum;
import static com.example.project3.utils.Static.listnewmusic;
import static com.example.project3.utils.Static.listplaylist;
import static com.example.project3.utils.Static.listtrending;

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
    SongAdapter trendingAdapter;
    SongAdapterList newestAdapter;
    PlaylistAdapter playlistAdapter;
    AlbumAdapter albumAdapter;
    Context context;
    String title ;

    ImageButton buttonmoretrending,buttonmorenewest,buttonmorealbum,buttonmoreplaylist ;

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
        title=getString(R.string.app_name);
        ((MainActivity) getActivity()).setTitleToolbar(title);


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
        trendingAdapter = new SongAdapter(context, listtrending);
        trendingAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PlayerHelper.playmusic(context,position);
                currentlist=listtrending;
            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        rvrecent.setAdapter(trendingAdapter);
        getTrending();



        rvnewest=view.findViewById(R.id.rv);
        rvnewest.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        rvnewest.setHasFixedSize(true);
        //set data and list adapter
        newestAdapter = new SongAdapterList(context, listnewmusic,R.layout.item_song_list_home,false,getActivity());
        newestAdapter.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                PlayerHelper.playmusic(context,position);
                currentlist=listnewmusic;
            }

            @Override
            public void onMoreClick(SongModel position) {

            }

            @Override
            public void onCheckboxselected(int total) {

            }


        });
        rvnewest.setAdapter(newestAdapter);
        getNewest();


        rvplaylist=view.findViewById(R.id.rvplaylist);
        rvplaylist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        rvplaylist.setHasFixedSize(true);
        //set data and list adapter
        playlistAdapter = new PlaylistAdapter(context, listplaylist);
        playlistAdapter.setOnItemClickListener(( obj, position) -> {
            ((MainActivity) getActivity()).loadFragment(PlaylistDetailFragment.newInstance(String.valueOf(obj.getId()),obj),"",true);


        });
        rvplaylist.setAdapter(playlistAdapter);
        getPlaylist();


        rvAlbum=view.findViewById(R.id.rvalbum);
        rvAlbum.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        rvAlbum.setHasFixedSize(true);
        //set data and list adapter
        albumAdapter = new AlbumAdapter(context, listalbum,R.layout.item_album);
        albumAdapter.setOnItemClickListener(( obj, position) -> {
            ((MainActivity) getActivity()).loadFragment(AlbumDetailFragment.newInstance(obj.getId(),obj),"",true);


        });
        rvAlbum.setAdapter(albumAdapter);
       getAlbum();


    }


    void initButton(View view){
        buttonmoretrending=view.findViewById(R.id.buttonmoretrending);
        buttonmorenewest=view.findViewById(R.id.newestmore);
        buttonmorealbum=view.findViewById(R.id.albummore);
        buttonmoreplaylist=view.findViewById(R.id.playlistmore);

        buttonmorenewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(NewestFragment.newInstance("",""),"",true);

            }
        });




        buttonmoretrending.setOnClickListener(v -> {
            ((MainActivity) getActivity()).loadFragment(TrendingFragment.newInstance("",""),"",true);


        });
        buttonmorealbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(AllAlbumFragment.newInstance("",""),"",true);
            }
        });

        buttonmoreplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(AllPlaylistFragment.newInstance("",""),"",true);
            }
        });

    }
    void getTrending(){
        rvrecent.removeAllViews();
        listtrending.clear();
        String url= Config.TOPCHART;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("song");
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    SongModel modelSong = new SongModel();
                    modelSong.setId(jsonObject.getInt("id"));
                    modelSong.setSongurl(jsonObject.getString("filemp3"));
                    modelSong.setLyric(jsonObject.getString("lyric"));
                    modelSong.setDuration(jsonObject.getString("duration"));
                    modelSong.setTitle(jsonObject.getString("songname"));
                    modelSong.setGenre(jsonObject.getString("genrename"));
                    modelSong.setImageurl(jsonObject.getString("songcover"));
                    modelSong.setArtist(jsonObject.getString("artistname"));
                    modelSong.setAlbum(jsonObject.getString("albumname"));
                    modelSong.setYears(jsonObject.getString("year"));
                    modelSong.setAlbumcover(jsonObject.getString("albumcover"));
                    modelSong.setPlays(jsonObject.getInt("plays"));

                    listtrending.add(modelSong);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            trendingAdapter.notifyDataSetChanged();

        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    void getNewest(){
        rvnewest.removeAllViews();
        listnewmusic.clear();
        String url= Config.NEWMUSIC;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("song");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        SongModel modelSong = new SongModel();
                        modelSong.setId(jsonObject.getInt("id"));
                        modelSong.setSongurl(jsonObject.getString("filemp3"));
                        modelSong.setLyric(jsonObject.getString("lyric"));
                        modelSong.setDuration(jsonObject.getString("duration"));
                        modelSong.setTitle(jsonObject.getString("songname"));
                        modelSong.setGenre(jsonObject.getString("genrename"));
                        modelSong.setImageurl(jsonObject.getString("songcover"));
                        modelSong.setArtist(jsonObject.getString("artistname"));
                        modelSong.setAlbum(jsonObject.getString("albumname"));
                        modelSong.setYears(jsonObject.getString("year"));
                        modelSong.setPlays(jsonObject.getInt("plays"));
                        listnewmusic.add(modelSong);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                newestAdapter.notifyDataSetChanged();

            }
        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    void getPlaylist(){
        rvplaylist.removeAllViews();
        listplaylist.clear();
        String url= Config.ALLPLAYLIST;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("playlist");
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    PLaylistModel pLaylistModel = new PLaylistModel();
                    pLaylistModel.setId(jsonObject.getInt("id"));
                    pLaylistModel.setName(jsonObject.getString("name"));
                    pLaylistModel.setImgurl(jsonObject.getString("cover"));
                    pLaylistModel.setTotalsong(jsonObject.getInt("totalsong"));
                    listplaylist.add(pLaylistModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            playlistAdapter.notifyDataSetChanged();

        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    void getAlbum(){
        rvAlbum.removeAllViews();
        listalbum.clear();
        String url= Config.ALLALBUM;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("album");
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    AlbumModel albumModel= new AlbumModel();
                    albumModel.setAlbumName(jsonObject.getString("name"));
                    albumModel.setId(String.valueOf(jsonObject.getInt("id")));
                    albumModel.setYears(jsonObject.getString("year"));
                    albumModel.setArtistName(jsonObject.getString("artist"));
                    albumModel.setImageUrl(jsonObject.getString("cover"));
                    albumModel.setPlays(jsonObject.getInt("plays"));
                    albumModel.setArtistcover(jsonObject.getString("artistcover"));



                    listalbum.add(albumModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            albumAdapter.notifyDataSetChanged();

        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}