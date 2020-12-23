package com.example.bar_code_scanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.bar_code_scanner.model.CoilMaster;

import java.util.List;

@Dao
public interface CoilMasterDAO {
    /**
     * Get all the Scans stored in our database.
     */
    @Query("SELECT * FROM COIL_MASTER_20200813")
    LiveData<List<CoilMaster>> getAllCoils();

    @Query("SELECT * FROM COIL_MASTER_20200813 WHERE COILID =:id")
    CoilMaster getCoilFromID(String id);

    /**
     * Inserts a Scan to the local database.
     * This method SHOULD NOT BE CALLED since we are prepopulating data from an sql database instance.
     * @param coil
     */
    @Insert
    void insertCoil(CoilMaster coil);


    /**
     * Returns a count of all the items in the database.
     * @return
     */
    @Query("SELECT COUNT(COILID) FROM COIL_MASTER_20200813")
    int getDataCount();


}
