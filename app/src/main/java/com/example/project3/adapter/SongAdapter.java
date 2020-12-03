package com.example.project3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.model.SongModel;
import com.example.project3.utils.Ads;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SongModel> items = new ArrayList<>();
    int  selectedKey =-1;
    Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onMoreClick(SongModel position);
    }
    int menu;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public SongAdapter(Context context, List<SongModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public View lyt_parent;
        public TextView artistname;
        public TextView songtitle;
        public ImageView albumcover,artistcover;
        public ImageView songactive;
        public ImageButton more;
        public TextView no;
        public TextView duration;
        public TextView plays;
        public OriginalViewHolder(View v) {
            super(v);

            songtitle=v.findViewById(R.id.titlesong);
            artistname=v.findViewById(R.id.artistname);
            albumcover=v.findViewById(R.id.imageView);
            artistcover=v.findViewById(R.id.imageView2);
            duration=v.findViewById(R.id.textView8);
//            songactive=v.findViewById(R.id.songactive);
            lyt_parent=v.findViewById(R.id.mainly);
            plays=v.findViewById(R.id.textView7);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending, parent, false);
        vh = new SongAdapter.OriginalViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SongModel obj = items.get(position);
        if (holder instanceof SongAdapter.OriginalViewHolder) {
            final SongAdapter.OriginalViewHolder view = (SongAdapter.OriginalViewHolder) holder;

            Glide
                    .with(ctx)
                    .load(obj.getImageurl())
                    .centerCrop()
                    .into(view.artistcover);
            Glide
                    .with(ctx)
                    .load(obj.getAlbumcover())
                    .centerCrop()
                    .into(view.albumcover);
            view.artistname.setText(obj.getArtist());
            view.songtitle.setText(obj.getTitle());
            view.duration.setText(obj.getDuration());
            view.plays.setText(obj.getPlays()+" Plays");
            if (!(selectedKey == -1)) {
                if (position != selectedKey) {
//                    view.songactive.setVisibility(View.GONE);
//                    view.songtitle.setTextColor(ContextCompat.getColor(ctx, R.color.white));

                } else {
//                    view.songactive.setVisibility(View.VISIBLE);
//                    view.songtitle.setTextColor(ContextCompat.getColor(ctx, R.color.purple_200));

                }
            }

//            if (menu==0){
//                view.more.setVisibility(View.GONE);
//            }
//            view.more.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PopupMenu popup = new PopupMenu(ctx,view , Gravity.CENTER, R.style.PopupMenu, R.style.PopupMenu);
//                    popup.getMenuInflater().inflate(R.menu.playlist_menu, popup.getMenu());
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        public boolean onMenuItemClick(MenuItem item) {
//                            if (item.getTitle().equals("Remove")) {
//                                mOnItemClickListener.onMoreClick(obj);
//                                notifyDataSetChanged();
//                            }
//                            return true;
//                        }
//                    });
//                    popup.show();
//                }
//            });


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    selectedKey = position;
                    notifyDataSetChanged();

                    if (mOnItemClickListener != null) {
                        Ads ads = new Ads(ctx,true);
                        ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                            @Override
                            public void onAdsfinish() {
                                mOnItemClickListener.onItemClick(position);

                            }
                        });

                    }
                }
            });
//            Tools.displayImageOriginal(ctx, view.imageView, obj.getImageurl());


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}