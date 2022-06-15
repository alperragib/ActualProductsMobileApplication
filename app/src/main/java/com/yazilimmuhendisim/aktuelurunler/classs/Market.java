package com.yazilimmuhendisim.aktuelurunler.classs;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Market {

        private int id;
        private String adi;
        private Bitmap logo;
        private ArrayList<Urun> urunler;

    public Market(int id, String adi, Bitmap logo, ArrayList<Urun> urunler) {
        this.id = id;
        this.adi = adi;
        this.logo = logo;
        this.urunler = urunler;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public ArrayList<Urun> getUrunler() {
        return urunler;
    }

    public void setUrunler(ArrayList<Urun> urunler) {
        this.urunler = urunler;
    }
}
