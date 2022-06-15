package com.yazilimmuhendisim.aktuelurunler.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.adapter.FavUrunAdapter;
import com.yazilimmuhendisim.aktuelurunler.classs.Urun;
import com.yazilimmuhendisim.aktuelurunler.sqlite.FavUrunlerDAO;
import com.yazilimmuhendisim.aktuelurunler.sqlite.VeritabaniYardimcisi;

import java.util.ArrayList;
import java.util.Collections;

public class FavUrunlerFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static ScrollView linearLayout;
    private static ArrayList<Urun> urunArrayList;
    private static FavUrunAdapter adapter;
    private static Context context;
    private static VeritabaniYardimcisi vt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fav_urunler, container, false);
        context = getContext();
        vt = new VeritabaniYardimcisi(context);
        recyclerView = root.findViewById(R.id.recyclerView_fav_urunler);
        linearLayout = root.findViewById(R.id.scrollViewFavUrunYok);
        urunArrayList = new FavUrunlerDAO(context).tumUrunler(vt);

        if(urunArrayList.size()>0){
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.sort(urunArrayList, Urun.SortIdDESC);
            adapter = new FavUrunAdapter(context,urunArrayList);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemViewCacheSize(10);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        }else{
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
        return root;
    }

    public static void favUrunSilindi(){
        urunArrayList = new FavUrunlerDAO(context).tumUrunler(vt);

        if(urunArrayList.size()>0){
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.sort(urunArrayList, Urun.SortIdDESC);
            adapter = new FavUrunAdapter(context,urunArrayList);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemViewCacheSize(10);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        }else{
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
