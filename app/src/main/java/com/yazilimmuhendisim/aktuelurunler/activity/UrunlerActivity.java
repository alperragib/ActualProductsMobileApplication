package com.yazilimmuhendisim.aktuelurunler.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.adapter.UrunAdapter;
import com.yazilimmuhendisim.aktuelurunler.classs.Urun;

import java.util.ArrayList;

public class UrunlerActivity extends AppCompatActivity {

    public static ArrayList<Urun> urunArrayList;
    public static String marketAdi;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);
        if(urunArrayList==null || urunArrayList.isEmpty() || marketAdi==null){
            finish();
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(marketAdi+" İndirimli Ürünler");
        }

        recyclerView = findViewById(R.id.recyclerView_urunler);
        UrunAdapter adapter = new UrunAdapter(this,urunArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
