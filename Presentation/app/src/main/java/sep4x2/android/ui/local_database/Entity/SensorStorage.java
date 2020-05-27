package sep4x2.android.ui.local_database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Sensor_storage_table")
public class SensorStorage {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double Humidity;
    private double Temperature;
    private double Noise;
    private double Co2;
    private String UpdateTime;
}
