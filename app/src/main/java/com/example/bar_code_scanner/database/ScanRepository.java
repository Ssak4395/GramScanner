package com.example.bar_code_scanner.database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.bar_code_scanner.dao.ScanDAO;
import com.example.bar_code_scanner.model.Scan;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class ScanRepository {
    private ScanDAO scanDao;
    private LiveData<List<Scan>> allScans;

    public ScanRepository(Application application)
    {
        ScanDatabase db = ScanDatabase.getDatabase(application);
        scanDao = db.scanDAO();
        allScans = scanDao.getAllScans();
    }

    public LiveData<List<Scan>> getAllScans() {
        return allScans;
    }

    public void insert (Scan scan) {
        new insertAsyncTask(scanDao).execute(scan);
    }

    public void deleteSpecificScan(Scan scan) {new deleteSpecificScan(scanDao).execute(scan);}

    public void deleteAll()
    {
        new deleteAllAsyncTask(scanDao).execute();
    }

    public Scan getSpecificScanFromId(String id) throws ExecutionException, InterruptedException {
        return new getSpecificScanByID(scanDao).execute(id).get();
    }




    private static class insertAsyncTask extends AsyncTask<Scan, Void, Void> {

        private ScanDAO mAsyncTaskDao;

        insertAsyncTask(ScanDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Scan... params) {
            mAsyncTaskDao.insertScan(params[0]);
            System.out.println("The amount of data stored in this database is" + mAsyncTaskDao.getDataCount());
            return null;
        }
    }



    private static class deleteSpecificScan extends AsyncTask<Scan,Void,Void>
    {
        private ScanDAO scanDao;

        deleteSpecificScan(ScanDAO dao) {
            this.scanDao = dao;
        }
        @Override
        protected Void doInBackground(Scan... scans) {
            scanDao.deleteScan(scans[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends  AsyncTask<Void, Void, Void>
    {
        private ScanDAO mAsyncTaskDao;

        deleteAllAsyncTask(ScanDAO scanDao)
        {
            this.mAsyncTaskDao = scanDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class getSpecificScanByID extends AsyncTask<String, Scan,Scan>
    {

        private ScanDAO mAsyncTaskDao;

        getSpecificScanByID(ScanDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Scan doInBackground(String... strings) {
            return mAsyncTaskDao.getCoilFromID(strings[0]);
        }
    }


}
