package com.example.bar_code_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.bar_code_scanner.asynctasks.JSoup;
import com.example.bar_code_scanner.model.CoilMaster;
import com.example.bar_code_scanner.model.Scan;
import com.example.bar_code_scanner.utilities.Helper;
import com.example.bar_code_scanner.view_model.CoilMasterViewModel;
import com.example.bar_code_scanner.view_model.ScanViewModel;
import com.google.zxing.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * TO-DO by Developer.
 */
public class ScannerActivity extends AppCompatActivity  {
private CodeScanner codeScanner;
private CodeScannerView scannerView;
private CoilMasterViewModel coilMasterViewModel;
private ScanViewModel scanViewModel;
private TextView resultData;
private String choice;
private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannerView);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        choice = getIntent().getExtras().getString("choice");
        coilMasterViewModel = ViewModelProviders.of(this).get(CoilMasterViewModel.class);
        scanViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        resultData = findViewById(R.id.resultsOfQr);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());
                        String[] splitResultString = result.getText().split("\\.");
                        Intent intent1;
                        String id = splitResultString[0];

                        if(result.getText().contains("http://"))
                        {
                            id = resolveIDFromURL(result.getText());

                            try {
                                Scan scan = scanViewModel.getSpecificScan(id);
                                if(scan == null)
                                {
                                    Toast.makeText(mContext,"Could not find in database, searching internet",Toast.LENGTH_SHORT).show();
                                    new JSoup(result.getText(),mContext,scanViewModel,choice).execute(new Object());
                                }
                                else
                                {
                                    Helper.buildAlertDialog(mContext,"Alert!","Item has already been scanned, please delete","Okay");
                                    Helper.vibrateDevice(mContext);
                                    intent1 = new Intent(mContext,MainView.class);
                                    intent1.putExtra("choice",choice);
                                    startActivity(intent1);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        try {
                            CoilMaster checkIfExist = coilMasterViewModel.getSpecificCoilFromDB(id);
                            if(isAlreadyScanned(checkIfExist))
                            {
                                Helper.buildAlertDialog(mContext,"Alert!","Item has already been scanned, please delete","Okay");
                                intent1 = new Intent(mContext,MainView.class);
                                intent1.putExtra("choice",choice);
                                startActivity(intent1);
                            }
                            else {
                                Scan scan = new Scan(checkIfExist.getCOILID(), checkIfExist.getTYPE(), checkIfExist.getWEIGHT(), checkIfExist.getSTATUS(), checkIfExist.getCOLOR());
                                Date currentTime = Calendar.getInstance().getTime();
                                scan.setTimeScanned(currentTime);
                                scanViewModel.insert(scan);
                                Helper.vibrateDevice(mContext);
                                intent1 = new Intent(mContext,MainView.class);
                                intent1.putExtra("choice",choice);
                                startActivity(intent1);
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    private String resolveIDFromURL(String urlToSplit)
    {
       String[] spliterator = urlToSplit.split("=");
       String idToFindString = spliterator[1];
       String[] containsID = idToFindString.split("\\.");
       String myID = containsID[0];
       return myID;
    }

   private boolean isAlreadyScanned(CoilMaster coilMaster) throws ExecutionException, InterruptedException {
       Scan scan = scanViewModel.getSpecificScan(coilMaster.getCOILID());
       return scan != null;
    }
}