package com.example.bar_code_scanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bar_code_scanner.model.CoilMaster;
import com.example.bar_code_scanner.model.Scan;

import java.util.List;

@Dao
public interface ScanDAO {

    /**
     * Get all the Scans stored in our database.
     */
    @Query("SELECT * FROM scans")
    LiveData<List<Scan>> getAllScans();



    /**
     * Inserts a Scan to the local database.
     * @param scan
     */
    @Insert
    void insertScan(Scan scan);

    /**
     * Updates a specific scan in the database.
     * @param scan
     */
    @Update
    void updateScan(Scan scan);

    /**
     * Deletes a scan in the database.
     * @param scan
     */
    @Delete
    void deleteScan(Scan scan);


    /**
     * Returns a count of all the items in the database.
     * @return
     */
    @Query("SELECT COUNT(COILid) FROM Scans")
    int getDataCount();

    @Query("DELETE FROM Scans")
    void deleteAll();

    @Query("SELECT * FROM Scans WHERE COILid =:id")
    Scan getCoilFromID(String id);

}
