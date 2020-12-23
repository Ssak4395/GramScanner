package com.example.bar_code_scanner.database;
import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.bar_code_scanner.dao.CoilMasterDAO;
import com.example.bar_code_scanner.model.CoilMaster;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoilMasterRepository {
    private CoilMasterDAO coilMasterDAO;
    private LiveData<List<CoilMaster>> allCoils;

    public LiveData<List<CoilMaster>> getAllCoils() {
        return allCoils;
    }

    public CoilMasterRepository(Application application) throws IOException {
        CoilDatabase db = CoilDatabase.getDatabase(application);
        coilMasterDAO = db.coilMasterDAO();
        allCoils = coilMasterDAO.getAllCoils();
    }

    public void insertCoil(CoilMaster coilMaster)
    {
        new insertAsyncTask(coilMasterDAO).execute(coilMaster);
    }

    public int getTotalDatacount() throws ExecutionException, InterruptedException {
        return new getTotalItems(coilMasterDAO).execute().get();
    }

    public CoilMaster getSpecificCoil(String coilID) throws ExecutionException, InterruptedException {
        return new getSpecificCoilByID(coilMasterDAO).execute(coilID).get();
    }

    private static class insertAsyncTask extends AsyncTask<CoilMaster, Void, Void> {

        private CoilMasterDAO mAsyncTaskDao;

        insertAsyncTask(CoilMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CoilMaster... params) {
            mAsyncTaskDao.insertCoil(params[0]);
            return null;
        }
    }

    private static class getSpecificCoilByID extends AsyncTask<String, CoilMaster,CoilMaster>
    {

        private CoilMasterDAO mAsyncTaskDao;
        getSpecificCoilByID(CoilMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected CoilMaster doInBackground(String... strings) {
            return mAsyncTaskDao.getCoilFromID(strings[0]);
        }
    }

    private static class getTotalItems extends AsyncTask<Integer, Integer,Integer>
    {

        private CoilMasterDAO mAsyncTaskDao;

        getTotalItems(CoilMasterDAO dao)
        {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return mAsyncTaskDao.getDataCount();
        }
    }


}
