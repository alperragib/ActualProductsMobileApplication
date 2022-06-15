package com.yazilimmuhendisim.aktuelurunler.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yazilimmuhendisim.aktuelurunler.classs.Urun;

import java.io.File;
import java.util.ArrayList;

public class FavUrunlerDAO {

    private Context context;
    public FavUrunlerDAO(Context m_context){
        this.context=m_context;
    }
    public void urunEkle(VeritabaniYardimcisi vt, Urun urun){
        SQLiteDatabase dbx = vt.getWritableDatabase();
        ContentValues degerler = new ContentValues();

        degerler.put("id",urun.getId());
        degerler.put("baslik",urun.getBaslik());
        degerler.put("aciklama",urun.getAciklama());
        degerler.put("fiyat",urun.getFiyat());
        degerler.put("market_id",urun.getMarket_id());
        degerler.put("gelme_tarihi",urun.getGelme_tarihi());

        dbx.insertOrThrow("urunler",null,degerler);
        dbx.close();
    }

    public ArrayList<Urun> tumUrunler(VeritabaniYardimcisi vt){
        ArrayList<Urun> urunArrayList = new ArrayList<>();

        SQLiteDatabase dbx = vt.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM urunler",null);

        while (c.moveToNext()){
            Urun urun = new Urun(c.getInt(c.getColumnIndex("id"))
                    ,c.getString(c.getColumnIndex("baslik"))
                    ,c.getString(c.getColumnIndex("aciklama"))
                    ,c.getString(c.getColumnIndex("fiyat"))
                    ,urunOkuBitmap(String.valueOf(c.getInt(c.getColumnIndex("id"))))
                    ,c.getInt(c.getColumnIndex("market_id"))
                    ,c.getString(c.getColumnIndex("gelme_tarihi"))
                    );
            urunArrayList.add(urun);
        }
        c.close();
        return urunArrayList;
    }

    public boolean urunFavmi(VeritabaniYardimcisi vt,int urun_id){

        SQLiteDatabase dbx = vt.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM urunler WHERE id='"+urun_id+"'",null);

        while (c.moveToNext()){

            if(c.getInt(c.getColumnIndex("id"))==urun_id){
                return true;
            }
        }
        c.close();
        return false;
    }

    public void urunSil(VeritabaniYardimcisi vt,int id){
        SQLiteDatabase dbx = vt.getWritableDatabase();
        dbx.delete("urunler","id=?",new String[]{String.valueOf(id)});
        dbx.close();
    }
    private Bitmap urunOkuBitmap(String urun_id){
        try {
            String filename = "urun_"+urun_id+".png";
            File file = context.getFileStreamPath(filename);
            if(file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                return BitmapFactory.decodeFile(file.getPath(), options);
            }
        }catch (Exception e){

        }
        return null;
    }
}
