package com.yazilimmuhendisim.aktuelurunler.classs;

import android.graphics.Bitmap;

import java.util.Comparator;

public class Urun {

        private int id;
        private String baslik;
        private String aciklama;
        private String fiyat;
        private Bitmap urun_gorseli;
        private int market_id;
        private String gelme_tarihi;

    public Urun(int id, String baslik, String aciklama, String fiyat, Bitmap urun_gorseli, int market_id, String gelme_tarihi) {
        this.id = id;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.fiyat = fiyat;
        this.urun_gorseli = urun_gorseli;
        this.market_id = market_id;
        this.gelme_tarihi = gelme_tarihi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public Bitmap getUrun_gorseli() {
        return urun_gorseli;
    }

    public void setUrun_gorseli(Bitmap urun_gorseli) {
        this.urun_gorseli = urun_gorseli;
    }

    public int getMarket_id() {
        return market_id;
    }

    public void setMarket_id(int market_id) {
        this.market_id = market_id;
    }

    public String getGelme_tarihi() {
        return gelme_tarihi;
    }

    public void setGelme_tarihi(String gelme_tarihi) {
        this.gelme_tarihi = gelme_tarihi;
    }
    public static Comparator<Urun> SortIdDESC = new Comparator<Urun>() {

        public int compare(Urun s1, Urun s2) {

            int rollno1 = s1.getId();
            int rollno2 = s2.getId();
            return rollno2-rollno1;
        }};
}
