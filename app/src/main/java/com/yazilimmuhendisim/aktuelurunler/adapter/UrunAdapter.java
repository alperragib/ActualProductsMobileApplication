package com.yazilimmuhendisim.aktuelurunler.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.classs.Urun;
import com.yazilimmuhendisim.aktuelurunler.sqlite.FavUrunlerDAO;
import com.yazilimmuhendisim.aktuelurunler.sqlite.VeritabaniYardimcisi;

import java.util.List;

public class UrunAdapter extends RecyclerView.Adapter<UrunAdapter.CardViewTasarimTutucu>{
    private Context context;
    private List<Urun> urunList;
    private VeritabaniYardimcisi vt;

    public UrunAdapter(Context context, List<Urun> urunList){
        this.context=context;
        this.urunList=urunList;
        vt = new VeritabaniYardimcisi(context);
    }

    public class CardViewTasarimTutucu extends RecyclerView.ViewHolder{
        public ImageView urunImage;
        public CardView urunCard;
        public FloatingActionButton fav_button;
        public TextView baslik,aciklama,fiyat,gelme_tarihi;


        public CardViewTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            urunImage = itemView.findViewById(R.id.urun_imageView);
            urunCard = itemView.findViewById(R.id.urun_cardView);
            fav_button = itemView.findViewById(R.id.urun_fab_fav_button);
            baslik = itemView.findViewById(R.id.urun_baslik_textView);
            aciklama = itemView.findViewById(R.id.urun_aciklama_textView);
            gelme_tarihi = itemView.findViewById(R.id.urun_gelme_tarihi_textView);
            fiyat = itemView.findViewById(R.id.urun_fiyat);

        }
    }

    @NonNull
    @Override
    public CardViewTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.urun_card_tasarim,parent,false);
        return new CardViewTasarimTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewTasarimTutucu holder, int position) {

        String[] ay = { "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

        final Urun urun = urunList.get(position);

        holder.urunImage.setImageBitmap(urun.getUrun_gorseli());
        holder.setIsRecyclable(false);
        holder.baslik.setText(urun.getBaslik());
        holder.aciklama.setText(urun.getAciklama());
        holder.fiyat.setText(urun.getFiyat()+" Tl");

        try {
            String[] tarih_split = urun.getGelme_tarihi().trim().split("\\.");
            holder.gelme_tarihi.setText("• Bu ürün "+tarih_split[0]+" "+ay[Integer.parseInt(tarih_split[1].trim())-1]+" "+tarih_split[2]+" tarihinden itibaren mağazada olacaktır.");
        }catch (Exception e){
            holder.gelme_tarihi.setText("• Bu ürün "+urun.getGelme_tarihi().trim()+" tarihinden itibaren mağazada olacaktır.");
        }


        if(new FavUrunlerDAO(context).urunFavmi(vt,urun.getId())){
            holder.fav_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
            holder.fav_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_white));
        }else{
            holder.fav_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorWhite)));
            holder.fav_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_accent));
        }
        holder.fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new FavUrunlerDAO(context).urunFavmi(vt,urun.getId())){
                    new FavUrunlerDAO(context).urunSil(vt,urun.getId());
                    Toast.makeText(context,urun.getBaslik()+" favorilerden kaldırıldı.",Toast.LENGTH_SHORT).show();
                }else{
                    new FavUrunlerDAO(context).urunEkle(vt,urun);
                    Toast.makeText(context,urun.getBaslik()+" favorilere eklendi.",Toast.LENGTH_SHORT).show();
                }
                if(new FavUrunlerDAO(context).urunFavmi(vt,urun.getId())){
                    holder.fav_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
                    holder.fav_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_white));

                }else{
                    holder.fav_button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorWhite)));
                    holder.fav_button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_accent));

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return urunList.size();
    }
}
