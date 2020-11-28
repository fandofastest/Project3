package com.example.project3.fragment.libraryfragment;

import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.MyPlaylistAdapter;
import com.example.project3.adapter.SongAdapterList;
import com.example.project3.fragment.AlbumDetailFragment;
import com.example.project3.model.MyPlaylistModel;
import com.example.project3.model.PLaylistModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.project3.utils.Static.listmyplaylist;
import static io.realm.Realm.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    ImageButton createpl;
    RecyclerView rvplaylist;
    MyPlaylistAdapter myPlaylistAdapter;

    public MyPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlaylistFragment newInstance(String param1, String param2) {
        MyPlaylistFragment fragment = new MyPlaylistFragment();
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
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvplaylist=view.findViewById(R.id.rv);
        rvplaylist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        rvplaylist.setHasFixedSize(true);
        //set data and list adapter
        myPlaylistAdapter = new MyPlaylistAdapter(context, listmyplaylist);
        myPlaylistAdapter.setOnItemClickListener(new MyPlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyPlaylistModel obj, int position) {

                ((MainActivity) getActivity()).loadFragment(MyPlaylistDetailFragment.newInstance(obj),"");


            }
        });
        rvplaylist.setAdapter(myPlaylistAdapter);

        createpl=view.findViewById(R.id.createplaylist);

        createpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog();

            }
        });




    }

    public  void showCreateDialog(){
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_createplaylist);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText inputtext =dialog.findViewById(R.id.inputplaylist);

                inputtext.setFocusable(true);
                inputtext.requestFocus();
                dialog.findViewById(R.id.submit).setOnClickListener(v -> {
                    String newplaylist =inputtext.getText().toString();
                    createPlaylist(newplaylist);
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.cancel_action).setOnClickListener(v -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show();
                });
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }



    public void createPlaylist(String playlistname){
        MyPlaylistModel myPlaylistModel= new MyPlaylistModel();
        myPlaylistModel.setName(playlistname);
        myPlaylistModel.setTotalsong(0);
        RealmHelper realmHelper = new RealmHelper(context);
        realmHelper.creatnewplaylist(myPlaylistModel);
        realmHelper.setMyRealmListener(() -> {
            Toast.makeText(context,"Success",LENGTH_LONG).show();
            myPlaylistAdapter.notifyDataSetChanged();

        });

    }




}