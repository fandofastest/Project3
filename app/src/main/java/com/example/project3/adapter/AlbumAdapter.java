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

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AlbumModel> items = new ArrayList<>();

    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, AlbumModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AlbumAdapter(Context context, List<AlbumModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,artist;
        public TextView artistname;
        public TextView albumname;
        public TextView years;
        public View itemlayout;
        public OriginalViewHolder(View v) {
            super(v);

            image=v.findViewById(R.id.songimage);
            artist=v.findViewById(R.id.artisimage);
            albumname=v.findViewById(R.id.playlist);
            itemlayout=v.findViewById(R.id.mainly);
            years=v.findViewById(R.id.years);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AlbumModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
//            Glide
//                    .with(ctx)
//                    .load(obj.getImageUrl())
//                    .centerCrop()
//                    .into(view.image);
            view.image.setImageResource(R.drawable.albumimgnew);
            view.artist.setImageResource(R.drawable.artisnew);
            view.albumname.setText(obj.getAlbumName());
            view.years.setText(obj.getYears());
            if (obj.getYears()==null){
                view.years.setText("2020");
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