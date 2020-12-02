package com.example.project3.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.kaopiz.kprogresshud.KProgressHUD;


public class Ads {

      String admobinter="ca-app-pub-3940256099942544/1033173712";
     String admobbanner="ca-app-pub-3940256099942544/6300978111";
   public  static   String appid="ca-app-pub-3940256099942544~3347511713";
    AdView mAdView;
    Context context;
    com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    KProgressHUD hud;

    public interface MyCustomObjectListener {
         void onAdsfinish();
    }

    private MyCustomObjectListener listener;

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    public Ads(Context context, Boolean loadinter) {
        this.context = context;
        if (loadinter){
            hud = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading")
                    .setCancellable(true)
                    .setDetailsLabel("Please Wait")
                    .setMaxProgress(100)
                    .show();
            showinteradmob(this.context,admobinter);
        }
    }

    public  void showinteradmob(final Context context, String inter) {
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
                if (hud.isShowing()){
                    hud.dismiss();
                }

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                hud.dismiss();
                if (hud.isShowing()){
                    hud.dismiss();
                }
                listener.onAdsfinish();

                Log.e("adsss", "onAdFailedToLoad: "+loadAdError );


            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                listener.onAdsfinish();
                if (hud.isShowing()){
                    hud.dismiss();
                }
            }
        });


    }

    public  void ShowBannerAds(LinearLayout mAdViewLayout, Display display) {
        mAdViewLayout.removeAllViews();
        mAdView  = new AdView(context);
        AdSize adSize = getAdSize(context,display);
        mAdView.setAdSize(adSize);
        mAdView.setAdUnitId(admobbanner);
        AdRequest.Builder builder = new AdRequest.Builder();
        mAdView.loadAd(builder.build());
//        mAdViewLayout.addView(mAdView);
            mAdViewLayout.addView(mAdView);
    }


    private AdSize getAdSize(Context context, Display display) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }


}