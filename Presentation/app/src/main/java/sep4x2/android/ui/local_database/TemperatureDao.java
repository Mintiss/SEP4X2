package sep4x2.android.ui.local_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorStorage;

@Dao
public interface TemperatureDao {


    @Insert
    void insert(SensorStorage sensorStorage);


    @Query("SELECT DISTINCT Temperature, UpdateTime FROM Sensor_storage_table")
   LiveData<SensorStorage> getTemperature();




}
