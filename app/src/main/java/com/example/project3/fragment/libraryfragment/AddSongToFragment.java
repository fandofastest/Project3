package com.example.project3.fragment.libraryfragment;

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
import android.widget.Button;
import android.widget.ImageButton;

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.helper.PlayerHelper;
import com.example.project3.model.SongModel;
import com.example.project3.utils.RealmHelper;

import java.util.List;

import static com.example.project3.utils.MusicService.currentlist;
import static com.example.project3.utils.Static.listnewmusic;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSongToFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSongToFragment extends Fragment {

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
    StickyScrollView stickyScrollView;
    Button added;
    ImageButton selectall;

    public AddSongToFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSongToFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSongToFragment newInstance(String param1, String param2) {
        AddSongToFragment fragment = new AddSongToFragment();
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
        return inflater.inflate(R.layout.fragment_add_song_to, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        added= view.findViewById(R.id.addtosong);
        added.setVisibility(View.GONE);

        selectall=view.findViewById(R.id.selectall);
        recyclerView=view.findViewById(R.id.rv);
        stickyScrollView=view.findViewById(R.id.sticky);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listnewmusic,R.layout.item_song_checkbox,true,getActivity());

        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
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
                    added.setVisibility(View.VISIBLE);
                    added.setText("Add "+total+" To Playlist");
            }

        });
        recyclerView.setAdapter(songAdapterList);

        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper realmHelper=  new RealmHelper(context);




                for (int i = 0; i <songAdapterList.getListselected().size() ; i++) {

                    SongModel songModel= songAdapterList.getListselected().get(i);
                    realmHelper.saveplaylists(songModel,mParam1);

                    realmHelper.setMyRealmListener(new RealmHelper.MyRealmListener() {
                        @Override
                        public void onsuccess() {

                            Log.e("zxzx", "onsuccess: " );

                        }

                        @Override
                        public void onsuccessdata(List<SongModel> list) {

                        }
                    });



                }
                ((MainActivity)getActivity()).backfragment();

            }
        });
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songAdapterList.isSelectedAll()){
                    songAdapterList.unselectall();
                }
                else {
                    songAdapterList.selectAll();
                }
            }
        });

    }
}