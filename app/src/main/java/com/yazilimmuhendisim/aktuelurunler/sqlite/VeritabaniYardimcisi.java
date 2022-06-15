package com.yazilimmuhendisim.aktuelurunler.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class VeritabaniYardimcisi extends SQLiteOpenHelper {

    public VeritabaniYardimcisi(@Nullable Context context) {
        super(context, "database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE urunler (id INTEGER, baslik TEXT,aciklama TEXT,fiyat TEXT,market_id INTEGER,gelme_tarihi TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS hedefler");
        onCreate(db);
    }

}
