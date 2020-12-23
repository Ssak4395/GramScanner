package com.example.bar_code_scanner.database;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.bar_code_scanner.dao.ScanDAO;
import com.example.bar_code_scanner.model.Scan;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import typeconverter.DateConverter;

@Database(entities = {Scan.class}, version = 3, exportSchema = true)
@TypeConverters({DateConverter.class})
abstract class ScanDatabase extends RoomDatabase {

    abstract ScanDAO scanDAO();

    private static volatile ScanDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ScanDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScanDatabase.class) {
                if (INSTANCE == null) {
                    Builder<ScanDatabase> scans = Room.databaseBuilder(context.getApplicationContext(),
                            ScanDatabase.class, "Scans");
                    scans.addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration();
                    INSTANCE = scans
                            .build();

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
            databaseWriteExecutor.execute(() -> {
            });


        }
    };
}
