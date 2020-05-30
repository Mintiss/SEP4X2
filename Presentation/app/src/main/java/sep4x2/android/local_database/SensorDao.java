package sep4x2.android.local_database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sep4x2.android.local_database.Entity.SensorData;

@Dao
public interface SensorDao {

    @Insert
    void insert(SensorData sensorData);

    @Query("SELECT * FROM Sensor_storage_table")
    List<SensorData> getAllSensorData();

    @Query("DELETE FROM Sensor_storage_table")
    public void nukeTable();
}
