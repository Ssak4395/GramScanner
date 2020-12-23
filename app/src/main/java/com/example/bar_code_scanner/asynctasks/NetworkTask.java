package com.example.bar_code_scanner.asynctasks;
import android.content.Context;
import android.os.AsyncTask;
import com.example.bar_code_scanner.model.Scan;
import com.example.bar_code_scanner.utilities.Helper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NetworkTask extends AsyncTask<Void, Void,Void> {

    public List<Scan> scansToCommit;
    private Context context;
    boolean hasFailed = false;
    String choice = "";

    public NetworkTask(List<Scan> scansToCommit, Context context,String choice)
    {
        this.scansToCommit = scansToCommit;
        this.context = context;
        this.choice = choice;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();

        System.out.println("Value passed to async task " + choice);

        if(choice == "Transfer")
        {
            for(int i = 0; i< scansToCommit.size(); ++i)
            {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,scansToCommit.get(i).getCOILDID() + "," + Helper.dateToSQLString(scansToCommit.get(i).getTimeScanned()));
                Request request = new Request.Builder()
                        .url("https://localhost:44321/api/v1/Transfer-Hume-Coil")
                        .post(body)
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    hasFailed = true;

                }
            }
        }
        else
            for(int i = 0; i< scansToCommit.size(); ++i)
            {
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("id", scansToCommit.get(i).getCOILDID())
                        .addFormDataPart("date", Helper.dateToSQLString(scansToCommit.get(i).getTimeScanned())).build();
                Request request = new Request.Builder()
                        .url("http://192.168.0.213:8080/api/v1/procedure/Coil-Factory-Skin")
                        .post(requestBody)
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    hasFailed = true;

                }
            }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (hasFailed)
        {
            Helper.buildAlertDialog(context,"Alert!","GramAPI is down OR Error has occurred, try again later","Okay");
        }
        else{
            Helper.buildAlertDialog(context,"Success","Database has been successfully updated","Okay");
        }
    }
}
