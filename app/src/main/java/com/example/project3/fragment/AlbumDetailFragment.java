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

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.model.AlbumModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.Static.listtrending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ALBUM = "album";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AlbumModel albumModel;
    ImageView artistcover,albumcover;
    Context context;
    RecyclerView recyclerView;
    ImageButton playbut,libbut;
    SongAdapterList songAdapterList;
    List<SongModel> listsong= new ArrayList<>();
    StickyScrollView stickyScrollView;
    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AlbumDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumDetailFragment newInstance(String param1, AlbumModel albumModel){
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ALBUM, albumModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ALBUM);
        }
        context=getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            albumModel = bundle.getParcelable(ALBUM); // Key
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artistcover=view.findViewById(R.id.artistalbum);
        albumcover=view.findViewById(R.id.albumimage);
        playbut=view.findViewById(R.id.buttonplaymusic);
        libbut=view.findViewById(R.id.libbutton);

        Glide
                .with(context)
                .load(albumModel.getImageUrl())
                .centerCrop()
                .into(albumcover);

        Glide
                .with(context)
                .load(albumModel.getArtistcover())
                .centerCrop()
                .into(artistcover);



        recyclerView=view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listsong,R.layout.item_song_main,true);
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onMoreClick(SongModel position) {

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
                    albumcover.setVisibility(View.VISIBLE);
                    artistcover.setVisibility(View.VISIBLE);

                    ConstraintLayout constraintLayout = view.findViewById(R.id.boxtitle);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.START,R.id.buttonplaymusic,ConstraintSet.START,0);
                    constraintSet.connect(R.id.libbutton,ConstraintSet.TOP,R.id.buttonplaymusic,ConstraintSet.BOTTOM,30);

                    constraintSet.applyTo(constraintLayout);

                }
                else {
                    recyclerView.setNestedScrollingEnabled(true);

                    albumcover.setVisibility(View.GONE);
                    artistcover.setVisibility(View.GONE);
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
        String url= Config.ALBUMDETAIL+q;
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