    package com.example.project3.fragment.libraryfragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.PlaylistAdapter;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.fragment.PlaylistDetailFragment;
import com.example.project3.model.MyPlaylistModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.project3.utils.Static.listplaylist;

    /**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlaylistDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlaylistDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MYPLAYLIST = "myplaylist";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView pltitle;
    MyPlaylistModel myPlaylistModel;
    SongAdapterList songAdapterList;
    Context context;
    RecyclerView recyclerView;
    ImageView nosong;
    ImageButton addsong;
    List<SongModel> listsong = new ArrayList<>();
    public MyPlaylistDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyPlaylistDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlaylistDetailFragment newInstance(MyPlaylistModel myPlaylistModel) {
        MyPlaylistDetailFragment fragment = new MyPlaylistDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MYPLAYLIST, myPlaylistModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myPlaylistModel = bundle.getParcelable(MYPLAYLIST); // Key
        }
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_playlist_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nosong=view.findViewById(R.id.nosong);
        addsong=view.findViewById(R.id.addsong);
        pltitle=view.findViewById(R.id.playlistname);
        pltitle.setText(myPlaylistModel.getName());
        recyclerView=view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        //set data and list adapter
        songAdapterList = new SongAdapterList(context, listsong,R.layout.item_song_main,true,getActivity());
        songAdapterList.setOnItemClickListener(new SongAdapterList.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onMoreClick(SongModel position) {

            }
        });
        recyclerView.setAdapter(songAdapterList);

        getsong();
    }

    void getsong(){

        RealmHelper realmHelper = new RealmHelper(context);
       listsong= realmHelper.getSongsbyPlaylistid(String.valueOf(myPlaylistModel.getId()));
       if (listsong.size()>0 ){
           nosong.setVisibility(View.GONE);
           addsong.setVisibility(View.GONE);
       }
       songAdapterList.notifyDataSetChanged();

    }


}