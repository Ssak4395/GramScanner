package com.example.bar_code_scanner.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.bar_code_scanner.MainView;
import com.example.bar_code_scanner.model.CoilMaster;
import com.example.bar_code_scanner.model.Scan;
import com.example.bar_code_scanner.utilities.Helper;
import com.example.bar_code_scanner.view_model.ScanViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class JSoup extends AsyncTask<Object, Object, Object> {

    String url;
    ScanViewModel scanViewModel;
    HashMap<String,String> hashMap;
    private  Context context;
    boolean searchFailed = false;
    private String choice;

    public JSoup(String urlToConnect, Context context, ScanViewModel scanViewModel,String choice)
    {
        this.url = urlToConnect;
        this.context = context;
        this.scanViewModel = scanViewModel;
        this.choice = choice;
    }

    @Override
    protected Object doInBackground(Object... objects) {
         hashMap = new HashMap();

        try {
            OkHttpClient okHttp = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            Document document = Jsoup.parse(okHttp.newCall(request).execute().body().string());
            hashMap.put("CoilID", document.getElementById("summary").select("tbody").select("tr").select("td").get(0).text());
            hashMap.put("Gauge", document.getElementById("summary").select("tbody").select("tr").select("td").get(2).text());
            hashMap.put("Width", document.getElementById("summary").select("tbody").select("tr").select("td").get(3).text());
            hashMap.put("Type", document.getElementById("summary").select("tbody").select("tr").select("td").get(6).text());
            hashMap.put("Color", document.getElementById("summary").select("tbody").select("tr").select("td").get(7).text());
            hashMap.put("Status","Not Found");
            hashMap.put("Weight", document.getElementById("summary").select("tbody").select("tr").select("td").get(12).text());



        } catch (IOException e) {
            e.printStackTrace();
            searchFailed = true;
        }

        return hashMap;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            if(isAlreadyScanned(hashMap.get("CoilID")))
            {
                Toast.makeText(context,"Coil has already been scanned",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else
            {
                Scan scan = new Scan(hashMap.get("CoilID"),hashMap.get("Type"),Double.parseDouble(hashMap.get("Weight").replace("kg","")), hashMap.get("Status"),hashMap.get("Color"));
                scan.setTimeScanned(Calendar.getInstance().getTime());
                scanViewModel.insert(scan);
                Intent intent = new Intent(context, MainView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Helper.vibrateDevice(context);
                intent.putExtra("choice",choice);
                context.startActivity(intent);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean isAlreadyScanned(String coilID) throws ExecutionException, InterruptedException {
        Scan scan = scanViewModel.getSpecificScan(coilID);
        return scan != null;
    }
}
