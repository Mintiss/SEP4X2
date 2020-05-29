package sep4x2.android.ui.local_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import sep4x2.android.ui.local_database.Entity.SensorData;

@Database(entities = {SensorData.class}, version = 2, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase{

    private static LocalDatabase instance;

    public abstract SensorDao sensorDao();

    public static synchronized  LocalDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class,"local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
