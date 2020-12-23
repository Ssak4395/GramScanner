package com.example.bar_code_scanner.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bar_code_scanner.database.ScanRepository;
import com.example.bar_code_scanner.model.Scan;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScanViewModel extends AndroidViewModel {
    private ScanRepository mRepository;
    private LiveData<List<Scan>> allScans;

    public ScanViewModel (Application application) {
        super(application);
        mRepository = new ScanRepository(application);
        allScans = mRepository.getAllScans();
    }

    LiveData<List<Scan>> getAllWords() { return allScans; }

    public void insert(Scan scan)
    {
        mRepository.insert(scan);
    }

    public void delete(Scan scan)
    {
      mRepository.deleteSpecificScan(scan);
    }

    public void deleteAll()
    {
        mRepository.deleteAll();
    }

    public LiveData<List<Scan>> getAllScans()
    {
        return mRepository.getAllScans();
    }

    public Scan getSpecificScan(String id) throws ExecutionException, InterruptedException {return mRepository.getSpecificScanFromId(id);}





}
