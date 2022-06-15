package com.yazilimmuhendisim.aktuelurunler.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yazilimmuhendisim.aktuelurunler.R;
import com.yazilimmuhendisim.aktuelurunler.service.NotificationServis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_fav)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*
        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_fav);
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorTeknosa));
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(3);
         */

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3332967002509193/6422722092");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                NotificationServis.class,15, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueue(request);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.toolbar_info:
                alertGoster();
                return true;
            case R.id.toolbar_app_degerlendir:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                return true;
            case R.id.toolbar_app_paylas:
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, getResources().getString(R.string.app_name));
                    startActivity(shareIntent);

                }catch (Exception e){

                }
                return true;
            case R.id.toolbar_gorus:
                try {
                    Intent sendEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:aktuelurunlerdestek@gmail.com"));
                    sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Aktüel Ürünler Görüş Bildir");
                    startActivity(Intent.createChooser(sendEmail, "Aktüel Ürünler uygulaması hakkındaki görüşlerini mail göndererek bize iletebilirsiniz."));
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://yazilimmuhendisim.net/")));
                }
                return true;
            default:return false;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    private void alertGoster(){

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Aktüel Ürünler Uygulaması Kullanım Şartları");
        alertDialog.setMessage("\n• Uygulamanın indirilmesi ve kullanılması halinde, uygulamayı indiren ve kullanan kullanıcılar, uygulamada yer alan marketlere ait indirimli ürünlerin üçüncü şahıslar tarafından temin edilip yayınlandığını ve ürünlerde oluşabilecek hatalardan dolayı marketlerin sorumlu olmadığını kabul etmiş sayılır.");
        alertDialog.setCancelable(true);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Tamam",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(!isFinishing()){
            alertDialog.show();
        }

    }
}
