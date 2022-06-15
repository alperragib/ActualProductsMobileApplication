package com.yazilimmuhendisim.aktuelurunler.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yazilimmuhendisim.aktuelurunler.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private int toplam_inecek=0,toplam_inen=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        if(dahiliOku("tum_urunler.txt")==null){
            tumUrunlerKaydet();
        }else{
            checkDatabase();
        }

    }
    private void startActivityMainDelay(int miliSecond) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        new Timer().schedule(task, miliSecond);
    }


    public void dahiliYaz(String json,String fileName){
        try {
            FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
            OutputStreamWriter yazici = new OutputStreamWriter(fos);
            yazici.write(json);
            yazici.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String dahiliOku(String fileName){
        try {
            FileInputStream fis = openFileInput(fileName);
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
    public void dahiliSil(String fileName){

        try {
            File yol = getFilesDir();
            File file = new File(yol,fileName);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void tumUrunlerKaydet() {
            String url = "https://yazilimmuhendisim.net/api/database_p2/tum_urunler.php";
            StringRequest istek = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(response.equals("ERR")){
                        AlertGoster();
                    }
                    else{
                        dahiliSil("tum_urunler.txt");
                        dahiliYaz(response,"tum_urunler.txt");
                        gorselleriKaydet(response);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertGoster();
                }
            }){
                protected Map<String,String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<>();
                    params.put("pass","AlperBaba024");
                    return params;
                }
            };
            istek.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            istek.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
            Volley.newRequestQueue(this).add(istek);
    }

    private void gorselleriKaydet(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray liste = jsonObject.getJSONArray("marketler");

            for(int i=0;i<liste.length();i++){

                JSONObject l = liste.getJSONObject(i);

                int id = l.getInt("id");
                String logo_url = l.getString("logo_url");

                String filename = "market_logo_"+id+".png";
                File fileMarket = getFileStreamPath(filename);
                if(fileMarket == null || !fileMarket.exists()) {
                    new DownloadTask(filename).execute(stringToURL(logo_url));
                    toplam_inecek++;
                }

                /*
                File fileMarket = getFileStreamPath(filename);
                if(fileMarket == null || !fileMarket.exists()) {
                    Bitmap bitmap_m = getBitmapFromURL(logo_url);
                    FileOutputStream fosUrun = openFileOutput(filename,MODE_PRIVATE);
                    if (bitmap_m != null) {
                        bitmap_m.compress(Bitmap.CompressFormat.PNG, 90, fosUrun);
                    }
                    fosUrun.close();
                }
*/

                JSONArray urunlerArray = l.getJSONArray("urunler");

                for(int j=0;j<urunlerArray.length();j++)
                {
                    JSONObject c = urunlerArray.getJSONObject(j);
                    int u_id = c.getInt("id");
                    String u_urun_gorseli_url = c.getString("urun_gorseli_url");

                    String filenameUrun = "urun_"+u_id+".png";
                    File fileUrun = getFileStreamPath(filenameUrun);
                    if(fileUrun == null || !fileUrun.exists()) {
                        new DownloadTask(filenameUrun).execute(stringToURL(u_urun_gorseli_url));
                        toplam_inecek++;
                    }

                }
            }

            if(toplam_inecek==0){
                startActivityMainDelay(10);
            }

        }catch (Exception e){
            dahiliSil("tum_urunler.txt");
            AlertGoster();
        }

    }



    private void checkDatabase(){

        String url = "https://yazilimmuhendisim.net/api/database_p1/alan_testi/check_network.php";

        StringRequest istek = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("OK")){
                    tumUrunlerKaydet();
                }else{
                    startActivityMainDelay(1000);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivityMainDelay(1000);

            }
        });
        istek.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        istek.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        Volley.newRequestQueue(this).add(istek);
    }
    public void AlertGoster(){

        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.setTitle("Aktüel Ürünler");
        alertDialog.setMessage("Uygulamayı ilk kez açtığınızda internet bağlantısına ihtiyaç duyar. Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tamam",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(dahiliOku("tum_urunler.txt")==null){
                            tumUrunlerKaydet();
                        }else{
                            checkDatabase();
                        }
                    }
                });
        if(!isFinishing()){
            alertDialog.show();
        }

    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        String filename;
        protected void onPreExecute(){

        }

        public DownloadTask (String g_filename){
            filename = g_filename;
        }
        protected Bitmap doInBackground(URL...urls){

            URL url = urls[0];
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);

            }catch(IOException e){

            }finally{
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }
        protected void onPostExecute(Bitmap result){
            if(result!=null){
                try {
                    File file = getFileStreamPath(filename);
                    if(file == null || !file.exists()) {
                        FileOutputStream fos = openFileOutput(filename,MODE_PRIVATE);
                        result.compress(Bitmap.CompressFormat.PNG, 50, fos);
                        fos.close();
                    }
                    toplam_inen++;
                }catch (Exception e){
                    dahiliSil("tum_urunler.txt");
                    AlertGoster();
                }
            }else {
                dahiliSil("tum_urunler.txt");
                AlertGoster();
            }

            if(toplam_inecek==toplam_inen){
                startActivityMainDelay(10);
            }
        }
    }
    protected URL stringToURL(String urlString){
        try{
            return new URL(urlString);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
