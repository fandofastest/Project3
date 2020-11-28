package com.example.project3.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project3.R;
import com.example.project3.helper.Dialog;
import com.example.project3.model.SongModel;
import com.example.project3.utils.RealmHelper;
import com.example.project3.utils.Tools;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.List;

public class SongAdapterList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SongModel> items = new ArrayList<>();
    int  selectedKey =-1;
    Context ctx;
    Activity activity;
    private OnItemClickListener mOnItemClickListener;
    int layout;
    boolean paddingfirst;
    List<SongModel> listselected = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onMoreClick(SongModel position);
    }
    int menu;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public List<SongModel> getListselected() {
        return listselected;
    }

    public SongAdapterList(Context context, List<SongModel> items, int layout, boolean paddingfirst, Activity activity) {
        this.items = items;
        this.layout=layout;
        this.paddingfirst=paddingfirst;
        this.activity=activity;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public View lyt_parent;
        public TextView artistname;
        public TextView songtitle;
        public  TextView totalplays;
        public TextView dura;
        public ImageView imageView;
        public ImageView songactive,lirik;
        public ImageButton more;
        public TextView no;
        public CheckBox checkBox;
        public OriginalViewHolder(View v) {
            super(v);
            lirik=v.findViewById(R.id.imageView5);
            totalplays=v.findViewById(R.id.totalplays);
            dura=v.findViewById(R.id.duration);
            songactive=v.findViewById(R.id.playbar);
            songtitle=v.findViewById(R.id.titlesong);
            artistname=v.findViewById(R.id.artist);
            imageView=v.findViewById(R.id.imageView3);
            lyt_parent=v.findViewById(R.id.mainly);
            more=v.findViewById(R.id.more);
            no=v.findViewById(R.id.no);
            checkBox=v.findViewById(R.id.checkBox);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        SongModel obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            if (paddingfirst){
                if (position==0){
                    view.lyt_parent.setPadding(0,50,0,0);
                }
                else {
                    view.lyt_parent.setPadding(0,0,0,0);

                }
            }

            Glide
                    .with(ctx)
                    .load(obj.getImageurl())
                    .centerCrop()
                    .into(view.imageView);

            try {
                view.artistname.setText(obj.getArtist());
                view.no.setText(Tools.parsenumber(position+1));

            }
            catch (Exception e){
                System.out.println(e);

            }





            if (!(layout==R.layout.item_song_list_home)) {

                if (layout == R.layout.item_song_checkbox) {
                    view.songtitle.setText(obj.getTitle());
                    view.totalplays.setText(obj.getPlays() + " Plays");
                    view.dura.setText(obj.getDuration());

                    view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                listselected.add(obj);
                                Log.e("xxxxxxx", "added: "+obj.getId() );
                            }
                            else {
                                listselected.remove(obj);
                                Log.e("xxxxxxx", "remove: "+obj.getId() );
                            }
                        }
                    });


                } else {

                    view.songtitle.setText(obj.getTitle());
                    view.songactive.setVisibility(View.INVISIBLE);
                    view.totalplays.setText(obj.getPlays() + " Plays");
                    view.dura.setText(obj.getDuration());
                    if (!(selectedKey == -1)) {
                        if (position != selectedKey) {
                            view.songactive.setVisibility(View.INVISIBLE);
                            view.songtitle.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                            view.totalplays.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                            view.dura.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                            view.lirik.setImageResource(R.drawable.ic_lirikgrey);


                        } else {
                            view.songactive.setVisibility(View.VISIBLE);
                            view.songtitle.setTextColor(ContextCompat.getColor(ctx, R.color.merah));
                            view.totalplays.setTextColor(ContextCompat.getColor(ctx, R.color.merah));
                            view.dura.setTextColor(ContextCompat.getColor(ctx, R.color.merah));
                            view.lirik.setImageResource(R.drawable.lirik);


                        }
                    }

                    view.more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PowerMenuItem myitem = null;
                            RealmHelper realmHelper = new RealmHelper(ctx);

                            boolean isfav = realmHelper.checkIsFav(obj.getId());

                            if (isfav) {
                                myitem = new PowerMenuItem("Favorite", true);
                            } else {
                                myitem = new PowerMenuItem("Add to Favorite", false);
                            }

                            PowerMenu powerMenu = new PowerMenu.Builder(ctx)
                                    .addItem(myitem) // add an item.
                                    .addItem(new PowerMenuItem("Add to Playlist", false)) // aad an item list.
                                    .addItem(new PowerMenuItem("Share", false)) // aad an item list.
                                    .addItem(new PowerMenuItem("Rate this App", false)) // aad an item list.
                                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                                    .setMenuRadius(10f) // sets the corner radius.
                                    .setMenuShadow(10f) // sets the shadow.
                                    .setTextColor(ContextCompat.getColor(ctx, R.color.white))
                                    .setTextGravity(Gravity.LEFT)
                                    .setTextTypeface(ResourcesCompat.getFont(ctx, R.font.nsregular))
                                    .setSelectedTextColor(ContextCompat.getColor(ctx, R.color.merah))
                                    .setMenuColor(ContextCompat.getColor(ctx, R.color.maincolour))
                                    .setSelectedMenuColor(ContextCompat.getColor(ctx, R.color.maincolour))
                                    .build();

                            powerMenu.setOnMenuItemClickListener((position1, item) -> {
                                if (position1 == 0) {

                                    realmHelper.actionfav(obj, isfav);
                                    notifyDataSetChanged();
                                } else if (position1 == 1) {
//                            ((MainActivity)ctx).showPlaylist(String.valueOf(obj.getId()));

                                } else if (position1 == 2) {
                                    Dialog.sharedialog(ctx);

                                } else if (position1 == 3) {

                                    Dialog.ratedialog(ctx, activity);

                                }

                                powerMenu.dismiss();
                            });
                            powerMenu.showAsDropDown(view);
                        }
                    });

                }
            }




            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    selectedKey = position;
                    notifyDataSetChanged();

                    if (mOnItemClickListener != null) {
                        if (layout==R.layout.item_song_checkbox){

                        }
                        else {
                            mOnItemClickListener.onItemClick(position);

                        }

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