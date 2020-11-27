package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.SongAdapterList;
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
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    RecyclerView recyclerView;
    SongAdapterList songAdapterList;
    List<SongModel> listsearch = new ArrayList<>();

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.rvsearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);



        if (listsearch.size()==0){
            listsearch=listtrending;
        }
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listsearch,R.layout.item_song_main,false);
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onMoreClick(SongModel position) {

            }


        });
        recyclerView.setAdapter(songAdapterList);

        SearchView searchView=view.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSong(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Handler mHandler= new Handler();
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSong(newText);
                        //Put your call to the server here (with mQueryString)
                    }
                }, 1000);
                return false;
            }
        });


    }

    void getSong(String q){
        recyclerView.removeAllViews();
        listsearch.clear();
        String url= Config.SEARCH+q;
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

                    listsearch.add(modelSong);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            songAdapterList.notifyDataSetChanged();

        }, error -> Log.e("err","test"));

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

}