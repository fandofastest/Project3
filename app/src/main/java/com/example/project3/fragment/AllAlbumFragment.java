package com.example.project3.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.AlbumAdapter;
import com.example.project3.model.AlbumModel;
import com.example.project3.utils.Ads;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.Static.listalbum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    Context context;
    StickyScrollView stickyScrollView;
    public AllAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllAlbumFragment newInstance(String param1, String param2) {
        AllAlbumFragment fragment = new AllAlbumFragment();
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
        return inflater.inflate(R.layout.fragment_all_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setHasFixedSize(false);
        LinearLayout bannerlayout=view.findViewById(R.id.banner_container);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Ads ads = new Ads(context,false);
        ads.ShowBannerAds(bannerlayout,display);
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
        albumAdapter = new AlbumAdapter(context, listalbum,R.layout.item_album_allalbum);
        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlbumModel obj, int position) {
                ((MainActivity) getActivity()).loadFragment(AlbumDetailFragment.newInstance(obj.getId(),obj),"",true);

            }


        });
        recyclerView.setAdapter(albumAdapter);


    }


}