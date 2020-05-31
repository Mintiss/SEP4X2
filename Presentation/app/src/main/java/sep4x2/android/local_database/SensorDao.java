package sep4x2.android.local_database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sep4x2.android.SharedSensors.CO2;
import sep4x2.android.SharedSensors.Humidity;
import sep4x2.android.SharedSensors.Noise;
import sep4x2.android.SharedSensors.Temperature;
import sep4x2.android.local_database.Entity.SensorData;


@Dao
public interface SensorDao {

    @Insert
    void insert(SensorData sensorData);

    @Query("SELECT * FROM Sensor_storage_table")
    List<SensorData> getAllSensorData();

    @Query("SELECT * FROM Sensor_storage_table ORDER BY metricsId DESC LIMIT 1")
    LiveData<SensorData> getLastestSensorData();

    @Query("SELECT DISTINCT temperature,updateTime FROM Sensor_storage_table")
    List<Temperature> getAllTemperatureData();

    @Query("SELECT DISTINCT humidity,updateTime FROM Sensor_storage_table")
    List<Humidity> getAllHumidityData();

    @Query("SELECT DISTINCT co2,updateTime FROM Sensor_storage_table")
    List<CO2> getAllCO2Data();

    @Query("SELECT DISTINCT noise,updateTime FROM Sensor_storage_table")
    List<Noise> getAllNoiseData();

    @Query("DELETE FROM Sensor_storage_table")
    public void nukeTable();
}
