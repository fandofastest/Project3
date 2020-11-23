package com.example.project3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project3.R;
import com.example.project3.model.MyPlaylistModel;


import java.util.ArrayList;
import java.util.List;

public class MyPlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyPlaylistModel> items = new ArrayList<>();

    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    String[] colorstring = {"E92FF5","F52F82","2FF5BA","2FBAF5","F5A62F","7E51FF"};


    public interface OnItemClickListener {
        void onItemClick(MyPlaylistModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public MyPlaylistAdapter(Context context, List<MyPlaylistModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView playlistname;
        public TextView songcount;
        public View itemlayout;
        public ImageButton menuly;

        public OriginalViewHolder(View v) {
            super(v);

            image=v.findViewById(R.id.iconmyplaylist);
            playlistname=v.findViewById(R.id.myplaylistname);
            songcount=v.findViewById(R.id.totalsongs);
            itemlayout=v.findViewById(R.id.mainly);
            menuly=v.findViewById(R.id.imageButton11);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myplaylist, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyPlaylistModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.playlistname.setText(obj.getName());
            Log.e("warna", String.valueOf(position % 7));
            view.image.setColorFilter(Color.parseColor("#"+colorstring[position % 6]), PorterDuff.Mode.SRC_IN);


            if (items.size()> 1){
                view.songcount.setText(obj.getTotalsong() + " Song");
            }
            else {
                view.songcount.setText(obj.getTotalsong() + " Songs");
            }

            view.itemlayout.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {

                }
                notifyDataSetChanged();
            });

            view.menuly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    PowerMenu powerMenu = new PowerMenu.Builder(ctx)
//                            .addItem(new PowerMenuItem("Remove", false)) // aad an item list.
//                            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
//                            .setMenuRadius(10f) // sets the corner radius.
//                            .setMenuShadow(10f) // sets the shadow.
//                            .setTextColor(ContextCompat.getColor(ctx, R.color.white))
//                            .setTextGravity(Gravity.LEFT)
//                            .setTextTypeface(ResourcesCompat.getFont(ctx, R.font.ubuntu))
//                            .setSelectedTextColor(ContextCompat.getColor(ctx, R.color.blue))
//                            .setSelectedMenuColor(ContextCompat.getColor(ctx,R.color.bluesoft))
//                            .setMenuColor(ContextCompat.getColor(ctx, R.color.bluesoft))
//                            .build();
//
//                    powerMenu.setOnMenuItemClickListener((position1, item) -> {
//                        if (item.getTitle().equals("Remove")){
//                            RealmHelper realmHelper= new RealmHelper(ctx);
//                            realmHelper.deletePlaylist(obj.getId());
//                            realmHelper.setMyRealmListener(new RealmHelper.MyRealmListener() {
//                                @Override
//                                public void onsuccess() {
//                                    notifyDataSetChanged();
//                                }
//                            });
//
//                            powerMenu.dismiss();
//
//                        }
//                    });
//                    powerMenu.showAsDropDown(view); // view is an anchor

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}