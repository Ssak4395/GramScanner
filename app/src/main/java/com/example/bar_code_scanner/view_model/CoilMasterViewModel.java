package com.example.bar_code_scanner.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bar_code_scanner.database.CoilMasterRepository;
import com.example.bar_code_scanner.model.CoilMaster;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoilMasterViewModel extends AndroidViewModel {
    private CoilMasterRepository mRepository;

    public CoilMasterViewModel(@NonNull Application application) throws IOException {
        super(application);
        mRepository = new CoilMasterRepository(application);
    }


    public CoilMaster getSpecificCoilFromDB(String coilToGet) throws ExecutionException, InterruptedException {
        return mRepository.getSpecificCoil(coilToGet);
    }
}
