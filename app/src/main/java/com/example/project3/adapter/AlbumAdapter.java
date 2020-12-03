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
import com.example.project3.model.AlbumModel;
import com.example.project3.utils.Ads;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AlbumModel> items = new ArrayList<>();
    int layout;
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(AlbumModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AlbumAdapter(Context context, List<AlbumModel> items,int layout) {
        this.items = items;
        this.layout=layout;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,artist;
        public TextView artistname;
        public TextView albumname;
        public TextView years;
        public View itemlayout;
        public TextView plays;
        public TextView no;
        public OriginalViewHolder(View v) {
            super(v);

            image=v.findViewById(R.id.songimage);
            artist=v.findViewById(R.id.artisimage);
            albumname=v.findViewById(R.id.playlist);
            itemlayout=v.findViewById(R.id.mainly);
            years=v.findViewById(R.id.years);
            plays=v.findViewById(R.id.totalplays);
            no=v.findViewById(R.id.textView12);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AlbumModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            Glide
                    .with(ctx)
                    .load(obj.getImageUrl())
                    .centerCrop()
                    .into(view.image);
            Glide
                    .with(ctx)
                    .load(obj.getArtistcover())
                    .centerCrop()
                    .into(view.artist);
            view.albumname.setText(obj.getAlbumName());
            view.years.setText(obj.getYears());
            view.plays.setText(obj.getPlays()+" Plays");
            if (obj.getYears()==null){
                view.years.setText("2020");
            }
            view.no.setText(String.valueOf(position+1));
            view.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        Ads ads = new Ads(ctx,true);
                        ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                            @Override
                            public void onAdsfinish() {
                                mOnItemClickListener.onItemClick(obj,position);

                            }
                        });

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