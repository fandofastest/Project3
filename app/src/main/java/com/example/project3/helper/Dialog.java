package com.example.project3.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bullhead.equalizer.DialogEqualizerFragment;
import com.example.project3.BuildConfig;
import com.example.project3.MainActivity;
import com.example.project3.R;
import com.example.project3.adapter.MyPlaylistAdapter;
import com.example.project3.adapter.PlaylistAdapter;
import com.example.project3.model.MyPlaylistModel;
import com.example.project3.model.PLaylistModel;
import com.example.project3.model.SongModel;
import com.example.project3.utils.MusicService;
import com.example.project3.utils.RealmHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.project3.utils.Static.listmyplaylist;
import static io.realm.Realm.getApplicationContext;

public  class Dialog {


    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    
    
    public static void showToast(String pesan,Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.llToastBackground));
        TextView toastText = layout.findViewById(R.id.toastMessage);
        ImageView toastImage = layout.findViewById(R.id.imgType);
        toastText.setText(pesan);
        toastImage.setImageResource(R.drawable.ic_ceklis);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }




    public static void ratedialog(Context context,Activity activity){
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_rate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);
        final AppCompatRatingBar rating_bar = (AppCompatRatingBar) dialog.findViewById(R.id.rating_bar);
        ((AppCompatButton) dialog.findViewById(R.id.cancel_action)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = et_post.getText().toString().trim();
                if (review.isEmpty()) {
                    Toast.makeText(context, "Please fill review text", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("rate", String.valueOf(rating_bar.getRating()));
                    if (rating_bar.getRating()>3){
                        rateAction(activity);
                    }
                }

                dialog.dismiss();
                Toast.makeText(context, "Thanks", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void rateAction(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public  static void sharedialog(Context context){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }


  public static  void showExitDialog(Context context,Activity activity){
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.findViewById(R.id.submit).setOnClickListener(v -> {
            activity.finish();
        });
        dialog.findViewById(R.id.cancel_action).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(context, "Dismiss", Toast.LENGTH_SHORT).show();
        });
//        LinearLayout bannerlayout=dialog.findViewById(R.id.banner_container);
        Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
//        Ads ads = new Ads(this,false);
//        ads.ShowBannerAds(bannerlayout,display);
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


    public static  void addToPlaylist(Context context, Activity activity, SongModel songModel){
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_to_playlist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        RecyclerView recyclerView=dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        RealmHelper realmHelper= new RealmHelper(context);
        listmyplaylist=realmHelper.getallPlaylist();
        MyPlaylistAdapter myPlaylistAdapter = new MyPlaylistAdapter(context,listmyplaylist);
        myPlaylistAdapter.setOnItemClickListener(new MyPlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyPlaylistModel obj, int position) {
                realmHelper.saveplaylists(songModel, String.valueOf(obj.getId()));
                realmHelper.setMyRealmListener(new RealmHelper.MyRealmListener() {
                    @Override
                    public void onsuccess() {

                    }

                    @Override
                    public void onsuccessdata(List<SongModel> list) {

                    }
                });
                dialog.dismiss();
                Toast.makeText(context,"Added" +songModel.getTitle(),LENGTH_LONG).show();

            }
        });
        recyclerView.setAdapter(myPlaylistAdapter);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dialog.findViewById(R.id.button).setOnClickListener(v -> {
            dialog.dismiss();
        });

//        LinearLayout bannerlayout=dialog.findViewById(R.id.banner_container);
        Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
//        Ads ads = new Ads(this,false);
//        ads.ShowBannerAds(bannerlayout,display);
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }
   public static void privacydisclaimer(String mytitle,String myisi,Context context){
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.fragment_privacy_disclaimer);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 1000;
        lp.height =1600;
        TextView title= dialog.findViewById(R.id.title);
        TextView isi= dialog.findViewById(R.id.isi);

//        LinearLayout bannerlayout=dialog.findViewById(R.id.banner_container);
        Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
//        Ads ads = new Ads(MainActivity.this,false);
//        ads.ShowBannerAds(bannerlayout,display);

        title.setText(mytitle);
        isi.setText(myisi);

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public static void showEqialog(FragmentManager fm,Context context) {
        int sesiid= MusicService.sessionId;
        if (MusicService.PLAYERSTATUS.equals("STOP")){
            sesiid=1;
        }
        Log.e("showEqialog", "showEqialog: "+sesiid);

        DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
                .setAudioSessionId(sesiid)
                .themeColor(ContextCompat.getColor(context, R.color.maincolour))
                .textColor(ContextCompat.getColor(context, R.color.white))
                .accentAlpha(ContextCompat.getColor(context, R.color.merah))
                .darkColor(ContextCompat.getColor(context, R.color.merah))
                .setAccentColor(ContextCompat.getColor(context, R.color.merah))
                .build();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.show(fm, "fragment_edit_name");
    }

    public static void settimerdialog(Context context,FragmentManager fragmentManager) {
        TimePickerDialog mDialogHourMinute;
        Calendar calendar = Calendar.getInstance();




        mDialogHourMinute = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setCancelStringId("Cancel")
                .setSureStringId("Set")
                .setTitleStringId("Sleep Time")
                .setYearText("Year")
                .setMonthText("Month")
                .setDayText("Day")
                .setHourText("Hour")
                .setMinuteText("Minute")
                .setCyclic(false)
                .setThemeColor(context.getResources().getColor(R.color.timepicker_dialog_bg))
                .setCallBack(new OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

                        Date d = new Date(millseconds);
                        calendar.setTime(d);



                        SimpleDateFormat sdfDate = new SimpleDateFormat("HH");
                        SimpleDateFormat sdfmenit = new SimpleDateFormat("mm");
                        String jam = sdfDate.format(d);
                        String menit = sdfmenit.format(d);






                        Intent intent = new Intent("musicplayer");
                        intent.putExtra("status", "settimer");
                        intent.putExtra("end", millseconds);

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        Toast.makeText(getApplicationContext(), "Timer set : " + jam + "Hours " + menit + " Minutes", LENGTH_LONG).show();

                    }
                })
                .build();

        mDialogHourMinute.show(fragmentManager,"frament");



    }



}
