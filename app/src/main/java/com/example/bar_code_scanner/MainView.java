package com.example.bar_code_scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bar_code_scanner.asynctasks.JSoup;
import com.example.bar_code_scanner.asynctasks.NetworkTask;
import com.example.bar_code_scanner.model.CoilMaster;
import com.example.bar_code_scanner.model.Scan;
import com.example.bar_code_scanner.utilities.Helper;
import com.example.bar_code_scanner.utilities.Scan_Adapter;
import com.example.bar_code_scanner.view_model.CoilMasterViewModel;
import com.example.bar_code_scanner.view_model.ScanViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Displays the main dashboard to the user, contains a recycler view of all their scans.
 */
public class MainView extends AppCompatActivity {

    private ScanViewModel scanViewModel;
    private Context context = this;
    private Button scan;
    private RecyclerView recyclerView;
    private CoilMasterViewModel coilMasterViewModel;
    private ImageView update;
    private String choice;


    /**
     * Starts the user interface, sets up the scene.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        initView();
        choice = getIntent().getExtras().getString("choice");
                                                 //Inits the view objects, for example button, recycler adapter, etc etc
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scanViewModel.getAllScans().getValue().size() == 0) { Helper.buildAlertDialog(context,"Alert!","Please add scans to start","Okay");}
                else startAPICalls(scanViewModel.getAllScans().getValue());}
        });
        scanViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        coilMasterViewModel = ViewModelProviders.of(this).get(CoilMasterViewModel.class);//Access the View Model, the main abstraction of our database.
         recyclerView = findViewById(R.id.recycler_items); //Recyclerview to be displayed.
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //Sets the layout manager
        recyclerView.setHasFixedSize(true); //Sets the fixed size to true,
        Scan_Adapter scan_adapter = new Scan_Adapter(context,scanViewModel,coilMasterViewModel);
        recyclerView.setAdapter(scan_adapter); //Set the adapter, the adapter is used to interface with our custom layout.
        scanViewModel.getAllScans().observe(this, new Observer<List<Scan>>() {
            @Override
            public void onChanged(List<Scan> scans) {
                scan_adapter.setScans(scans); //Displays all the scans in our recyclerview.
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.vibrateDevice(context);
                scanCode();


            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void initView()
    {
        update = findViewById(R.id.update);
        scan = findViewById(R.id.scan_button);

    }


    private void startAPICalls(List<Scan> scans)
    {
        scans = scanViewModel.getAllScans().getValue();
        new NetworkTask(scans,this,choice).execute();
    }


    private void scanCode()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptActivity.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Please scan a barcode");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null)
        {
            if(result.getContents() != null)
            {
                Log.d("Result","The result content found is " + result.getContents());
                String[] splitResultString = result.getContents().split("\\.");
                Intent intent1;
                String id = splitResultString[0];

                if(result.getContents().contains("http://"))
                {
                    id = resolveIDFromURL(result.getContents());

                    try {
                        Scan scan = scanViewModel.getSpecificScan(id);
                        if(scan == null)
                        {
                            Toast.makeText(this,"Could not find in database, searching internet",Toast.LENGTH_SHORT).show();
                            new JSoup(result.getContents(),this,scanViewModel,choice).execute(new Object());
                        }
                        else
                        {
                            Toast.makeText(this,"This item has already been scanned, please delete and try again",Toast.LENGTH_LONG).show();
                            Helper.vibrateDevice(this);
                            intent1 = new Intent(this,MainView.class);
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
                            Toast.makeText(this,"This item has already been scanned, please delete and try again",Toast.LENGTH_LONG).show();
                            intent1 = new Intent(this,MainView.class);
                            intent1.putExtra("choice",choice);
                            startActivity(intent1);
                        }
                        else {
                            Scan scan = new Scan(checkIfExist.getCOILID(), checkIfExist.getTYPE(), checkIfExist.getWEIGHT(), checkIfExist.getSTATUS(), checkIfExist.getCOLOR());
                            Date currentTime = Calendar.getInstance().getTime();
                            scan.setTimeScanned(currentTime);
                            scanViewModel.insert(scan);
                            Helper.vibrateDevice(this);
                            intent1 = new Intent(this,MainView.class);
                            intent1.putExtra("choice",choice);
                            startActivity(intent1);
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
            }
            else
            {
                Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();

            }
            super.onActivityResult(requestCode, resultCode, data);

    }


    private boolean isAlreadyScanned(CoilMaster coilMaster) throws ExecutionException, InterruptedException {
        Scan scan = scanViewModel.getSpecificScan(coilMaster.getCOILID());
        return scan != null;
    }
    private String resolveIDFromURL(String urlToSplit)
    {
        String[] spliterator = urlToSplit.split("=");
        String idToFindString = spliterator[1];
        String[] containsID = idToFindString.split("\\.");
        String myID = containsID[0];
        return myID;
    }
}