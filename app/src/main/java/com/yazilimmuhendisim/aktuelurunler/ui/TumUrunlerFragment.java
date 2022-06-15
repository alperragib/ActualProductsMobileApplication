package com.yazilimmuhendisim.aktuelurunler.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.activity.SplashActivity;
import com.yazilimmuhendisim.aktuelurunler.adapter.TumUrunAdapter;
import com.yazilimmuhendisim.aktuelurunler.classs.Urun;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class TumUrunlerFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Urun> urunArrayList;
    private TumUrunAdapter adapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tum_urunler, container, false);
        context = getContext();
        recyclerView = root.findViewById(R.id.recyclerView_tum_urunler);

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        getUrunler();

        return root;
    }

    private void getUrunler(){
        try {
            urunArrayList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(dahiliOku("tum_urunler.txt"));
            JSONArray liste = jsonObject.getJSONArray("marketler");

            for(int i=0;i<liste.length();i++){

                JSONObject l = liste.getJSONObject(i);

                int id = l.getInt("id");
                String adi = l.getString("adi");

                JSONArray urunlerArray = l.getJSONArray("urunler");

                for(int j=0;j<urunlerArray.length();j++)
                {
                    JSONObject c = urunlerArray.getJSONObject(j);
                    int u_id = c.getInt("id");
                    String u_baslik = c.getString("baslik");
                    String u_aciklama = c.getString("aciklama");
                    String u_fiyat = c.getString("fiyat");
                    int u_market_id = c.getInt("market_id");
                    String u_gelme_tarihi = c.getString("gelme_tarihi");
                    Bitmap urun_gorseli_bitmap = urunOkuBitmap(String.valueOf(u_id));

                    Urun urun = new Urun(u_id,u_baslik,u_aciklama,u_fiyat,urun_gorseli_bitmap,u_market_id,u_gelme_tarihi);
                    urunArrayList.add(urun);
                }
            }
            Collections.sort(urunArrayList, Urun.SortIdDESC);
            adapter = new TumUrunAdapter(getContext(),urunArrayList);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemViewCacheSize(10);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);


        }catch (Exception e){
            Toast.makeText(context,"Bir hata oluştu!",Toast.LENGTH_SHORT).show();
        }

    }
    public String dahiliOku(String fileName){
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader okuyucu = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String satir = "";

            while ((satir = okuyucu.readLine()) != null){
                sb.append(satir+"\n");
            }
            okuyucu.close();
            return sb.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private Bitmap urunOkuBitmap(String urun_id){
        try {
            String filename = "urun_"+urun_id+".png";
            File file = context.getFileStreamPath(filename);
            if(file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                return BitmapFactory.decodeFile(file.getPath(), options);
            }else{
                if(getActivity()!=null){
                    getActivity().finish();
                    Intent intentM = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intentM);
                }
                return null;
            }
        }catch (Exception e){
            if(getActivity()!=null){
                getActivity().finish();
                Intent intentM = new Intent(getActivity(), SplashActivity.class);
                startActivity(intentM);
            }
            return null;
        }

    }

}
