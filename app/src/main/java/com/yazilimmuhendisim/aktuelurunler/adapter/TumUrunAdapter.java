package com.yazilimmuhendisim.aktuelurunler.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class TumUrunAdapter extends RecyclerView.Adapter<TumUrunAdapter.CardViewTasarimTutucu2>{
    private Context context;
    private List<Urun> urunList;
    VeritabaniYardimcisi vt;

    public TumUrunAdapter(Context context, List<Urun> urunList){
        this.context=context;
        this.urunList=urunList;
        vt = new VeritabaniYardimcisi(context);
    }

    public class CardViewTasarimTutucu2 extends RecyclerView.ViewHolder{
        public ImageView urunImage,markaLogo;
        public CardView urunCard;
        public FloatingActionButton fav_button;
        public TextView baslik,aciklama,fiyat,gelme_tarihi;


        public CardViewTasarimTutucu2(@NonNull View itemView) {
            super(itemView);
            markaLogo = itemView.findViewById(R.id.tum_urun_fab_marka_button);
            urunImage = itemView.findViewById(R.id.tum_urun_imageView);
            urunCard = itemView.findViewById(R.id.tum_urun_cardView);
            fav_button = itemView.findViewById(R.id.tum_urun_fab_fav_button);
            baslik = itemView.findViewById(R.id.tum_urun_baslik_textView);
            aciklama = itemView.findViewById(R.id.tum_urun_aciklama_textView);
            gelme_tarihi = itemView.findViewById(R.id.tum_urun_gelme_tarihi_textView);
            fiyat = itemView.findViewById(R.id.tum_urun_fiyat);

        }
    }

    @NonNull
    @Override
    public CardViewTasarimTutucu2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tum_urun_card_tasarim,parent,false);
        return new CardViewTasarimTutucu2(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewTasarimTutucu2 holder, int position) {

        String[] ay = { "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

        final Urun urun = urunList.get(position);

        holder.markaLogo.setImageBitmap(marketLogoOkuBitmap(String.valueOf(urun.getMarket_id())));
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

    private Bitmap marketLogoOkuBitmap(String market_id){
        try {
            String filename = "market_logo_"+market_id+".png";
            File file = context.getFileStreamPath(filename);
            if(file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap original = BitmapFactory.decodeFile(file.getPath(), options);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.PNG, 80, out);
                return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            }
        }catch (Exception e){

        }
        return null;
    }
}
