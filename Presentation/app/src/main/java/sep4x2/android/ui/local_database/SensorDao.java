package sep4x2.android.ui.local_database;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sep4x2.android.ui.local_database.Entity.SensorData;

@Dao
public interface SensorDao {

 /*
    @Insert
    void insert(MutableLiveData<SensorData> sensorData);


  @Query("SELECT DISTINCT temperature, updateTime FROM Sensor_storage_table")
   LiveData<List<SensorData>> getTemperature();

*/


}
