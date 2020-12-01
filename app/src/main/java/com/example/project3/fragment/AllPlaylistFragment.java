package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.AlbumAdapter;
import com.example.project3.adapter.PlaylistAdapter;
import com.example.project3.helper.SpacesItemDecoration;
import com.example.project3.model.AlbumModel;
import com.example.project3.model.PLaylistModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.Static.listplaylist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    PlaylistAdapter playlistAdapter;
    StickyScrollView stickyScrollView;
    Context context;
    public AllPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllPlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllPlaylistFragment newInstance(String param1, String param2) {
        AllPlaylistFragment fragment = new AllPlaylistFragment();
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
        return inflater.inflate(R.layout.fragment_all_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.rvallplaylist);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));

        stickyScrollView=view.findViewById(R.id.scrolll);
        stickyScrollView.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(int i, int i1, int i2, int i3) {
                Log.e("onScrollChanged", "onScrollChanged: "+i1 );
                if (i1<50){
                    recyclerView.setNestedScrollingEnabled(false);

                }
                else {
                    recyclerView.setNestedScrollingEnabled(true);

                }

            }

            @Override
            public void onScrollStopped(boolean b) {


            }
        });

        //set data and list adapter
        playlistAdapter = new PlaylistAdapter(context, listplaylist);
        playlistAdapter.setOnItemClickListener(new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PLaylistModel obj, int position) {
                ((MainActivity) getActivity()).loadFragment(PlaylistDetailFragment.newInstance(String.valueOf(obj.getId()),obj),"",true);

            }


        });
        recyclerView.setAdapter(playlistAdapter);

    }

}