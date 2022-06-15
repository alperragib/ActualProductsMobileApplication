package com.yazilimmuhendisim.aktuelurunler.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.activity.MainActivity;
import com.yazilimmuhendisim.aktuelurunler.classs.Urun;
import com.yazilimmuhendisim.aktuelurunler.sqlite.FavUrunlerDAO;
import com.yazilimmuhendisim.aktuelurunler.sqlite.VeritabaniYardimcisi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class NotificationServis extends Worker {
    private VeritabaniYardimcisi vt;
    private Context mcontext;
    public NotificationServis(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mcontext = context;
        vt = new VeritabaniYardimcisi(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        checkNotification();
        return Result.success();
    }

    private void checkNotification() {
try{
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());

    String[] dateAndTime = currentDateandTime.trim().split(" ");
    String[] date = dateAndTime[0].trim().split("\\.");
    //String[] time = dateAndTime[1].trim().split(":");
/*
        Log.e("ALPERBABA","Saat:"+time[0]);
        Log.e("ALPERBABA","Dakika:"+time[1]);
        Log.e("ALPERBABA","Gün:"+date[0]);
        Log.e("ALPERBABA","Ay:"+date[1]);
        Log.e("ALPERBABA","Yıl:"+date[2]);
*/
    ArrayList<Urun> urunArrayList = new FavUrunlerDAO(mcontext).tumUrunler(vt);

    for(int i=0;i<urunArrayList.size();i++){
        Urun urun = urunArrayList.get(i);
        String[] tarih_split = urun.getGelme_tarihi().trim().split("\\.");
        SharedPreferences prefs = mcontext.getSharedPreferences("Shared", MODE_PRIVATE);
        int idName = prefs.getInt(String.valueOf(urun.getId()), 0);
        if(tarih_split[2].trim().equals(date[2].trim()) && tarih_split[1].trim().equals(date[1].trim()) && idName==0){
            String[] ay = { "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

            if(Integer.parseInt(tarih_split[0].trim()) - Integer.parseInt(date[0].trim())==0){
                Bildirim(String.valueOf(urun.getId()),"Favoriye eklediğiniz ürün mağazada!",urun.getBaslik()+" başlıklı ürün "+tarih_split[0]+" "+ay[Integer.parseInt(tarih_split[1].trim())-1]+" "+tarih_split[2]+" tarihinde raflarda yerini alacaktır.");
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("Shared", MODE_PRIVATE).edit();
                editor.putInt(String.valueOf(urun.getId()), 1);
                editor.apply();
            }else if(Integer.parseInt(tarih_split[0].trim()) - Integer.parseInt(date[0].trim()) == 1){
                Bildirim(String.valueOf(urun.getId()),"Favoriye eklediğiniz ürün çok yakında mağazada!",urun.getBaslik()+" başlıklı ürün "+tarih_split[0]+" "+ay[Integer.parseInt(tarih_split[1].trim())-1]+" "+tarih_split[2]+" tarihinde raflarda yerini alacaktır.");
                SharedPreferences.Editor editor = mcontext.getSharedPreferences("Shared", MODE_PRIVATE).edit();
                editor.putInt(String.valueOf(urun.getId()), 1);
                editor.apply();
            }

        }
    }
}catch (Exception e){

}

    }
    private void Bildirim(String kanal_id,String baslik,String icerik)
    {
        NotificationCompat.Builder builder;
        NotificationManager bildirimYoneticisi = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(mcontext, MainActivity.class);
        intent.putExtra("bildirimKanali",1);
        PendingIntent gidilecekIntent = PendingIntent.getActivity(mcontext,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String kanalAd ="Aktüel Ürünler";
            String kanalTanim = "Aktüel Ürünler uygulaması bildirim kanalıdır.";
            int  kanalOnceligi = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel kanal = null;
            if (bildirimYoneticisi != null) {
                kanal = bildirimYoneticisi.getNotificationChannel(kanal_id);
            }

            if(kanal == null)
            {
                kanal = new NotificationChannel(kanal_id,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanim);
                if (bildirimYoneticisi != null) {
                    bildirimYoneticisi.createNotificationChannel(kanal);
                }

            }

            builder = new NotificationCompat.Builder(mcontext,kanal_id);

            builder.setContentTitle(baslik);
            //builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.ic_shopping_cart_logo);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(icerik));
        }
        else
        {
            builder = new NotificationCompat.Builder(mcontext);

            builder.setContentTitle(baslik);
            //builder.setContentText(icerik);
            builder.setSmallIcon(R.drawable.ic_shopping_cart_logo);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(icerik));
        }
        try {
            if (bildirimYoneticisi != null) {
                bildirimYoneticisi.notify(Integer.parseInt(kanal_id),builder.build());
            }
        }catch (Exception e){

        }

    }
}
