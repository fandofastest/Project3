package com.example.project3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.model.PLaylistModel;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PLaylistModel> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;



    public interface OnItemClickListener {
        void onItemClick(View view, PLaylistModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PlaylistAdapter(Context context, List<PLaylistModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView playlistname;
        public TextView songcount;
        public View itemlayout;

        public OriginalViewHolder(View v) {
            super(v);

            image=v.findViewById(R.id.songimage);
            playlistname=v.findViewById(R.id.playlist);
            songcount=v.findViewById(R.id.totalsong);
            itemlayout=v.findViewById(R.id.mainly);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_home, parent, false);
        vh = new PlaylistAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PLaylistModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
//            Glide
//                    .with(ctx)
//                    .load(obj.getImgurl())
//                    .centerCrop()
//                    .into(view.image);


            view.playlistname.setText(obj.getName());

            if (obj.getTotalsong() < 1 ){
                view.songcount.setText(obj.getTotalsong() +" Song");
            }
            else {
                view.songcount.setText(String.valueOf(obj.getTotalsong())+" Songs");
            }



            view.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}