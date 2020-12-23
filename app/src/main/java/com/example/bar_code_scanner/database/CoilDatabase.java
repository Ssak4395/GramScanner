package com.example.bar_code_scanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.bar_code_scanner.dao.CoilMasterDAO;
import com.example.bar_code_scanner.model.CoilMaster;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Database(entities = {CoilMaster.class}, version = 4, exportSchema = true)
public abstract class CoilDatabase extends RoomDatabase {
    abstract CoilMasterDAO coilMasterDAO();
    private static volatile CoilDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static CoilDatabase getDatabase(final Context context) throws IOException {
        if (INSTANCE == null) {
            synchronized (CoilDatabase.class) {
                if (INSTANCE == null) {
                    RoomDatabase.Builder<CoilDatabase> coils = Room.databaseBuilder(context.getApplicationContext(), CoilDatabase.class, "CoilMaster.db")
                            .createFromAsset("CoilMaster.db")
                            .addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration();

                    INSTANCE = coils.build();


                }
            }
        }
        return INSTANCE;
    }


    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}



