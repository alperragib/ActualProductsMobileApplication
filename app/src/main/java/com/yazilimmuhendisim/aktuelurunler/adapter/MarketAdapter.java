package com.yazilimmuhendisim.aktuelurunler.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.activity.UrunlerActivity;
import com.yazilimmuhendisim.aktuelurunler.classs.Market;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.CardViewTasarimTutucu>{
    private Context context;
    private List<Market> marketList;

    public MarketAdapter(Context context, List<Market> marketList){
        this.context=context;
        this.marketList=marketList;


    }

    public class CardViewTasarimTutucu extends RecyclerView.ViewHolder{
        public ImageView marketLogo;
        public CardView marketCard;
        public InterstitialAd mInterstitialAd;


        public CardViewTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            marketLogo = itemView.findViewById(R.id.market_imageView);
            marketCard = itemView.findViewById(R.id.market_cardView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int h = width/3;
            marketLogo.setMinimumHeight(h);
            marketLogo.setMinimumWidth(h);

            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {}
            });
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-3332967002509193/6422722092");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());



        }
    }

    @NonNull
    @Override
    public CardViewTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_card_tasarim,parent,false);
        return new CardViewTasarimTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewTasarimTutucu holder, int position) {
        final Market market = marketList.get(position);

        holder.marketLogo.setImageBitmap(market.getLogo());
        holder.setIsRecyclable(false);
        holder.marketCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrunlerActivity.marketAdi = market.getAdi();
                UrunlerActivity.urunArrayList = market.getUrunler();
                Intent intent_urunler = new Intent(context,UrunlerActivity.class);
                context.startActivity(intent_urunler);
                if (holder.mInterstitialAd.isLoaded()) {
                    holder.mInterstitialAd.show();
                }
            }
        });

        if(market.getId()==3){
            holder.marketCard.setCardBackgroundColor(context.getResources().getColor(R.color.colorSok));
        }
        if(market.getId()==5){
            holder.marketCard.setCardBackgroundColor(context.getResources().getColor(R.color.colorTeknosa));
        }

    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }
}
