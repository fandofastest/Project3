package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.helper.PlayerHelper;
import com.example.project3.model.PLaylistModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.Config;
import com.example.project3.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.MusicService.currentlist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String PLAYLIST = "playlist";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView playlistname,totalsong;
    ImageView playlistcover;
    Context context;
    RecyclerView recyclerView;
    ImageButton playbut,libbut;
    SongAdapterList songAdapterList;
    List<SongModel> listsong= new ArrayList<>();
    StickyScrollView stickyScrollView;
    PLaylistModel pLaylistModel;

    public PlaylistDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PlaylistDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistDetailFragment newInstance(String param1, PLaylistModel pLaylistModel) {
        PlaylistDetailFragment fragment = new PlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(PLAYLIST, pLaylistModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        context=getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            pLaylistModel = bundle.getParcelable(PLAYLIST); // Key
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlistcover=view.findViewById(R.id.albumimage);
        playbut=view.findViewById(R.id.buttonplaymusic);
        libbut=view.findViewById(R.id.libbutton);
        playlistname=view.findViewById(R.id.playlistname);
        totalsong=view.findViewById(R.id.textView17);
        playlistname.setText(pLaylistModel.getName());
        totalsong.setText(String.valueOf(pLaylistModel.getTotalsong()));
        Glide
                .with(context)
                .load(pLaylistModel.getImgurl())
                .centerCrop()
                .into(playlistcover);

        playbut.setOnClickListener(v -> {
            PlayerHelper.playmusic(context, Tools.getRandomNumber(0,listsong.size()));
            currentlist=listsong;

        });

        recyclerView=view.findViewById(R.id.rvplaylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listsong,R.layout.item_song_main,true,getActivity());
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PlayerHelper.playmusic(context, position);
                currentlist=listsong;

            }

            @Override
            public void onMoreClick(SongModel position) {

            }

            @Override
            public void onCheckboxselected(int total) {

            }


        });
        recyclerView.setAdapter(songAdapterList);
        getSong(mParam1);
        stickyScrollView=view.findViewById(R.id.scrolll);
        stickyScrollView.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(int i, int i1, int i2, int i3) {
                Log.e("onScrollChanged", "onScrollChanged: "+i1 );
                if (i1<50){
                    recyclerView.setNestedScrollingEnabled(false);
                    playbut.setVisibility(View.VISIBLE);
                    playlistcover.setVisibility(View.VISIBLE);
                    ConstraintLayout constraintLayout = view.findViewById(R.id.boxtitle);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.START,R.id.buttonplaymusic,ConstraintSet.START,0);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.TOP,R.id.buttonplaymusic,ConstraintSet.BOTTOM,30);
                    constraintSet.applyTo(constraintLayout);

                }
                else {
                    recyclerView.setNestedScrollingEnabled(true);
                    playlistcover.setVisibility(View.GONE);
                    ConstraintLayout constraintLayout = view.findViewById(R.id.boxtitle);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.START,R.id.buttonplaymusic,ConstraintSet.END,30);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.TOP,R.id.buttonplaymusic,ConstraintSet.TOP,0);
                    constraintSet.applyTo(constraintLayout);

                }

            }

            @Override
            public void onScrollStopped(boolean b) {


            }
        });

    }
    void getSong(String q){
        recyclerView.removeAllViews();
        listsong.clear();
        String url= Config.DETAILPLAYLIST+q;
        Log.e("getSong", "getSong: "+url );
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("playlist");
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
                    listsong.add(modelSong);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            songAdapterList.notifyDataSetChanged();

        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
}